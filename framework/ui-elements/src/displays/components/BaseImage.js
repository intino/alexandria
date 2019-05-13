import React from "react";
import AbstractBaseImage from "../../../gen/displays/components/AbstractBaseImage";
import BaseImageNotifier from "../../../gen/displays/notifiers/BaseImageNotifier";
import BaseImageRequester from "../../../gen/displays/requesters/BaseImageRequester";

export default class BaseImage extends AbstractBaseImage {

	constructor(props) {
		super(props);
		this.notifier = new BaseImageNotifier(this);
		this.requester = new BaseImageRequester(this);
	};

	refresh = (value) => {
	};
}