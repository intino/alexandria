import React from "react";
import AbstractTemplate from "../../../gen/displays/components/AbstractTemplate";
import TemplateNotifier from "../../../gen/displays/notifiers/TemplateNotifier";
import TemplateRequester from "../../../gen/displays/requesters/TemplateRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Template extends AbstractTemplate {

	constructor(props) {
		super(props);
		// this.notifier = new TemplateNotifier(this);
		// this.requester = new TemplateRequester(this);
	};

}

DisplayFactory.register("Template", Template);