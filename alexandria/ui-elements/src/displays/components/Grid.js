import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGrid from "../../../gen/displays/components/AbstractGrid";
import GridNotifier from "../../../gen/displays/notifiers/GridNotifier";
import GridRequester from "../../../gen/displays/requesters/GridRequester";

const styles = theme => ({});

class Grid extends AbstractGrid {

	constructor(props) {
		super(props);
		this.notifier = new GridNotifier(this);
		this.requester = new GridRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Grid);