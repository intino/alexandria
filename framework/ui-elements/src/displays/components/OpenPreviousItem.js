import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractOpenPreviousItem from "../../../gen/displays/components/AbstractOpenPreviousItem";
import OpenPreviousItemNotifier from "../../../gen/displays/notifiers/OpenPreviousItemNotifier";
import OpenPreviousItemRequester from "../../../gen/displays/requesters/OpenPreviousItemRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import Actionable from "./Actionable";

const styles = theme => ({});

class OpenPreviousItem extends AbstractOpenPreviousItem {

	constructor(props) {
		super(props);
		this.notifier = new OpenPreviousItemNotifier(this);
		this.requester = new OpenPreviousItemRequester(this);
	};


}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(OpenPreviousItem));
DisplayFactory.register("OpenPreviousItem", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(OpenPreviousItem)));