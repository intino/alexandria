import React from "react";
import AbstractProxyDisplay from "../../gen/displays/AbstractProxyDisplay";
import ProxyDisplayNotifier from "../../gen/displays/notifiers/ProxyDisplayNotifier";
import ProxyDisplayRequester from "../../gen/displays/requesters/ProxyDisplayRequester";
import Spinner from "alexandria-ui-elements/src/displays/components/Spinner";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Typography from '@material-ui/core/Typography';
import 'alexandria-ui-elements/res/styles/layout.css';
import { OwnerUnitContext } from 'alexandria-ui-elements/src/displays/PassiveView'

export default class ProxyDisplay extends AbstractProxyDisplay {

	constructor(props) {
		super(props);
		this.notifier = new ProxyDisplayNotifier(this);
		this.requester = new ProxyDisplayRequester(this);
		this.state = {
            displayType: null,
            error: null,
            connecting: true,
		};
	};

	render() {
		if (this.state.error != null) return this._renderError();
		if (this.state.ownerUnit == null || this.state.connecting) return this._renderLoading();

		const available = this.requester.available(this.state.ownerUnit);
		if (!available) return this._renderError();

		return (
			<OwnerUnitContext.Provider value={this.state.ownerUnit}>
				<div id="component" style={{height:"100%",width:"100%"}}>
					{React.createElement(DisplayFactory.get(this.state.display.type), { id: this.state.display.id })}
					{this.requester.ready()}
				</div>
			</OwnerUnitContext.Provider>
		);
	}

	_renderLoading = () => {
		return (
			<div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }>
				<Spinner/>
			</div>
		);
	};

	_renderError = () => {
		const available = this.requester.available(this.state.ownerUnit);
		const message = this.state.error != null ? this.state.error : "no connection with " + this.state.ownerUnit + "!";
		return (
			<div className="layout horizontal" style={ {margin: "10px 0", height: "100%"} }>
				<Typography style={{color:"red"}}>{this.translate(message)}</Typography>
			</div>
		);
	};

	refresh = (info) => {
	    if (this.unit != null && this.state.connecting) return;
	    this.unit = info.unit;
	    window.setTimeout(() => this.requester.connect(info.unit, this.onConnect.bind(this, info), this.onConnectError.bind(this, info)), 100);
	};

	refreshError = (error) => {
		this.setState({error});
	};

	onConnect = (info) => {
	    this.setState({ ownerUnit: info.unit, display: info.display, connecting: false, error: null });
	    this.requester.connected();
	};

	onConnectError = (info) => {
	    this.setState({ ownerUnit: info.unit, display: info.display, connecting: false, error: this.translate("no connection with") + " " + info.unit });
	};

}

DisplayFactory.register("ProxyDisplay", ProxyDisplay);