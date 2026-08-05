#!/usr/bin/env bash
set -euo pipefail

systemctl is-active --quiet mkdp postgresql nginx
systemctl is-enabled --quiet mkdp mkdp-backup.timer mkdp-corp-code-sync.timer nginx postgresql
nginx -t
test "$(readlink -f /etc/nginx/sites-enabled/mkdp)" = /etc/nginx/sites-available/mkdp
curl -fsS http://127.0.0.1:4180/actuator/health | grep -q '"status":"UP"'
curl -fsS -H 'Host: mkdp.qwer4.org' http://127.0.0.1/actuator/health | grep -q '"status":"UP"'

runuser -u postgres -- psql -d mkdp -At <<'SQL'
SELECT 'encoding=' || current_setting('server_encoding');
SELECT 'companies=' || count(*) FROM company;
SELECT 'flyway=' || max(version) FROM flyway_schema_history WHERE success;
SQL

systemctl start mkdp-backup.service
latest_backup="$(find /var/backups/mkdp -maxdepth 1 -type f -name 'mkdp-*.dump' -printf '%T@ %f %s\n' | sort -nr | head -1)"
[[ -n "$latest_backup" ]] || { echo "backup=missing" >&2; exit 1; }
printf 'backup=%s\n' "$latest_backup"
printf 'nginx-link=%s\n' "$(readlink -f /etc/nginx/sites-enabled/mkdp)"

dart_key="$(grep -oP '(?<=^DART_API_KEY=).*' /etc/mkdp/mkdp.env || true)"
if [[ -z "$dart_key" ]]; then
  echo "WARNING: DART_API_KEY is still blank in /etc/mkdp/mkdp.env — company search/overview/disclosures/financials will fail against DART until it's set" >&2
fi

printf 'runtime=healthy\n'
