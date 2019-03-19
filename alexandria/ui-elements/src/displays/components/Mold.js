import React from "react";
import AbstractMold from "../../../gen/displays/components/AbstractMold";
import MoldNotifier from "../../../gen/displays/notifiers/MoldNotifier";
import MoldRequester from "../../../gen/displays/requesters/MoldRequester";

export default class Mold extends AbstractMold {
	state = {};

	constructor(props) {
		super(props);
		this.notifier = new MoldNotifier(this);
		this.requester = new MoldRequester(this);
	};

}