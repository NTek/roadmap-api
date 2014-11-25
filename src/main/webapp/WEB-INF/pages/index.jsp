<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setAttribute("prefix", request.getContextPath());%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">

    <title>Roadmap hello world!</title>
    <meta name="description" content="Roadmap hello world">
    <meta name="author" content="Oleg Vasiliev">

    <script type="text/javascript" charset="utf8" src="${prefix}/static/js-libs/sockjs-0.3.4.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${prefix}/static/js-libs/stomp.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${prefix}/static/index.js"></script>

</head>
<body>
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>
<div>
    <div>
        <button surveyId="connect" onclick="connect();">Connect</button>
        <button surveyId="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
    </div>
    <div surveyId="conversationDiv">
        <label>What is your name?</label><input type="text" surveyId="name"/>
        <button surveyId="sendName" onclick="sendName();">Send</button>
        <p surveyId="response"></p>
    </div>
</div>
</body>
</html>
