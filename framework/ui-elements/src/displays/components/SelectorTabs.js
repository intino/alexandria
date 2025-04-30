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
            optionsVisibility: []
        };
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
	    const selected = this.state.selected !== -1 ? this.state.selected : 0;
	    const children = this._visibleOptions();
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
        return (<Tab label={tab} index={i} style={this.styleOf(tab)}/>);
    };

    refreshSelected = (tab) => {
        let realTab = tab - this._countInvisible(tab);
        this.setState({ selected: realTab });
	};

    refreshOptionsVisibility = (optionsVisibility) => {
        const visibility = [];
        for (var i=0; i<optionsVisibility.length; i++) visibility[optionsVisibility[i].index] = optionsVisibility[i].visible;
        this.setState({ optionsVisibility: visibility });
	};

	_countInvisible = (pos) => {
	    const optionsVisibility = this.state.optionsVisibility.length > 0 ? this.state.optionsVisibility : this._defaultOptionsVisibility();
	    let result = 0;
	    for (var i=0; i<optionsVisibility.length; i++) {
	        if (i <= pos && optionsVisibility[i] === false) result++;
	    }
	    return result;
	};

	_defaultOptionsVisibility = () => {
	    const children = this.children();
	    const result = [];
	    for (var i=0; i<children.length; i++) {
	        const name = children[i].props.name;
	        result[i] = this.props.hiddenOptions == null || this.props.hiddenOptions.indexOf(name) == -1;
	    }
	    return result;
	};

	_visibleOptions = () => {
	    const children = this.children();
	    const result = [];
	    for (var i=0; i<children.length; i++) {
	        if (!this._isVisible(children[i], i)) continue;
	        result.push(children[i]);
	    }
	    return result;
	}

	_isVisible = (tab, pos) => {
	    const optionsVisibility = this.state.optionsVisibility;
	    return optionsVisibility.length > 0 ? optionsVisibility[pos] : (tab != null && tab.props != null && (tab.props.visible == null || tab.props.visible !== false));
	}

	handleChange = (e, value) => {
	    const children = this._visibleOptions();
	    let selected = children[value] != null ? children[value].props.name : null;
	    if (selected == null && !(children instanceof Array) && children.props != null) selected = children.props.name;
	    if (selected != null) this.requester.selectByName(selected);
	    else this.requester.select(value);
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorTabs));
DisplayFactory.register("SelectorTabs", withStyles(styles, { withTheme: true })(withSnackbar(SelectorTabs)));