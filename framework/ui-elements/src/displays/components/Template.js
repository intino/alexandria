import React from "react";
import AbstractTemplate from "../../../gen/displays/components/AbstractTemplate";
import TemplateNotifier from "../../../gen/displays/notifiers/TemplateNotifier";
import TemplateRequester from "../../../gen/displays/requesters/TemplateRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import BrowserUtil from "../../util/BrowserUtil";

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

	fixHeight = (height) => {
	    return height != null && height === "100.0%" && BrowserUtil.isFirefox() && this.isRoot() ? "100vh" : height;
	};

	isRoot = () => {
	    return this.props.id != null && this.props.id.indexOf(".") == -1;
	};

}

DisplayFactory.register("Template", Template);