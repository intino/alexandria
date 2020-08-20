import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDisplayStamp from "../../../gen/displays/components/AbstractDisplayStamp";
import DisplayStampNotifier from "../../../gen/displays/notifiers/DisplayStampNotifier";
import DisplayStampRequester from "../../../gen/displays/requesters/DisplayStampRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class DisplayStamp extends AbstractDisplayStamp {

	constructor(props) {
		super(props);
		this.notifier = new DisplayStampNotifier(this);
		this.requester = new DisplayStampRequester(this);
        this.state = {
            ...this.state,
            display : null
        };
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		return (<div style={this.style()}>{this.renderInstances(null, null, this.style())}</div>);
	};

	refreshDisplay = () => {
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(DisplayStamp));
DisplayFactory.register("DisplayStamp", withStyles(styles, { withTheme: true })(withSnackbar(DisplayStamp)));