import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDateBlock from "../../../gen/displays/blocks/AbstractDateBlock";
import DateBlockNotifier from "../../../gen/displays/notifiers/DateBlockNotifier";
import DateBlockRequester from "../../../gen/displays/requesters/DateBlockRequester";

const styles = theme => ({});

class DateBlock extends AbstractDateBlock {

	constructor(props) {
		super(props);
		this.notifier = new DateBlockNotifier(this);
		this.requester = new DateBlockRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(DateBlock);