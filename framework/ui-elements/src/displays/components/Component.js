import React from "react";
import AlexandriaDisplay from "../Display";
import Theme from "app-elements/gen/Theme";
import ComponentNotifier from "../notifiers/ComponentNotifier";
import {RiseLoader} from "react-spinners";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import CookieConsent, { Cookies } from "react-cookie-consent";

export default class Component extends AlexandriaDisplay {

    state = {
        loading: true,
        visible: this.props.visible != null ? this.props.visible : true,
        color: this.props.color != null ? this.props.color : null
    };

    constructor(props) {
        super(props);
    };

    style() {
        let style = this._addFormats();
        style = this._addSpacing(style);
        return style;
    };

    userMessage = (info) => {
        this.showMessage(info.message, info.type);
    };

    renderDynamicLoaded(components) {
        const theme = Theme.get();
        return (
            <React.Fragment>
                {this.state.loading ? <div style={{position:'absolute',top:'50%',left:'43%'}}><RiseLoader color={theme.palette.secondary.main} loading={this.state.loading}/></div> : undefined }
                <div style={ { visibility: this.state.loading ? "hidden" : "" } }>{components}</div>
            </React.Fragment>
        );
    };

    variant = (defaultVariant) => {
        return ComponentBehavior.variant(this.props, defaultVariant);
    };

    refreshLoading = (loading) => {
        this.setState({ loading: loading });
    };

    refreshVisibility = (value) => {
        this.setState({ visible: value });
    };

    refreshColor = (color) => {
        this.setState({ color: color });
    };

    _addFormats() {
        const { format } = this.props;
        const formats = format != null ? format.split(" ") : [];
        if (formats.length <= 0) return undefined;
        const theme = Theme.get();
        const result = {};
        formats.forEach(f => {
            let style = theme.formats[f];
            if (style == null) return;
            for (let rule in style) result[rule] = style[rule];
        });
        return result;
    };

    applyStyles = (styles, to) => {
        if (styles == null) return;
        for (let rule in styles) to[rule] = styles[rule];
    };

    _addSpacing(style) {
        if (style == null) style = {};
        if (this.props.spacingstyle == null) return style;
        const spacingStyle = this.props.spacingstyle;
        if (spacingStyle.bottom != null) style.marginBottom = spacingStyle.bottom + "px";
        if (spacingStyle.right != null) style.marginRight = spacingStyle.right + "px";
        return style;
    };

    _widthDefined = () => {
        return this.props.width != null && this.props.width.indexOf("-1") === -1;
    };

    _heightDefined = () => {
        return this.props.height != null && this.props.height.indexOf("-1") === -1;
    };

    _trace = (value) => {
        if (!this.props.traceable) return;
        if (!this._traceConsentAccepted()) return;
        Cookies.set(this.props.id, encodeURIComponent(JSON.stringify(value)));
    };

    _traceValue = () => {
        if (!this.props.traceable) return null;
        if (!this._traceConsentAccepted()) return null;
        const value = Cookies.get(this.props.id);
        return value != null ? JSON.parse(decodeURIComponent(value)) : null;
    };

    _traceConsentAccepted = () => {
        return Cookies.get(this._traceConsentVariable()) != null;
    };

    _traceConsent = () => {
        if (!this.props.traceable) return (<React.Fragment/>);
        return (
            <CookieConsent cookieName={this._traceConsentVariable()} buttonText={this.translate("I understand")} buttonStyle={{fontSize:'11pt'}}>
                <div style={{textAlign:'left',fontSize:'11pt'}}>{this.translate("This website uses cookies to enhance the user experience.")}</div>
            </CookieConsent>
        );
    };

    _traceConsentVariable = () => {
        return Application.configuration.url.replace(/[^\w\s]/gi, '');
    };
}