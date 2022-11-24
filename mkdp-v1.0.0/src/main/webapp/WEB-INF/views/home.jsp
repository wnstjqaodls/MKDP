<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P><br>
<a href="setCookie">setcookie</a><br>
<a href="getCookie">getcookie</a>
<a href="getHeader">getHeader</a>
<a href="redirect">redirect</a>
<a href="forward">forward</a>
</body>
</html>
