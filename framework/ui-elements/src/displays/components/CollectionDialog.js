import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractCollectionDialog from "../../../gen/displays/components/AbstractCollectionDialog";
import CollectionDialogNotifier from "../../../gen/displays/notifiers/CollectionDialogNotifier";
import CollectionDialogRequester from "../../../gen/displays/requesters/CollectionDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import {Button, Dialog, DialogActions, Divider} from "@mui/material";
import BaseDialog from "./BaseDialog";
import Theme from "app-elements/gen/Theme";
import {dialogPaperStyles} from "./ContainerStyles";
import {dialogActionButtonStyles, dialogPrimaryButtonStyles} from "./ButtonStyles";

const styles = theme => ({
	...BaseDialog.Styles(theme),
	search : {
		padding: "0 2px 14px",
		marginBottom: "2px"
	},
	toolbar: {
		padding: "14px 20px 18px",
	},
	toolbarButton: {
		...dialogActionButtonStyles(theme),
	},
	toolbarPrimaryButton: {
		...dialogPrimaryButtonStyles(theme),
	},
	toolbarDivider: {
		borderColor: theme.palette.mode === "dark" ? "rgba(255,255,255,0.08)" : "rgba(15,23,42,0.08)",
	}
});

class CollectionDialog extends AbstractCollectionDialog {
	static DefaultWidth = "600px";
	static DefaultHeight = "400px";

	constructor(props) {
		super(props);
		this.notifier = new CollectionDialogNotifier(this);
		this.requester = new CollectionDialogRequester(this);
		this.acceptButton = React.createRef();
		this.openedNotified = false;
		this.state = {
		    ...this.state,
		    selectionCount : 0,
		};
	};

	notifyOpened = () => {
		if (this.openedNotified) return;
		this.openedNotified = true;
		if (this.requester != null && this.requester.ready != null) this.requester.ready();
	};

	componentDidUpdate(prevProps, prevState) {
		if (prevState.opened && !this.state.opened) this.openedNotified = false;
	}

	render() {
		const handleClose = (event, reason) => {
			if (this.state.modal && reason === "backdropClick") return;
			this.handleClose(event, reason);
		};
		const runtimeTheme = Theme.get() || this.props.theme;
		const paperStyle = dialogPaperStyles(runtimeTheme);
		const paperClassName = `${this.props.classes.dialogPaper || ""} ${runtimeTheme != null && runtimeTheme.palette != null && runtimeTheme.palette.mode === "dark" ? "alexandria-dialog-paper-dark" : ""}`.trim();
		return (
			<Dialog fullScreen={this.props.fullscreen} open={this.state.opened}
                    fullWidth={this._widthDefined()} maxWidth={this._widthDefined() ? "xl" : "sm"}
				onClose={handleClose}
				slots={this.props.fullscreen ? { transition: this._transition() } : undefined}
                    slotProps={{ transition: { onEntered: this.notifyOpened } }}
				PaperComponent={!this.props.fullscreen ? this.DraggablePaper : undefined}
                    PaperProps={!this.props.fullscreen ? { className: paperClassName, style: paperStyle } : { className: paperClassName, style: paperStyle }}
                    aria-labelledby={this.props.id + "_draggable"}>
				{this.renderTitle()}
				{this.renderContent(() => this.content())}
                {this.renderToolbar()}
			</Dialog>
		);
	};

	refreshSelectionCount = (selectionCount) => {
	    this.setState({selectionCount});
	};

	renderToolbar = (content) => {
	    const { classes } = this.props;
	    return (
	        <React.Fragment>
                <Divider component="li" style={{listStyle:"none"}} className={classes.toolbarDivider}/>
                <DialogActions className={classes.toolbar}>
                    <Button className={classes.toolbarButton} onClick={this.handleClose.bind(this)} color="primary" style={{marginRight:'10px'}}>{this.cancelLabel()}</Button>
                    <Button className={classes.toolbarPrimaryButton} ref={this.acceptButton} onClick={this.handleAccept.bind(this)} color="primary" variant="contained">{this.acceptLabel()}</Button>
                </DialogActions>
            </React.Fragment>
	    );
	};

    content = () => {
        const { classes } = this.props;
        return (
            <div style={{display:'flex', flexDirection:'column', width:'100%', height:'100%', minHeight:0}}>
                <div className={classes.search} style={{flex:'0 0 auto'}}>{this.renderInstances()}</div>
                <div className="layout vertical flex" style={{flex:'1 1 auto', minHeight:0, width:'100%'}}>
                    {this.props.children}
                </div>
            </div>
        );
    };

	handleAccept = () => {
	    this.requester.accept();
	};

	cancelLabel = () => {
		return this.translate(this.props.cancelLabel != null ? this.props.cancelLabel : "Cancel");
	};

	acceptLabel = () => {
		return this.translate(this.props.acceptLabel != null ? this.props.acceptLabel : "Accept");
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (!this._widthDefined() && !this.props.fullscreen) result.width = CollectionDialog.DefaultWidth;
		if (!this._heightDefined() && !this.props.fullscreen) result.height = CollectionDialog.DefaultHeight;
		return result;
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(CollectionDialog));
DisplayFactory.register("CollectionDialog", withStyles(styles, { withTheme: true })(withSnackbar(CollectionDialog)));
