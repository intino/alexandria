import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractOpenDrawer from "../../../gen/displays/components/AbstractOpenDrawer";
import OpenDrawerNotifier from "../../../gen/displays/notifiers/OpenDrawerNotifier";
import OpenDrawerRequester from "../../../gen/displays/requesters/OpenDrawerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import Actionable from "./Actionable";

class OpenDrawer extends AbstractOpenDrawer {

	constructor(props) {
		super(props);
		this.notifier = new OpenDrawerNotifier(this);
		this.requester = new OpenDrawerRequester(this);
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(OpenDrawer));
DisplayFactory.register("OpenDrawer", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(OpenDrawer)));