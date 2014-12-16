<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%request.setAttribute("prefix", request.getContextPath());%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>WebSocket chat</title>
    <meta name="description" content="Roadmap hello world">
    <meta name="author" content="Oleg Vasiliev">
    <script type="text/javascript" charset="utf8" src="${prefix}/static/js-libs/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${prefix}/static/js-libs/sockjs-0.3.4.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${prefix}/static/js-libs/stomp.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${prefix}/static/chat.js"></script>
</head>
<body>
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>
<div>
    <div>
        Logged in as <strong><sec:authentication property="principal.username"/></strong> (<a
            href="/logout">logout</a>)
    </div>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
    </div>
    <div id="conversationDiv">
        <input type="text" id="text" placeholder="Message"/>
        <button id="sendbtn" onclick="sendMsg();">Send</button>
        <p id="response"></p>
    </div>
</div>
</body>
</html>
