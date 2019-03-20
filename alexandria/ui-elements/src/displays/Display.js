import React from "react";
import PassiveView from "./PassiveView";
import Typography from "@material-ui/core/Typography";
import * as Elements from "app-elements/gen/Displays";

export default class Display extends PassiveView {

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

    renderInstances = (container, emptyMessage) => {
        if (container == null) container = "__elements";
        var instances = this.state[container];
        if (instances == null || instances.length <= 0) {
            if (emptyMessage) return (<Typography>{this.translate(emptyMessage)}</Typography>);
            return;
        }
        return instances.map((instance, index) => {
            instance.pl.context = () => { return instance.pl.o };
            return (<div key={index}>{React.createElement(Elements[instance.tp], instance.pl)}</div>);
        });
    };

    translate = (word) => {
        return this.translator.translate(word);
    };

    _updateInstancesState = (container, instances) => {
        let object = {};
        object[container] = instances;
        this.setState(object);
    };
}