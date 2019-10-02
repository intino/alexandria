import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { withSnackbar } from 'notistack';
import AbstractTable from "../../../gen/displays/components/AbstractTable";
import TableNotifier from "../../../gen/displays/notifiers/TableNotifier";
import TableRequester from "../../../gen/displays/requesters/TableRequester";
import AutoSizer from 'react-virtualized-auto-sizer';
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import Heading from "./Heading";
import { CollectionStyles } from "./Collection";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import Block from "./Block";

const styles = theme => ({
	...CollectionStyles(theme),
	label: {
		color: theme.palette.grey.primary,
		marginRight: "5px"
	},
	headerView : {
		borderBottom: "1px solid #ddd",
		width: "100%"
	},
	itemView : {
		borderBottom: "1px solid #ddd",
		height: "100%",
		'&:hover' : {
			background: '#ddd'
		},
		'&:hover $selector' : {
			display: 'block'
		}
	},
});

class Table extends AbstractTable {

	constructor(props) {
		super(props);
		this.notifier = new TableNotifier(this);
		this.requester = new TableRequester(this);
	};

	render() {
		const { classes } = this.props;
		const selectable = this.props.selection != null;
		// const offset = React.Children.count(this.props.children) > 0 ? Heading.Height : 0;

		return (
			<React.Fragment>
				{ ComponentBehavior.labelBlock(this.props) }
				<div className={classNames(classes.headerView, "layout horizontal", selectable ? classes.selectable : {})}>{this.props.children}</div>
				<div className="layout flex" style={{width:"100%",height:"100%"}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width))}</AutoSizer></div>
			</React.Fragment>
		);
	}

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Table));
DisplayFactory.register("Table", withStyles(styles, { withTheme: true })(withSnackbar(Table)));