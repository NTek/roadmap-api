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
    <script type="text/javascript" charset="utf8" src="${prefix}/static/js-libs/jquery-1.10.2.min.js"></script>

    <link href="${prefix}/static/css/login.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1>Ajax login example</h1>

    <h2>Output in console, code example in sources</h2>
    <script>

        $.post(
                "http://roadmap-api.herokuapp.com/login",
                {
                    username: "testuser1@mail.com",
                    password: "123"
                },
                onlogin
        );

        function onlogin(data) {
            console.log("You logged in:")
//            console.log(data)
            console.log(JSON.stringify(data, "", 4))

            getApps()
        }

        function getApps() {
            $.get(
                    "http://roadmap-api.herokuapp.com/apps",
                    function (data) {
                        console.log("Your app list:")
                        console.log(data)

                    }
            );
        }
    </script>
</div>
</body>
</html>
