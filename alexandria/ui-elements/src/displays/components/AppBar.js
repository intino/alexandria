import React from "react";
import MaterialAppBar from '@material-ui/core/AppBar';
import { withStyles } from '@material-ui/core/styles';
import AbstractAppBar from "../../../gen/displays/components/AbstractAppBar";
import AppBarNotifier from "../../../gen/displays/notifiers/AppBarNotifier";
import AppBarRequester from "../../../gen/displays/requesters/AppBarRequester";

const styles = theme => ({
});

class AppBar extends AbstractAppBar {

	constructor(props) {
		super(props);
		this.notifier = new AppBarNotifier(this);
		this.requester = new AppBarRequester(this);
	};

	render() {
		const color = this.props.color != null ? this.props.color : "primary";
		const position = this.props.position != null ? this.props.position : "relative";
		return (
			<MaterialAppBar style={this.style()}
							color={color.toLowerCase()}
					  		position={position.toLowerCase()}>
				<React.Fragment>{this.props.children}</React.Fragment>
			</MaterialAppBar>
		);
	};
}

export default withStyles(styles, { withTheme: true })(AppBar);