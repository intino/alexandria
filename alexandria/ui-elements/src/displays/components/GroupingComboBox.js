import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Typography } from "@material-ui/core";
import Select, { components } from "react-select";
import AbstractGroupingComboBox from "../../../gen/displays/components/AbstractGroupingComboBox";
import GroupingComboBoxNotifier from "../../../gen/displays/notifiers/GroupingComboBoxNotifier";
import GroupingComboBoxRequester from "../../../gen/displays/requesters/GroupingComboBoxRequester";
import { BaseGroupingStyles } from "./BaseGrouping";
import classNames from "classnames";

const styles = theme => ({
	...BaseGroupingStyles(theme),
	container : {
		minWidth: "200px"
	},
	group : {
		padding: "0",
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
				<Select isMulti isSearchable closeMenuOnSelect={false} placeholder={this.selectMessage()} options={this.state.groups.map(group => { return { value: group.label, label: group.label, group: group } })}
						className="basic-multi-select" classNamePrefix="select"
						components={{ Option: this.renderGroup.bind(this)}}
						onChange={this.handleChange}
				/>
			</div>
		);
	}

	renderGroup = (options) => {
		const { data, isDisabled, ...props } = options;
		const { classes } = this.props;
		const group = data.group;
		return !isDisabled ? (
			<components.Option {...props}>
				<div className={classNames(classes.group, "option layout horizontal")}>
					<Typography className="flex" variant="body2">{group.label}</Typography>
					<Typography className={classes.count} variant="body2">{group.count}</Typography>
				</div>
			</components.Option>
		) : null;
	};

	handleChange = (selection) => {
		this.updateSelection(selection.map(s => s.value));
	};

	selectMessage = () => {
		return this.translate("Select " + (this.props.label != null ? this.props.label : " an option"));
	};

}

export default withStyles(styles, { withTheme: true })(GroupingComboBox);