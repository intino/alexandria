import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractAction from "../../../gen/displays/components/AbstractAction";
import ActionNotifier from "../../../gen/displays/notifiers/ActionNotifier";
import ActionRequester from "../../../gen/displays/requesters/ActionRequester";
import Actionable from "./Actionable"
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

class Action extends AbstractAction {
	constructor(props) {
		super(props);
		this.notifier = new ActionNotifier(this);
		this.requester = new ActionRequester(this);
	};
}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(Action));
DisplayFactory.register("Action", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(Action)));