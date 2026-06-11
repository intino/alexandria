import React from "react";
import {DisplayRouterContext} from "../DisplayRouter";

export default class ReadyGate extends React.Component {
	static contextType = DisplayRouterContext;

	componentDidMount() {
		const sendReady = () => {
			if (this.context != null && this.context.ready != null) {
				this.context.ready();
				return;
			}
			if (Application != null && Application.services != null && Application.services.pushService != null) {
				Application.services.pushService.ready();
			}
		};
		if (typeof window === "undefined") return;
		if (document.readyState === "complete") {
			window.setTimeout(sendReady, 0);
			return;
		}
		window.addEventListener("load", sendReady, { once: true });
	}

	render() {
		return null;
	}
}
