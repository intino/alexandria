import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractLocationEditable from "../../../gen/displays/components/AbstractLocationEditable";
import LocationEditableNotifier from "../../../gen/displays/notifiers/LocationEditableNotifier";
import LocationEditableRequester from "../../../gen/displays/requesters/LocationEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { DrawingManager } from '@react-google-maps/api'

const styles = theme => ({});

class LocationEditable extends AbstractLocationEditable {

	constructor(props) {
		super(props);
		this.notifier = new LocationEditableNotifier(this);
		this.requester = new LocationEditableRequester(this);
		this.editor = React.createRef();
	};

	render() {
		return this.renderLayer(() => this.renderEditor());
	};

	renderEditor = () => {
		return (
			<DrawingManager ref={this.editor}
							onRectangleComplete={this.handleChange.bind(this)}
							onPolygonComplete={this.handleChange.bind(this)} />
		);
	};

	handleChange = (polygon) => {
		this.editor.current.setOptions({drawingControl: false});
		console.log(polygon);
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(LocationEditable));
DisplayFactory.register("LocationEditable", withStyles(styles, { withTheme: true })(withSnackbar(LocationEditable)));