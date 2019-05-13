import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOption from "../../../gen/displays/components/AbstractOption";
import OptionNotifier from "../../../gen/displays/notifiers/OptionNotifier";
import OptionRequester from "../../../gen/displays/requesters/OptionRequester";

const styles = theme => ({});

class Option extends AbstractOption {

	constructor(props) {
		super(props);
		this.notifier = new OptionNotifier(this);
		this.requester = new OptionRequester(this);
	};

	render() {
        const { classes } = this.props;
		return (
			<React.Fragment><div name={this.props.name}>{this.props.label}</div></React.Fragment>
		);
	};


}

export default withStyles(styles, { withTheme: true })(Option);