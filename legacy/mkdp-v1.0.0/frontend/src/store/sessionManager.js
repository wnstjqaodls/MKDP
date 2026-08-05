// sessionManager.js

export default {
    // 세션에 데이터 저장
    saveSessionData(data) {
        sessionStorage.setItem('userSession', JSON.stringify(data));
    },

    // 세션에서 데이터 가져오기
    getSessionData() {
        const sessionData = sessionStorage.getItem('userSession');
        return sessionData ? JSON.parse(sessionData) : null;
    },

    // 세션에서 데이터 제거 (로그아웃 시)
    clearSessionData() {
        sessionStorage.removeItem('userSession');
    },

    // 사용자 로그인 상태 확인
    isUserLoggedIn() {
        return this.getSessionData() !== null;
    }
};
