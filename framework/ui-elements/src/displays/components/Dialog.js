import React from "react";
import {Dialog as MuiDialog} from "@mui/material"
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractDialog from "../../../gen/displays/components/AbstractDialog";
import DialogNotifier from "../../../gen/displays/notifiers/DialogNotifier";
import DialogRequester from "../../../gen/displays/requesters/DialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import BaseDialog from "./BaseDialog";
import Theme from "app-elements/gen/Theme";
import {containerPalette, dialogPaperStyles} from "./ContainerStyles";

const styles = theme => ({
	...BaseDialog.Styles(theme),
	sized : {
		minHeight: "100%",
		minWidth: "100%",
	},
});

class Dialog extends AbstractDialog {

	constructor(props) {
		super(props);
		this.notifier = new DialogNotifier(this);
		this.requester = new DialogRequester(this);
	};

	render() {
		return this.renderDialog(this.props.children);
	};

	renderDialog(children) {
		const handleClose = (event, reason) => {
			if (this.state.modal && reason === "backdropClick") return;
			this.handleClose(event, reason);
		};
		const runtimeTheme = Theme.get() || this.props.theme;
		const paperStyle = dialogPaperStyles(runtimeTheme);
		const palette = containerPalette(runtimeTheme);
		const isDark = runtimeTheme != null && runtimeTheme.palette != null && runtimeTheme.palette.mode === "dark";
		const paperClassName = `${this.props.classes.dialogPaper || ""} ${isDark ? "alexandria-dialog-paper-dark" : ""}`.trim();
		const slotProps = {
			paper: { className: paperClassName, style: paperStyle },
			backdrop: {
				style: {
					backgroundColor: palette.backdrop,
				}
			}
		};
		return (
			<MuiDialog fullScreen={this.props.fullscreen} open={this.state.opened}
			           fullWidth={this._widthDefined()} maxWidth={this._widthDefined() ? "xl" : "sm"}
				   onClose={handleClose}
				   slots={this.props.fullscreen ? { transition: this._transition() } : undefined}
				   PaperComponent={!this.props.fullscreen ? this.DraggablePaper : undefined}
				   slotProps={slotProps}
                       aria-labelledby={this.props.id + "_draggable"}>
				{this.renderTitle()}
				{this.renderContent(() => children)}
			</MuiDialog>
		);
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Dialog));
DisplayFactory.register("Dialog", withStyles(styles, { withTheme: true })(withSnackbar(Dialog)));
