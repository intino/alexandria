import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import AbstractSlider from "../../../gen/displays/components/AbstractSlider";
import SliderNotifier from "../../../gen/displays/notifiers/SliderNotifier";
import SliderRequester from "../../../gen/displays/requesters/SliderRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import BaseSlider from "./BaseSlider";

const styles = theme => ({...BaseSlider.Styles(theme)});

class Slider extends AbstractSlider {

	constructor(props) {
		super(props);
		this.notifier = new SliderNotifier(this);
		this.requester = new SliderRequester(this);
	};

	render() {
		return this.renderComponent();
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Slider));
DisplayFactory.register("Slider", withStyles(styles, { withTheme: true })(withSnackbar(Slider)));