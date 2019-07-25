import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCollectionDialog from "../../../gen/displays/components/AbstractCollectionDialog";
import CollectionDialogNotifier from "../../../gen/displays/notifiers/CollectionDialogNotifier";
import CollectionDialogRequester from "../../../gen/displays/requesters/CollectionDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import {Dialog, DialogContent, Slide} from "@material-ui/core";
import SearchBox from "./SearchBox";
import BaseDialog from "./BaseDialog";

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
	};

	render() {
		return (
			<Dialog fullScreen={this.props.fullscreen} open={this.state.opened} onClose={this.handleClose.bind(this)} TransitionComponent={this.props.fullscreen ? BaseDialog.Transition : undefined}>
				{this.renderTitle()}
				{this.renderContent(() => this.content())}
			</Dialog>
		);
	};

	content = () => {
		const { classes } = this.props;
		return (
			<React.Fragment>
				<div className={classes.search}>{this.renderInstances()}</div>
				<div className="layout vertical flex" style={{height:'calc(100% - 40px)'}}>{this.props.children}</div>
			</React.Fragment>
		);
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