const PushService = (function () {
    var callbacks = {};
    var service = {};

    service.pendingMessages = [];
    service.connections = [];
    service.retries = [];
    service.pendingConnections = [];
    service.successCallbacks = [];
    service.errorCallbacks = [];

    service.openConnection = function (name, url, successCallback, errorCallback) {
        registerCallbacks(this, successCallback, errorCallback);
        if (isConnecting(this, name)) return;
        this.pendingConnections.push(name);

        var socketUrl = url;
        var socket = new WebSocket(socketUrl);
        socket.name = name;
        socket.ready = false;

        if (service.retries[socket.name] == null) service.retries[socket.name] = 0;

        socket.onopen = function(e) {
            console.log("WebSocket connection opened.");
            service.retries[socket.name] = 0;
            socket.ready = true;
            window.setInterval(() => ping(socket, service), 30000);
            sendPendingMessages(service);
            service.successCallbacks.forEach(c => c());
        };

        socket.onmessage = function (event) {
            if (event.data instanceof Blob) return;
            var data = JSON.parse(event.data);
            var callbacks = callback(data.n).slice(0);
            callbacks.forEach(function (callback) {
                callback(data.p);
            });
        };

        socket.onerror = function(e) {
            service.errorCallbacks.forEach(c => c());
        };

        socket.onclose = function(e) {
            socket.ready = false;
            if (service.retries[socket.name] >= 3) {
                this.notifyClose(socket);
                return;
            }
            this.reconnect(e);
        };

        socket.reconnect = function(e) {
            try {
                service.retries[socket.name]++;
                console.log("WebSocket connection lost. Status code: " + e.code + ". Retry: " + service.retries[socket.name] + ".");
                service.openConnection(socket.name, socketUrl);
            }
            catch(e) {
                window.setTimeout(service.openConnection(socket.name, socketUrl), 1000*2);
            }
        }

        socket.notifyClose = function(socket) {
            if (service.onCloseListener) service.onCloseListener(socket.name);
            callbacks = {};
        };

        if (this.connections["Default"] == null) this.connections["Default"] = socket;
        this.connections[name] = socket;

        this.pendingConnections = this.pendingConnections.filter(v => v !== name);
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

    service.isConnectionRegistered = function(app) {
        return this.connections[app] != null;
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

    function ping(socket, service) {
        if (!socket.ready) return;
        service.send({op:'ping'}, socket.name);
    }

    function registerCallbacks(service, successCallback, errorCallback) {
        if (successCallback != null) service.successCallbacks.push(successCallback);
        if (errorCallback != null) service.errorCallbacks.push(errorCallback);
    }

    function isConnecting(service, name) {
        return (service.connections[name] != null && service.connections[name].ready) || service.pendingConnections.includes(name);
    }

    return service;
})();

export default PushService;