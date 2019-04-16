import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractImageExamplesMold from "../../../gen/displays/templates/AbstractImageExamplesMold";
import ImageExamplesMoldNotifier from "../../../gen/displays/notifiers/ImageExamplesMoldNotifier";
import ImageExamplesMoldRequester from "../../../gen/displays/requesters/ImageExamplesMoldRequester";

const styles = theme => ({});

class ImageExamplesMold extends AbstractImageExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new ImageExamplesMoldNotifier(this);
		this.requester = new ImageExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(ImageExamplesMold);