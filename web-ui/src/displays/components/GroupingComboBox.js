import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGroupingComboBox from "../../../gen/displays/components/AbstractGroupingComboBox";
import GroupingComboBoxNotifier from "../../../gen/displays/notifiers/GroupingComboBoxNotifier";
import GroupingComboBoxRequester from "../../../gen/displays/requesters/GroupingComboBoxRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class GroupingComboBox extends AbstractGroupingComboBox {

	constructor(props) {
		super(props);
		this.notifier = new GroupingComboBoxNotifier(this);
		this.requester = new GroupingComboBoxRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(GroupingComboBox);
DisplayFactory.register("GroupingComboBox", withStyles(styles, { withTheme: true })(GroupingComboBox));