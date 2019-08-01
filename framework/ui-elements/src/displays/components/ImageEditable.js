import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractImageEditable from "../../../gen/displays/components/AbstractImageEditable";
import ImageEditableNotifier from "../../../gen/displays/notifiers/ImageEditableNotifier";
import ImageEditableRequester from "../../../gen/displays/requesters/ImageEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { AddAPhoto } from '@material-ui/icons';

const styles = theme => ({
	input: {
		display: "none"
	},
	image: {
		display: "block",
		position: "absolute",
		height: "100%",
		width: "100%"
	},
	overlay: {
		position: "absolute",
		background: "rgba(0, 0, 0, 0.4)",
		width: "100%",
		height: "100%",
		display: "flex",
		"justify-content": "center",
		"align-items": "center",
		cursor: "pointer"
	},
	icon: {
		color: "#e8e8e8"
	}
});

class ImageEditable extends AbstractImageEditable {
	state = {
		value: this.props.value,
		readonly: this.props.readonly
	};

	constructor(props) {
		super(props);
		this.notifier = new ImageEditableNotifier(this);
		this.requester = new ImageEditableRequester(this);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	handleChange(e) {
		this.requester.notifyChange(e.target.files[0]);
	};

	render() {
		const { classes } = this.props;
		const inputId = this.props.id + "-image-input";
		return (
			<div style={this.style()}>
				{this.state.value && <img className={classes.image} title={this.props.label} src={this.state.value} />}
				<label htmlFor={inputId} className={classes.overlay}>
					<AddAPhoto className={classes.icon} />
				</label>
				<input accept="image/*" id={inputId} type="file"
					   className={classes.input} onChange={this.handleChange.bind(this)}
					   disabled={this.state.readonly} />
			</div>
		);
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this.props.width != null) {
			result.width = this.props.width;
			result.minWidth = this.props.width;
		}
		if (this.props.height != null) {
			result.height = this.props.height;
			result.minHeight = this.props.height;
		}
		result.position = "relative";
		return result;
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(ImageEditable));
DisplayFactory.register("ImageEditable", withStyles(styles, { withTheme: true })(withSnackbar(ImageEditable)));