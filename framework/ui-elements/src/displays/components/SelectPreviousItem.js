import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractSelectPreviousItem from "../../../gen/displays/components/AbstractSelectPreviousItem";
import SelectPreviousItemNotifier from "../../../gen/displays/notifiers/SelectPreviousItemNotifier";
import SelectPreviousItemRequester from "../../../gen/displays/requesters/SelectPreviousItemRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";

const styles = theme => ({});

class SelectPreviousItem extends AbstractSelectPreviousItem {

	constructor(props) {
		super(props);
		this.notifier = new SelectPreviousItemNotifier(this);
		this.requester = new SelectPreviousItemRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectPreviousItem));
DisplayFactory.register("SelectPreviousItem", withStyles(styles, { withTheme: true })(withSnackbar(SelectPreviousItem)));