import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenPage from "../../../gen/displays/components/AbstractOpenPage";
import OpenPageNotifier from "../../../gen/displays/notifiers/OpenPageNotifier";
import OpenPageRequester from "../../../gen/displays/requesters/OpenPageRequester";
import {Typography} from "@material-ui/core";

const styles = theme => ({
	link : {
		color: theme.palette.primary.main,
		cursor: "pointer"
	}
});

class OpenPage extends AbstractOpenPage {
	state = {
		icon : null,
		path : null,
		title : this.props.title
	};

	constructor(props) {
		super(props);
		this.notifier = new OpenPageNotifier(this);
		this.requester = new OpenPageRequester(this);
	};

	render() {
		const { classes } = this.props;
		const format = this.props.format != null && this.props.format !== "default" ? this.props.format.split(" ")[0] : "body1";
		return (<a onClick={this.handleClick.bind(this)} style={this.style()}><Typography variant={format} className={classes.link}>{this.state.title}</Typography></a>);
	};

	handleClick(e) {
		this.requester.open();
	};

	redirect = (path) => {
		let url = this.buildApplicationUrl(path);
		if (this.props.format === "blank") window.open(url, "_blank");
		else window.location.href = url;
	};

	refresh = (title) => {
		this.setState({ title });
	};
}

export default withStyles(styles, { withTheme: true })(OpenPage);