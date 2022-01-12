import React from "react";
import { withStyles } from '@material-ui/core/styles';
import {ListItemSecondaryAction, Typography} from "@material-ui/core";
import Select, { components } from "react-select";
import AbstractGroupingComboBox from "../../../gen/displays/components/AbstractGroupingComboBox";
import GroupingComboBoxNotifier from "../../../gen/displays/notifiers/GroupingComboBoxNotifier";
import GroupingComboBoxRequester from "../../../gen/displays/requesters/GroupingComboBoxRequester";
import { BaseGroupingStyles } from "./BaseGrouping";
import classNames from "classnames";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import NumberUtil from 'alexandria-ui-elements/src/util/NumberUtil';

const styles = theme => ({
    ...BaseGroupingStyles(theme),
    container : {
        minWidth: "200px"
    },
    group : {
        padding: "0",
    },
    groupStyles : {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    groupBadgeStyles : {
        backgroundColor: '#EBECF0',
        borderRadius: '2em',
        color: '#172B4D',
        display: 'inline-block',
        fontSize: 12,
        fontWeight: 'normal',
        lineHeight: '1',
        minWidth: 1,
        padding: '0.16666666666667em 0.5em',
        textAlign: 'center',
    }
});

class GroupingComboBox extends AbstractGroupingComboBox {

    constructor(props) {
        super(props);
        this.notifier = new GroupingComboBoxNotifier(this);
        this.requester = new GroupingComboBoxRequester(this);
        this.state = {
            ...this.state,
        }
    };

    render() {
        if (!this.state.visible) return (<React.Fragment/>);

        const { classes } = this.props;
        const selectedOptions = this.state.selection.map(s => this._findGroup(s));
        const options = this._options();
        return (
            <div className={classes.container} style={this.style()}>
                <Select isMulti isSearchable closeMenuOnSelect={false} placeholder={this.selectMessage()}
                        options={options}
                        className="basic-multi-select" classNamePrefix="select"
                        components={{ Option: this.renderGroup.bind(this)}}
						formatGroupLabel={this._groupComponent.bind(this)}
                        filterOption={this.handleFilter.bind(this)}
                        onChange={this.handleChange}
                        value={selectedOptions}
                />
            </div>
        );
    };

    _findGroup = (option) => {
        let group = this._flattenGroups().filter(g => g.label === option)[0];
        return group != null ? this._groupOf(group) : null;
    };

    renderGroup = (options) => {
        const { data, isDisabled, ...props } = options;
        const { classes } = this.props;
        const group = data.group;
        return !isDisabled ? (
            <components.Option {...props}>
                <div className={classNames(classes.group, "option layout horizontal center")}>
                    {group.color != null && <Typography className={classes.colorBox} variant="body2" style={{backgroundColor:group.color}}></Typography>}
                    <Typography className="flex" variant="body2">{group.label}</Typography>
                    {group.count > 0 && <Typography className={classes.count} variant="body2">{NumberUtil.format(group.count, "0,0")}</Typography>}
                </div>
            </components.Option>
        ) : null;
    };

	handleFilter = (candidate, condition) => {
	    const split = condition.toLowerCase().split(" ");
	    const label = candidate.label != null ? candidate.label.toLowerCase() : "";
	    const value = candidate.value != null ? candidate.value.toLowerCase() : "";
	    for (var i=0; i<split.length; i++) {
	        if (label.indexOf(split[i]) == -1 && value.indexOf(split[i]) == -1) return false;
	    }
		return true;
	};

    handleChange = (selection) => {
        this.updateSelection(selection.map(s => s.value));
    };

    selectMessage = () => {
        const placeholder = this.props.placeholder;
        return placeholder != null && placeholder !== "" ? this.translate(placeholder) : (this.props.label != null ? this.translate("Select") + " " + this.props.label : this.translate("Select an option"));
    };

    _groupComponent = (data) => {
        const { classes } = this.props;
        return (
            <div className={classes.groupStyles}>
                <span>{data.label}</span>
                <span className={classes.groupBadgeStyles}>{data.options.length}</span>
            </div>
        );
    };

    _options = () => {
        const result = this.state.groups.map(entry => this._groupEntryOf(entry));
        if (result.length == 1 && result[0].label === "default") return result[0].options;
        return result;
    };

    _groupEntryOf = (entry) => {
        const result = { label: entry.label };
        result.options = entry.groups.map(g => this._groupOf(g));
        return result;
    };

    _groupOf = (group) => {
        return { value: group.label, label: group.label, group: group };
    };

    _flattenGroups = () => {
        const result = [];
        const entries = this.state.groups;
        for (let i=0; i<entries.length; i++) {
            for (let j=0; j<entries[i].groups.length; j++) {
                result.push(entries[i].groups[j]);
            }
        }
        return result;
    }

}

export default withStyles(styles, { withTheme: true })(GroupingComboBox);
DisplayFactory.register("GroupingComboBox", withStyles(styles, { withTheme: true })(GroupingComboBox));