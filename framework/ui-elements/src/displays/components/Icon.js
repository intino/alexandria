import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
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
		return this.renderLayer(<img src={this._icon()} title={this._title()} style={{width:"24px",height:"24px",...this.style()}}/>);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Icon));
DisplayFactory.register("Icon", withStyles(styles, { withTheme: true })(withSnackbar(Icon)));