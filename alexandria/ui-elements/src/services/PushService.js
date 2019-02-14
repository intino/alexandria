const PushService = (function () {
    var socketUrl = {};
    var callbacks = {};
    var service = {};

    service.retries = 0;
    service.openConnection = function (url) {
        var ws = new WebSocket(url);
        socketUrl = url;

        ws.onopen = function(e) {
            console.log("WebSocket connection opened.");
            service.retries = 0;
        };

        ws.onmessage = function (event) {
            var data = JSON.parse(event.data);
            var callbacks = callback(data.name).slice(0);
            callbacks.forEach(function (callback) {
                callback(data.parameters);
            });
        };

        ws.onclose = function(e) {
            if (service.retries >= 3) {
                this.notifyClose();
                return;
            }

            this.reconnect(e);
        };

        ws.reconnect = function(e) {
            try {
                service.retries++;
                console.log("WebSocket connection lost. Status code: " + e.code + ". Retry: " + service.retries + ".");
                service.openConnection(socketUrl);
            }
            catch(e) {
                window.setTimeout(service.openConnection(socketUrl), 1000*2);
            }
        }

        ws.notifyClose = function() {
            var cottonNetwork = document.querySelector("cotton-push-network");
            if (cottonNetwork == null) {
                cottonNetwork = document.createElement("cotton-push-network");
                document.body.appendChild(cottonNetwork);
            }
            cottonNetwork.open();
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
    };

    function callback(name) {
        return name in callbacks ? callbacks[name] : [];
    }

    return service;
})();

export default PushService;