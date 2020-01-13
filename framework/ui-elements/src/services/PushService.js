const PushService = (function () {
    var callbacks = {};
    var service = {};

    service.pendingMessages = [];
    service.connections = [];
    service.retries = [];

    service.openConnection = function (name, url) {
        var socket = new WebSocket(url);
        socket.name = name;
        socket.ready = false;

        if (service.retries[socket.name] == null) service.retries[socket.name] = 0;

        socket.onopen = function(e) {
            console.log("WebSocket connection opened.");
            service.retries[socket.name] = 0;
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
            if (service.retries[socket.name] >= 3) {
                socket.ready = false;
                this.notifyClose(socket);
                return;
            }

            this.reconnect(e);
        };

        socket.reconnect = function(e) {
            try {
                service.retries[socket.name]++;
                console.log("WebSocket connection lost. Status code: " + e.code + ". Retry: " + service.retries[socket.name] + ".");
                service.openConnection(socket.name, url);
            }
            catch(e) {
                window.setTimeout(service.openConnection(socket.name, url), 1000*2);
            }
        }

        socket.notifyClose = function(socket) {
            if (service.onCloseListener) service.onCloseListener(socket.name);
            callbacks = {};
        };

        if (this.connections.length <= 0) this.connections["Default"] = socket;
        this.connections[name] = socket;
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

    service.send = function(message, app) {
        if (app == null) app = "Default";
        const socket = this.connections[app];
        if (socket != null && socket.ready) socket.send(encodeURIComponent(JSON.stringify(message)));
        else service.pendingMessages.push({ message: message, app: app });
    };

    service.existsConnection = function(app) {
        const socket = this.connections[app];
        return socket != null && socket.ready;
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
            service.send(pendingMessage.message, pendingMessage.app);
        }
    }

    return service;
})();

export default PushService;