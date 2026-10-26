import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {Tab as MuiTab, Tabs, Tooltip} from '@mui/material';
import AbstractSelectorTabs from "../../../gen/displays/components/AbstractSelectorTabs";
import SelectorTabsNotifier from "../../../gen/displays/notifiers/SelectorTabsNotifier";
import SelectorTabsRequester from "../../../gen/displays/requesters/SelectorTabsRequester";
import Divider from './Divider';
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import {TabContent} from "./Tab";

const styles = theme => ({
	root: {
		minHeight: "36px",
		padding: "0 8px",
	},
	indicator: {
		height: "2px",
		borderRadius: "999px",
		backgroundColor: theme.palette.primary.main,
	},
	verticalIndicator: {
		height: "auto",
		width: "2px",
		left: "auto",
		right: 0,
		borderRadius: "999px",
		backgroundColor: theme.palette.primary.main,
		transform: "scaleY(0.7)",
		transformOrigin: "center",
	},
	verticalLeftIndicator: {
		height: "auto",
		width: "2px",
		left: 0,
		right: "auto",
		borderRadius: "999px",
		backgroundColor: theme.palette.primary.main,
		transform: "scaleY(0.7)",
		transformOrigin: "center",
	},
	horizontalTopIndicator: {
		height: "2px",
		top: 0,
		bottom: "auto",
		borderRadius: "999px",
		backgroundColor: theme.palette.primary.main,
	},
	verticalTab: {
		alignItems: "center",
		boxSizing: "border-box",
		justifyContent: "center",
		minHeight: "48px",
		minWidth: 0,
		padding: "0 12px",
	},
	tabRoot: {
		minHeight: "36px",
		paddingTop: 0,
		paddingBottom: 0,
		...(theme.palette.mode === "dark" ? {
			color: "rgba(226,232,240,0.72)",
			"&.Mui-selected": {
				color: "#90caf9",
			},
			"&.Mui-disabled": {
				opacity: 0.4,
			}
		} : {})
	},
	optionTooltip: {
		backgroundColor: theme.palette.mode === "dark" ? "#1e293b" : "#ffffff",
		border: `1px solid ${theme.palette.mode === "dark" ? "rgba(148,163,184,0.28)" : "rgba(148,163,184,0.32)"}`,
		borderRadius: "6px",
		boxShadow: theme.palette.mode === "dark" ? "0 8px 20px rgba(0,0,0,0.32)" : "0 6px 16px rgba(15,23,42,0.14)",
		color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.9)" : "#64748b",
		fontSize: "0.75rem",
		fontWeight: 500,
		lineHeight: 1.25,
		padding: "6px 10px"
	}
});

class SelectorTabs extends AbstractSelectorTabs {

	constructor(props) {
        super(props);
        this.notifier = new SelectorTabsNotifier(this);
        this.requester = new SelectorTabsRequester(this);
        this.state = {
            ...this.state,
            selected: 0,
            optionsVisibility: [],
            hoveredOption: -1
        };
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
	    const { classes } = this.props;
	    const selected = this.state.selected >= 0 ? this.state.selected : false;
	    const children = this._visibleOptions();
	    const scrollButtons = this.props.scrollButtons != undefined ? this.props.scrollButtons.toLowerCase() : "off";
	    const variant = scrollButtons !== "off" ? "scrollable" : undefined;
		const vertical = this.props.layout != null && this.props.layout.toLowerCase() === "vertical";
		const indicatorPosition = this.props.indicatorPosition != null ? this.props.indicatorPosition.toLowerCase() : (vertical ? "right" : "bottom");
		const indicatorClass = vertical ? (indicatorPosition === "left" ? classes.verticalLeftIndicator : classes.verticalIndicator) : (indicatorPosition === "top" ? classes.horizontalTopIndicator : classes.indicator);
		const tabsStyle = {...this.style(), ...this.highlightBackgroundStyle(), ...this.highlightStyle(), minHeight: "36px", padding: "0 8px"};
        return (
			<Tabs value={selected} variant={variant} orientation={vertical ? "vertical" : "horizontal"} scrollButtons={scrollButtons}
				  onChange={this.handleChange.bind(this)} color={this.props.color} style={tabsStyle}
				  className={classes.root} classes={{ indicator: indicatorClass }}>
				{React.Children.map(children, (child, i) => { return this.renderTab(child, i, vertical); })}
            </Tabs>
        );
    }

	renderTab = (tab, i, vertical) => {
	    const className = tab.props.className;
        if (className != null && className.indexOf("divider") !== -1) return (<Divider/>);
		const description = this.optionDescription(tab);
		const content = vertical ? (
			<span style={{display: "inline-flex", alignItems: "center", justifyContent: "center", lineHeight: 0}}>
				{tab}
			</span>
		) : tab;
		const label = (
			<TabContent highlighted={this.state.hoveredOption === i} theme={this.props.theme}>
				{content}
			</TabContent>
		);
		const tabLabel = description != null && description !== "" ? (
			<Tooltip title={this.translate(description)} placement="bottom" enterDelay={250}
				slotProps={{tooltip: {className: this.props.classes.optionTooltip}}}>
				{label}
			</Tooltip>
		) : label;
		const style = {
			...(this.styleOf(tab) || {}),
			minHeight: "36px",
			paddingTop: 0,
			paddingBottom: 0,
			...(vertical ? {
			alignItems: "center",
			boxSizing: "border-box",
			flex: "0 0 auto",
			height: "auto",
			justifyContent: "center",
			maxHeight: "none",
			minHeight: "36px",
			padding: "0 12px"
		} : {})
		};
		return (<MuiTab label={tabLabel} index={i} style={style} className={vertical ? `${this.props.classes.tabRoot} ${this.props.classes.verticalTab}` : this.props.classes.tabRoot}
			onMouseEnter={() => this.refreshHoveredOption(i)}
			onMouseLeave={this.clearHoveredOption}
			onFocus={() => this.refreshHoveredOption(i)}
			onBlur={this.clearHoveredOption}/>);
    };

	refreshHoveredOption = (index) => {
		if (this.state.hoveredOption !== index) this.setState({hoveredOption: index});
	};

	clearHoveredOption = () => {
		if (this.state.hoveredOption !== -1) this.setState({hoveredOption: -1});
	};

	optionDescription = (tab) => {
		const descriptions = this.props.optionDescriptions || {};
		return descriptions[tab.props.name];
	};

    refreshSelected = (tab) => {
		if (tab == null || tab < 0) {
			this.setState({ selected: -1 });
			return;
		}
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
