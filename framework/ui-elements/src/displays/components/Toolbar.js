import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractToolbar from "../../../gen/displays/components/AbstractToolbar";
import ToolbarNotifier from "../../../gen/displays/notifiers/ToolbarNotifier";
import ToolbarRequester from "../../../gen/displays/requesters/ToolbarRequester";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	toolbar : {
	},
	operation : {
	}
});

class Toolbar extends AbstractToolbar {

	constructor(props) {
		super(props);
		this.notifier = new ToolbarNotifier(this);
		this.requester = new ToolbarRequester(this);
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
		const { classes } = this.props;
		return (<div className={classNames("layout horizontal center", classes.toolbar)} style={this.style()}>{React.Children.map(this.props.children, (child, i) => { return this.renderItem(child); })}</div>);
	};

	renderItem = (item) => {
		const { classes } = this.props;
		return (<div className={classes.operation}>{item}</div>);
	};

}

export default withStyles(styles, { withTheme: true })(Toolbar);
DisplayFactory.register("Toolbar", withStyles(styles, { withTheme: true })(Toolbar));