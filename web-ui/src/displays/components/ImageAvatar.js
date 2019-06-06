import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractImageAvatar from "../../../gen/displays/components/AbstractImageAvatar";
import ImageAvatarNotifier from "../../../gen/displays/notifiers/ImageAvatarNotifier";
import ImageAvatarRequester from "../../../gen/displays/requesters/ImageAvatarRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class ImageAvatar extends AbstractImageAvatar {

	constructor(props) {
		super(props);
		this.notifier = new ImageAvatarNotifier(this);
		this.requester = new ImageAvatarRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(ImageAvatar);
DisplayFactory.register("ImageAvatar", withStyles(styles, { withTheme: true })(ImageAvatar));