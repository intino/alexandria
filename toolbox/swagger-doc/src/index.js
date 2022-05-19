import './index.css'
import 'swagger-ui/dist/swagger-ui.css'

import $ from 'jquery'
import SwaggerUIBundle from 'swagger-ui'
import SwaggerUIStandalonePreset from 'swagger-ui/dist/swagger-ui-standalone-preset'
import CustomizePlugin from './plugins/CustomizePlugin'

$.getJSON('./config.json', config => {

    var _url = config.url ? config.url : undefined;
    var _urls = config.urls ? config.urls : undefined;
    var _primary = config.primary ? config.primary : undefined;

    // Transform url to urls if necessary
    _urls = (!_urls && _url) ? [{ "url": _url, "name": "" }] : _urls;

    // Update header
    $(document).attr("title", config.title);

    SwaggerUIBundle({
        dom_id: "#swagger-ui",
        urls: _urls,
        "urls.primaryName": _primary,
        deepLinking: true,
        filter: false,
        validatorUrl: null,
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        plugins: [
            SwaggerUIBundle.plugins.DownloadUrl,
            CustomizePlugin
        ],
        layout: "StandaloneLayout",

        custom: {
            title: config.title,
            subtitle: config.subtitle,
            color: config.color,
            background: config.background,
            selectorBorderColor: config.selectorBorderColor
        }
    });
});