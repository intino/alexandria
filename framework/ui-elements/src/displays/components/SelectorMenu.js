import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import classNames from "classnames";
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import ListSubheader from '@mui/material/ListSubheader';
import Collapse from "@mui/material/Collapse";
import ExpandMore from "@mui/icons-material/ExpandMore";
import ExpandLess from "@mui/icons-material/ExpandLess";
import AbstractSelectorMenu from "../../../gen/displays/components/AbstractSelectorMenu";
import SelectorMenuNotifier from "../../../gen/displays/notifiers/SelectorMenuNotifier";
import SelectorMenuRequester from "../../../gen/displays/requesters/SelectorMenuRequester";
import Block from './Block';
import Divider from './Divider';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	item: {
		padding: "6px 16px",
		border: "1px solid transparent",
		borderRadius: "16px",
		boxSizing: "border-box",
		'&:focus': {
			backgroundColor: "transparent",
			color: "inherit",
			outline: "none"
		},
	},
	hovered: {
		backgroundColor: `color-mix(in srgb, ${theme.palette.primary.main} 10%, ${theme.palette.mode === "dark" ? "#1e293b" : "white"}) !important`,
		color: theme.palette.mode === "dark" ? theme.palette.primary.light : theme.palette.primary.dark
	},
	selected : {
		backgroundColor: `color-mix(in srgb, ${theme.palette.primary.main} 10%, ${theme.palette.mode === "dark" ? "#1e293b" : "white"}) !important`,
		border: `1px solid ${theme.palette.primary.main}`,
		borderRadius: "16px",
		boxSizing: "border-box",
		color: theme.palette.mode === "dark" ? theme.palette.primary.light : theme.palette.primary.dark,
		'&:hover': {
			backgroundColor: `color-mix(in srgb, ${theme.palette.primary.main} 14%, ${theme.palette.mode === "dark" ? "#1e293b" : "white"}) !important`
		},
		'&:focus, &.Mui-focusVisible': {
			backgroundColor: `color-mix(in srgb, ${theme.palette.primary.main} 10%, ${theme.palette.mode === "dark" ? "#1e293b" : "white"}) !important`,
			color: `${theme.palette.mode === "dark" ? theme.palette.primary.light : theme.palette.primary.dark} !important`,
			outline: "none",
			boxShadow: "none"
		}
	}
});

class SelectorMenu extends AbstractSelectorMenu {

	constructor(props) {
		super(props);
		this.notifier = new SelectorMenuNotifier(this);
		this.requester = new SelectorMenuRequester(this);
		this.state = {
		    ...this.state,
            selected : -1,
            open : [],
			hovered: -1,
		}
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
		const children = this.children();
		this._index = -1;
		return (
			<List component="nav" style={{...this.style(), ...this.highlightBackgroundStyle(), ...this.highlightStyle()}}>
				{React.Children.map(children, (child, i) => { return this.renderItem(child); })}
			</List>
		);
	};

	renderItem = (item) => {
		const className = item.props.className;

		if (className != null && className.indexOf("divider") !== -1) return (<Divider/>);

		if (item.type == Block)
			return this.renderBlock(item);

		const { classes } = this.props;

		this._index++;
		const index = this._index;
		return (<ListItemButton className={classNames(classes.item, index === this.state.selected ? classes.selected : undefined, this.state.hovered === index && index !== this.state.selected ? classes.hovered : undefined)}
						  selected={index === this.state.selected}
						  onClick={this.handleSelect.bind(this, index)}
						  onMouseEnter={() => this.setState({hovered: index})}
						  onMouseLeave={() => this.setState({hovered: -1})}
						  onFocus={() => this.setState({hovered: index})}
						  onBlur={() => this.setState({hovered: -1})}>{item}</ListItemButton>);
	};

	renderBlock = (block) => {
		if (block.props.collapsible) return (
			<React.Fragment>
				<ListItemButton onClick={this.handleOpen.bind(this, block)}><ListItemText>{block.props.label}</ListItemText>{this.state.open[block.props.label] ? <ExpandLess /> : <ExpandMore />}</ListItemButton>
				<Collapse in={this.state.open[block.props.label]} timeout="auto">{this.renderBlockList(block)}</Collapse>
			</React.Fragment>
		);
		return this.renderBlockList(block);
	};

	renderBlockList = (block) => {
		return (
			<List component="nav" subheader={block.props.collapsible ? undefined : <ListSubheader>{block.props.label}</ListSubheader>} disablePadding>
				{React.Children.map(block.props.children, (child, i) => { return (this.renderItem(child)); })}
			</List>
		);
	};

	handleSelect = (pos) => {
		this.requester.select(pos);
	};

	handleOpen = (block) => {
		const open = this.state.open;
		open[block.props.label] = !open[block.props.label];
		this.setState({ open : open });
	};

	refreshSelected = (selection) => {
        const open = this.state.open;
        selection.ancestors.forEach(a => open[a] = true);
		this.setState({ selected: selection.option, open: open });
	};
}

export default withStyles(styles, { withTheme: true })(SelectorMenu);
DisplayFactory.register("SelectorMenu", withStyles(styles, { withTheme: true })(SelectorMenu));
