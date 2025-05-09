import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractExternalTemplateStamp from "../../../gen/displays/components/AbstractExternalTemplateStamp";
import ExternalTemplateStampNotifier from "../../../gen/displays/notifiers/ExternalTemplateStampNotifier";
import ExternalTemplateStampRequester from "../../../gen/displays/requesters/ExternalTemplateStampRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class ExternalTemplateStamp extends AbstractExternalTemplateStamp {

	constructor(props) {
		super(props);
		this.notifier = new ExternalTemplateStampNotifier(this);
		this.requester = new ExternalTemplateStampRequester(this);
	};

    render() {
        return (<div className="layout vertical flex" style={{width:"100%",height:"100%"}}>{this.renderInstances(null, null, {width:"100%",height:"100%"})}</div>);
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(ExternalTemplateStamp));
DisplayFactory.register("ExternalTemplateStamp", withStyles(styles, { withTheme: true })(withSnackbar(ExternalTemplateStamp)));