import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractWizardNavigator from "../../../gen/displays/components/AbstractWizardNavigator";
import WizardNavigatorNotifier from "../../../gen/displays/notifiers/WizardNavigatorNotifier";
import WizardNavigatorRequester from "../../../gen/displays/requesters/WizardNavigatorRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import MuiButton from "@mui/material/Button";
import MuiStep from "@mui/material/Step";
import MuiStepLabel from "@mui/material/StepLabel";
import {dialogActionButtonStyles, dialogPrimaryButtonStyles} from "./ButtonStyles";

const styles = theme => ({
	button: {
		...dialogActionButtonStyles(theme),
		padding: "8px 14px",
		minHeight: "36px",
	},
	primaryButton: {
		...dialogPrimaryButtonStyles(theme),
		padding: "8px 14px",
		minHeight: "36px",
	},
});

class WizardNavigator extends AbstractWizardNavigator {

	constructor(props) {
		super(props);
		this.notifier = new WizardNavigatorNotifier(this);
		this.requester = new WizardNavigatorRequester(this);
		this.state = {
		    active: 0,
		    stepsCount: 1,
		    allowNext: false,
		    allowBack: false,
		    allowFinish: false,
		    finished: false,
		    ...this.state,
		}
	};

	render() {
	    const active = this.state.active;
	    const count = this.state.stepsCount;
		return (
		    <div className="layout horizontal center flex">
                <MuiButton className={this.props.classes.button} onClick={() => this.requester.back()} disabled={!this.state.allowBack}>{this.translate("Back")}</MuiButton>
			    <div className="layout horizontal center-center flex">{this._renderSteps()}</div>
			    <MuiButton className={this.props.classes.button} onClick={() => this.requester.next()} style={{display:active != count-1 ? "block" : "none"}} disabled={!this.state.allowNext}>{this.translate("Next")}</MuiButton>
			    <MuiButton className={this.props.classes.primaryButton} variant="contained" onClick={() => this.handleFinish()} style={{display:this.state.allowFinish ? "block" : "none"}}>{this.translate("Finish")}</MuiButton>
		    </div>
        );
	};

	_renderSteps = () => {
	    const stepsCount = this.state.stepsCount;
	    const result = [];
	    for (var i=0;i<stepsCount;i++) result.push(this._renderStep(i));
	    return result;
	};

	_renderStep = (index) => {
	    const isCompleted = index < this.state.active || (index == this.state.stepsCount-1 && this.state.finished);
	    const isActive = index == this.state.active;
		const stepProps = { active: isActive, disabled: !isCompleted, completed: isCompleted };
		const clickFunction = isCompleted ? this.handleSelectStep.bind(this, index) : null;
		const style = isCompleted ? { cursor: "pointer" } : {};
	    return (
			<MuiStep key={index} {...stepProps} style={{marginRight:"10px",...style}} onClick={clickFunction}>
				<MuiStepLabel icon={index+1}></MuiStepLabel>
			</MuiStep>
        );
	};

    handleFinish = () => {
        this.requester.finish();
    }

	handleSelectStep = (index) => {
	    this.requester.select(index);
	};

	refresh = ({active, stepsCount, allowBack, allowNext, allowFinish, finished}) => {
	    this.setState({active, stepsCount, allowBack, allowNext, allowFinish, finished});
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(WizardNavigator));
DisplayFactory.register("WizardNavigator", withStyles(styles, { withTheme: true })(withSnackbar(WizardNavigator)));
