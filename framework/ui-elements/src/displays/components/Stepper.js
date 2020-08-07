import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractStepper from "../../../gen/displays/components/AbstractStepper";
import StepperNotifier from "../../../gen/displays/notifiers/StepperNotifier";
import StepperRequester from "../../../gen/displays/requesters/StepperRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import MuiStepper from "@material-ui/core/Stepper";
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

class Stepper extends AbstractStepper {

	constructor(props) {
		super(props);
		this.notifier = new StepperNotifier(this);
		this.requester = new StepperRequester(this);
		this.state = {
			...this.state,
			active: undefined,
			allowNext: true,
			allowBack: true,
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
			</React.Fragment>
		);
	};

	_renderContent(){
		const { position, classes } = this.props;
		const classNames = clsx(position, classes);
		const isTop =  this._isTop();
		return (
			<div className={classNames} style={{height:"100%"}}>
				{isTop && this._renderStepper()}
				{this._renderActive()}
				{!isTop && this._renderStepper()}
			</div>
		);
	};

	_renderActive(){
		const active = this.state.active;
		const children  = React.Children.toArray(this.props.children);
		const isInRange = this._inRange(active, children.length);
		return (
			<div className={this.props.classes.content} style={{height:"100%",overflow:'auto'}}>
				{ isInRange && React.cloneElement(children[active], { showContent: true })}
			</div>
		);
	};

	_inRange(active, total) {
		return total && active !== undefined && active > -1 && active < total;
	};

	_renderStepper() {
		const { orientation } = this.props;
		const children = this._visibleChildren();
		return (
			<MuiStepper orientation={orientation}>{
				React.Children.map(children,
					(child, i) => React.cloneElement(child, { index: i })
				)
			}</MuiStepper>
		);
	};

	_visibleChildren() {
		const { children } = this.props;
		const visibleList = this.state.visibleList;
		return children.filter((child, i) => visibleList[i] == true);
	};

	_renderToolbar() {
	    if (this.props.style === "NoToolbar") return;
		const count = React.Children.count(this._visibleChildren());
		return (<div>
			<MuiButton onClick={() => this.requester.back()} disabled={this.state.active <= 0 || !this.state.allowBack}>{this.translate("Back")}</MuiButton>
			<MuiButton onClick={() => this.requester.next()} disabled={this.state.active >= count || !this.state.allowNext}>{this.translate("Next")}</MuiButton>
		</div>);
	};

	refresh({active, allowNext, allowBack, visibleList}) {
		this.setState({active,allowNext,allowBack,visibleList});
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Stepper));
DisplayFactory.register("Stepper", withStyles(styles, { withTheme: true })(withSnackbar(Stepper)));