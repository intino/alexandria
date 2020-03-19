import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractProxyStamp from "../../../gen/displays/components/AbstractProxyStamp";
import ProxyStampNotifier from "../../../gen/displays/notifiers/ProxyStampNotifier";
import ProxyStampRequester from "../../../gen/displays/requesters/ProxyStampRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class ProxyStamp extends AbstractProxyStamp {

	constructor(props) {
		super(props);
		this.notifier = new ProxyStampNotifier(this);
		this.requester = new ProxyStampRequester(this);
	};

    render() {
        return (<div className="layout vertical flex" style={{width:"100%",height:"100%"}}>{this.renderInstances(null, null, {width:"100%",height:"100%"})}</div>);
    }

}

export default withStyles(styles, { withTheme: true })(withSnackbar(ProxyStamp));
DisplayFactory.register("ProxyStamp", withStyles(styles, { withTheme: true })(withSnackbar(ProxyStamp)));