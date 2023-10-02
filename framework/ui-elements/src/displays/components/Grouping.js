import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGrouping from "../../../gen/displays/components/AbstractGrouping";
import GroupingNotifier from "../../../gen/displays/notifiers/GroupingNotifier";
import GroupingRequester from "../../../gen/displays/requesters/GroupingRequester";
import {Checkbox, List, ListItem, ListItemSecondaryAction, ListItemText, Typography} from "@material-ui/core";
import { BaseGroupingStyles } from "./BaseGrouping";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import NumberUtil from 'alexandria-ui-elements/src/util/NumberUtil';

const styles = theme => ({...BaseGroupingStyles(theme)});

class Grouping extends AbstractGrouping {

    constructor(props) {
        super(props);
        this.notifier = new GroupingNotifier(this);
        this.requester = new GroupingRequester(this);
        this.state = {
            ...this.state,
        }
    };

    render() {
        if (!this.state.visible) return (<React.Fragment/>);

        const { classes } = this.props;
        return (
            <div className={classes.container} style={this.style()}>
                {this.props.label != null ? <Typography className={classes.label} variant="subtitle1">{this.translate(this.props.label)}</Typography> : undefined}
                {this.renderToolbar()}
                {this.state.visibleGroups.length > 0 && <List>{this.state.visibleGroups.map((group, i) => this.renderGroup(group, i))}</List>}
                {this.renderEmpty()}
                {this.renderMoreGroups()}
            </div>
        );
    };

    renderGroup = (group, index) => {
        const { classes } = this.props;
        return (
            <ListItem key={index} className={classes.group} role={undefined} dense button onClick={this.handleToggle(group)}>
                <Checkbox className={classes.checkbox} checked={this.state.selection.indexOf(group.label) !== -1} tabIndex={-1} disableRipple/>
                <ListItemText style={{margin:'0'}}>
                    <div className="layout horizontal center">
                        <div className="layout flex">{group.label}</div>
                        {group.color != null && <Typography className={classes.colorBox} variant="body2" style={{backgroundColor:group.color,marginLeft:'10px'}}></Typography>}
                    </div>
                </ListItemText>
                {group.count > 0 && <ListItemSecondaryAction className={classes.count}>{NumberUtil.format(group.count, "0,0")}</ListItemSecondaryAction>}
            </ListItem>
        );
    };

    handleToggle = group => () => {
        this.toggle(group);
    };

    refreshPageSize = (size) => {
        this.setState({pageSize: size});
    };

    refreshGroups = (groups) => {
        const flattenGroups = this.flattenGroups(groups);
        this.setState({ groups: flattenGroups, visibleGroups: this.visibleGroups(flattenGroups, this.state.pageSize, this.state.condition) });
    };

    flattenGroups = (groups) => {
        const result = [];
        for (let i=0; i<groups.length; i++) {
            for (let j=0; j<groups[i].groups.length; j++)
                result.push(groups[i].groups[j]);
        }
        return result;
    };

}

export default withStyles(styles, { withTheme: true })(Grouping);
DisplayFactory.register("Grouping", withStyles(styles, { withTheme: true })(Grouping));