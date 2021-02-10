import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSorting from "../../../gen/displays/components/AbstractSorting";
import SortingNotifier from "../../../gen/displays/notifiers/SortingNotifier";
import SortingRequester from "../../../gen/displays/requesters/SortingRequester";
import {Typography} from "@material-ui/core";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import { BaseSortingStyles } from "./BaseSorting";

const styles = theme => ({
    ...BaseSortingStyles(theme),
});

class Sorting extends AbstractSorting {

	constructor(props) {
		super(props);
		this.notifier = new SortingNotifier(this);
		this.requester = new SortingRequester(this);
	};

	render() {
		const {classes} = this.props;
		const label = this.attribute("label");
		return (
			<a onClick={this.handleToggle.bind(this)} style={this.style()}>
				<Typography variant={this.variant("body1")} className={classes.link}>{this.translate(label !== "" && label != null ? label : "no label")}</Typography>
			</a>
		);
	};

}

export default withStyles(styles, { withTheme: true })(Sorting);
DisplayFactory.register("Sorting", withStyles(styles, { withTheme: true })(Sorting));