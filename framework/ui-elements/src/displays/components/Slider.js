import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { withSnackbar } from 'notistack';
import MuiSlider from '@material-ui/lab/Slider';
import AbstractSlider from "../../../gen/displays/components/AbstractSlider";
import SliderNotifier from "../../../gen/displays/notifiers/SliderNotifier";
import SliderRequester from "../../../gen/displays/requesters/SliderRequester";
import Delayer from "../../util/Delayer";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({});

class Slider extends AbstractSlider {
	state = {
		value: this.props.value,
		ordinals: [],
	};

	constructor(props) {
		super(props);
		this.notifier = new SliderNotifier(this);
		this.requester = new SliderRequester(this);
	};

	render() {
		if (this.state.ordinals.length <= 0)
			return (<div style={this.style()}>{this.translate("No ordinals defined!")}</div>);

		return (
			<div style={this.style()} className="layout horizontal flex">
				{this.renderOrdinals()}
				{this.renderSlider()}
			</div>
		);
	};

	renderOrdinals = () => {
		if (this.state.ordinals.length <= 1) return null;
		return (<div>renderizar los ordinales!!!</div>)
	};

	renderSlider = () => {
		const range = this.props.range;
		const value = this.state.value != null && this.state.value !== -1 ? this.state.value : 0;
		const ordinal = this.state.ordinals[0];
		return (<MuiSlider min={range.min} max={range.max} value={value} step={ordinal.step} onChange={this.handleChange.bind(this)}/>);
	};

	handleChange = (e, value) => {
		Delayer.execute(this, () => this.requester.update(value));
		this.setState({value});
	};

	refreshOrdinals = (ordinals) => {
		this.setState({ordinals});
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Slider));
DisplayFactory.register("Slider", withStyles(styles, { withTheme: true })(withSnackbar(Slider)));