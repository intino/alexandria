import React from "react";
import AbstractBaseLocation from "../../../gen/displays/components/AbstractBaseLocation";
import BaseLocationNotifier from "../../../gen/displays/notifiers/BaseLocationNotifier";
import BaseLocationRequester from "../../../gen/displays/requesters/BaseLocationRequester";
import { GoogleMap } from '@react-google-maps/api'
import PlaceMark from "./geo/PlaceMark";
import GeoBehavior from "./behaviors/GeoBehavior";
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	map : {
		minHeight: "100%"
	}
});

export default class BaseLocation extends AbstractBaseLocation {

	state = {
		location: null,
		icon: null
	};

	constructor(props) {
		super(props);
		this.notifier = new BaseLocationNotifier(this);
		this.requester = new BaseLocationRequester(this);
		this.container = React.createRef();
		this.googleMapLayer = React.createRef();
	};

	renderLayer = (content) => {
		const { classes } = this.props;

		window.setTimeout(() => this.resize(), 100);

		return (
			<div ref={this.container} className="layout flex" style={{height:"100%"}}>
				<GoogleMap className={classes.map} zoom={this.props.zoom.defaultZoom}
						   center={GeoBehavior.center(this)}>
					<div ref={this.googleMapLayer} style={{width:'100%'}}/>
					{this.renderPlaceMark()}
					{content != null && content()}
				</GoogleMap>
			</div>
		);
	};

	resize = function() {
		const container = this.container.current;
		this.googleMapLayer.current.style.height = $(container).height() + "px";
	};

	renderPlaceMark = () => {
		const icon = this.state.icon;
		const location = this.state.location;
		if (location == null) return;
		return (
			<PlaceMark placeMark={{icon:icon,location:location}}/>
		);
	};

	setup = (info) => {
		this.setState({ icon: info.icon });
	};

	refresh = (location) => {
		this.setState({ location: location });
	};

}