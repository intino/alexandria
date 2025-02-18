import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractWizard from "../../../gen/displays/components/AbstractWizard";
import WizardNotifier from "../../../gen/displays/notifiers/WizardNotifier";
import WizardRequester from "../../../gen/displays/requesters/WizardRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import MuiStepper from "@material-ui/core/Stepper";
import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from "@material-ui/core";
import Step from "./Step";
import MuiButton from "@material-ui/core/Button";
import { withSnackbar } from 'notistack';

const Positions = {
	Top: "top",
	Bottom: "bottom",
	Left: "left",
	Right: "right"
};

const styles = theme => ({
	root:{
		display: "flex"
	},
	row: {
		flexDirection: "row"
	},
	column: {
		flexDirection: "column"
	},
	content: {
		width: "100%"
	}
});

function clsx(position, classes){
	let classNames = [];
	classNames.push(classes.root);
	if (position === Positions.Top || position === Positions.Bottom) classNames.push(classes.column);
	if (position === Positions.Left || position === Positions.Right) classNames.push(classes.row);
	return classNames.join(" ");
}

class Wizard extends AbstractWizard {

	constructor(props) {
		super(props);
		this.notifier = new WizardNotifier(this);
		this.requester = new WizardRequester(this);
		this.state = {
			...this.state,
			active: undefined,
			allowBack: true,
			allowNext: true,
			allowFinish: false,
			openConfirm: false,
			visibleList: [],
		};
	};

	_isTop(){
		return this.props.position === Positions.Top || this.props.position === Positions.Left;
	};

	render() {
		return (
			<React.Fragment>
				{this._renderContent()}
				{this._renderToolbar()}
			    {this._renderConfirm()}
			</React.Fragment>
		);
	};

	_renderContent(){
		const { position, classes } = this.props;
		const classNames = clsx(position, classes);
		const isTop =  this._isTop();
		return (
			<div className={classNames} style={{height:"100%",overflow:'auto'}}>
				{isTop && this._renderWizard()}
				{this._renderActive()}
				{!isTop && this._renderWizard()}
			</div>
		);
	};

	_renderActive(){
		const active = this.state.active;
		const children  = React.Children.toArray(this.props.children);
		const isInRange = this._inRange(active, children.length);
		return (
			<div className={this.props.classes.content} style={{height:"100%"}}>
				{ isInRange && React.cloneElement(children[active], { showContent: true })}
			</div>
		);
	};

	_inRange(active, total) {
		return total && active !== undefined && active > -1 && active < total;
	};

	_renderWizard() {
	    if (this.props.style === "ContentOnly") return (<React.Fragment/>);
		const { orientation } = this.props;
		const children = this._visibleChildren();
		return (
			<MuiStepper orientation={orientation}>
			    {React.Children.map(children, (child, i) => this._renderStep(child, i))}
			</MuiStepper>
		);
	};

	_renderStep = (step, index) => {
	    return index == this.state.active || this.props.orientation === "vertical" ? React.cloneElement(step, { index: index }) : (<Step index={index}></Step>);
	};

	_visibleChildren() {
		const { children } = this.props;
		const visibleList = this.state.visibleList;
		return children.filter((child, i) => visibleList[i] == true);
	};

	_renderToolbar() {
	    if (this.props.style === "NoToolbar" || this.props.style === "ContentOnly") return (<React.Fragment/>);
		const count = React.Children.count(this._visibleChildren());
		return (
		    <div className="layout horizontal">
			    <MuiButton onClick={() => this.requester.back()} disabled={!this.state.allowBack}>{this.translate("Back")}</MuiButton>
			    <MuiButton onClick={() => this.requester.next()} style={{display:this.state.active != count-1 ? "block" : "none"}} disabled={!this.state.allowNext}>{this.translate("Next")}</MuiButton>
			    <MuiButton onClick={() => this.handleFinish()} style={{display:this.state.allowFinish ? "block" : "none"}}>{this.translate("Finish")}</MuiButton>
		    </div>
        );
	};

	_renderConfirm = () => {
	    if (!this.requireConfirm()) return;
		const openConfirm = this.state.openConfirm != null ? this.state.openConfirm : false;
		return (
		    <Dialog onClose={this.handleConfirmClose} open={openConfirm}>
				<DialogTitle onClose={this.handleConfirmClose}>{this.translate("Confirm")}</DialogTitle>
				<DialogContent><DialogContentText>{this.translate(this.props.confirmMessage)}</DialogContentText></DialogContent>
				<DialogActions>
					<MuiButton onClick={this.handleConfirmClose} color="primary" style={{marginRight:'10px'}}>{this.translate("Cancel")}</MuiButton>
					<MuiButton variant="contained" onClick={this.handleConfirmAccept} color="primary">{this.translate("OK")}</MuiButton>
				</DialogActions>
			</Dialog>
		);
	};

	handleConfirmClose = () => {
	    this.setState({openConfirm: false});
	};

	handleConfirmAccept = () => {
	    this.setState({openConfirm: false});
	    this.requester.finishConfirmed();
	};

    handleFinish = () => {
        this.showConfirmDialog();
    }

	requireConfirm = () => {
		return this.props.confirmMessage != null && this.props.confirmMessage !== "";
	};

	refresh({active, allowNext, allowBack, allowFinish, visibleList}) {
		this.setState({active,allowNext,allowBack,allowFinish,visibleList});
	};

	showConfirmDialog = () => {
        if (this.requireConfirm()) {
            this.setState({openConfirm : true});
            return;
        }
	    this.requester.finishConfirmed();
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Wizard));
DisplayFactory.register("Wizard", withStyles(styles, { withTheme: true })(withSnackbar(Wizard)));