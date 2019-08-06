import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Tabs, Tab } from '@material-ui/core';
import AbstractSelectorTabs from "../../../gen/displays/components/AbstractSelectorTabs";
import SelectorTabsNotifier from "../../../gen/displays/notifiers/SelectorTabsNotifier";
import SelectorTabsRequester from "../../../gen/displays/requesters/SelectorTabsRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class SelectorTabs extends AbstractSelectorTabs {
    state = {
        selected: -1
    };

	constructor(props) {
		super(props);
		this.notifier = new SelectorTabsNotifier(this);
		this.requester = new SelectorTabsRequester(this);
	};

	render() {
	    const selected = this.state.selected !== -1 ? this.state.selected : 0;
        return (
            <Tabs value={selected} variant="fullWidth"
                  onChange={this.handleChange.bind(this)} color={this.props.color}>
                {React.Children.map(this.props.children, (child, i) => { return this.renderTab(child); })}
            </Tabs>
        );
    }

    renderTab = (tab) => {
        return (
            <Tab label={tab}/>
        );
    };

    refreshSelected = (tab) => {
        this.setState({ selected: tab });
	};

	handleChange = (e, value) => {
        this.requester.select(value);
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorTabs));
DisplayFactory.register("SelectorTabs", withStyles(styles, { withTheme: true })(withSnackbar(SelectorTabs)));