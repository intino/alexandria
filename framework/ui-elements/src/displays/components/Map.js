import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMap from "../../../gen/displays/components/AbstractMap";
import MapNotifier from "../../../gen/displays/notifiers/MapNotifier";
import MapRequester from "../../../gen/displays/requesters/MapRequester";
import {CollectionStyles} from "./Collection";
import { GoogleMap, LoadScript, MarkerClusterer, Marker } from '@react-google-maps/api'
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	...CollectionStyles(theme),
});

class Map extends AbstractMap {

	constructor(props) {
		super(props);
		this.notifier = new MapNotifier(this);
		this.requester = new MapRequester(this);
		this.container = React.createRef();
	};

	render() {
		const key = Application.configuration.googleApiKey;
		const container = this.container.current;
		const height = $(container).height();
		return (
			<div ref={this.container} className="layout flex">
				<LoadScript googleMapsApiKey={key}>
					<GoogleMap className="map" zoom={this.props.zoom.defaultZoom} center={this._center()}>
						<div style={{height: height, width: '100%'}}/>
						{this.state.placeMarks.map((placemark, i) => (
							<Marker
								key={i}
								position={this.location(placemark)}
								// clusterer={clusterer}
							/>
						))}
					</GoogleMap>
				</LoadScript>
			</div>
		);
	};

	_center = () => {
		return this.props.center != null ? this.props.center : undefined;
	};

	placeMarks = (placeMarks) => {
		this.setState({ placeMarks: placeMarks });
	};

	location = (placeMark) => {
		var wkt = new Wkt.Wkt();
		wkt.read(placeMark.location);
		return this._fixWkt(wkt).toObject(this._markerConfiguration());
	};

	_fixWkt = (wkt) => {
		for (var i=0; i<wkt.components.length; i++)
			this._fixWktComponent(wkt.components[i]);
		return wkt;
	};

	_fixWktComponent = (component) => {
		if (component instanceof Array) {
			for(var i=0; i<component.length; i++) {
				this._fixWktComponent(component[i]);
			}
		}
		else {
			var x = component.x;
			component.x = component.y;
			component.y = x;
		}
	};

	_markerConfiguration = () => {
		return {};
	};
}
/*
<MarkerClusterer
	options={{imagePath:"https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m"}}>
	{
		(clusterer) => [
			{lat: -31.563910, lng: 147.154312},
			{lat: -33.718234, lng: 150.363181},
			{lat: -33.727111, lng: 150.371124},
			{lat: -33.848588, lng: 151.209834},
			{lat: -33.851702, lng: 151.216968},
			{lat: -34.671264, lng: 150.863657},
			{lat: -35.304724, lng: 148.662905},
			{lat: -36.817685, lng: 175.699196},
			{lat: -36.828611, lng: 175.790222},
			{lat: -37.750000, lng: 145.116667},
			{lat: -37.759859, lng: 145.128708},
			{lat: -37.765015, lng: 145.133858},
			{lat: -37.770104, lng: 145.143299},
			{lat: -37.773700, lng: 145.145187},
			{lat: -37.774785, lng: 145.137978},
			{lat: -37.819616, lng: 144.968119},
			{lat: -38.330766, lng: 144.695692},
			{lat: -39.927193, lng: 175.053218},
			{lat: -41.330162, lng: 174.865694},
			{lat: -42.734358, lng: 147.439506},
			{lat: -42.734358, lng: 147.501315},
			{lat: -42.735258, lng: 147.438000},
			{lat: -43.999792, lng: 170.463352}
		].map((location, i) => (
			<Marker
				key={i}
				position={location}
				clusterer={clusterer}
			/>
		))
	}
</MarkerClusterer>
*/

export default withStyles(styles, { withTheme: true })(Map);