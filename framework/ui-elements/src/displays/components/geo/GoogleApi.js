import * as React from "react";
import I18nComponent from "../../I18nComponent";
import { LoadScript } from "@react-google-maps/api";

class GoogleApi extends I18nComponent {
    static Loading = false;

    state = {
        timeout: null,
        loaded: false
    };

    constructor(props) {
        super(props);
        this.timeout = window.setInterval(this.checkIfScriptLoaded, 200);
    };

    render() {
        if (this.state.loaded) {
            window.google = this.google;
            window.clearInterval(this.timeout);
            return this.props.children;
        }
        if (GoogleApi.Loading) return null;
        GoogleApi.Loading = true;
        const key = Application.configuration.googleApiKey;
        return (<LoadScript libraries={["visualization","drawing"]} googleMapsApiKey={key}/>);
    };

    checkIfScriptLoaded = () => {
        if (window.google) this.google = window.google;
        this.setState({ loaded: this.google != null });
    };

    componentWillUnmount () {
        window.clearInterval(this.timeout);
    };

}

export default GoogleApi;