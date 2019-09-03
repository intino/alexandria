const PushService = (function () {
    var callbacks = {};
    var service = {};

    service.pendingMessages = [];
    service.connections = [];
    service.notified = false;

    service.openConnection = function (url) {
        var socket = new WebSocket(url);
        socket.retries = 0;
        socket.ready = false;

        socket.onopen = function(e) {
            console.log("WebSocket connection opened.");
            socket.retries = 0;
            socket.ready = true;
            sendPendingMessages(service);
        };

        socket.onmessage = function (event) {
            var data = JSON.parse(event.data);
            var callbacks = callback(data.n).slice(0);
            callbacks.forEach(function (callback) {
                callback(data.p);
            });
        };

        socket.onerror = function(e) {
        };

        socket.onclose = function(e) {
            if (socket.retries >= 3) {
                socket.ready = false;
                this.notifyClose();
                return;
            }

            this.reconnect(e);
        };

        socket.reconnect = function(e) {
            try {
                socket.retries++;
                console.log("WebSocket connection lost. Status code: " + e.code + ". Retry: " + socket.retries + ".");
                service.openConnection(url);
            }
            catch(e) {
                window.setTimeout(service.openConnection(url), 1000*2);
            }
        }

        socket.notifyClose = function() {
            if (service.notified) return;
            if (service.onCloseListener) service.onCloseListener();
            callbacks = {};
        };

        if (this.connections.length <= 0) this.connections["Default"] = socket;
        this.connections[url] = socket;
    };

    service.listen = function (name, callback) {
        if (!(name in callbacks))
            callbacks[name] = [];
        callbacks[name].push(callback);
        return {
            deregister: function () {
                var index = callbacks[name].indexOf(callback);
                if (index === -1) return;
                callbacks[name].splice(index, 1);
            }
        }
    };

    service.send = function(message, socketUrl) {
        if (socketUrl == null) socketUrl = "Default";
        const socket = this.connections[socketUrl];
        if (socket != null && socket.ready) socket.send(JSON.stringify(message));
        else service.pendingMessages.push({ message: message, socket: socketUrl });
    };

    service.onClose = function(listener) {
        this.onCloseListener = listener;
    };

    function callback(name) {
        return name in callbacks ? callbacks[name] : [];
    }

    function sendPendingMessages(service) {
        for (let i=0; i<service.pendingMessages; i++) {
            const pendingMessage = service.pendingMessages[i];
            service.send(pendingMessage.message, pendingMessage.socket);
        }
    }

    return service;
})();

export default PushService;