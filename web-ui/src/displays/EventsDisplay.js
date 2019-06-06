import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractEventsDisplay from "../../gen/displays/AbstractEventsDisplay";
import EventsDisplayNotifier from "../../gen/displays/notifiers/EventsDisplayNotifier";
import EventsDisplayRequester from "../../gen/displays/requesters/EventsDisplayRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class EventsDisplay extends AbstractEventsDisplay {

	constructor(props) {
		super(props);
		this.notifier = new EventsDisplayNotifier(this);
		this.requester = new EventsDisplayRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(EventsDisplay);
DisplayFactory.register("EventsDisplay", withStyles(styles, { withTheme: true })(EventsDisplay));