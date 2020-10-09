import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCollectionDialog from "../../../gen/displays/components/AbstractCollectionDialog";
import CollectionDialogNotifier from "../../../gen/displays/notifiers/CollectionDialogNotifier";
import CollectionDialogRequester from "../../../gen/displays/requesters/CollectionDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import {Dialog, DialogContent, DialogActions, Button, Divider, Slide} from "@material-ui/core";
import SearchBox from "./SearchBox";
import BaseDialog from "./BaseDialog";
import { makeDraggable } from "./BaseDialog";

const styles = theme => ({
	...BaseDialog.Styles(theme),
	search : {
		padding: "0 10px",
		marginBottom: "5px"
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
		this.state = {
		    ...this.state,
		    selectionCount : 0,
		};
	};

	render() {
		return (
			<Dialog fullScreen={this.props.fullscreen} open={this.state.opened}
                    fullWidth={this._widthDefined()} maxWidth={this._widthDefined() ? "xl" : "sm"}
			        onClose={this.handleClose.bind(this)}
                    disableBackdropClick={this.state.modal}
                    disableEscapeKeyDown={this.state.modal}
			        TransitionComponent={this.props.fullscreen ? BaseDialog.Transition : undefined}
			        PaperComponent={!this.props.fullscreen ? makeDraggable.bind(this, this.props.id, this.sizeStyle()) : undefined}
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
	    return (
	        <React.Fragment>
                <Divider component="li" style={{listStyle:"none"}}/>
                <DialogActions>
                    <Button onClick={this.handleClose.bind(this)} color="primary" style={{marginRight:'10px'}}>{this.cancelLabel()}</Button>
                    <Button ref={this.acceptButton} onClick={this.handleAccept.bind(this)} color="primary" variant="contained" disabled={this.state.selectionCount<=0}>{this.acceptLabel()}</Button>
                </DialogActions>
            </React.Fragment>
	    );
	};

	content = () => {
		const { classes } = this.props;
		return (
			<React.Fragment>
				<div className={classes.search}>{this.renderInstances()}</div>
				<div className="layout vertical flex" style={{height:'calc(100% - 55px)'}}>{this.props.children}</div>
			</React.Fragment>
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