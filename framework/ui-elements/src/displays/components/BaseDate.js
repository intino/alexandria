import React from "react";
import AbstractBaseDate from "../../../gen/displays/components/AbstractBaseDate";
import BaseDateNotifier from "../../../gen/displays/notifiers/BaseDateNotifier";
import BaseDateRequester from "../../../gen/displays/requesters/BaseDateRequester";

export default class BaseDate extends AbstractBaseDate {

	constructor(props) {
		super(props);
		this.notifier = new BaseDateNotifier(this);
		this.requester = new BaseDateRequester(this);
	};

	refresh = (value) => {
	};
}