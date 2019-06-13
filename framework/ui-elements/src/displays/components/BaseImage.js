import React from "react";
import AbstractBaseImage from "../../../gen/displays/components/AbstractBaseImage";

export default class BaseImage extends AbstractBaseImage {

	constructor(props) {
		super(props);
	};

	refresh = (value) => {
		this.setState({ value: value });
	};
}