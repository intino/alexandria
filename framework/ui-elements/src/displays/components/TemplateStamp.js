import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractTemplateStamp from "../../../gen/displays/components/AbstractTemplateStamp";
import TemplateStampNotifier from "../../../gen/displays/notifiers/TemplateStampNotifier";
import TemplateStampRequester from "../../../gen/displays/requesters/TemplateStampRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";

const styles = theme => ({});

class TemplateStamp extends AbstractTemplateStamp {

	constructor(props) {
		super(props);
		this.notifier = new TemplateStampNotifier(this);
		this.requester = new TemplateStampRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(TemplateStamp));
DisplayFactory.register("TemplateStamp", withStyles(styles, { withTheme: true })(withSnackbar(TemplateStamp)));