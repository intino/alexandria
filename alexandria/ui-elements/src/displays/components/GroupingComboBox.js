import React from "react";
import { withStyles } from '@material-ui/core/styles';
import {Select, InputLabel, Chip, Input, MenuItem} from "@material-ui/core";
import AbstractGroupingComboBox from "../../../gen/displays/components/AbstractGroupingComboBox";
import GroupingComboBoxNotifier from "../../../gen/displays/notifiers/GroupingComboBoxNotifier";
import GroupingComboBoxRequester from "../../../gen/displays/requesters/GroupingComboBoxRequester";
import { BaseGroupingStyles } from "./BaseGrouping"

const styles = theme => ({
	...BaseGroupingStyles(theme),
	container : {
		minWidth: "200px"
	},
	chips: {
		display: 'flex',
		flexWrap: 'wrap',
	},
	chip: {
		margin: theme.spacing(1)/4,
		height: "22px"
	}
});

class GroupingComboBox extends AbstractGroupingComboBox {

	constructor(props) {
		super(props);
		this.notifier = new GroupingComboBoxNotifier(this);
		this.requester = new GroupingComboBoxRequester(this);
	};

	render() {
		const { classes } = this.props;
		return (
			<div className={classes.container} style={this.style()}>
				{this.props.label != null ? <InputLabel className={classes.label} htmlFor="select-multiple-chip">{this.props.label}</InputLabel> : undefined}
				<Select displayEmpty style={{width:"100%",padding:"10px",minHeight:"60px"}} multiple value={this.state.selection}
					onChange={this.handleChange} input={<Input id="select-multiple-chip" />}
					renderValue={selection => this.renderSelection(selection)}>
					<MenuItem disabled value="">{this.selectMessage()}</MenuItem>
					{this.state.groups.map((group, index) => this.renderGroup(group, index))}
				</Select>
			</div>
		);
	}

	renderGroup = (group, index) => {
		const { classes } = this.props;
		return (
			<MenuItem key={group.label} value={group.label} className={classes.group}>{this.renderGroupContent(group)}</MenuItem>
		);
	};

	handleChange = (e) => {
		this.updateSelection(e.target.value);
	};

	renderSelection = (selection) => {
		const { classes } = this.props;

		if (selection.length === 0)
			return this.selectMessage();

		return (
			<div className={classes.chips}>
				{selection.map(value => (<Chip key={value} label={value} className={classes.chip} />))}
			</div>
		);
	};

	selectMessage = () => {
		return (<em>{this.translate("Select " + (this.props.label != null ? this.props.label : " an option"))}</em>);
	};

}

export default withStyles(styles, { withTheme: true })(GroupingComboBox);