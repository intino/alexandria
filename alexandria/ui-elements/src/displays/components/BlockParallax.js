import React from "react";
import AbstractBlockParallax from "../../../gen/displays/components/AbstractBlockParallax";
import BlockParallaxNotifier from "../../../gen/displays/notifiers/BlockParallaxNotifier";
import BlockParallaxRequester from "../../../gen/displays/requesters/BlockParallaxRequester";
import { Parallax } from "react-materialize"
import {withStyles} from "@material-ui/core";
import 'alexandria-ui-elements/res/styles/parallax.css';

const styles = theme => ({
	container : {
		position: "relative"
	},
	children: {
		position: "absolute",
		top: "0",
		height: "100%",
		width: "100%"
	}
});

class BlockParallax extends AbstractBlockParallax {
	state = {
		background: null
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockParallaxNotifier(this);
		this.requester = new BlockParallaxRequester(this);
	};

	render() {
		const {classes} = this.props;
		const hasBackground = this.state.background != null;

		return (
			<div className={classes.container}>
				{ hasBackground ? <Parallax imageSrc={this.state.background}></Parallax> : undefined}
				<div className={classes.children} style={this.style()}>{this.props.children}</div>
			</div>
		);
	};

	refresh = (background) => {
		this.setState({background});
	};

}

export default withStyles(styles, { withTheme: true })(BlockParallax);