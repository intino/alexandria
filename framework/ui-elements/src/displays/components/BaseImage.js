import React from "react";
import AbstractBaseImage from "../../../gen/displays/components/AbstractBaseImage";

export default class BaseImage extends AbstractBaseImage {

	constructor(props) {
		super(props);
		this.reloaded = React.createRef();
	};

	refresh = (value) => {
	    this.reloaded.current = false;
		this.setState({ value: value });
	};

}