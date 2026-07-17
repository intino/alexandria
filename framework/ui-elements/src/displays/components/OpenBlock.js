import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractOpenBlock from "../../../gen/displays/components/AbstractOpenBlock";
import OpenBlockNotifier from "../../../gen/displays/notifiers/OpenBlockNotifier";
import OpenBlockRequester from "../../../gen/displays/requesters/OpenBlockRequester";
import Actionable from "./Actionable"
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

class OpenBlock extends AbstractOpenBlock {

	constructor(props) {
		super(props);
		this.notifier = new OpenBlockNotifier(this);
		this.requester = new OpenBlockRequester(this);
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(OpenBlock));
DisplayFactory.register("OpenBlock", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(OpenBlock)));