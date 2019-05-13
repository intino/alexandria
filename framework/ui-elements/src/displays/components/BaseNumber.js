import React from "react";
import AbstractBaseNumber from "../../../gen/displays/components/AbstractBaseNumber";
import BaseNumberNotifier from "../../../gen/displays/notifiers/BaseNumberNotifier";
import BaseNumberRequester from "../../../gen/displays/requesters/BaseNumberRequester";

export default class BaseNumber extends AbstractBaseNumber {

	constructor(props) {
		super(props);
		this.notifier = new BaseNumberNotifier(this);
		this.requester = new BaseNumberRequester(this);
	};

	refresh = (value) => {
	};
}