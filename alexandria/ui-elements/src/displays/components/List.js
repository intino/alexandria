import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AutoSizer from 'react-virtualized-auto-sizer';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";
import CollectionBehavior from "./behaviors/CollectionBehavior";

const styles = theme => ({
	scrolling: {
		background: "#ddd",
		height: "50%",
		borderRadius: "5px"
	},
	itemView : {
		height: "100%",
		padding: "0 10px",
		'&:hover': {
			background: '#ddd',
		}
	}
});

class List extends AbstractList {
	state = {
		itemCount: 20,
		pageSize: 20,
		page: 0
	};

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
		this.behavior = new CollectionBehavior(this);
	};

	render() {
		return (<AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width))}</AutoSizer>);
	};

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, pageSize: info.pageSize });
	};

	refresh = () => {
		this.behavior.refresh();
	};
}

export default withStyles(styles, { withTheme: true })(List);