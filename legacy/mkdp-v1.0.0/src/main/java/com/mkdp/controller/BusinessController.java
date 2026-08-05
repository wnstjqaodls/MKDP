package com.mkdp.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mkdp.service.DisclosureService;
import com.mkdp.service.DisclosureServiceIF;
import com.mkdp.vo.BacktestRequestVO;

@CrossOrigin("*")
@Controller
public class BusinessController {
	private static final String BASE_URL = "https://opendart.fss.or.kr/api/company.json";
	private static final String CERTIFICATION_KEY = "7d0f1dcd2423d0a924566799752d81b114b9debe";
	private static final String CROP_CODE_API_URL = "https://opendart.fss.or.kr/api/corpCode.xml";
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BusinessController.class);
	/** http 요청을 받아서 API Dart 기업개황 을 Map데이터로 돌려준다  */
	@ResponseBody
	@RequestMapping(value = "/companyOverview", method = RequestMethod.GET)
	public Map<Object,Object> getCompanyOverview(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {

		Map<Object, Object> result = new HashMap<>();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		String url = BASE_URL;
		String crtfc_key = CERTIFICATION_KEY;
		String corp_code = "00126380";

		URIBuilder builder = new URIBuilder(url);
		builder.setParameter("crtfc_key", crtfc_key);
		builder.setParameter("corp_code", corp_code);

		// 프록시 서버를 통해 요청을 보냄
		HttpGet request1 = new HttpGet(builder.build());
		request1.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
		CloseableHttpResponse apiResponse = httpClient.execute(request1);

		try {
			HttpEntity entity = (HttpEntity) apiResponse.getEntity();
			String responseBody = EntityUtils.toString((org.apache.http.HttpEntity) entity, StandardCharsets.UTF_8);
			result.put("result", responseBody);
		} finally {
			apiResponse.close();
		}
		
		
		return result;


	}
	
	/** http 요청을 받아서 API Dart 고유번호 을 Map데이터로 돌려준다  */
	@ResponseBody
	@RequestMapping(value = "/companyCropCode", method = RequestMethod.GET)
	public Map<Object,Object> companyCropCodeController(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {	
		
		LOGGER.info("companyCropCode API 호출");
		Map<Object, Object> result = new HashMap<>();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		String url = CROP_CODE_API_URL;
		String crtfc_key = CERTIFICATION_KEY;
		
		URIBuilder builder = new URIBuilder(url);
		builder.setParameter("crtfc_key", crtfc_key);

		
		// 프록시 서버를 통해 요청을 보냄
		HttpGet request1 = new HttpGet(builder.build());
		request1.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
		CloseableHttpResponse apiResponse = httpClient.execute(request1);
		
		try {
			HttpEntity entity = (HttpEntity) apiResponse.getEntity();
			String responseBody = EntityUtils.toString((org.apache.http.HttpEntity) entity, StandardCharsets.UTF_8);

			result.put("result", responseBody);
		} finally {
			apiResponse.close();
		}


		return result;

	}

	@ResponseBody
    @RequestMapping(value = "/api/updateCorpCodes", method = RequestMethod.POST)
    public Map<String, Object> updateCorpCodes() {
		LOGGER.info("updateCropCodes 호출");
        Map<String, Object> result = new HashMap<>();
        
        try {
			// 1. ZIP 파일 다운로드
			// byte[] zipFile = downloadCorpCodeFile();
			String url = CROP_CODE_API_URL;
			String crtfc_key = CERTIFICATION_KEY;
			
			URIBuilder builder = new URIBuilder(url);
			builder.setParameter("crtfc_key", crtfc_key);
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			
			// 프록시 서버를 통해 요청을 보냄
			HttpGet request1 = new HttpGet(builder.build());
			request1.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
			CloseableHttpResponse apiResponse = httpClient.execute(request1);
			LOGGER.info("apiResponse 결과 :"+apiResponse.getEntity().toString());
			
			
            byte[] zipFile = String.valueOf(apiResponse.getEntity().getContent()).getBytes();
            // 2. ZIP 파일 압축해제 및 XML 파싱
			DisclosureServiceIF disclosureService = new DisclosureService();
            List<String> companies = disclosureService.parseCorpCodeXml(zipFile);
            
			// 3. 기업 코드 업데이트
            disclosureService.updateCompanies(companies);
			//TODO : 이부분 테스트해보기 디버깅해서 데이터보기
            result.put("success", true);
            result.put("message", "기업 코드 업데이트 완료");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @ResponseBody
    @RequestMapping("/api/backtest")
    public Map<String, Object> runBacktest(@RequestBody BacktestRequestVO request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 포트폴리오 설정 검증
            //validatePortfolioSettings(request);
            
            // 2. 각 기업의 주가 데이터 조회
            // Map<String, List<StockPriceVO>> stockData = getStockPriceData(
            //     request.getCompanyCodes(),
            //     request.getStartDate(),
            //     request.getEndDate()
            // );
            
            // 3. 백테스트 수행
            // BacktestResultVO backtestResult = performBacktest(
            //     request.getInitialAmount(),
            //     request.getAllocations(),
            //     stockData
            // );
            
            // result.put("success", true);
            // result.put("result", backtestResult);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
		
	}
