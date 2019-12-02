import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractStep from "../../../gen/displays/components/AbstractStep";
import StepNotifier from "../../../gen/displays/notifiers/StepNotifier";
import StepRequester from "../../../gen/displays/requesters/StepRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import MuiStep from "@material-ui/core/Step";
import MuiStepLabel from "@material-ui/core/StepLabel";
import StepIcon from "./icon/StepIcon";
import { withSnackbar } from 'notistack';
import Delayer from "../../util/Delayer";

const styles = theme => ({});

class Step extends AbstractStep {

	constructor(props) {
		super(props);
		this.notifier = new StepNotifier(this);
		this.requester = new StepRequester(this);
		this.state = {
		    ...this.state,
            icon: undefined,
            isActive: false,
            isDisabled: false,
            isCompleted: false
		};
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		return (this.props.showContent) ? this._renderContent() : this._renderStep();
	};

	_renderContent(){
		return this.props.children;
	}

	_renderStep(){
		const { index, label } = this.props;
		const { isActive, isDisabled, isCompleted, icon } = this.state;
		const stepProps = { active: isActive, disabled: isDisabled, completed: isCompleted };
		const labelProps = { icon : (icon || index + 1), StepIconComponent: (icon ? StepIcon : undefined) };
		return (
			<MuiStep key={index} {...stepProps}>
				<MuiStepLabel {...labelProps}>{label}</MuiStepLabel>
			</MuiStep>
		);
	};

	componentDidUpdate() {
		if (this.props.showContent) Delayer.execute(this, () => this.requester.contentRendered());
	};

	refresh = (newState) => {
		this.setState(newState);
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Step));
DisplayFactory.register("Step", withStyles(styles, { withTheme: true })(withSnackbar(Step)));