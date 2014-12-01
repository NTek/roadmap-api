<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%request.setAttribute("prefix", request.getContextPath());%>
<html lang="en">
<head>
    <title>Login</title>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Oleg Vasiliev">

    <link href="${prefix}/static/css/bootstrap.min.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->

    <link href="${prefix}/static/css/login.css" rel="stylesheet">
</head>
<body cz-shortcut-listen="true">
<div class="container">
    <form class="form-signin" method="POST" accept-charset="UTF-8">
        <h3 class="form-signin-heading">You know what to do ;)</h3>
        <input type="text" name="username" class="form-control" placeholder="Login" required="" autofocus=""
               autocomplete="off" autocapitalize="off" autocorrect="off">
        <input type="password" name="password" class="form-control" placeholder="Password" required="">
        <button class="btn btn-lg btn-primary btn-block" type="submit">Enter</button>
        <h5 class="alert alert-info">try: bob/mike/alice and 123</h5>

        <c:if test="${param.error!=null}">
            <h5 class="alert alert-danger">Invalid credentials!</h5>
        </c:if>
        <c:if test="${param.logout!=null}">
            <h5 class="alert alert-success">You successfully logged out!</h5>
        </c:if>
    </form>
</div>
</body>
</html>
