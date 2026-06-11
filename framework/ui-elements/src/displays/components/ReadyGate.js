import React from "react";

export default class ReadyGate extends React.Component {
	componentDidMount() {
		const sendReady = () => {
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
