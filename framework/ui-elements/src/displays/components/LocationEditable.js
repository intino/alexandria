import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Button } from '@material-ui/core';
import AbstractLocationEditable from "../../../gen/displays/components/AbstractLocationEditable";
import LocationEditableNotifier from "../../../gen/displays/notifiers/LocationEditableNotifier";
import LocationEditableRequester from "../../../gen/displays/requesters/LocationEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { DrawingManager } from '@react-google-maps/api'
import GeometryUtil from "../../util/GeometryUtil";

const styles = theme => ({
	remove : {
		position: "absolute",
		top: "0",
		margin: "10px",
		padding: "0 8px",
		fontSize: "8pt",
	}
});

class LocationEditable extends AbstractLocationEditable {

	constructor(props) {
		super(props);
		this.notifier = new LocationEditableNotifier(this);
		this.requester = new LocationEditableRequester(this);
        this.state = {
            ...this.state,
            drawingControl: true,
            drawingMode: null,
            readonly: this.props.readonly,
        };
	};

	render() {
		return this.renderLayer(() => this.renderEditor());
	};

	renderEditor = () => {
		if (this.state.readonly) return;

		const { classes } = this.props;
		const drawingModes = this._drawingModes();
		const drawingOptions = { drawingControl:this.state.drawingControl, drawingControlOptions:{drawingModes:drawingModes} };

		drawingOptions.drawingMode = this.state.drawingControl ? this.state.drawingMode : null;

		return (
			<div>
				<DrawingManager options={drawingOptions}
								onMarkerComplete={this.handleMarkerChange.bind(this)}
								onPolylineComplete={this.handlePolylineChange.bind(this)}
								onRectangleComplete={this.handleRectangleChange.bind(this)}
								onPolygonComplete={this.handlePolygonChange.bind(this)} />
				{!this.state.drawingControl && <Button className={classes.remove} onClick={this.handleRemove.bind(this)} variant="contained">{this.translate("Remove location")}</Button>}
			</div>
		);
	};

	_drawingModes = () => {
		let drawingModes = this.props.modes;
		if (drawingModes == null) return ['marker', 'polygon', 'polyline', 'rectangle'];
		let result = [];
		for (let i=0; i<drawingModes.length; i++) {
			let mode = drawingModes[i].toLowerCase();
			if (mode.toLowerCase() === "point") result.push('marker');
			else result.push(mode);
		}
		return result;
	};

	handleMarkerChange = (marker) => {
		this.requester.notifyChange(GeometryUtil.toPoint(marker));
		this.notifyChange(marker, "marker");
	};

	handlePolylineChange = (polyline) => {
		this.requester.notifyChange(GeometryUtil.toPolyline(polyline));
		this.notifyChange(polyline, "polyline");
	};

	handleRectangleChange = (rectangle) => {
		this.requester.notifyChange(GeometryUtil.rectangleToPolygon(rectangle));
		this.notifyChange(rectangle, "rectangle");
	};

	handlePolygonChange = (polygon) => {
		this.requester.notifyChange(GeometryUtil.toPolygon(polygon));
		this.notifyChange(polygon, "polygon");
	};

	notifyChange = (geometry, mode) => {
		this.selectedShape = geometry;
		this.setState({drawingControl : false, drawingMode: mode});
	};

	handleRemove = () => {
		if (this.selectedShape != null) this.selectedShape.setMap(null);
		this.requester.notifyChange(null);
		this.setState({ location: null, drawingControl : true});
	};

	refresh = (location) => {
	    if (this.selectedShape != null) this.selectedShape.setMap(null);
	    this.selectedShape = null;
		this.setState({ location: location, drawingControl : location == null });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(LocationEditable));
DisplayFactory.register("LocationEditable", withStyles(styles, { withTheme: true })(withSnackbar(LocationEditable)));