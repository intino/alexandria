import React, { Suspense } from "react";
import AbstractOperation from "../../../gen/displays/components/AbstractOperation";
import OperationNotifier from "../../../gen/displays/notifiers/OperationNotifier";
import OperationRequester from "../../../gen/displays/requesters/OperationRequester";
import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button, IconButton, Typography } from "@material-ui/core";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export const MuiIcon = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./operation/Icon"), 300));
	});
});

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
		// this.notifier = new OperationNotifier(this);
		// this.requester = new OperationRequester(this);
	};

	render = () => {
		return (
			<Suspense fallback={<div></div>}>
				{this.renderOperation()}
			</Suspense>
		);
	};

	renderOperation = () => {
		return (
			<React.Fragment>
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
		return (<a onClick={this.handleClick.bind(this)}>
				<Typography style={this.style()} variant={this.variant("body1")} className={classes.link}>{this._title()}</Typography>
			</a>
		);
	};

	renderButton = () => {
		const {classes} = this.props;
		return (<Button style={this.style()} size={this._size()} color="primary" variant={this._highlightVariant()}
						disabled={this._disabled()} onClick={this.handleClick.bind(this)}
						className={classes.button}>
				{this._title()}
			</Button>
		);
	};

	_highlightVariant = () => {
		const highlighted = this.props.highlighted;
		if (highlighted == null) return undefined;
		else if (highlighted.toLowerCase() === "outline") return "outlined";
		return "contained";
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
							onClick={this.handleClick.bind(this)} className={classes.materialIconButton}
							style={this.style()} >
				<MuiIcon icon={this._icon()}/>
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

DisplayFactory.register("Operation", Operation);