import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractWidgetTypeTemplate from "../../../gen/displays/templates/AbstractWidgetTypeTemplate";
import WidgetTypeTemplateNotifier from "../../../gen/displays/notifiers/WidgetTypeTemplateNotifier";
import WidgetTypeTemplateRequester from "../../../gen/displays/requesters/WidgetTypeTemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {useSnackbar} from "notistack";

const styles = theme => ({});

class WidgetTypeTemplate extends AbstractWidgetTypeTemplate {

	constructor(props) {
		super(props);
		this.notifier = new WidgetTypeTemplateNotifier(this);
		this.requester = new WidgetTypeTemplateRequester(this);
		Application.services.pushService.onClose(() => {
			const { enqueueSnackbar, closeSnackbar } = useSnackbar();
			const message = Application.services.translatorService.translate("Connection lost");
			const options = { variant: "error", anchorOrigin: { vertical: 'top', horizontal: 'center' }};
			enqueueSnackbar(message, options);
		});
	};


}

export default withStyles(styles, { withTheme: true })(WidgetTypeTemplate);
DisplayFactory.register("WidgetTypeTemplate", withStyles(styles, { withTheme: true })(WidgetTypeTemplate));