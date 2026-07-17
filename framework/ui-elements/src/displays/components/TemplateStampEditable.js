import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractTemplateStampEditable from "../../../gen/displays/components/AbstractTemplateStampEditable";
import TemplateStampEditableNotifier from "../../../gen/displays/notifiers/TemplateStampEditableNotifier";
import TemplateStampEditableRequester from "../../../gen/displays/requesters/TemplateStampEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";

const styles = theme => ({});

class TemplateStampEditable extends AbstractTemplateStampEditable {

	constructor(props) {
		super(props);
		this.notifier = new TemplateStampEditableNotifier(this);
		this.requester = new TemplateStampEditableRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(TemplateStampEditable));
DisplayFactory.register("TemplateStampEditable", withStyles(styles, { withTheme: true })(withSnackbar(TemplateStampEditable)));