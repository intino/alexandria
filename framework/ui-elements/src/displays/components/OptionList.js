import React from "react";
import AbstractOptionList from "../../../gen/displays/components/AbstractOptionList";
import OptionListNotifier from "../../../gen/displays/notifiers/OptionListNotifier";
import OptionListRequester from "../../../gen/displays/requesters/OptionListRequester";

export default class OptionList extends AbstractOptionList {

	constructor(props) {
		super(props);
		this.notifier = new OptionListNotifier(this);
		this.requester = new OptionListRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}