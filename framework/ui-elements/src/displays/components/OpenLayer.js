import React, { Suspense } from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenLayer from "../../../gen/displays/components/AbstractOpenLayer";
import OpenLayerNotifier from "../../../gen/displays/notifiers/OpenLayerNotifier";
import OpenLayerRequester from "../../../gen/displays/requesters/OpenLayerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Layer from 'alexandria-ui-elements/src/displays/components/Layer';
import Actionable from "./Actionable";

const styles = theme => ({});

class OpenLayer extends AbstractOpenLayer {

	constructor(props) {
		super(props);
		this.notifier = new OpenLayerNotifier(this);
		this.requester = new OpenLayerRequester(this);
		this.state = {
		    ...this.state,
		}
	};

	render = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		return (
		    <React.Fragment>
                <Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
                    {this.renderActionable()}
                </Suspense>
                {this.renderInstances()}
            </React.Fragment>
		);
	};

	doExecute = () => {
	    if (!this.canExecute()) return;
	    this.requester.openLayer();
	};

	openAddress = () => {
        this.requester.execute();
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(OpenLayer);
DisplayFactory.register("OpenLayer", withStyles(Actionable.Styles, { withTheme: true })(OpenLayer));