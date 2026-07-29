const PushService = (function () {
    const PendingIncomingMessageMaxAge = 2000;
    const PendingIncomingRetryMaxDelay = 250;
    var callbacks = {};
    var service = {};

    service.pendingMessages = [];
    service.pendingIncomingMessages = [];
    service.pendingIncomingFlush = false;
    service.pendingIncomingFlushTimer = null;
    service.flushingIncomingMessages = false;
    service.connections = [];
    service.retries = [];
    service.pendingConnections = [];
    service.successCallbacks = [];
    service.errorCallbacks = [];

    service.openConnection = function (name, url, successCallback, errorCallback) {
        registerCallbacks(this, successCallback, errorCallback);
        if (isConnecting(this, name)) return;
        this.pendingConnections.push(name);

        var socketUrl = url.indexOf("tzo=") == -1 ? (url + (url.indexOf("?") != -1 ? "&" : "?") + "tzo=" + (new Date()).getTimezoneOffset()) : url;
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
            if (shouldQueueIncomingMessage()) {
                queueIncomingMessage(data.n, data.p);
                return;
            }
            var listeners = callback(data.n).slice(0);
            if (listeners.length > 0) {
                if (dispatchToListeners(listeners, data.p)) return;
            }
            queueIncomingMessage(data.n, data.p);
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
        scheduleIncomingFlush(0, true);
        return {
            deregister: function () {
                if (callbacks[name] == null) return;
                var index = callbacks[name].indexOf(callback);
                if (index === -1) return;
                callbacks[name].splice(index, 1);
                if (callbacks[name].length === 0) delete callbacks[name];
            }
        }
    };

    service.send = function(message, app) {
        if (app == null) app = "Default";
        const socket = this.connections[app];
        if (socket != null && socket.ready) socket.send(encodeURIComponent(JSON.stringify(message)));
        else service.pendingMessages.push({ message: message, app: app });
    };

    service.ready = function(app) {
        this.send({ op: "ready", s: "displayrouter" }, app);
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

    function queueIncomingMessage(name, payload) {
        const now = Date.now();
        service.pendingIncomingMessages.push({ name, payload, queuedAt: now, attempts: 0, nextRetryAt: now });
        scheduleIncomingFlush(0);
    }

    function flushPendingIncomingMessages() {
        if (service.flushingIncomingMessages) return;
        if (service.pendingIncomingMessages.length === 0) {
            return;
        }

        service.flushingIncomingMessages = true;
        const queuedMessages = service.pendingIncomingMessages.slice(0);
        service.pendingIncomingMessages = [];
        const remaining = [];
        const blockedTargets = new Set();
        const now = Date.now();
        queuedMessages.forEach(function (pendingMessage) {
            const name = pendingMessage.name;
            const payload = pendingMessage.payload;
            const target = targetKey(name, payload);
            normalizePendingIncomingMessage(pendingMessage, now);
            if (isExpiredPendingIncomingMessage(pendingMessage, now)) return;
            if (blockedTargets.has(target)) {
                remaining.push(pendingMessage);
                return;
            }
            if (pendingMessage.nextRetryAt > now) {
                blockedTargets.add(target);
                remaining.push(pendingMessage);
                return;
            }
            const listeners = callback(name).slice(0);
            if (listeners.length === 0) {
                blockedTargets.add(target);
                deferPendingIncomingMessage(pendingMessage, now);
                remaining.push(pendingMessage);
                return;
            }
            const handled = dispatchToListeners(listeners, payload);
            if (!handled) {
                blockedTargets.add(target);
                deferPendingIncomingMessage(pendingMessage, now);
                remaining.push(pendingMessage);
                return;
            }
        });
        service.pendingIncomingMessages = remaining.concat(service.pendingIncomingMessages);
        service.flushingIncomingMessages = false;

        if (service.pendingIncomingMessages.length > 0) scheduleIncomingFlush(nextIncomingRetryDelay());
    }

    function scheduleIncomingFlush(delay, force) {
        if (service.pendingIncomingMessages.length === 0) return;
        if (force && service.pendingIncomingFlushTimer != null) {
            window.clearTimeout(service.pendingIncomingFlushTimer);
            service.pendingIncomingFlushTimer = null;
            service.pendingIncomingFlush = false;
        }
        if (service.pendingIncomingFlush) return;
        service.pendingIncomingFlush = true;
        service.pendingIncomingFlushTimer = window.setTimeout(function () {
            service.pendingIncomingFlushTimer = null;
            service.pendingIncomingFlush = false;
            flushPendingIncomingMessages();
        }, delay);
    }

    function normalizePendingIncomingMessage(message, now) {
        if (message.queuedAt == null) message.queuedAt = now;
        if (message.attempts == null) message.attempts = 0;
        if (message.nextRetryAt == null) message.nextRetryAt = now;
    }

    function isExpiredPendingIncomingMessage(message, now) {
        return now - message.queuedAt >= PendingIncomingMessageMaxAge;
    }

    function deferPendingIncomingMessage(message, now) {
        message.attempts++;
        const delay = Math.min(PendingIncomingRetryMaxDelay, 16 * Math.pow(2, Math.min(message.attempts - 1, 4)));
        message.nextRetryAt = now + delay;
    }

    function nextIncomingRetryDelay() {
        const now = Date.now();
        const scheduledTargets = new Set();
        return service.pendingIncomingMessages.reduce(function (delay, message) {
            normalizePendingIncomingMessage(message, now);
            const target = targetKey(message.name, message.payload);
            if (scheduledTargets.has(target)) return delay;
            scheduledTargets.add(target);
            const expiration = message.queuedAt + PendingIncomingMessageMaxAge;
            return Math.min(delay, Math.max(0, Math.min(message.nextRetryAt, expiration) - now));
        }, PendingIncomingRetryMaxDelay);
    }

    function shouldQueueIncomingMessage() {
        return service.flushingIncomingMessages || service.pendingIncomingFlush || service.pendingIncomingMessages.length > 0;
    }

    function targetKey(name, payload) {
        if (payload == null) return name + "|global";
        const display = payload.i != null ? payload.i : "";
        const owner = payload.o != null ? payload.o : "";
        const component = payload.n != null ? payload.n : "";
        if (display === "" && owner === "" && component === "") return name + "|global";
        return owner + "|" + display + "|" + component;
    }

    function dispatchToListeners(listeners, payload) {
        let handled = false;
        listeners.forEach(function (listener) {
            const result = listener(payload) === true;
            if (result) handled = true;
        });
        return handled;
    }

    function sendPendingMessages(service) {
        for (let i = 0; i < service.pendingMessages.length; i++) {
            const pendingMessage = service.pendingMessages[i];
            service.send(pendingMessage.message, pendingMessage.app);
        }
        service.pendingMessages = [];
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
        return (service.connections[name] != null && service.connections[name].ready) || isPendingConnection(service, name);
    }

    function isPendingConnection(service, name) {
        for (let i=0; i<service.pendingConnections.length; i++) {
            if (service.pendingConnections[i] == name) return true;
        }
        return false;
    }

    return service;
})();

export default PushService;
