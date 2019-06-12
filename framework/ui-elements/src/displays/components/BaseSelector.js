import React from "react";
import AbstractBaseSelector from "../../../gen/displays/components/AbstractBaseSelector";
import BaseSelectorNotifier from "../../../gen/displays/notifiers/BaseSelectorNotifier";
import BaseSelectorRequester from "../../../gen/displays/requesters/BaseSelectorRequester";

export default class BaseSelector extends AbstractBaseSelector {

	constructor(props) {
		super(props);
		this.notifier = new BaseSelectorNotifier(this);
		this.requester = new BaseSelectorRequester(this);
	};


}