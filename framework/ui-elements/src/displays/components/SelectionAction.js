import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractSelectionAction from "../../../gen/displays/components/AbstractSelectionAction";
import SelectionActionNotifier from "../../../gen/displays/notifiers/SelectionActionNotifier";
import SelectionActionRequester from "../../../gen/displays/requesters/SelectionActionRequester";
import Actionable from "./Actionable";
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

class SelectionAction extends AbstractSelectionAction {
	constructor(props) {
		super(props);
		this.notifier = new SelectionActionNotifier(this);
		this.requester = new SelectionActionRequester(this);
	};
}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SelectionAction));
DisplayFactory.register("SelectionAction", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SelectionAction)));