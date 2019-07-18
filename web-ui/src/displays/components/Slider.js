import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSlider from "../../../gen/displays/components/AbstractSlider";
import SliderNotifier from "../../../gen/displays/notifiers/SliderNotifier";
import SliderRequester from "../../../gen/displays/requesters/SliderRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Slider extends AbstractSlider {

	constructor(props) {
		super(props);
		this.notifier = new SliderNotifier(this);
		this.requester = new SliderRequester(this);
	};

	refreshOrdinals = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(Slider);
DisplayFactory.register("Slider", withStyles(styles, { withTheme: true })(Slider));