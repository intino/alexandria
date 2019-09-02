import React from "react";
import AbstractProxyDisplay from "../../gen/displays/AbstractProxyDisplay";
import ProxyDisplayNotifier from "../../gen/displays/notifiers/ProxyDisplayNotifier";
import ProxyDisplayRequester from "../../gen/displays/requesters/ProxyDisplayRequester";
import Spinner from "alexandria-ui-elements/src/displays/components/Spinner";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import 'alexandria-ui-elements/res/styles/layout.css';

export default class ProxyDisplay extends AbstractProxyDisplay {
	state = {
		baseUrl: null,
		displayType: null,
		error: null,
	};

	constructor(props) {
		super(props);
		this.notifier = new ProxyDisplayNotifier(this);
		this.requester = new ProxyDisplayRequester(this);
	};

	render() {
		if (this.state.error != null) {
			this._renderError();
			return;
		}

		if (this.state.baseUrl == null) {
			this._renderLoading();
			return;
		}

		return (
			<div id="component" base-url={this.state.baseUrl}>
				{React.createElement(DisplayFactory.get(this.state.displayType))}
				{this.requester.ready()}
			</div>
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
		return (
			<div className="layout horizontal center center-justified" style="height:100%;">
				<div id="error" className="error">{this.state.error}</div>
			</div>
		);
	};

	refresh = (info) => {
		this.setState({baseUrl: info.baseUrl, displayType: info.displayType});
	};

	refreshError = (error) => {
		this.setState({error});
	};

}

DisplayFactory.register("ProxyDisplay", ProxyDisplay);