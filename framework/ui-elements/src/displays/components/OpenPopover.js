import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenPopover from "../../../gen/displays/components/AbstractOpenPopover";
import OpenPopoverNotifier from "../../../gen/displays/notifiers/OpenPopoverNotifier";
import OpenPopoverRequester from "../../../gen/displays/requesters/OpenPopoverRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class OpenPopover extends AbstractOpenPopover {

	constructor(props) {
		super(props);
		this.notifier = new OpenPopoverNotifier(this);
		this.requester = new OpenPopoverRequester(this);
		this.state = {
		    ...this.state,
		    triggerId : null,
		};
	};

    refreshTriggerId = (triggerId) => {
        this.setState({triggerId});
    };

    triggerId = () => {
        if (this.state.triggerId != null) return this.state.triggerId;
        return this.props.id;
    };

	clickEvent = () => {
	    return this.props.triggerEvent === "MouseClick" ? this.handleClick.bind(this) : null;
	};

	mouseEnterEvent = () => {
	    return this.props.triggerEvent === "MouseOver" ? this.openPopover.bind(this) : null;
	};

	mouseLeaveEvent = (e) => {
	    return this.props.triggerEvent === "MouseOver" ? this.closePopover.bind(this) : null;
	};

	openPopover = (e) => {
	    if (this.closeTimeout != null) window.clearTimeout(this.closeTimeout);
        this.execute();
	};

	closePopover = (e) => {
        this.requester.closePopover();
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(OpenPopover));
DisplayFactory.register("OpenPopover", withStyles(styles, { withTheme: true })(withSnackbar(OpenPopover)));