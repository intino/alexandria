import React from "react";
import AbstractBaseFile from "../../../gen/displays/components/AbstractBaseFile";
import BaseFileNotifier from "../../../gen/displays/notifiers/BaseFileNotifier";
import BaseFileRequester from "../../../gen/displays/requesters/BaseFileRequester";

export default class BaseFile extends AbstractBaseFile {

	constructor(props) {
		super(props);
		this.notifier = new BaseFileNotifier(this);
		this.requester = new BaseFileRequester(this);
	};

	refresh = (value) => {
	};
}