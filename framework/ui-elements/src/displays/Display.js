import React from "react";
import PassiveView from "./PassiveView";
import Typography from "@material-ui/core/Typography";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import CookieConsent, { Cookies } from "react-cookie-consent";

export const enrichDisplayProperties = (instance) => {
    instance.pl.context = () => { return instance.pl.o };
    instance.pl.owner = () => { return instance.i != null ? instance.i : instance.pl.id };
};

export default class Display extends PassiveView {
    static TraceConsentRendered;
    address = null;

    constructor(props) {
        super(props);
        this.state = {
            traceable: this.props.traceable
        }
    };

    addInstance = (instance) => {
        let instances = this.instances(instance.c);
        instances.push(instance);
        this._registerInstances(instance.c, instances);
    };

    addInstances = (params) => {
        let container = params.c;
        let currentInstances = this.instances(container);
        params.value.forEach(instance => currentInstances.push(instance));
        this._registerInstances(container, currentInstances);
    };

    insertInstance = (instance) => {
        let instances = this.instances(instance.c);
        instances[instance.idx] = instance;
        this._registerInstances(instance.c, instances);
    };

    insertInstances = (params) => {
        let container = params.c;
        let currentInstances = this.instances(container);
        params.value.forEach(instance => currentInstances[instance.idx] = instance);
        this._registerInstances(container, currentInstances);
    };

    removeInstance = (params) => {
        let id = params.id;
        let container = params.c;
        const instances = this.instances(container);
        let newInstances = [];
        for (var i = 0; i < instances.length; i++) {
            if (instances[i].pl.id === id) continue;
            newInstances.push(instances[i]);
        }
        this._registerInstances(container, newInstances);
    };

    clearContainer = (params) => {
        this._registerInstances(params.c, []);
    };

    redirect = (params) => {
        let url = params.url;
        if (url == null || url === "") url = Application.configuration.baseUrl;
        window.location.href = url;
    };

    addressed = (params) => {
        this.address = Application.configuration.basePath + params.address;
    };

    historyAddress = () => {
        return this.address;
    };

    instances = (container) => {
        if (container == null) container = "__elements";
        return this.state[container] != null ? this.state[container] : [];
    };

    renderInstances = (container, props, style) => {
        let instances = this.instances(container);
        if (instances == null || instances.length <= 0) return this.renderEmptyInstances(props);
        return instances.map((instance, index) => {
            enrichDisplayProperties(instance);
            this.copyProps(props, instance.pl);
            return this.renderInstance(instance, props, style, index)
        });
    };

    renderEmptyInstances = (props) => {
        return props != null && props.noItemsMessage != null ? (<Typography style={{margin:'5px 0'}} variant="body1">{this.translate(props.noItemsMessage)}</Typography>) : undefined;
    };

    renderInstance = (instance, props, style, index) => {
        return (<div key={index} style={style}>{React.createElement(DisplayFactory.get(instance.tp), instance.pl)}</div>);
    };

    buildApplicationUrl = (path) => {
        let configuration = Application.configuration;
        let url = configuration.baseUrl;
        if (configuration.basePath !== "") url += basePath;
        return url + path;
    };

    copyProps = (from, to, excludedList) => {
        excludedList = excludedList != null ? excludedList : "";
        excludedList += "id,context";
        for (var index in from) {
            if (excludedList != null && excludedList.indexOf(index) !== -1) continue;
            to[index] = from[index];
        }
    };

    showError = (error) => {
        this.showMessage(error, 'error');
    };

    showMessage = (message, type) => {
        const loading = type.toLowerCase() === "loading";
        const messageType = loading ? "info" : type.toLowerCase();
        const options = { variant: messageType, autoHideDuration: !loading ? 2000 : undefined, anchorOrigin: { vertical: 'top', horizontal: 'center' }};
        if (this.snack != null) this.props.closeSnackbar(this.snack);
        if (this.messageTimeout != null) window.clearTimeout(this.messageTimeout);
        this.messageTimeout = window.setTimeout(() => {
            const snack = this.props.enqueueSnackbar(message, options);
            if (loading) this.snack = snack;
        }, 100);
    };

    componentWillUnmount() {
        if (this.notifier != null) this.notifier.detached();
    };

    attribute = (name) => {
        return this.state[name] != null ? this.state[name] : this.props[name];
    };

//    _context(container) {
//        var result = (this.props.context != null ? this.props.context() + "." : "") + this.props.id;
//        if (container != null) result += "." + container;
//        return result;
//    };

    trace = (value, name) => {
        if (!this.state.traceable) return;
        if (!this._traceConsentAccepted()) return;
        Cookies.set(name != null ? name : this.props.id, JSON.stringify(value));
    };

    traceValue = (name) => {
        if (!this.state.traceable) return null;
        if (!this._traceConsentAccepted()) return null;
        const value = Cookies.get(name != null ? name : this.props.id);
        return value != null ? JSON.parse(value) : null;
    };

    renderTraceConsent = () => {
        if (!this.state.traceable) return (<React.Fragment/>);
        if (Display.TraceConsentRendered != undefined && Display.TraceConsentRendered != this.props.id) return (<React.Fragment/>);
        Display.TraceConsentRendered = this.props.id;
        return (
            <CookieConsent cookieName={this._traceConsentVariable()} buttonText={this.translate("I understand")} buttonStyle={{fontSize:'11pt'}}>
                <div style={{textAlign:'left',fontSize:'11pt'}}>{this.translate("This website uses cookies to enhance the user experience.")}</div>
            </CookieConsent>
        );
    };

    _context() {
        return (this.props.owner != null ? this.props.owner() + "." : "") + this.props.id;
    };

    _owner() {
        return this.props.id;
    };

    _registerInstances = (container, instances) => {
        let object = {};
        object[container] = instances;
        this.setState(object);
    };

    _traceConsentAccepted = () => {
        return Cookies.get(this._traceConsentVariable()) != null;
    };

    _traceConsentVariable = () => {
        return Application.configuration.url.replace(/[^\w\s]/gi, '');
    };
}