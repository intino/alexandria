import React from "react";
import Component from "../Component";
import OptionListNotifier from "../../../gen/displays/notifiers/OptionListNotifier";
import OptionListRequester from "../../../gen/displays/requesters/OptionListRequester";

export default class OptionList extends Component {

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