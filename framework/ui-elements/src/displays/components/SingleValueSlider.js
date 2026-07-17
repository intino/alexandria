import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractSingleValueSlider from "../../../gen/displays/components/AbstractSingleValueSlider";
import SingleValueSliderNotifier from "../../../gen/displays/notifiers/SingleValueSliderNotifier";
import SingleValueSliderRequester from "../../../gen/displays/requesters/SingleValueSliderRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";

const styles = theme => ({});

class SingleValueSlider extends AbstractSingleValueSlider {

	constructor(props) {
		super(props);
		this.notifier = new SingleValueSliderNotifier(this);
		this.requester = new SingleValueSliderRequester(this);
	};

	refreshSelected = () => {
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SingleValueSlider));
DisplayFactory.register("SingleValueSlider", withStyles(styles, { withTheme: true })(withSnackbar(SingleValueSlider)));