<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<button id="btnAPI">기업개황 API 요청버튼</button>
<!-- <a id="btnAPI" href="companyOverview">기업개황 API 요청</a> -->
<div>Dart 기업 개황 API call 받아온데이터 리스트</div>
<p id="apiContent"></p>
</body>
<script type="text/javascript">
$(function() {

 	$("#btnAPI").click(function() {
		$.ajax({
			url 	: "companyOverview",
			type 	: "get",
			data 	: {clientData : "meaningless", num : 123456},
			dataType : 'json',
			success	: function(data, status, xhr) {
				alert("success!");
				$("#apiContent").text(JSON.stringify(data.result));
			},
			error : function(xhr, status, error) {
				alert("error is "+error);
			},
			complete : function(xhr, status){

			}
		});
	});
	
});
</script>
</html>

