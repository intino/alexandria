import React from "react";
import { withStyles } from '@material-ui/core/styles';
import classNames from "classnames";
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import Collapse from "@material-ui/core/Collapse";
import ExpandMore from "@material-ui/icons/ExpandMore";
import ExpandLess from "@material-ui/icons/ExpandLess";
import AbstractSelectorMenu from "../../../gen/displays/components/AbstractSelectorMenu";
import SelectorMenuNotifier from "../../../gen/displays/notifiers/SelectorMenuNotifier";
import SelectorMenuRequester from "../../../gen/displays/requesters/SelectorMenuRequester";
import Block from './Block';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	item: {
		padding: "6px 16px",
		'&:focus': {
			backgroundColor: theme.palette.primary.main,
			color: theme.palette.common.white
		},
	},
	selected : {
		backgroundColor: theme.palette.primary.main + " !important",
		color: theme.palette.common.white,
		'&:hover': {
			backgroundColor: theme.palette.primary.main
		},
	}
});

class SelectorMenu extends AbstractSelectorMenu {
	state = {
		selected : -1,
		open : []
	};

	constructor(props) {
		super(props);
		this.notifier = new SelectorMenuNotifier(this);
		this.requester = new SelectorMenuRequester(this);
	};

	render() {
		this._index = -1;
		return (
			<List component="nav">
				{React.Children.map(this.props.children, (child, i) => { return this.renderItem(child); })}
			</List>
		);
	};

	renderItem = (item) => {
		if (item.type == Block)
			return this.renderBlock(item);

		const { classes } = this.props;

		this._index++;
		return (<ListItem button className={classNames(this._index === this.state.selected ? classes.selected : undefined, classes.item)}
						  selected={this._index === this.state.selected}
						  onClick={this.handleSelect.bind(this, this._index)}>{item}</ListItem>);
	};

	renderBlock = (block) => {
		if (block.props.collapsible) return (
			<React.Fragment>
				<ListItem button onClick={this.handleOpen.bind(this, block)}><ListItemText>{block.props.label}</ListItemText>{this.state.open[block.props.label] ? <ExpandLess /> : <ExpandMore />}</ListItem>
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