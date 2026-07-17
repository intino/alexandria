import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractOpenNextItem from "../../../gen/displays/components/AbstractOpenNextItem";
import OpenNextItemNotifier from "../../../gen/displays/notifiers/OpenNextItemNotifier";
import OpenNextItemRequester from "../../../gen/displays/requesters/OpenNextItemRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";

const styles = theme => ({});

class OpenNextItem extends AbstractOpenNextItem {

	constructor(props) {
		super(props);
		this.notifier = new OpenNextItemNotifier(this);
		this.requester = new OpenNextItemRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(OpenNextItem));
DisplayFactory.register("OpenNextItem", withStyles(styles, { withTheme: true })(withSnackbar(OpenNextItem)));