import React from "react";
import AppBar from '@material-ui/core/AppBar';
import { withStyles } from '@material-ui/core/styles';
import AbstractHeader from "../../../gen/displays/components/AbstractHeader";
import HeaderNotifier from "../../../gen/displays/notifiers/HeaderNotifier";
import HeaderRequester from "../../../gen/displays/requesters/HeaderRequester";

const styles = theme => ({
});

class Header extends AbstractHeader {

	constructor(props) {
		super(props);
		this.notifier = new HeaderNotifier(this);
		this.requester = new HeaderRequester(this);
	};

	render() {
		const color = this.props.color != null ? this.props.color : "primary";
		const position = this.props.position != null ? this.props.position : "relative";
		return (
			<AppBar style={this.style()}
							color={color.toLowerCase()}
					  		position={position.toLowerCase()}>
				<React.Fragment>{this.props.children}</React.Fragment>
			</AppBar>
		);
	};
}

export default withStyles(styles, { withTheme: true })(Header);