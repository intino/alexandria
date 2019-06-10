const PushService = (function () {
    var socketUrl = {};
    var callbacks = {};
    var service = {};
    var ws = null;

    service.retries = 0;
    service.openConnection = function (url) {
        this.ws = new WebSocket(url);
        socketUrl = url;

        this.ws.onopen = function(e) {
            console.log("WebSocket connection opened.");
            service.retries = 0;
        };

        this.ws.onmessage = function (event) {
            var data = JSON.parse(event.data);
            var callbacks = callback(data.n).slice(0);
            callbacks.forEach(function (callback) {
                callback(data.p);
            });
        };

        this.ws.onclose = function(e) {
            if (service.retries >= 3) {
                this.notifyClose();
                return;
            }

            this.reconnect(e);
        };

        this.ws.reconnect = function(e) {
            try {
                service.retries++;
                console.log("WebSocket connection lost. Status code: " + e.code + ". Retry: " + service.retries + ".");
                service.openConnection(socketUrl);
            }
            catch(e) {
                window.setTimeout(service.openConnection(socketUrl), 1000*2);
            }
        }

        this.ws.notifyClose = function() {
            if (service.onCloseListener) service.onCloseListener();
            callbacks = {};
        };
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

    service.send = function(message) {
        this.ws.send(JSON.stringify(message));
    };

    service.onClose = function(listener) {
        this.onCloseListener = listener;
    };

    function callback(name) {
        return name in callbacks ? callbacks[name] : [];
    }

    return service;
})();

export default PushService;