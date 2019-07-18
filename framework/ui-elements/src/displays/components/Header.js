import React from "react";
import AppBar from '@material-ui/core/AppBar';
import { withStyles } from '@material-ui/core/styles';
import AbstractHeader from "../../../gen/displays/components/AbstractHeader";
import HeaderNotifier from "../../../gen/displays/notifiers/HeaderNotifier";
import HeaderRequester from "../../../gen/displays/requesters/HeaderRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	color: {
		color: "black"
	}
});

class Header extends AbstractHeader {

	constructor(props) {
		super(props);
		this.notifier = new HeaderNotifier(this);
		this.requester = new HeaderRequester(this);
	};

	render() {
		const { classes } = this.props;
		const position = this.props.position != null ? this.props.position : "relative";
		return (
			<AppBar style={this.style()}
					position={position.toLowerCase()}
					className={classes.color}>
				<React.Fragment>{this.props.children}</React.Fragment>
			</AppBar>
		);
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this.props.color != null) result.backgroundColor = this.props.color;
		if (this._widthDefined()) result.width = this.props.width;
		if (this._heightDefined()) result.height = this.props.height;
		return result;
	};
}

export default withStyles(styles, { withTheme: true })(Header);
DisplayFactory.register("Header", withStyles(styles, { withTheme: true })(Header));