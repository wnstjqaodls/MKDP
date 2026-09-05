#!/usr/bin/env bash
set -euo pipefail

APP_NAME="mkdp"
APP_USER="mkdp"
APP_DIR="/opt/mkdp"
ENV_DIR="/etc/mkdp"
ENV_FILE="$ENV_DIR/mkdp.env"
BACKUP_DIR="/var/backups/mkdp"
STAGED_JAR="/tmp/mkdp.jar"

[[ -s "$STAGED_JAR" ]] || { echo "Missing $STAGED_JAR" >&2; exit 1; }

export DEBIAN_FRONTEND=noninteractive
export LANG=C.UTF-8
export LC_ALL=C.UTF-8
printf 'LANG=C.UTF-8\n' > /etc/default/locale
apt-get update
apt-get install -y --no-install-recommends ca-certificates curl gnupg locales nginx openjdk-17-jre-headless openssl postgresql-common
sed -i 's/^# *en_US.UTF-8 UTF-8/en_US.UTF-8 UTF-8/' /etc/locale.gen
locale-gen en_US.UTF-8

if [[ ! -f /etc/apt/sources.list.d/pgdg.list ]]; then
  curl -fsSL https://www.postgresql.org/media/keys/ACCC4CF8.asc \
    | gpg --dearmor -o /usr/share/keyrings/postgresql-pgdg.gpg
  echo "deb [signed-by=/usr/share/keyrings/postgresql-pgdg.gpg] https://apt.postgresql.org/pub/repos/apt bookworm-pgdg main" \
    > /etc/apt/sources.list.d/pgdg.list
  apt-get update
fi
apt-get install -y --no-install-recommends postgresql-16 postgresql-client-16

if ! id "$APP_USER" >/dev/null 2>&1; then
  useradd --system --home-dir "$APP_DIR" --shell /usr/sbin/nologin "$APP_USER"
fi
install -d -m 0750 -o root -g "$APP_USER" "$APP_DIR" "$ENV_DIR"
install -d -m 0700 -o postgres -g postgres "$BACKUP_DIR"

if [[ ! -f "$ENV_FILE" ]]; then
  db_password="$(openssl rand -hex 32)"
  sync_token="$(openssl rand -hex 32)"
  umask 077
  printf '%s\n' \
    'SERVER_ADDRESS=127.0.0.1' \
    'SERVER_PORT=4180' \
    'SPRING_PROFILES_ACTIVE=postgres' \
    'DB_URL=jdbc:postgresql://127.0.0.1:5432/mkdp' \
    'DB_USERNAME=mkdp' \
    "DB_PASSWORD=$db_password" \
    'DART_API_KEY=' \
    "SYNC_TOKEN=$sync_token" \
    > "$ENV_FILE"
  echo "NOTE: DART_API_KEY is blank — fill it in at $ENV_FILE and restart $APP_NAME.service" >&2
fi
chown root:"$APP_USER" "$ENV_FILE"
chmod 0640 "$ENV_FILE"

set -a
# shellcheck disable=SC1090
source "$ENV_FILE"
set +a

if ! pg_lsclusters --no-header | awk '$1 == "16" && $2 == "main" { found = 1 } END { exit !found }'; then
  pg_createcluster --start --locale=C.UTF-8 --encoding=UTF8 16 main
fi
systemctl enable --now postgresql
cluster_encoding="$(runuser -u postgres -- psql -Atqc "SELECT pg_encoding_to_char(encoding) FROM pg_database WHERE datname = 'template1'")"
if [[ "$cluster_encoding" == "SQL_ASCII" ]]; then
  if runuser -u postgres -- psql -Atqc "SELECT 1 FROM pg_database WHERE datname = 'mkdp'" | grep -qx 1; then
    echo "Refusing to recreate a SQL_ASCII cluster that already contains mkdp" >&2
    exit 1
  fi
  pg_dropcluster --stop 16 main
  pg_createcluster --start --locale=C.UTF-8 --encoding=UTF8 16 main
fi
runuser -u postgres -- psql --set=role_password="$DB_PASSWORD" <<'SQL'
SELECT 'CREATE ROLE mkdp LOGIN'
WHERE NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'mkdp')\gexec
SELECT format('ALTER ROLE mkdp PASSWORD %L', :'role_password')\gexec
SELECT 'CREATE DATABASE mkdp OWNER mkdp'
WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'mkdp')\gexec
SQL

systemctl stop "$APP_NAME.service" 2>/dev/null || true
if [[ -f "$APP_DIR/mkdp.jar" ]]; then
  cp -a "$APP_DIR/mkdp.jar" "$APP_DIR/mkdp.jar.prev"
fi
install -m 0640 -o root -g "$APP_USER" "$STAGED_JAR" "$APP_DIR/mkdp.jar"

cat > "/etc/systemd/system/$APP_NAME.service" <<'EOF'
[Unit]
Description=MKDP DART Disclosure Service
After=network-online.target postgresql.service
Wants=network-online.target

[Service]
Type=simple
User=mkdp
Group=mkdp
WorkingDirectory=/opt/mkdp
EnvironmentFile=/etc/mkdp/mkdp.env
ExecStart=/usr/bin/java -jar /opt/mkdp/mkdp.jar
Restart=on-failure
RestartSec=5
NoNewPrivileges=true
PrivateTmp=true
ProtectHome=true
ProtectSystem=strict
ReadWritePaths=/tmp

[Install]
WantedBy=multi-user.target
EOF

cat > /etc/nginx/sites-available/mkdp <<'EOF'
server {
    listen 80 default_server;
    listen [::]:80 default_server;
    server_name mkdp.qwer4.org;

    allow 127.0.0.1;
    allow 192.168.20.2;
    allow 192.168.20.105;
    allow 192.168.20.109;
    allow 192.168.20.110;
    allow 192.168.20.113;
    allow 192.168.88.251;
    deny all;

    client_max_body_size 2m;

    location / {
        proxy_pass http://127.0.0.1:4180;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto https;
        proxy_read_timeout 60s;
        add_header X-Content-Type-Options nosniff always;
        add_header X-Frame-Options DENY always;
        add_header Referrer-Policy no-referrer always;
    }
}
EOF
rm -f /etc/nginx/sites-enabled/default /etc/nginx/sites-enabled/mkdp
ln -s /etc/nginx/sites-available/mkdp /etc/nginx/sites-enabled/mkdp

cat > /usr/local/sbin/mkdp-backup <<'EOF'
#!/usr/bin/env bash
set -euo pipefail
backup_dir=/var/backups/mkdp
umask 077
install -d -m 0700 -o postgres -g postgres "$backup_dir"
target="$backup_dir/mkdp-$(date +%Y%m%d-%H%M%S).dump"
runuser -u postgres -- pg_dump --format=custom --file="$target" mkdp
find "$backup_dir" -maxdepth 1 -type f -name 'mkdp-*.dump' -mtime +30 -delete
test -s "$target"
EOF
chmod 0750 /usr/local/sbin/mkdp-backup

cat > /etc/systemd/system/mkdp-backup.service <<'EOF'
[Unit]
Description=Backup MKDP PostgreSQL database
After=postgresql.service

[Service]
Type=oneshot
ExecStart=/usr/local/sbin/mkdp-backup
EOF

cat > /etc/systemd/system/mkdp-backup.timer <<'EOF'
[Unit]
Description=Daily MKDP database backup

[Timer]
OnCalendar=*-*-* 03:30:00
Persistent=true
RandomizedDelaySec=10m

[Install]
WantedBy=timers.target
EOF

cat > /usr/local/sbin/mkdp-corp-code-sync <<'EOF'
#!/usr/bin/env bash
set -euo pipefail
source /etc/mkdp/mkdp.env
curl -fsS -X POST -H "X-Sync-Token: $SYNC_TOKEN" http://127.0.0.1:4180/api/admin/corp-codes/sync
EOF
chmod 0750 /usr/local/sbin/mkdp-corp-code-sync

cat > /etc/systemd/system/mkdp-corp-code-sync.service <<'EOF'
[Unit]
Description=Sync MKDP company master from DART corpCode.xml
After=mkdp.service

[Service]
Type=oneshot
ExecStart=/usr/local/sbin/mkdp-corp-code-sync
EOF

cat > /etc/systemd/system/mkdp-corp-code-sync.timer <<'EOF'
[Unit]
Description=Weekly MKDP company master sync

[Timer]
OnCalendar=Sun *-*-* 04:00:00
Persistent=true
RandomizedDelaySec=10m

[Install]
WantedBy=timers.target
EOF

systemd-analyze verify "/etc/systemd/system/$APP_NAME.service" /etc/systemd/system/mkdp-backup.service \
  /etc/systemd/system/mkdp-backup.timer /etc/systemd/system/mkdp-corp-code-sync.service \
  /etc/systemd/system/mkdp-corp-code-sync.timer
nginx -t
test "$(readlink -f /etc/nginx/sites-enabled/mkdp)" = /etc/nginx/sites-available/mkdp
systemctl daemon-reload
systemctl enable --now "$APP_NAME.service" nginx mkdp-backup.timer mkdp-corp-code-sync.timer
systemctl reload nginx

for _ in $(seq 1 60); do
  if curl -fsS http://127.0.0.1:4180/actuator/health | grep -q '"status":"UP"'; then
    break
  fi
  sleep 1
done
curl -fsS http://127.0.0.1:4180/actuator/health | grep -q '"status":"UP"'
curl -fsS -H 'Host: mkdp.qwer4.org' http://127.0.0.1/actuator/health | grep -q '"status":"UP"'
app_listener="$(ss -ltnH | awk '$4 ~ /:4180$/ { print $4 }')"
[[ "$app_listener" == *"127.0.0.1"* ]] || { echo "Unexpected app listener: $app_listener" >&2; exit 1; }
ss -ltn | grep -q '127.0.0.1:5432'
systemctl start mkdp-backup.service
test -n "$(find "$BACKUP_DIR" -maxdepth 1 -type f -name 'mkdp-*.dump' -print -quit)"

echo "MKDP runtime installed and healthy"
