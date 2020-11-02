function tr(text) {
    return '<tr>' + text + '</tr>';
}

function td(text) {
    return '<td>' + text + '</td>';
}

function row(title, content) {
    return $(
        tr(
            td(title) +
            td(content)
        ));
}

function refreshTable() {
    $.get('/link', function (data) {
        let attr, mainTable = $('#linkTable tbody');
        mainTable.empty();
        for (attr in data) {
            if (data.hasOwnProperty(attr)) {
                mainTable.append(row(data[attr].title, data[attr].content));
            }
        }
    });
}

$(document).ready(function () {
    connect()
    refreshTable()
});

let stompClient = null;

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/activity', function (message) {
            console.log(message);
            refreshTable();
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendData() {
    let title = $('#title');
    let content = $('#content');
    const data = JSON.stringify({
        title: title.val(),
        content: content.val()
    });
    stompClient.send("/app/changeLink", {}, data);
}

$(function () {
    $("#addForm").on('submit', function (e) {
        e.preventDefault();
    });
    $("#addLink").click(() => sendData());
});
