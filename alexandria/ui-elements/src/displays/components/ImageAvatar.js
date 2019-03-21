import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractImageAvatar from "../../../gen/displays/components/AbstractImageAvatar";
import ImageAvatarNotifier from "../../../gen/displays/notifiers/ImageAvatarNotifier";
import ImageAvatarRequester from "../../../gen/displays/requesters/ImageAvatarRequester";

const styles = theme => ({
	value: {
		minHeight: "100px",
		minWidth: "100px",
		borderRadius: "100px"
	}
});

class ImageAvatar extends AbstractImageAvatar {
	state = {
		value : this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new ImageAvatarNotifier(this);
		this.requester = new ImageAvatarRequester(this);
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
		if (this.props.width != null) result.width = this.props.width;
		if (this.props.height != null) result.height = this.props.height;
		return result;
	};

	refresh = (value) => {
		this.setState({ value });
	};
}

export default withStyles(styles, { withTheme: true })(ImageAvatar);