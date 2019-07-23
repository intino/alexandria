import React from "react";
import AbstractBaseLocation from "../../../gen/displays/components/AbstractBaseLocation";
import BaseLocationNotifier from "../../../gen/displays/notifiers/BaseLocationNotifier";
import BaseLocationRequester from "../../../gen/displays/requesters/BaseLocationRequester";
import GoogleApi from "./geo/GoogleApi";
import { GoogleMap, LoadScript } from '@react-google-maps/api'
import PlaceMark from "./geo/PlaceMark";
import GeoBehavior from "./behaviors/GeoBehavior";

const styles = theme => ({});

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
	};

	renderLayer = (content) => {
		const container = this.container.current;
		const height = $(container).height();
		return (
			<div ref={this.container} className="layout flex">
				<GoogleApi>
					<GoogleMap className="map" zoom={this.props.zoom.defaultZoom}
							   center={GeoBehavior.center(this)}>
						<div style={{height: height, width: '100%'}}/>
						{this.renderPlaceMark()}
						{content != null && content()}
					</GoogleMap>
				</GoogleApi>
			</div>
		);
	};

	renderPlaceMark = () => {
		const icon = this.state.icon;
		const location = this.state.location;
		const label = this.translate(this.props.label != null ? this.props.label : undefined);
		if (location == null) return;
		return (
			<PlaceMark placeMark={{label:label,icon:icon,location:location}} clusterer={false}/>
		);
	};

	setup = (info) => {
		this.setState({ icon: info.icon });
	};

	refresh = (location) => {
		this.setState({ location: location });
	};

}