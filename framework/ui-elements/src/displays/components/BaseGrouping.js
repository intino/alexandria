import React from "react";
import AbstractBaseGrouping from "../../../gen/displays/components/AbstractBaseGrouping";
import {Typography, Input } from "@material-ui/core";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';

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
    },
    filter: {
        padding: "0 10px",
    },
    filterMessage : {
        color: theme.palette.grey.primary,
        fontSize: "8pt",
    },
    showMore : {
        margin: "0 10px 20px",
        cursor: "pointer",
        display: "inline-block",
        color: theme.palette.primary.main,
    },
    colorBox : {
        width: "16px",
        height: "16px",
        borderRadius: "4px",
        marginRight: "5px",
        border: "1px solid #efefef",
    }
});

export default class BaseGrouping extends AbstractBaseGrouping {
    static DefaultPageSize = 5;

    constructor(props) {
        super(props);
        this.state = {
            ...this.state,
            groups : [],
            visibleGroups : [],
            selection: [],
            pageSize: this.props.pageSize != null ? this.props.pageSize : BaseGrouping.DefaultPageSize,
            condition: null,
            visible: this.props.visible != null ? this.props.visible : true,
        };
    };

    renderToolbar = () => {
        if (this.state.groups.length <= this.state.pageSize) return;

        const { classes } = this.props;
        return (
            <div className={classes.filter}>
                <Input style={{width:"100%"}} type="search" placeholder={this.translate("Filter...")} onChange={this.handleFilter.bind(this)}/>
                <div className={classNames("layout horizontal end-justified", classes.filterMessage)}>
                    <div>{this.state.visibleGroups.length}</div>&nbsp;/&nbsp;<div>{this.countFilteredGroups()}&nbsp;{this.translate("groups")}</div>
                </div>
            </div>
        );
    };

    renderEmpty = () => {
        if (this.state.visibleGroups.length > 0) return;
        return (<Typography variant="body2" style={{margin:"0 10px 10px"}}>{this.translate("No groups found")}</Typography>);
    };

    handleFilter = (e) => {
        const condition = e.target.value.toLowerCase();
        this.setState({ condition: condition, visibleGroups: this.visibleGroups(this.state.groups, this.state.pageSize, condition) });
    };

    renderMoreGroups = () => {
        if (this.countFilteredGroups() <= this.state.pageSize) return;
        return (<a className={this.props.classes.showMore} onClick={this.handleMoreGroups.bind(this)}>{this.translate("show more...")}</a>);
    };

    handleMoreGroups = () => {
        const newPageSize = this.state.pageSize+this.props.pageSize;
        this.setState({ pageSize: newPageSize, visibleGroups: this.visibleGroups(this.state.groups, newPageSize, this.state.condition)});
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
    };

    refreshSelection = (selection) => {
        this.setState({ selection: selection });
    };

    refreshGroups = (groups) => {
        this.setState({ groups: groups, visibleGroups: this.visibleGroups(groups, this.state.pageSize, this.state.condition) });
    };

    visibleGroups = (groups, pageSize, condition) => {
        const filteredGroups = this.filter(groups, condition);
        const count = filteredGroups.length < pageSize ? filteredGroups.length : pageSize;
        return filteredGroups.slice(0, count);
    };

    filter = (groups, condition) => {
        if (condition == null) return groups;
        return groups.filter(g => g.label.toLowerCase().indexOf(condition) !== -1);
    };

    countFilteredGroups = () => {
        return this.filter(this.state.groups, this.state.condition).length;
    };
}