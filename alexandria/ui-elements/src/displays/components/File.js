import React from "react";
import AbstractFile from "../../gen/displays/components/AbstractFile";
import FileNotifier from "../../gen/displays/notifiers/FileNotifier";
import FileRequester from "../../gen/displays/requesters/FileRequester";

export default class File extends AbstractFile {

	constructor(props) {
		super(props);
		this.notifier = new FileNotifier(this);
		this.requester = new FileRequester(this);
	};

	render() {
		return (
			<React.Fragment></React.Fragment>
		);
	};

	refresh = (value) => {
	};
}