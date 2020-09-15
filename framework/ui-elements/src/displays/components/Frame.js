import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractFrame from "../../../gen/displays/components/AbstractFrame";
import FrameNotifier from "../../../gen/displays/notifiers/FrameNotifier";
import FrameRequester from "../../../gen/displays/requesters/FrameRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	container : {
		width: "100%",
		height: "100%",
		border: "0",
		position: "relative",
	},
});

class Frame extends AbstractFrame {

	constructor(props) {
		super(props);
		this.notifier = new FrameNotifier(this);
		this.requester = new FrameRequester(this);
        this.state = {
            ...this.state,
            url : this.props.url
        };
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		if (this.state.url == null) return (<React.Fragment/>);
		const { classes } = this.props;
		return (<div style={this.style()}><iframe src={this.state.url} className={classes.container}/></div>);
	};

    refresh = (url) => {
        this.setState({url});
    };

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this._widthDefined() && result.width == null) result.width = this.props.width;
		if (this._heightDefined() && result.height == null) result.height = this.props.height;
		return result;
	};
}

export default withStyles(styles, { withTheme: true })(Frame);
DisplayFactory.register("Frame", withStyles(styles, { withTheme: true })(Frame));