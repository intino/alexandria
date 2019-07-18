import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMagazine from "../../../gen/displays/components/AbstractMagazine";
import MagazineNotifier from "../../../gen/displays/notifiers/MagazineNotifier";
import MagazineRequester from "../../../gen/displays/requesters/MagazineRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({});

class Magazine extends AbstractMagazine {

	constructor(props) {
		super(props);
		this.notifier = new MagazineNotifier(this);
		this.requester = new MagazineRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Magazine);
DisplayFactory.register("Magazine", withStyles(styles, { withTheme: true })(Magazine));