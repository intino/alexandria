import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTemporalSlider from "../../../gen/displays/components/AbstractTemporalSlider";
import TemporalSliderNotifier from "../../../gen/displays/notifiers/TemporalSliderNotifier";
import TemporalSliderRequester from "../../../gen/displays/requesters/TemporalSliderRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import BaseSlider from "./BaseSlider";

const styles = theme => ({...BaseSlider.Styles(theme)});

class TemporalSlider extends AbstractTemporalSlider {

	constructor(props) {
		super(props);
		this.notifier = new TemporalSliderNotifier(this);
		this.requester = new TemporalSliderRequester(this);
	};

	render() {
		return this.renderComponent();
	};

	showValueDialog = () => {
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(TemporalSlider));
DisplayFactory.register("TemporalSlider", withStyles(styles, { withTheme: true })(withSnackbar(TemporalSlider)));