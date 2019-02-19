import React from "react";
import AbstractMenu from "../../gen/displays/components/AbstractMenu";
import MenuNotifier from "../../gen/displays/notifiers/MenuNotifier";
import MenuRequester from "../../gen/displays/requesters/MenuRequester";

export default class Menu extends AbstractMenu {

	constructor(props) {
		super(props);
		this.notifier = new MenuNotifier(this);
		this.requester = new MenuRequester(this);
	};

	render() {
		return (
			<React.Fragment></React.Fragment>
		);
	};


}