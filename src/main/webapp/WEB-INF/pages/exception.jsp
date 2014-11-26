<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Error Page</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
</head>
<body>
<h1>Error Page</h1>

<p>Application has encountered an error. View page source for details.</p>

<!--
    Failed URL: ${url}
    Exception:  ${exception.getClass().getSimpleName()} - ${exception.message}
        <c:forEach items="${exception.stackTrace}" var="ste">    ${ste}
    </c:forEach>
    -->
</body>
</html>
