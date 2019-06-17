import React from "react";
import PassiveView from "./PassiveView";
import Typography from "@material-ui/core/Typography";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Display extends PassiveView {
    state = {};

    constructor(props) {
        super(props);
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
        for (var i = 0; i < instances.length; i++)
            if (instances[i].pl.id === id) break;
        if (i >= instances.length) return;
        this._registerInstances(container, instances.splice(i, 1));
    };

    clearContainer = (params) => {
        this._registerInstances(params.c, []);
    };

    instances = () => {
        return this.instances("__elements");
    };

    instances = (container) => {
        return this.state[container] != null ? this.state[container] : [];
    };

    renderInstances = (container, props, style) => {
        if (container == null) container = "__elements";
        let instances = this.state[container];
        if (instances == null || instances.length <= 0) {
            if (props != null && props.noItemsMessage != null) return (<Typography>{this.translate(props.noItemsMessage)}</Typography>);
            return;
        }
        return instances.map((instance, index) => {
            instance.pl.context = () => { return instance.pl.o };
            instance.pl.owner = () => { return instance.i };
            this.copyProps(props, instance.pl);
            return (<div key={index} style={style}>{React.createElement(DisplayFactory.get(instance.tp), instance.pl)}</div>);
        });
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
        const options = { variant: type.toLowerCase(), autoHideDuration: 2000, anchorOrigin: { vertical: 'top', horizontal: 'center' }};
        if (this.messageTimeout != null) window.clearTimeout(this.messageTimeout);
        this.messageTimeout = window.setTimeout(() => this.props.enqueueSnackbar(message, options), 100);
    };

    componentWillUnmount() {
        if (this.notifier != null) this.notifier.detached();
    };

    attribute = (name) => {
        return this.state[name] != null ? this.state[name] : this.props[name];
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

}