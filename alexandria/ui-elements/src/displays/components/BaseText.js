import React from "react";
import AbstractBaseText from "../../../gen/displays/components/AbstractBaseText";
import BaseTextNotifier from "../../../gen/displays/notifiers/BaseTextNotifier";
import BaseTextRequester from "../../../gen/displays/requesters/BaseTextRequester";

export default class BaseText extends AbstractBaseText {

	constructor(props) {
		super(props);
		this.notifier = new BaseTextNotifier(this);
		this.requester = new BaseTextRequester(this);
	};

	refresh = (value) => {
	};
}