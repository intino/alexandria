import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { FixedSizeList as ReactWindowList } from 'react-window';
import InfiniteLoader from 'react-window-infinite-loader';
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
		pageSize: 20
	};

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
		this.behavior = new CollectionBehavior(this);
	};

	render() {
		const items = this.instances("rows");
		const threshold = Math.round(this.state.pageSize * 0.8);
		const isItemLoaded = index => !!items[index];

		this.behavior.pageSize(this.state.pageSize);

		return (
			<AutoSizer>
				{({ height, width }) => (
					<InfiniteLoader isItemLoaded={isItemLoaded} itemCount={this.state.itemCount}
									loadMoreItems={this.behavior.moreItems.bind(this, items)}
									threshold={threshold}>
						{({ onItemsRendered, ref }) => (
							<ReactWindowList useIsScrolling={this.props.scrollingMark} ref={ref} onScroll={this.behavior.scrolling.bind(this, items)}
											 onItemsRendered={this.behavior.refreshItemsRendered.bind(this, items, onItemsRendered)}
											 height={height} width={width} itemCount={this.state.itemCount} itemSize={this.props.itemHeight}>
								{this.behavior.renderItem.bind(this, items)}
							</ReactWindowList>
						)}
					</InfiniteLoader>
				)}
			</AutoSizer>
		);
	};

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, pageSize: info.pageSize });
	};
}

export default withStyles(styles, { withTheme: true })(List);