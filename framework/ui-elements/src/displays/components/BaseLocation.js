import React from "react";
import AbstractBaseLocation from "../../../gen/displays/components/AbstractBaseLocation";
import BaseLocationNotifier from "../../../gen/displays/notifiers/BaseLocationNotifier";
import BaseLocationRequester from "../../../gen/displays/requesters/BaseLocationRequester";
import { GoogleMap } from '@react-google-maps/api'
import PlaceMark from "./geo/PlaceMark";
import SearchDialog from "./geo/SearchDialog";
import GeoBehavior from "./behaviors/GeoBehavior";
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	map : {
		minHeight: "100%"
	}
});

export default class BaseLocation extends AbstractBaseLocation {

	constructor(props) {
		super(props);
		this.notifier = new BaseLocationNotifier(this);
		this.requester = new BaseLocationRequester(this);
		this.container = React.createRef();
		this.googleMapLayer = React.createRef();
        this.state = {
            ...this.state,
            location: null,
            icon: null,
            center: null,
            zoom: null,
        }
	};

	renderLayer = (content) => {
		const { classes } = this.props;

		window.setTimeout(() => this.resize(), 100);

		return (
			<div ref={this.container} className="layout flex" style={{position:'relative',height:"100%"}}>
			    {this.renderSearch()}
				<GoogleMap className={classes.map} zoom={GeoBehavior.zoom(this).defaultZoom}
						   center={GeoBehavior.center(this)} options={this.mapOptions()}
						   onLoad={this.registerMap.bind(this)}>
					<div ref={this.googleMapLayer} style={{width:'100%'}}/>
					{this.renderPlaceMark()}
					{content != null && content()}
				</GoogleMap>
			</div>
		);
	};

	renderSearch = () => {
	    if (!this.allowSearch()) return (<React.Fragment/>);
	    return (<SearchDialog map={this.getMap.bind(this)} classes={this.props.classes} mapOptions={this.mapOptions()}/>);
	};

	getMap = () => {
	    return this.map;
	};

	registerMap = (map) => {
	    this.map = map;
	};

	resize = function() {
		if (this.googleMapLayer.current == null) return;
		const container = this.container.current;
		const height = $(container).height();
		if (height == 0) {
		    window.setTimeout(() => this.resize(), 100);
		    return;
		}
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

    refreshCenter = (center) => {
        this.setState({ center });
    };

    refreshZoom = (value) => {
        const zoom = GeoBehavior.zoom(this);
        this.setState({ min: zoom.min, max: zoom.max, defaultZoom: value });
    };

    refreshZoomRange = (range) => {
        const zoom = GeoBehavior.zoom(this);
        this.setState({ min: range.min, max: range.max, defaultZoom: zoom.defaultZoom });
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

    allowSearch = () => {
        const controls = this.props.controls != null ? this.props.controls : "";
        return controls.indexOf("all") != -1 || controls.indexOf("search") != -1;
    };
}