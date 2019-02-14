import React from "react";
import AbstractEditor from "../../gen/displays/AbstractEditor";
import EditorNotifier from "../../gen/displays/notifiers/EditorNotifier";
import EditorRequester from "../../gen/displays/requesters/EditorRequester";

export default class Editor extends AbstractEditor {

	constructor(props) {
		super(props);
		this.notifier = new EditorNotifier(this);
		this.requester = new EditorRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}