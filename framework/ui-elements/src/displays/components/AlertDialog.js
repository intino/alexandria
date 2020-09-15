import React from "react";
import { withStyles } from '@material-ui/core/styles';
import {Dialog, DialogContent, DialogContentText, DialogActions, Button, Dialog as MuiDialog} from "@material-ui/core";
import AbstractAlertDialog from "../../../gen/displays/components/AbstractAlertDialog";
import AlertDialogNotifier from "../../../gen/displays/notifiers/AlertDialogNotifier";
import AlertDialogRequester from "../../../gen/displays/requesters/AlertDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import BaseDialog from './BaseDialog';
import { makeDraggable } from "./BaseDialog";

const styles = theme => ({...BaseDialog.Styles(theme)});

class AlertDialog extends AbstractAlertDialog {

	constructor(props) {
		super(props);
		this.notifier = new AlertDialogNotifier(this);
		this.requester = new AlertDialogRequester(this);
	};

	render() {
		return (
			<Dialog fullScreen={this.props.fullscreen}
                    fullWidth={this._widthDefined()} maxWidth={this._widthDefined() ? "xl" : "sm"}
			        open={this.state.opened} onClose={this.handleClose.bind(this)}
                    disableBackdropClick={this.state.modal}
                    disableEscapeKeyDown={this.state.modal}
			        TransitionComponent={this.props.fullscreen ? BaseDialog.Transition : undefined}
			        PaperComponent={!this.props.fullscreen ? makeDraggable.bind(this, this.props.id, this.sizeStyle()) : undefined}
                    aria-labelledby={this.props.id + "_draggable"}>
				{this.renderTitle()}
				{this.renderContent(() => <DialogContentText>{this.props.message}</DialogContentText>)}
				<DialogActions>
					<Button onClick={this.handleClose.bind(this)} color="primary">{this.closeLabel()}</Button>
					{this.props.acceptLabel != null && <Button onClick={this.handleAccept.bind(this)} color="primary">{this.acceptLabel()}</Button>}
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