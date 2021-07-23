import React from "react";
import I18nComponent from "./I18nComponent";

export const OwnerUnitContext = React.createContext('');

export default class PassiveView extends I18nComponent {
    static contextType = OwnerUnitContext;

    constructor(props) {
        super(props);
        this.notifier = null;
        this.requester = null;
    };

    name = () => {
        return this.constructor.name;
    };

    shortId() {
        if (this.props.id == null) return null;
        return this.props.id.substring(this.props.id.lastIndexOf(".")+1);
    };

    ownerUnit = () => {
        return this.context !== "" ? this.context : null;
    };
}