import * as React from "react";
import I18nComponent from "../../I18nComponent";
import { LoadScript } from "@react-google-maps/api";

class GoogleApiLoader extends I18nComponent {

    constructor(props) {
        super(props);
    };

    render() {
        const key = Application.configuration.googleApiKey;
        return (<LoadScript onLoad={this.notifyLoad.bind(this)} libraries={["visualization","drawing"]} googleMapsApiKey={key}>{this.props.children}</LoadScript>);
    };

    notifyLoad = () => {
        if (this.props.onLoad) this.props.onLoad();
    };

}

export default GoogleApiLoader;