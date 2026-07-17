import * as React from "react";
import I18nComponent from "../../I18nComponent";

class GoogleApiLoader extends I18nComponent {

    constructor(props) {
        super(props);
        this.scriptId = "alexandria-google-maps-script";
        this.state = {
            loaded: window.google != null && window.google.maps != null,
        };
    };

    componentDidMount() {
        this.loadScript();
    };

    componentWillUnmount() {
        if (window.__alexandriaGoogleMapsInit === this.handleScriptLoad) delete window.__alexandriaGoogleMapsInit;
    };

    render() {
        if (!this.state.loaded) return (<React.Fragment/>);
        return (
            <React.Fragment>
                {this.props.children}
            </React.Fragment>
        );
    };

    loadScript = () => {
        if (this.isGoogleMapsReady()) {
            this.setState({ loaded: true }, this.notifyLoad);
            return;
        }

        const existingScript = document.getElementById(this.scriptId);
        if (existingScript != null) {
            if (existingScript.getAttribute("data-state") === "ready") {
                this.setState({ loaded: true }, this.notifyLoad);
                return;
            }
            existingScript.addEventListener("error", this.handleScriptError);
            return;
        }

        window.__alexandriaGoogleMapsInit = this.handleScriptLoad;

        const script = document.createElement("script");
        script.id = this.scriptId;
        script.type = "text/javascript";
        script.async = true;
        script.defer = true;
        script.src = this.scriptUrl();
        script.onerror = this.handleScriptError;
        document.head.appendChild(script);
    };

    scriptUrl = () => {
        const key = Application.configuration.googleApiKey;
        const params = [];
        if (key) params.push(`key=${key}`);
        params.push(`v=weekly`);
        params.push(`libraries=marker`);
        const language = this.language();
        if (language) params.push(`language=${language}`);
        const mapId = Application.configuration.googleMapId;
        if (mapId) params.push(`map_ids=${mapId}`);
        params.push(`loading=async`);
        params.push(`callback=__alexandriaGoogleMapsInit`);
        return `https://maps.googleapis.com/maps/api/js?${params.join("&")}`;
    };

    handleScriptLoad = () => {
        if (!this.isGoogleMapsReady()) {
            window.setTimeout(this.handleScriptLoad, 50);
            return;
        }
        const script = document.getElementById(this.scriptId);
        if (script != null) script.setAttribute("data-state", "ready");
        this.setState({ loaded: true }, this.notifyLoad);
    };

    handleScriptError = () => {
        const script = document.getElementById(this.scriptId);
        if (script != null) script.setAttribute("data-state", "error");
    };

    notifyLoad = () => {
        if (this.props.onLoad) this.props.onLoad();
    };

    isGoogleMapsReady = () => {
        return window.google != null &&
            window.google.maps != null &&
            typeof window.google.maps.Map === "function";
    };

}

export default GoogleApiLoader;
