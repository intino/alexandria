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

	constructor(props) {
		super(props);
		this.notifier = new SelectorTabsNotifier(this);
		this.requester = new SelectorTabsRequester(this);
		this.state = {
		    ...this.state,
            selected: -1,
            hiddenOptions: []
        };
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
	    const selected = this.state.selected !== -1 ? this.state.selected : 0;
	    const children = this.children();
	    const scrollButtons = this.props.scrollButtons != undefined ? this.props.scrollButtons.toLowerCase() : "off";
	    const variant = scrollButtons !== "off" ? "scrollable" : undefined;
        return (
            <Tabs value={selected} variant="fullWidth" variant={variant} scrollButtons={scrollButtons}
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
        let realTab = tab - this._countInvisible(tab);
        this.setState({ selected: realTab });
	};

    refreshOptionsVisibility = (options) => {
        this.setState({ hiddenOptions: options });
	};

	_countInvisible = (pos) => {
	    const hiddenOptions = this.state.hiddenOptions;
	    let result = 0;
	    for (var i=0; i<hiddenOptions.length; i++) {
	        if (hiddenOptions[i] < pos) result++;
	        else break;
	    }
	    return result;
	}

	_isVisible = (pos) => {
	    const hiddenOptions = this.state.hiddenOptions;
	    for (var i=0; i<hiddenOptions.length; i++) {
	        if (hiddenOptions[i] == pos) return false;
	    }
	    return true;
	}

	handleChange = (e, value) => {
	    while (!this._isVisible(value)) value++;
	    const children = this.children();
	    let selected = null;
	    for (let i=0; i<children.length; i++) {
	        if (i == value) selected = children[i].props.name;
	    }
        this.requester.select(selected != null ? selected : value);
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorTabs));
DisplayFactory.register("SelectorTabs", withStyles(styles, { withTheme: true })(withSnackbar(SelectorTabs)));