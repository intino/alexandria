import React from "react";
import { withStyles } from '@material-ui/core/styles';
import {Dialog as MuiDialog, DialogContent} from "@material-ui/core";
import AbstractDecisionDialog from "../../../gen/displays/components/AbstractDecisionDialog";
import DecisionDialogNotifier from "../../../gen/displays/notifiers/DecisionDialogNotifier";
import DecisionDialogRequester from "../../../gen/displays/requesters/DecisionDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class DecisionDialog extends AbstractDecisionDialog {

	constructor(props) {
		super(props);
		this.notifier = new DecisionDialogNotifier(this);
		this.requester = new DecisionDialogRequester(this);
	};

	render() {
		return (
			<MuiDialog style={this.style()} open={this.state.opened} onClose={this.handleClose.bind(this)}>
				{this.renderTitle()}
				<DialogContent>{this.props.children}</DialogContent>
			</MuiDialog>
		);
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(DecisionDialog));
DisplayFactory.register("DecisionDialog", withStyles(styles, { withTheme: true })(withSnackbar(DecisionDialog)));