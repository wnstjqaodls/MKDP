import axios from 'axios';

/**
 * 공통 HTTP 요청 함수를 정의한 모듈. 2023-11-27 Made - KJS 
 */ 
const MKDP_HTTP = {
  
  /**
   * HTTP POST 요청을 보내고 응답을 반환합니다.
   * @param {string} url - 요청을 보낼 서버의 URL
   * @param {Object} param - 서버에 전송할 JSON 타입 데이터
   * @returns {Promise} 서버로부터의 응답을 담은 프로미스 객체
   */
  MKDP_Request(url, requestParam) {
    // Axios 인스턴스 생성
    const instance = axios.create({
      baseURL: 'http://localhost:8050/mkdpApp',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    return new Promise(function (resolve, reject) {
      // 생성된 인스턴스를 사용하여 POST 요청 보내기
      instance.post(url, JSON.stringify(requestParam))
        .then((response) => {
          if (response.data.result) {
            resolve(response.data.result);
          }
        })
        .catch((error) => {
          console.log(error.message);
          reject(error);
        });
    });
  },
};

export { MKDP_HTTP };
