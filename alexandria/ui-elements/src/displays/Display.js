import React from "react";
import PassiveView from "./PassiveView";
import Typography from "@material-ui/core/Typography";
import * as Elements from "app-elements/gen/Displays";

export default class Display extends PassiveView {
    state = {};

    constructor(props) {
        super(props);
        this.translator = Application.services.translatorService;
    };

    instanceId() {
        return this.props.id;
    };

    addInstance = (instance) => {
        let container = instance.c;
        let instances = this.state[container];
        if (instances == null) instances = [];
        instances.push(instance);
        this._updateInstancesState(container, instances);
    };

    removeInstance = (params) => {
        let id = params.id;
        let container = params.c;
        const instances = this.state[container];
        for (var i = 0; i < instances.length; i++)
            if (instances[i].i === id) break;
        if (i >= instances.length) return;
        this._updateInstancesState(container, instances.splice(index, 1));
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
            instance.pl.context = () => { return instance.pl.o};
            this.copyProps(props, instance.pl);
            return (<div key={index} style={style}>{React.createElement(Elements[instance.tp], instance.pl)}</div>);
        });
    };

    translate = (word) => {
        return this.translator.translate(word);
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

    _updateInstancesState = (container, instances) => {
        let object = {};
        object[container] = instances;
        this.setState(object);
    };

    componentWillUnmount() {
        if (this.notifier != null) this.notifier.detached();
    }
}