import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Tabs, Tab } from '@material-ui/core';
import AbstractSelectorTabs from "../../../gen/displays/components/AbstractSelectorTabs";
import SelectorTabsNotifier from "../../../gen/displays/notifiers/SelectorTabsNotifier";
import SelectorTabsRequester from "../../../gen/displays/requesters/SelectorTabsRequester";
import Divider from './Divider';
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
	    const children = this.children();
        return (
            <Tabs value={selected} variant="fullWidth"
                  onChange={this.handleChange.bind(this)} color={this.props.color} style={this.style()}>
                {React.Children.map(children, (child, i) => { return this.renderTab(child, i); })}
            </Tabs>
        );
    }

    renderTab = (tab, i) => {
	    const className = tab.props.className;
        if (className != null && className.indexOf("divider") !== -1) return (<Divider/>);
        return this._isVisible(i) ? (<Tab label={tab}/>) : null;
    };

    refreshSelected = (tab) => {
        this.setState({ selected: tab });
	};

    refreshOptionsVisibility = (options) => {
        this.setState({ hiddenOptions: options });
	};

	_isVisible = (pos) => {
	    const hiddenOptions = this.state.hiddenOptions;
	    for (var i=0; i<hiddenOptions.length; i++) {
	        if (hiddenOptions[i] == pos) return false;
	    }
	    return true;
	}

	handleChange = (e, value) => {
        this.requester.select(value);
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorTabs));
DisplayFactory.register("SelectorTabs", withStyles(styles, { withTheme: true })(withSnackbar(SelectorTabs)));