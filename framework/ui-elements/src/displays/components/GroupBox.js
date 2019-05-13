import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGroupBox from "../../../gen/displays/components/AbstractGroupBox";
import GroupBoxNotifier from "../../../gen/displays/notifiers/GroupBoxNotifier";
import GroupBoxRequester from "../../../gen/displays/requesters/GroupBoxRequester";
import { List, ListItem, ListItemText, ListItemSecondaryAction, Checkbox, Typography } from "@material-ui/core";
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
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

class GroupBox extends AbstractGroupBox {
	state = {
		groups : [],
		selection: []
	};

	constructor(props) {
		super(props);
		this.notifier = new GroupBoxNotifier(this);
		this.requester = new GroupBoxRequester(this);
	};

	render() {
		return (
			<div style={this.style()}>
				{this.props.label != null ? <Typography className={this.props.classes.label} variant="subtitle1">{this.props.label}</Typography> : undefined}
				<List>{this.state.groups.map((group, i) => this.renderGroup(group, i))}</List>
			</div>
		);
	}

	renderGroup = (group, index) => {
		const { classes } = this.props;
		return (
			<ListItem key={index} className={classes.group} role={undefined} dense button onClick={this.handleToggle(group)}>
				<Checkbox className={classes.checkbox} checked={this.state.selection.indexOf(group.label) !== -1} tabIndex={-1} disableRipple/>
				<ListItemText>{group.label}</ListItemText>
				<ListItemSecondaryAction className={classes.count}>{group.count}</ListItemSecondaryAction>
			</ListItem>
		);
	};

	handleToggle = group => () => {
		const { selection } = this.state;
		const currentIndex = selection.indexOf(group.label);
		const newSelection = [...selection];

		if (currentIndex === -1) newSelection.push(group.label);
		else newSelection.splice(currentIndex, 1);

		this.requester.select(newSelection);
		this.setState({ selection: newSelection });
	};

	refreshGroups = (groups) => {
		this.setState({ groups });
	};
}

export default withStyles(styles, { withTheme: true })(GroupBox);