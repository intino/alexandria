import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTable from "../../../gen/displays/components/AbstractTable";
import TableNotifier from "../../../gen/displays/notifiers/TableNotifier";
import TableRequester from "../../../gen/displays/requesters/TableRequester";
import AutoSizer from 'react-virtualized-auto-sizer';
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import Heading from "./Heading";
import { CollectionStyles } from "./Collection";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	...CollectionStyles(theme),
	headerView : {
		borderBottom: "1px solid #ddd",
		height: "100%"
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
		const offset = React.Children.count(this.props.children) > 0 ? Heading.Height : 0;

		return (
			<React.Fragment>
				<div className={classNames(classes.headerView, "layout horizontal")}>{this.props.children}</div>
				<AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height-offset, width))}</AutoSizer>
			</React.Fragment>
		);
	}

}

export default withStyles(styles, { withTheme: true })(Table);
DisplayFactory.register("Table", withStyles(styles, { withTheme: true })(Table));