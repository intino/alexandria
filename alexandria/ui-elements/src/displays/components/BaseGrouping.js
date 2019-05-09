import React from "react";
import AbstractBaseGrouping from "../../../gen/displays/components/AbstractBaseGrouping";
import BaseGroupingNotifier from "../../../gen/displays/notifiers/BaseGroupingNotifier";
import BaseGroupingRequester from "../../../gen/displays/requesters/BaseGroupingRequester";
import {Checkbox, ListItem, ListItemSecondaryAction, ListItemText} from "@material-ui/core";

export const BaseGroupingStyles = theme => ({
	container : {
	},
	label : {
		padding: "10px 10px 0"
	},
	checkbox : {
		padding: "0 7px"
	},
	count : {
		color: theme.palette.grey.primary
	},
	group : {
		paddingLeft : "0"
	}
});

class BaseGrouping extends AbstractBaseGrouping {
	state = {
		groups : [],
		selection: []
	};

	constructor(props) {
		super(props);
		this.notifier = new BaseGroupingNotifier(this);
		this.requester = new BaseGroupingRequester(this);
	};

	renderGroupContent = (group) => {
		const { classes } = this.props;
		return (
			<React.Fragment>
				<Checkbox className={classes.checkbox} checked={this.state.selection.indexOf(group.label) !== -1} tabIndex={-1} disableRipple/>
				<ListItemText>{group.label}</ListItemText>
				<ListItemSecondaryAction className={classes.count}>{group.count}</ListItemSecondaryAction>
			</React.Fragment>
		);
	};

	toggle = group => {
		const { selection } = this.state;
		const currentIndex = selection.indexOf(group.label);
		const newSelection = [...selection];

		if (currentIndex === -1) newSelection.push(group.label);
		else newSelection.splice(currentIndex, 1);

		this.updateSelection(newSelection);
	};

	updateSelection = selection => {
		this.requester.select(selection);
		this.setState({ selection: selection });
	};

	refreshGroups = (groups) => {
		this.setState({ groups });
	};
}

export default BaseGrouping;