<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="login" method="Post">
post방식
아이디:<input type="text" name="userid"><br>
비밀번호:<input type="text" name="passwd"><br>
<input type="submit" value="로그인">
</form>
<form action="login" method="get">
get방식 
아이디:<input type="text" name="userid"><br>
비밀번호:<input type="text" name="passwd"><br>
<input type="submit" value="로그인">
</form>
</body>
</html>