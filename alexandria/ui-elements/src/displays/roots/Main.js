import React from "react";
import AbstractMain from "../../../gen/displays/roots/AbstractMain";
import MainNotifier from "../../../gen/displays/notifiers/MainNotifier";
import MainRequester from "../../../gen/displays/requesters/MainRequester";

export default class Main extends AbstractMain {

	constructor(props) {
		super(props);
		this.notifier = new MainNotifier(this);
		this.requester = new MainRequester(this);
	};

}