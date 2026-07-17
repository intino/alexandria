import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {Dialog as MuiDialog} from "@mui/material";
import AbstractDecisionDialog from "../../../gen/displays/components/AbstractDecisionDialog";
import DecisionDialogNotifier from "../../../gen/displays/notifiers/DecisionDialogNotifier";
import DecisionDialogRequester from "../../../gen/displays/requesters/DecisionDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import BaseDialog from "./BaseDialog";

const styles = theme => ({...BaseDialog.Styles(theme)});

class DecisionDialog extends AbstractDecisionDialog {

	constructor(props) {
		super(props);
		this.notifier = new DecisionDialogNotifier(this);
		this.requester = new DecisionDialogRequester(this);
	};

	render() {
		const handleClose = (event, reason) => {
			if (this.state.modal && reason === "backdropClick") return;
			this.handleClose(event, reason);
		};
		return (
			<MuiDialog fullScreen={this.props.fullscreen} open={this.state.opened}
			           fullWidth={this._widthDefined()} maxWidth={this._widthDefined() ? "xl" : "sm"}
			           onClose={handleClose}
			           slots={this.props.fullscreen ? { transition: this._transition() } : undefined}
			           PaperComponent={!this.props.fullscreen ? this.DraggablePaper : undefined}
                       aria-labelledby={this.props.id + "_draggable"}>
				{this.renderTitle()}
				{this.renderContent(() => <div style={{maxHeight:'300px',overflow:'auto',height:'100%'}}>{this.props.children}</div>)}
			</MuiDialog>
		);
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(DecisionDialog));
DisplayFactory.register("DecisionDialog", withStyles(styles, { withTheme: true })(withSnackbar(DecisionDialog)));
