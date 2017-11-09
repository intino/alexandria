var AlpacaFileInputWidget = function() {
    function widget() {}

    widget.prototype = new AlpacaInputWidget();
    widget.prototype.template = function() { return '<div class="component"></div><button type="button" class="btn btn-default download" aria-label="Left Align"><span class="glyphicon glyphicon-download" aria-hidden="true"></span><span class="title"></span></button>'; };

    widget.prototype.render = function(input, container, view) {
        AlpacaInputWidget.prototype.render.call(this, input, container, view);
        _configureDownload(this, input, container, view);
    };

    widget.prototype.converter = function() {
        return new AlpacaFileInputConverter();
    };

    widget.prototype.addEvents = function(input, alpacaInput, container) {
        var widget = this;
        alpacaInput.onChange = function(event) {
            widget.setDownloadVisibility(container, true);
        }
    };

    widget.prototype.setDownloadVisibility = function(container, visible) {
        var downloadButton = $(container.querySelector(".download"));
        downloadButton.get(0).style.display = visible ? "block" : "none";
    };

    widget.upload = function(input, files) {
        toBase64(files[0], function(base64File) {
            document.resourceManager.uploadResource({
                path: document.pathOf(input),
                file: {
                    name: files[0].name,
                    value: base64File
                }
            });
        });
    };

    function _configureDownload(widget, input, container, view) {
        var download = container.querySelector(".download .title");
        download.innerHTML = DialogAlpacaDictionary.labelFor("download");

        var downloadButton = $(container.querySelector(".download"));
        downloadButton.get(0).style.display = input.value != null && input.value != "" ? "block" : "none";
        downloadButton.on("click", function() {
            document.resourceManager.downloadResource(input.label);
        });
    }

    function toBase64(file, callback, failureCallback) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function () {
            callback(reader.result);
        };
        reader.onerror = function (error) {
            failureCallback('Error: ', error);
        };
    }

    return widget;
}();

var AlpacaFileInputConverter = function() {

    function converter() {}
    converter.prototype = new AlpacaInputConverter();

    converter.prototype.schema = function(input) {
        var result = AlpacaInputConverter.prototype.schema.call(this, input);
        result.type = "string";
        result.format = "uri";
        return result;
    };

    converter.prototype.options = function (input) {
        var result = AlpacaInputConverter.prototype.options.call(this, input);
        result.type = "file";
        result.label = input.label;
        result.events = addEvents(result, input);
        return result;
    };

    function addEvents(alpacaInput, input) {
        return {
            change : function(event) {
                AlpacaFileInputWidget.upload(input, event.target.files);
            }
        }
    }

    return converter;
}();
