import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSliderExamplesMold from "../../../gen/displays/templates/AbstractSliderExamplesMold";
import SliderExamplesMoldNotifier from "../../../gen/displays/notifiers/SliderExamplesMoldNotifier";
import SliderExamplesMoldRequester from "../../../gen/displays/requesters/SliderExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class SliderExamplesMold extends AbstractSliderExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new SliderExamplesMoldNotifier(this);
		this.requester = new SliderExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(SliderExamplesMold);
DisplayFactory.register("SliderExamplesMold", withStyles(styles, { withTheme: true })(SliderExamplesMold));