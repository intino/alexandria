import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseImage from "../../../gen/displays/components/AbstractBaseImage";
import BaseImageNotifier from "../../../gen/displays/notifiers/BaseImageNotifier";
import BaseImageRequester from "../../../gen/displays/requesters/BaseImageRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BaseImage extends AbstractBaseImage {

	constructor(props) {
		super(props);
		this.notifier = new BaseImageNotifier(this);
		this.requester = new BaseImageRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BaseImage);
DisplayFactory.register("BaseImage", withStyles(styles, { withTheme: true })(BaseImage));