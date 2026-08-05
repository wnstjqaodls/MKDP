<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>This is a login.jsp Page</h1>
<p>
1. \${userid} 의 값은 : ${userid}<br>
1. request.getAttribute("userid") 의 값은: <%=request.getAttribute("userid") %><br>
2  request.getAttribute("passwd") 의 값은 : <%= request.getAttribute("passwd") %><br>
3. request.getParameter("userid") 의 값은 : <%=request.getParameter("userid") %><br>
4. request.getParameter("passwd") 의 값은 : <%=request.getParameter("passwd") %><br>
</p>
</body>
</html>