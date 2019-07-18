import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOptionList from "../../../gen/displays/components/AbstractOptionList";
import OptionListNotifier from "../../../gen/displays/notifiers/OptionListNotifier";
import OptionListRequester from "../../../gen/displays/requesters/OptionListRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class OptionList extends AbstractOptionList {

	constructor(props) {
		super(props);
		this.notifier = new OptionListNotifier(this);
		this.requester = new OptionListRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(OptionList);
DisplayFactory.register("OptionList", withStyles(styles, { withTheme: true })(OptionList));