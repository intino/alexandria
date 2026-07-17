import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {Button, Dialog, DialogActions, DialogContentText} from "@mui/material";
import AbstractAlertDialog from "../../../gen/displays/components/AbstractAlertDialog";
import AlertDialogNotifier from "../../../gen/displays/notifiers/AlertDialogNotifier";
import AlertDialogRequester from "../../../gen/displays/requesters/AlertDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import BaseDialog from './BaseDialog';
import {dialogActionButtonStyles, dialogPrimaryButtonStyles} from "./ButtonStyles";

const styles = theme => ({
	...BaseDialog.Styles(theme),
	dialogButton: {
		...dialogActionButtonStyles(theme),
	},
	dialogPrimaryButton: {
		...dialogPrimaryButtonStyles(theme),
	},
});

class AlertDialog extends AbstractAlertDialog {

	constructor(props) {
		super(props);
		this.notifier = new AlertDialogNotifier(this);
		this.requester = new AlertDialogRequester(this);
	};

	render() {
		const handleClose = (event, reason) => {
			if (this.state.modal && reason === "backdropClick") return;
			this.handleClose(event, reason);
		};
		return (
			<Dialog fullScreen={this.props.fullscreen}
                    fullWidth={this._widthDefined()} maxWidth={this._widthDefined() ? "xl" : "sm"}
			        open={this.state.opened} onClose={handleClose}
			        slots={this.props.fullscreen ? { transition: this._transition() } : undefined}
			        PaperComponent={!this.props.fullscreen ? this.DraggablePaper : undefined}
                    aria-labelledby={this.props.id + "_draggable"}>
				{this.renderTitle()}
				{this.renderContent(() => <DialogContentText>{this.props.message}</DialogContentText>)}
				<DialogActions>
					<Button className={this.props.classes.dialogButton} onClick={this.handleClose.bind(this)} color="primary">{this.closeLabel()}</Button>
					{this.props.acceptLabel != null && <Button className={this.props.classes.dialogPrimaryButton} variant="contained" onClick={this.handleAccept.bind(this)} color="primary">{this.acceptLabel()}</Button>}
				</DialogActions>
			</Dialog>
		);
	};

	handleAccept = () => {
	    this.requester.accept();
	};

	closeLabel = () => {
		return this.translate(this.props.closeLabel != null ? this.props.closeLabel : "Close");
	};

	acceptLabel = () => {
		return this.translate(this.props.acceptLabel != null ? this.props.acceptLabel : "Accept");
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(AlertDialog));
DisplayFactory.register("AlertDialog", withStyles(styles, { withTheme: true })(withSnackbar(AlertDialog)));
