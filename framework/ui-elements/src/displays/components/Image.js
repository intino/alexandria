import React from "react";
import AbstractImage from "../../../gen/displays/components/AbstractImage";
import ImageNotifier from "../../../gen/displays/notifiers/ImageNotifier";
import ImageRequester from "../../../gen/displays/requesters/ImageRequester";
import {withStyles} from "@material-ui/core";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	value: {
		minHeight: "100px",
		minWidth: "100px"
	}
});

class Image extends AbstractImage {
	state = {
		value : this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new ImageNotifier(this);
		this.requester = new ImageRequester(this);
	};

	render() {
		const { classes } = this.props;

		return (
			<React.Fragment>
				<img className={classes.value} style={this.style()} title={this.props.label} src={this.state.value}/>
			</React.Fragment>
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
		return result;
	};

}

export default withStyles(styles, { withTheme: true })(Image);
DisplayFactory.register("Image", withStyles(styles, { withTheme: true })(Image));