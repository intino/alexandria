import React from "react";
import Typography from "@material-ui/core/Typography";
import { withStyles } from '@material-ui/core/styles';
import AbstractEventsDisplay from "../../gen/displays/AbstractEventsDisplay";
import EventsDisplayNotifier from "../../gen/displays/notifiers/EventsDisplayNotifier";
import EventsDisplayRequester from "../../gen/displays/requesters/EventsDisplayRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import classnames from "classnames";
import Text from "alexandria-ui-elements/src/displays/components/Text";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	list : {
		listStyle: "none",
		margin: 0,
		padding: 0
	},
	item : {
		marginTop: "10px",
		marginBottom: "5px"
	}
});

class EventsDisplay extends AbstractEventsDisplay {
	state = {
		eventList: []
	};

	constructor(props) {
		super(props);
		this.notifier = new EventsDisplayNotifier(this);
		this.requester = new EventsDisplayRequester(this);
	};

	render() {
		const {classes} = this.props;
		const eventList = this.state.eventList;
		return (
			<ul className={classes.list}>{eventList != null && eventList.length > 0 ? this.renderEvents() : this.emptyEvents()}</ul>
		);
	};

	renderEvents = () => {
		return this.state.eventList.map((property, index) => this.renderEvent(property, index));
	};

	renderEvent = (event, index) => {
		const {classes} = this.props;
		return (<li className={classnames(classes.item, "layout vertical")} key={index} >
			{ event.facets != null ? <Text format="body2 facetsAbsolute" mode="uppercase" value={event.facets.join(", ")}/> : undefined }
			<div className="layout horizontal">
				<Text format="h6" value={event.name + "("}/>
				{event.params.map((param, index) => {
					return (<Text format="h6" value={param.name + ": " + param.type} key={index}/>);
				})}
				<Text format="h6" value=")"/>
				<Text format="h6" value=":&nbsp;&nbsp;"/>
				<Text format="h6 widgetType" value={event.returnType}/>
			</div>
			<Typography>{event.description}</Typography>
		</li>);
	};

	emptyEvents = () => {
		return (<li><Typography>{this.translate("no events")}</Typography></li>);
	};

	refresh = (eventList) => {
		this.setState({eventList});
	};
}

export default withStyles(styles, { withTheme: true })(EventsDisplay);
DisplayFactory.register("EventsDisplay", withStyles(styles, { withTheme: true })(EventsDisplay));