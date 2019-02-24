import React from "react";
import { withStyles } from '@material-ui/core/styles';
import classNames from "classnames";
import MenuList from '@material-ui/core/MenuList';
import MenuItem from '@material-ui/core/MenuItem';
import AbstractSelectorMenu from "../../../gen/displays/components/AbstractSelectorMenu";
import SelectorMenuNotifier from "../../../gen/displays/notifiers/SelectorMenuNotifier";
import SelectorMenuRequester from "../../../gen/displays/requesters/SelectorMenuRequester";

const styles = theme => ({
	menuItem: {
		padding: "4px 10px",
		'&:focus': {
			backgroundColor: theme.palette.primary.main
		},
	},
	selected : {
		backgroundColor: theme.palette.primary.main,
		color: theme.palette.common.white,
		'&:hover': {
			backgroundColor: theme.palette.primary.main
		},
	}
});

class SelectorMenu extends AbstractSelectorMenu {
	state = {
		selected : 0
	};

	constructor(props) {
		super(props);
		this.notifier = new SelectorMenuNotifier(this);
		this.requester = new SelectorMenuRequester(this);
	};

	render() {
        const { classes } = this.props;
		return (
			<MenuList>
				{React.Children.map(this.props.children, (child, i) => {
						return (<MenuItem className={classNames(i === this.state.selected ? classes.selected : undefined, classes.menuItem)}
									 onClick={this.handleSelect.bind(this, i)}>{child}</MenuItem>);
				})}
			</MenuList>
		);
	};

	handleSelect = (pos) => {
		this.requester.select(pos);
	};

	refreshSelected = (selected) => {
		this.setState({ selected });
	};
}

export default withStyles(styles, { withTheme: true })(SelectorMenu);