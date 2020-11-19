import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGroupingToolbar from "../../../gen/displays/components/AbstractGroupingToolbar";
import GroupingToolbarNotifier from "../../../gen/displays/notifiers/GroupingToolbarNotifier";
import GroupingToolbarRequester from "../../../gen/displays/requesters/GroupingToolbarRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { Chip, Button, Typography } from '@material-ui/core';
import { withSnackbar } from 'notistack';
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
    label : {
        padding: "10px 10px 5px 10px"
    },
    content : {
        padding: '0 10px',
    },
    filter : {
        margin: '0 5px 5px 0',
    }
});

class GroupingToolbar extends AbstractGroupingToolbar {

	constructor(props) {
		super(props);
		this.notifier = new GroupingToolbarNotifier(this);
		this.requester = new GroupingToolbarRequester(this);
		this.state = {
		    filters: [],
		    applyEnabled: false,
		    ...this.state,
		}
	};

    render() {
        if (this.state.filters.length <= 0) return (<React.Fragment/>);
        const { classes } = this.props;
        return (
            <div className="layout vertical" style={this.style()}>
                <Typography className={classes.label} variant="subtitle1">{this.translate("Selected filters")}</Typography>
                <div className={classes.content}>
                    {this.renderFilters()}
                    {this.renderButtons()}
                </div>
            </div>
        );
    };

    renderFilters = () => {
        return (
            <React.Fragment>
                <div className="layout horizontal wrap">{this.state.filters.map((g, index) => this.renderFilter(g, index))}</div>
            </React.Fragment>
        );
    };

    renderFilter = (filter, index) => {
        const { classes } = this.props;
        const options = filter.options.join(", ");
        const maxOptions = 35;
        const label = filter.name + ": " + (options.length > maxOptions ? options.substring(0, maxOptions) + "..." : options);
        return <Chip key={index} label={label} title={options} className={classes.filter} variant="outlined" size="small" onDelete={this.handleRemoveFilter.bind(this, filter.name)}/>
    };

    renderButtons = () => {
        return (
            <div className="layout horizontal" style={{margin:'5px 0 10px 0'}}>
                <Button variant="text" color="primary" size="small" disabled={!this.state.applyEnabled} style={{marginRight:'10px'}} onClick={this.handleApply.bind(this)}>{this.translate("Apply filters")}</Button>
                <Button variant="text" color="primary" size="small" disabled={this.state.filters.length <= 0} onClick={this.handleReset.bind(this)}>{this.translate("Reset filters")}</Button>
            </div>
        );
    };

    refreshFilters = (filters) => {
        this.setState({filters: filters, applyEnabled: true});
    };

    handleRemoveFilter = (name) => {
        this.requester.removeFilter(name);
    };

    handleApply = () => {
        this.requester.apply();
        this.setState({applyEnabled:false});
    };

    handleReset = () => {
        this.requester.reset();
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(GroupingToolbar));
DisplayFactory.register("GroupingToolbar", withStyles(styles, { withTheme: true })(withSnackbar(GroupingToolbar)));