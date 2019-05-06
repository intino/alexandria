import React from "react";
import AbstractOperation from "../../../gen/displays/components/AbstractOperation";
import OperationNotifier from "../../../gen/displays/notifiers/OperationNotifier";
import OperationRequester from "../../../gen/displays/requesters/OperationRequester";
import * as Icons from "@material-ui/icons";
import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button, IconButton, Typography } from "@material-ui/core";
import { withStyles } from '@material-ui/core/styles';

export default class Operation extends AbstractOperation {
	state = {
		icon : this.props.icon,
		title: this.props.title,
		disabled: this.props.disabled,
		openConfirm : false
	};

	constructor(props) {
		super(props);
		this.notifier = new OperationNotifier(this);
		this.requester = new OperationRequester(this);
	};

	render = () => {
		return (<React.Fragment>
					{this.renderConfirm()}
					{this.renderTrigger()}
				</React.Fragment>
		);
	};

	renderTrigger = () => {
		const mode = this.props.mode.toLowerCase();
		if (mode === "button") return this.renderButton();
		else if (mode === "iconbutton") return this.renderIconButton();
		else if (mode === "materialiconbutton") return this.renderMaterialIconButton();
		return this.renderLink();
	};

	renderLink = () => {
		const {classes} = this.props;
		const format = this.props.format != null && this.props.format !== "default" ? this.props.format.split(" ")[0] : "body1";
		return (<a onClick={this.handleClick.bind(this)} style={this.style()}>
					<Typography variant={format} className={classes.link}>{this.state.title}</Typography>
				</a>
		);
	};

	renderButton = () => {
		return (<div>Button!!</div>);
	};

	renderIconButton = () => {
		return (<div>Icon Button!!</div>);
	};

	renderMaterialIconButton = () => {
		return (<IconButton aria-label={this.state.title} disabled={this.state.disabled}
							onClick={this.handleClick.bind(this)}>
					{React.createElement(Icons[this.props.icon])}
				</IconButton>
		);
	};

	renderConfirm = () => {
		if (!this.requireConfirm()) return;
		return (<Dialog onClose={this.handleConfirmClose} aria-labelledby="customized-dialog-title" open={this.state.openConfirm}>
				<DialogTitle id="customized-dialog-title" onClose={this.handleConfirmClose}>{this.translate("Confirm")}</DialogTitle>
				<DialogContent><DialogContentText>{this.props.confirm}</DialogContentText></DialogContent>
				<DialogActions>
					<Button onClick={this.handleConfirmClose} color="primary">{this.translate("Cancel")}</Button>
					<Button onClick={this.handleConfirmAccept} color="primary">{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	handleClick = (e) => {
		if (this.requireConfirm()) {
			this.setState({ openConfirm : true });
			return;
		}
		this.requester.execute();
	};

	handleConfirmAccept = () => {
		this.setState({ openConfirm : false });
		this.requester.execute();
	};

	handleConfirmClose = () => {
		this.setState({ openConfirm : false });
	};

	refresh = ({ title, disabled }) => {
		this.setState({ title, disabled });
	};

	refreshDisabled = (value) => {
		this.setState({ disabled: value });
	};

	requireConfirm = () => {
		return this.props.confirm != null && this.props.confirm !== "";
	}

};