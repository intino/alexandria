import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTable from "../../../gen/displays/components/AbstractTable";
import TableNotifier from "../../../gen/displays/notifiers/TableNotifier";
import TableRequester from "../../../gen/displays/requesters/TableRequester";
import AutoSizer from 'react-virtualized-auto-sizer';
import CollectionBehavior from "./behaviors/CollectionBehavior";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import Heading from "./Heading";

const styles = theme => ({
	scrolling: {
		background: "#ddd",
		height: "50%",
		borderRadius: "5px"
	},
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
	selectable : {
		paddingLeft: "35px"
	},
	selector : {
		display: "none",
		position: "absolute",
		left: "-5px"
	},
	selecting : {
		"& $selector" : {
			display: "block"
		}
	}
});

class Table extends AbstractTable {
	state = {
		selection: [],
		itemCount: 20,
		pageSize: 20,
		page: 0
	};

	constructor(props) {
		super(props);
		this.notifier = new TableNotifier(this);
		this.requester = new TableRequester(this);
		this.behavior = new CollectionBehavior(this);
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

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, pageSize: info.pageSize });
	};

	refresh = () => {
		this.behavior.refresh();
	};
}

export default withStyles(styles, { withTheme: true })(Table);