$(document).ready(function (event) {

    login("testuser1@mail.com", "123");

});

//var host = "http://192.168.1.152"
var host = ""

function login(username, password) {
    $.ajax({
        type: "POST",
        url: host + "/login",
        data: {
            username: username,
            password: password
        },
        xhrFields: {
            withCredentials: true
        },
        success: function (data) {
            console.log("Successfully logged in!")
            getApps();

        }
    });
}

function getApps() {
    $.ajax({
        type: "GET",
        url: host + "/apps",
        xhrFields: {
            withCredentials: true
        },
        success: function (data) {
            console.log("Applications list:")
            console.log(data)
        }
    });
}

