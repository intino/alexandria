import React, {Suspense} from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMap from "../../../gen/displays/components/AbstractMap";
import MapNotifier from "../../../gen/displays/notifiers/MapNotifier";
import MapRequester from "../../../gen/displays/requesters/MapRequester";
import {CollectionStyles} from "./Collection";
import { GoogleMap, MarkerClusterer, HeatmapLayer, KmlLayer } from '@react-google-maps/api'
import { Marker, Polygon, Polyline, InfoWindow } from '@react-google-maps/api'
import 'alexandria-ui-elements/res/styles/layout.css';
import GoogleApi from "./map/GoogleApi";

const styles = theme => ({
	...CollectionStyles(theme),
	message : {
		position: "absolute",
		top: 0,
		left: 0,
		color: "white",
		fontSize: "13pt",
		margin: "25% 20%",
		background: "#ca991e",
		padding: "5px 10px",
		borderRadius: "4px",
		border: "1px solid",
	}
});

class Map extends AbstractMap {

	state = {
		placeMarks: [],
		placeMark: null,
		kmlLayer: null,
	};

	constructor(props) {
		super(props);
		this.notifier = new MapNotifier(this);
		this.requester = new MapRequester(this);
		this.container = React.createRef();
	};

	render() {
		const container = this.container.current;
		const height = $(container).height();

		return (
			<div ref={this.container} className="layout flex">
				<GoogleApi>
					<GoogleMap className="map" zoom={this.props.zoom.defaultZoom} center={this._center()}>
						<div style={{height: height, width: '100%'}}/>
						{this.renderLayer()}
						{this.renderInfoWindow()}
					</GoogleMap>
				</GoogleApi>
			</div>
		);
	};

	renderLayer = () => {
		if (this.isCluster()) return this.renderCluster();
		else if (this.isHeatMap()) return this.renderHeatmap();
		else if (this.isKml()) return this.renderKml();
		return this.renderPlaceMarks();
	}

	renderCluster = () => {
		return (
			<MarkerClusterer
				options={{imagePath:"https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m"}}>
				{
					(clusterer) => {
						return this.state.placeMarks.map((p, i) => this.renderPlaceMark(p, i, clusterer));
					}
				}
			</MarkerClusterer>
		);
	};

	renderHeatmap = () => {
		const data = this.state.placeMarks.map(pm => new google.maps.LatLng(pm.location.pointList[0].lat, pm.location.pointList[0].lng));
		return (<HeatmapLayer data={data}/>);
	};

	renderKml = () => {
		const { classes } = this.props;
		var isLocal = this.state.kmlLayer != null && this.state.kmlLayer.indexOf("localhost") !== -1;
		return isLocal ? (<div className={classes.message}>{this.translate("Kml layers are not supported in local server")}</div>) : (<KmlLayer url={this.state.kmlLayer}/>);
	};

	renderPlaceMarks = () => {
		return this.state.placeMarks.map((p, i) => this.renderPlaceMark(p, i));
	};

	renderPlaceMark = (placeMark, pos, clusterer) => {
		const location = placeMark.location;
		if (location.type === "Polyline") return (<Polyline key={pos} path={location.pointList} clusterer={clusterer} onClick={this.showInfo.bind(this, placeMark)}/>);
		else if (location.type === "Polygon") return (<Polygon key={pos} path={location.pointList} clusterer={clusterer} onClick={this.showInfo.bind(this, placeMark)}/>);
		return (<Marker key={pos} position={location.pointList[0]} clusterer={clusterer} onClick={this.showInfo.bind(this, placeMark)}/>);
	};

	renderInfoWindow = () => {
		if (this.state.placeMark == null) return null;
		const placeMark = this.state.placeMark;
		return (
			<InfoWindow position={placeMark.location.pointList[0]}>
				<div>{placeMark.pos}</div>
			</InfoWindow>
		);
	};

	showInfo = (placeMark) => {
		this.setState({ placeMark: placeMark })
	};

	_center = () => {
		return this.props.center != null ? this.props.center : undefined;
	};

	placeMarks = (placeMarks) => {
		this.setState({ placeMarks: placeMarks });
	};

	isCluster = () => {
		return this.props.type != null && this.props.type.toLowerCase() === "cluster";
	};

	isHeatMap = () => {
		return this.props.type != null && this.props.type.toLowerCase() === "heatmap";
	};

	isKml = () => {
		return this.props.type != null && this.props.type.toLowerCase() === "kml";
	};

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, kmlLayer: info.kmlLayer });
	};
}

export default withStyles(styles, { withTheme: true })(Map);