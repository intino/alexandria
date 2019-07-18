import React from "react";
import AbstractSelectionOperation from "../../../gen/displays/components/AbstractSelectionOperation";
import SelectionOperationNotifier from "../../../gen/displays/notifiers/SelectionOperationNotifier";
import SelectionOperationRequester from "../../../gen/displays/requesters/SelectionOperationRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class SelectionOperation extends AbstractSelectionOperation {
	constructor(props) {
		super(props);
		this.notifier = new SelectionOperationNotifier(this);
		this.requester = new SelectionOperationRequester(this);
	};
}

DisplayFactory.register("SelectionOperation", SelectionOperation);