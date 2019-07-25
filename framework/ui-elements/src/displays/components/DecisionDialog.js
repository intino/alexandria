import React from "react";
import { withStyles } from '@material-ui/core/styles';
import {Dialog as MuiDialog, DialogContent} from "@material-ui/core";
import AbstractDecisionDialog from "../../../gen/displays/components/AbstractDecisionDialog";
import DecisionDialogNotifier from "../../../gen/displays/notifiers/DecisionDialogNotifier";
import DecisionDialogRequester from "../../../gen/displays/requesters/DecisionDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import BaseDialog from "./BaseDialog";

const styles = theme => ({...BaseDialog.Styles(theme)});

class DecisionDialog extends AbstractDecisionDialog {

	constructor(props) {
		super(props);
		this.notifier = new DecisionDialogNotifier(this);
		this.requester = new DecisionDialogRequester(this);
	};

	render() {
		return (
			<MuiDialog fullScreen={this.props.fullscreen} open={this.state.opened} onClose={this.handleClose.bind(this)} TransitionComponent={this.props.fullscreen ? BaseDialog.Transition : undefined}>
				{this.renderTitle()}
				{this.renderContent(() => this.props.children)}
			</MuiDialog>
		);
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(DecisionDialog));
DisplayFactory.register("DecisionDialog", withStyles(styles, { withTheme: true })(withSnackbar(DecisionDialog)));