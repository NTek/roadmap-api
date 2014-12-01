var stompClient = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function connect() {
    var socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat', function (chatmsg) {
            var msg = JSON.parse(chatmsg.body);
            showMessage(msg.username, msg.text);
        });
    });
}

function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
}

function sendMsg() {
    var text = document.getElementById('text').value;
    if (text == '') {
        alert("Enter a message!");
        return
    }
    stompClient.send("/app/chat", {}, JSON.stringify({ 'text': text }));
    $('#text').val("");
}

function showMessage(username, message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(username + ': ' + message));
    response.appendChild(p);
}

$(document).keypress(function (event) {
    if (event.keyCode == 13) {
        $('#sendbtn').trigger('click');
    }
});

$(document).ready(function (event) {
    setConnected(false);

});
