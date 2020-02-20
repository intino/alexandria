import React from "react";
import AbstractBaseSelectionAction from "../../../gen/displays/components/AbstractBaseSelectionAction";
import BaseSelectionActionNotifier from "../../../gen/displays/notifiers/BaseSelectionActionNotifier";
import BaseSelectionActionRequester from "../../../gen/displays/requesters/BaseSelectionActionRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class BaseSelectionAction extends AbstractBaseSelectionAction {
	constructor(props) {
		super(props);
		this.notifier = new BaseSelectionActionNotifier(this);
		this.requester = new BaseSelectionActionRequester(this);
	};
}