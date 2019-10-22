import React from "react";
import AbstractBaseNumber from "../../../gen/displays/components/AbstractBaseNumber";

export default class BaseNumber extends AbstractBaseNumber {

	constructor(props) {
		super(props);
	};

	refresh = (value) => {
	};

	refreshExpanded = (value) => {
		this.setState({expanded : value});
	};

	refreshPrefix = (value) => {
		this.setState({prefix : value});
	};

	refreshSuffix = (value) => {
		this.setState({suffix : value});
	};
}