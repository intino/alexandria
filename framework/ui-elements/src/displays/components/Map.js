import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMap from "../../../gen/displays/components/AbstractMap";
import MapNotifier from "../../../gen/displays/notifiers/MapNotifier";
import MapRequester from "../../../gen/displays/requesters/MapRequester";
import {CollectionStyles} from "./Collection";
import { GoogleMap, MarkerClusterer, HeatmapLayer, KmlLayer } from '@react-google-maps/api'
import 'alexandria-ui-elements/res/styles/layout.css';
import PlaceMark from "./geo/PlaceMark";
import SearchDialog from "./geo/SearchDialog";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import GeoBehavior from "./behaviors/GeoBehavior";
import GeometryUtil from "../../util/GeometryUtil";

export const MapStyles = theme => ({
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
	},
});

export class EmbeddedMap extends AbstractMap {

	constructor(props) {
		super(props);
		this.notifier = new MapNotifier(this);
		this.requester = new MapRequester(this);
		this.container = React.createRef();
        this.map = null;
        this.state = {
            ...this.state,
            placeMarks: [],
            center: this.props.center,
            zoom: this.props.zoom,
            placeMark: null,
            kmlLayer: null,
            closeAllInfoWindows: true,
        };
	};

	render() {
		const container = this.container.current;
		let height = $(container).height();
		return (
			<div ref={this.container} className="layout flex" style={{position:'relative'}}>
			    {this.renderSearch()}
				<GoogleMap
				    className="map"
				    mapContainerStyle={{height:"100%"}}
				    zoom={GeoBehavior.zoom(this).defaultZoom}
				    center={GeoBehavior.center(this)}
				    onLoad={this.registerMap.bind(this)}
				    options={this.mapOptions()}>
					<div style={{height: height - 100, width: '100%'}}/>
					{this.renderLayer()}
				</GoogleMap>
			</div>
		);
	};

	renderSearch = () => {
	    return (<SearchDialog map={this.getMap.bind(this)} classes={this.props.classes} mapOptions={this.mapOptions()}/>);
	};

	getMap = () => {
	    return this.map;
	};

	registerMap = (map) => {
	    this.map = map;
		map.addListener("click", this.hideActiveMarker.bind(this));
	};

	renderLayer = () => {
		if (this.isCluster()) return this.renderCluster();
		else if (this.isHeatMap()) return this.renderHeatmap();
		else if (this.isKml()) return this.renderKml();
		return this.renderPlaceMarks();
	};

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
		const data = this.state.placeMarks.map(pm => GeometryUtil.firstPoint(pm.location));
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
		const items = this.behavior.items();
		return (
			<PlaceMark icon={this.state.icon}
					   onShowInfo={this.handleShowInfo.bind(this)}
					   content={items.length > 0 ? items[0] : undefined}
					   key={pos} placeMark={placeMark} clusterer={clusterer}>
			</PlaceMark>
		);
	};

	handleShowInfo = (element) => {
		this.hideActiveMarker();
		element.showLoading();
		this.requester.showPlaceMark(element.props.placeMark.pos);
		this.current = element;
	};

	placeMarks = (placeMarks) => {
		this.setState({ placeMarks: placeMarks });
	};

	updateCenter = (center) => {
	    this.setState({center});
	};

	updateZoom = (zoom) => {
	    this.setState({zoom});
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
		this.setState({ itemCount : info.itemCount, kmlLayer: info.kmlLayer, icon: info.icon });
	};

	hideActiveMarker = () => {
		if (this.current == null) return;
		this.current.hideInfo();
	};

    mapOptions = () => {
        const controls = this.props.controls != null ? this.props.controls : "";
        const all = controls.indexOf("all") != -1;
		return {
            zoomControl: controls.indexOf("zoom") != -1 || all,
            mapTypeControl: controls.indexOf("maptype") != -1 || all,
            scaleControl: controls.indexOf("scale") != -1 || all,
            streetViewControl: controls.indexOf("streetview") != -1 || all,
            rotateControl: controls.indexOf("rotate") != -1 || all,
            fullscreenControl: controls.indexOf("fullscreen") != -1 || all
        };
    };

}

class Map extends EmbeddedMap {
    constructor(props) {
        super(props);
    }
}

export default withStyles(MapStyles, { withTheme: true })(Map);
DisplayFactory.register("Map", withStyles(MapStyles, { withTheme: true })(Map));