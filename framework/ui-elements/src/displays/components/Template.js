import React from "react";
import AbstractTemplate from "../../../gen/displays/components/AbstractTemplate";
import TemplateNotifier from "../../../gen/displays/notifiers/TemplateNotifier";
import TemplateRequester from "../../../gen/displays/requesters/TemplateRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Template extends AbstractTemplate {

	constructor(props) {
		super(props);
		this.state = {
		    ...this.state,
		    canClose : true
		};
	};

	enableCloseManager = () => {
	    const template = this;
	    window.addEventListener("beforeunload", function(event) {
	        if (template.state.canClose) return;
            event.returnValue = true;
            return true;
        });
	};

	canClose = (value) => {
	    this.setState({ canClose : value });
	};

}

DisplayFactory.register("Template", Template);