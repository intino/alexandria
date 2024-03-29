import React from "react";
import AbstractDisplayRouter from "../../gen/displays/AbstractDisplayRouter";
import DisplayRouterNotifier from "../../gen/displays/notifiers/DisplayRouterNotifier";
import DisplayRouterRequester from "../../gen/displays/requesters/DisplayRouterRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import history from 'alexandria-ui-elements/src/util/History';

class DisplayRouter extends AbstractDisplayRouter {
	static Listening = false;

	constructor(props) {
		super(props);
		this.notifier = new DisplayRouterNotifier(this);
		this.requester = new DisplayRouterRequester(this);
		this.stopListening = false;
		this.listenHistory();
	};

	listenHistory = () => {
		if (DisplayRouter.Listening) return;
		DisplayRouter.Listening = true;
		history.listen(({ pathname, search }) => { this.route(pathname + search); });
		history.onContinueListening = () => this.continueListening();
		history.onStopListening = () => this.stopListeningHistory();
	};

	continueListening = () => {
	    this.stopListening = false;
	};

	stopListeningHistory = () => {
	    this.stopListening = true;
	};

	route = (address) => {
	    if (this.stopListening) return;
		this.requester.dispatch(address);
	};

	render() {
		return (<React.Fragment>{this.props.children}</React.Fragment>);
	}
}

export default DisplayRouter;
DisplayFactory.register("DisplayRouter", DisplayRouter);