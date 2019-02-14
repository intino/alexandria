import React from "react";

export default class PassiveView extends React.Component {

    constructor(props) {
        super(props);
        this.notifier = null;
        this.requester = null;
    };

    name = () => {
        return this.constructor.name;
    }

}