import $ from 'jquery';

const FileService = {
    create : function (configuration) {

        return {
            upload: (message, successCallback, failureCallback) => {
                var request = buildRequest(message);

                var options = {
                    method: "POST",
                    cache: false,
                    url: buildUrl(message),
                    processData: !request.multipart,
                    data: request
                };

                if (request.multipart) {
                    options.contentType = false;
                }

                $.ajax(options).done(function(data) {
                    if (successCallback) successCallback(data);
                }).fail(function(error, textStatus, errorThrown) {
                    if (error.status == 501) {
                        if (failureCallback) {
                            failureCallback(JSON.parse(error.responseText));
                        }
                        else if (error.responseText.contains("err:cnf")) {
                            var element = document.body.querySelector("cotton-carrier-connection-lost");
                            if (element != null) { element.show(); }
                        }
                    }
                });
            },

            download: (message) => {
                var url = buildUrl(message) + '?' + $.param(buildRequest(message));
                var a = document.createElement('A');
                a.href = url;
                a.download = url;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
            }
        };

        function buildUrl(message) {
            return configuration.appUrl(message.app) + "/" + message.s.toLowerCase() + (message.d != null ? "/" + (message.o + ":" + message.c + ":" + message.d) : "");
        }

        function buildRequest(message) {
            var multipart = isFile(message.v);
            var request = emptyRequest(multipart);
            addParameter(request, "op", message.op);
            addParameter(request, "clientId", configuration.clientId);
            addParameter(request, "v", message.v);
            return request;
        }

        function emptyRequest(multipart) {
            var result = multipart ? new FormData() : {};
            result.multipart = multipart;
            return result;
        }

        function addParameter(request, parameter, raw) {
            if (request.multipart) {
                request.append(parameter, ((typeof raw) === "string" || isFile(raw)) ? raw : encodeURIComponent(JSON.stringify(raw)));
            } else {
                request[parameter] = ((typeof raw) === "string" || isFile(raw)) ? raw : encodeURIComponent(JSON.stringify(raw));
            }
        }

        function isFile(value) {
            if (value instanceof FileList) return true;
            return value != null && value.name != null && value.size != null && value.type != null;
        }

    }
};

export default FileService;