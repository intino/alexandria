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

    ownerUnit = () => {
        return this.context !== "" ? this.context : null;
    };
}