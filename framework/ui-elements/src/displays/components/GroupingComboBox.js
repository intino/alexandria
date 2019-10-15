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
});

class GroupingComboBox extends AbstractGroupingComboBox {

    constructor(props) {
        super(props);
        this.notifier = new GroupingComboBoxNotifier(this);
        this.requester = new GroupingComboBoxRequester(this);
    };

    render() {
        const { classes } = this.props;
        const selectedOptions = this.state.selection.map(s => this._groupOf(s));
        return (
            <div className={classes.container} style={this.style()}>
                <Select isMulti isSearchable closeMenuOnSelect={false} placeholder={this.selectMessage()}
                        options={this.state.groups.map(group => { return { value: group.label, label: group.label, group: group } })}
                        className="basic-multi-select" classNamePrefix="select"
                        components={{ Option: this.renderGroup.bind(this)}}
                        onChange={this.handleChange}
                        value={selectedOptions}
                />
            </div>
        );
    };

    _groupOf = (option) => {
        let group = this.state.groups.filter(g => g.label === option)[0];
        return group != null ? { value: group.label, label: group.label, group: group } : null;
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

    handleChange = (selection) => {
        this.updateSelection(selection.map(s => s.value));
    };

    selectMessage = () => {
        const placeholder = this.props.placeholder;
        return this.translate(placeholder != null && placeholder !== "" ? placeholder : "Select " + (this.props.label != null ? this.props.label : " an option"));
    };

}

export default withStyles(styles, { withTheme: true })(GroupingComboBox);
DisplayFactory.register("GroupingComboBox", withStyles(styles, { withTheme: true })(GroupingComboBox));