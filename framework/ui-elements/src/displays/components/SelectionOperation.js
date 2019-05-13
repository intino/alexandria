import React from "react";
import AbstractSelectionOperation from "../../../gen/displays/components/AbstractSelectionOperation";
import SelectionOperationNotifier from "../../../gen/displays/notifiers/SelectionOperationNotifier";
import SelectionOperationRequester from "../../../gen/displays/requesters/SelectionOperationRequester";

export default class SelectionOperation extends AbstractSelectionOperation {
	constructor(props) {
		super(props);
		this.notifier = new SelectionOperationNotifier(this);
		this.requester = new SelectionOperationRequester(this);
	};
}