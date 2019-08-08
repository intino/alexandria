import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { withSnackbar } from 'notistack';
import AbstractIcon from "../../../gen/displays/components/AbstractIcon";
import IconNotifier from "../../../gen/displays/notifiers/IconNotifier";
import IconRequester from "../../../gen/displays/requesters/IconRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({});

class Icon extends AbstractIcon {
	constructor(props) {
		super(props);
		this.notifier = new IconNotifier(this);
		this.requester = new IconRequester(this);
	};

	render() {
		return this.renderLayer(<img src={this._icon()} style={{width:"24px",height:"24px"}}/>);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Icon));
DisplayFactory.register("Icon", withStyles(styles, { withTheme: true })(withSnackbar(Icon)));