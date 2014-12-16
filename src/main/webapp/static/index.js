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
    });
}

function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
}

function subscribe() {
    var text = document.getElementById('subscribe_field').value;
    if (text == '') {
        alert("Enter a message!");
        return
    }
    stompClient.subscribe(text, function (chatmsg) {
        var msg = JSON.parse(chatmsg.body);
        showMessage(JSON.stringify(msg, "", 4));
    });
    $('#subscribe_field').val("");
    showMessage("Subscribed to " + text)
}

function showMessage(message) {
    $('#response').html(message);
}

$(document).keypress(function (event) {
    if (event.keyCode == 13) {
        $('#sendbtn').trigger('click');
    }
});

$(document).ready(function (event) {
    setConnected(false);

});
