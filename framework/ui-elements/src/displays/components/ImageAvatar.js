import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractImageAvatar from "../../../gen/displays/components/AbstractImageAvatar";
import ImageAvatarNotifier from "../../../gen/displays/notifiers/ImageAvatarNotifier";
import ImageAvatarRequester from "../../../gen/displays/requesters/ImageAvatarRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Delayer from '../../util/Delayer';

const styles = theme => ({
	value: {
		minHeight: "50px",
		minWidth: "50px",
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

	componentDidMount() {
		this.requester.load();
	};

	render() {
		const { classes } = this.props;
		return (
			<img className={classes.value} style={this.style()} title={this.props.label} src={this.state.value}/>
		);
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		result.width = this.props.width != null ? this.props.width : "50px";
		result.height = this.props.height != null ? this.props.height : "50px";
		return result;
	};

}

export default withStyles(styles, { withTheme: true })(ImageAvatar);
DisplayFactory.register("ImageAvatar", withStyles(styles, { withTheme: true })(ImageAvatar));