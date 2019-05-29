import React from "react";
import AbstractOperation from "../../../gen/displays/components/AbstractOperation";
import OperationNotifier from "../../../gen/displays/notifiers/OperationNotifier";
import OperationRequester from "../../../gen/displays/requesters/OperationRequester";
import * as Icons from "@material-ui/icons";
import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button, IconButton, Typography } from "@material-ui/core";

export default class Operation extends AbstractOperation {
	state = {
		icon : this.props.icon,
		title: this.props.title,
		disabled: this.props.disabled,
		openConfirm : false
	};

	static Styles = theme => ({
		link : {
			color: theme.palette.primary.main,
			cursor: "pointer"
		},
		button : {
			cursor: "pointer"
		},
		iconButton : {
			cursor: "pointer"
		},
		materialIconButton : {
			cursor: "pointer"
		}
	});

	constructor(props) {
		super(props);
		this.notifier = new OperationNotifier(this);
		this.requester = new OperationRequester(this);
	};

	render = () => {
		return this.renderOperation();
	};

	renderOperation = () => {
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
		return (<a onClick={this.handleClick.bind(this)} style={this.style()}>
					<Typography variant={this.variant("body1")} className={classes.link}>{this._title()}</Typography>
				</a>
		);
	};

	renderButton = () => {
		const {classes} = this.props;
		return (<Button size={this._size()} variant="contained" color="primary"
						disabled={this._disabled()} onClick={this.handleClick.bind(this)}
						className={classes.button}>
					{this._title()}
				</Button>
		);
	};

	renderIconButton = () => {
		const {classes} = this.props;
		return (<IconButton color="primary" aria-label={this._title()} disabled={this._disabled()}
							onClick={this.handleClick.bind(this)}
							className={classes.iconButton}>
					<img src={this._icon()} style={{width:"24px",height:"24px"}}/>
				</IconButton>
		);
	};

	renderMaterialIconButton = () => {
		const {classes} = this.props;
		return (<IconButton color="primary" aria-label={this._title()} disabled={this._disabled()}
							onClick={this.handleClick.bind(this)} className={classes.materialIconButton}>
					{React.createElement(Icons[this._icon()])}
				</IconButton>
		);
	};

	renderConfirm = () => {
		if (!this.requireConfirm()) return;
		const openConfirm = this.state.openConfirm != null ? this.state.openConfirm : false;
		return (<Dialog onClose={this.handleConfirmClose} open={openConfirm}>
				<DialogTitle onClose={this.handleConfirmClose}>{this.translate("Confirm")}</DialogTitle>
				<DialogContent><DialogContentText>{this.props.confirm}</DialogContentText></DialogContent>
				<DialogActions>
					<Button onClick={this.handleConfirmClose} color="primary">{this.translate("Cancel")}</Button>
					<Button variant="contained" onClick={this.handleConfirmAccept} color="primary">{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	handleClick(e) {
		this.execute();
	};

	execute = () => {
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

	refreshIcon = (value) => {
		this.setState({ icon: value });
	};

	requireConfirm = () => {
		return this.props.confirm != null && this.props.confirm !== "";
	};

	_title = () => {
		return this.translate(this.state.title != null ? this.state.title : this.props.title);
	};

	_icon = () => {
		return this.state.icon != null ? this.state.icon : this.props.icon;
	};

	_size = () => {
		const size = this.state.size != null ? this.state.size : this.props.size;
		return size != null ? size.toLowerCase() : "small";
	};

	_disabled = () => {
		return this.state.disabled != null ? this.state.disabled : false;
	}

};