import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTable from "../../../gen/displays/components/AbstractTable";
import TableNotifier from "../../../gen/displays/notifiers/TableNotifier";
import TableRequester from "../../../gen/displays/requesters/TableRequester";
import AutoSizer from 'react-virtualized-auto-sizer';
import {FixedSizeList as ReactWindowList} from "react-window";
import InfiniteLoader from 'react-window-infinite-loader';
import CollectionBehavior from "./behaviors/CollectionBehavior";
import classNames from "classnames";
import Heading from "./Heading";
import 'alexandria-ui-elements/res/styles/layout.css';

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
		'&:hover': {
			background: '#ddd',
		}
	}
});

class Table extends AbstractTable {
	state = {
		itemCount: 20,
		pageSize: 20
	};

	constructor(props) {
		super(props);
		this.notifier = new TableNotifier(this);
		this.requester = new TableRequester(this);
		this.behavior = new CollectionBehavior(this);
	};

	render() {
		const { classes } = this.props;
		const items = this.instances("rows");
		const threshold = Math.round(this.state.pageSize * 0.8);
		const isItemLoaded = index => !!items[index];
		const offset = React.Children.count(this.props.children) > 0 ? Heading.Height : 0;

		this.behavior.pageSize(this.state.pageSize);

		return (
			<React.Fragment>
				<div className={classNames(classes.headerView, "layout horizontal")}>{this.props.children}</div>
				<AutoSizer>
					{({ height, width }) => (
						<InfiniteLoader isItemLoaded={isItemLoaded} itemCount={this.state.itemCount}
										loadMoreItems={this.behavior.moreItems.bind(this, items)}
										threshold={threshold}>
							{({ onItemsRendered, ref }) => (
								<ReactWindowList ref={ref} onScroll={this.behavior.scrolling.bind(this, items)}
												 onItemsRendered={this.behavior.refreshItemsRendered.bind(this, items, onItemsRendered)}
												 height={height-offset} width={width} itemCount={this.state.itemCount} itemSize={this.props.itemHeight}>
									{this.behavior.renderItem.bind(this, items)}
								</ReactWindowList>
							)}
						</InfiniteLoader>
					)}
				</AutoSizer>
			</React.Fragment>
		);
	}

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, pageSize: info.pageSize });
	};
}

export default withStyles(styles, { withTheme: true })(Table);