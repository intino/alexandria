import React from "react";
import {withStyles} from '@material-ui/core/styles';
import AbstractLocationEditable from "../../../gen/displays/components/AbstractLocationEditable";
import LocationEditableNotifier from "../../../gen/displays/notifiers/LocationEditableNotifier";
import LocationEditableRequester from "../../../gen/displays/requesters/LocationEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from 'notistack';
import {Marker, Polygon, Polyline} from '@react-google-maps/api'
import GeometryUtil from "../../util/GeometryUtil";

const styles = theme => ({
	controls : {
		position: "absolute",
		zIndex: 2,
		display: "flex",
		gap: "8px",
		flexWrap: "wrap",
	},
	controlButton : {
		background: "white",
		border: "1px solid #dadce0",
		borderRadius: "2px",
		boxShadow: "0 1px 4px rgba(0,0,0,0.25)",
		color: "#202124",
		cursor: "pointer",
		fontFamily: "Roboto, Arial, sans-serif",
		fontSize: "13px",
		fontWeight: 500,
		lineHeight: "18px",
		padding: "8px 12px",
		transition: "background 0.15s ease, box-shadow 0.15s ease",
		'&:hover': {
			background: "#f8f9fa",
		},
		'&:active': {
			boxShadow: "0 1px 2px rgba(0,0,0,0.3)",
		},
	},
	controlButtonActive : {
		background: "#e8f0fe",
		borderColor: "#aecbfa",
		color: "#174ea6",
	},
	remove : {
		position: "absolute",
		background: "white",
		border: "1px solid #dadce0",
		borderRadius: "2px",
		boxShadow: "0 1px 4px rgba(0,0,0,0.25)",
		color: "#202124",
		cursor: "pointer",
		fontFamily: "Roboto, Arial, sans-serif",
		fontSize: "13px",
		fontWeight: 500,
		lineHeight: "18px",
		padding: "8px 12px",
		transition: "background 0.15s ease, box-shadow 0.15s ease",
		'&:hover': {
			background: "#f8f9fa",
		},
		'&:active': {
			boxShadow: "0 1px 2px rgba(0,0,0,0.3)",
		},
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
            focused: this.props.focused,
            draftPoints: [],
            draftRectangleStart: null,
        };
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
		return this.renderLayer(() => this.renderEditor());
	};

	renderEditor = () => {
		if (this.state.readonly) return;

		const { classes } = this.props;
		return (
			<div>
                {this.renderEditorControls()}
                {this.renderCurrentLocation()}
                {this.renderDraft()}
				{!this.state.drawingControl && <button type="button" className={classes.remove} style={this._removeButtonStyle()} onClick={this.handleRemove.bind(this)}>{this.translate("Remove location")}</button>}
			</div>
		);
	};

	renderEditorControls = () => {
		const drawingModes = this._drawingModes();
		const currentMode = this.state.drawingMode;
		return (
			<div className={this.props.classes.controls} style={this._editorControlsStyle()}>
				{drawingModes.map((mode) => (
					<button
						type="button"
						key={mode}
						className={`${this.props.classes.controlButton} ${currentMode === mode ? this.props.classes.controlButtonActive : ""}`}
						onClick={this.startDrawing.bind(this, mode)}>
						{this._modeLabel(mode)}
					</button>
				))}
				{currentMode != null && (currentMode === "polygon" || currentMode === "polyline") && this.state.draftPoints.length > 0 &&
					<button type="button" className={`${this.props.classes.controlButton} ${this.props.classes.controlButtonActive}`} onClick={this.finishDraft.bind(this)}>
						{this.translate("Finish")}
					</button>}
				{currentMode != null &&
					<button type="button" className={this.props.classes.controlButton} onClick={this.cancelDraft.bind(this)}>
						{this.translate("Cancel")}
					</button>}
			</div>
		);
	};

	renderCurrentLocation = () => {
		const location = this.state.location;
		if (location == null) return null;
		const structure = GeometryUtil.toGoogleMapStructure(location);
		if (location.type === "Polyline") return (<Polyline path={structure} options={{ clickable: false }} />);
		if (location.type === "Polygon") return (<Polygon paths={structure} options={{ clickable: false }} />);
		return (<Marker position={structure} />);
	};

	renderDraft = () => {
		const mode = this.state.drawingMode;
		if (mode == null) return null;
		if (mode === "marker") return null;
		if (mode === "rectangle") {
			const start = this.state.draftRectangleStart;
			if (start == null) return null;
			return (<Marker position={start} />);
		}
		if (this.state.draftPoints.length === 0) return null;
		return (
			<Polyline
				path={this.state.draftPoints}
				options={{
					clickable: false,
					strokeColor: "#1a73e8",
					strokeOpacity: 0.9,
					strokeWeight: 3,
					strokeDasharray: "4 4",
				}}
			/>
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

	_modeLabel = (mode) => {
		if (mode === "marker") return this.translate("Point");
		return this.translate(mode.charAt(0).toUpperCase() + mode.slice(1));
	};

	startDrawing = (mode) => {
		this.setState({
			drawingControl: this.state.location == null,
			drawingMode: mode,
			draftPoints: [],
			draftRectangleStart: null,
		});
	};

	registerMap = (map) => {
		this.map = map;
		map.addListener("click", this.handleMapClick.bind(this));
	};

	hideActiveMarker = () => {
	};

	handleMapClick = (event) => {
		if (this.state.readonly || this.state.drawingMode == null || event == null || event.latLng == null) return;
		const point = { lat: event.latLng.lat(), lng: event.latLng.lng() };
		if (this.state.drawingMode === "marker") {
			this.commitGeometry({ type: "Point", point }, null, true);
			return;
		}
		if (this.state.drawingMode === "rectangle") {
			if (this.state.draftRectangleStart == null) {
				this.setState({ draftRectangleStart: point });
				return;
			}
			this.commitGeometry(this.rectangleFromPoints(this.state.draftRectangleStart, point), null, true);
			return;
		}
		this.setState({ draftPoints: this.state.draftPoints.concat(point) });
	};

	finishDraft = () => {
		if (this.state.drawingMode === "polyline" && this.state.draftPoints.length >= 2) {
			this.commitGeometry({ type: "Polyline", path: { pointList: this.state.draftPoints } }, "polyline", true);
		} else if (this.state.drawingMode === "polygon" && this.state.draftPoints.length >= 3) {
			const points = this.state.draftPoints.concat([this.state.draftPoints[0]]);
			this.commitGeometry({ type: "Polygon", paths: [{ pointList: points }] }, "polygon", true);
		}
	};

	cancelDraft = () => {
		this.setState({ drawingMode: null, draftPoints: [], draftRectangleStart: null, drawingControl: this.state.location == null });
	};

	commitGeometry = (geometry, mode, clearDraft) => {
		this.requester.notifyChange(geometry);
		this.selectedShape = geometry;
		this.setState({
			location: geometry,
			drawingControl: false,
			drawingMode: clearDraft ? null : mode,
			draftPoints: [],
			draftRectangleStart: null,
		});
	};

	rectangleFromPoints = (start, end) => {
		const north = Math.max(start.lat, end.lat);
		const south = Math.min(start.lat, end.lat);
		const east = Math.max(start.lng, end.lng);
		const west = Math.min(start.lng, end.lng);
		return {
			type: "Polygon",
			paths: [{
				pointList: [
					{ lat: north, lng: west },
					{ lat: north, lng: east },
					{ lat: south, lng: east },
					{ lat: south, lng: west },
					{ lat: north, lng: west },
				],
			}],
		};
	};

	handleRemove = () => {
		this.selectedShape = null;
		this.requester.notifyChange(null);
		this.setState({ location: null, drawingControl : true, drawingMode: null, draftPoints: [], draftRectangleStart: null });
	};

	refresh = (location) => {
	    this.selectedShape = null;
		this.setState({ location: location, drawingControl : location == null, drawingMode: null, draftPoints: [], draftRectangleStart: null });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	refreshFocused = (focused) => {
		this.setState({ focused });
	};

    _removeButtonStyle = () => {
        const options = this.mapOptions();
        return {
            top: (options.fullscreenControl ? 60 : 10) + "px",
            right: "10px",
            left: "auto",
        };
    };

	_editorControlsStyle = () => {
		const options = this.mapOptions();
		return {
			position: "absolute",
			top: (options.mapTypeControl ? 60 : 10) + "px",
			left: "10px",
		};
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(LocationEditable));
DisplayFactory.register("LocationEditable", withStyles(styles, { withTheme: true })(withSnackbar(LocationEditable)));
