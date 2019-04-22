import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { FixedSizeList as ReactWindowList } from 'react-window';
import InfiniteLoader from 'react-window-infinite-loader';
import AutoSizer from 'react-virtualized-auto-sizer';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";
import CollectionBehavior from "./behaviors/CollectionBehavior";
import * as Elements from "app-elements/gen/Displays";

const styles = theme => ({
	scrolling: {
		background: "#ddd",
		height: "50%",
		borderRadius: "5px"
	}
});

class List extends AbstractList {

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
		this.behavior = new CollectionBehavior(this, this.itemView.bind(this));
	};

	render() {
		const items = this.instances("rows");
		const count = items.length;
		const isItemLoaded = index => !!items[index];

		return (
			<AutoSizer>
				{({ height, width }) => (
					<InfiniteLoader isItemLoaded={isItemLoaded} itemCount={10000} loadMoreItems={this.behavior.nextPage.bind(this)}>
						{({ onItemsRendered, ref }) => (
							<ReactWindowList ref={ref} onScroll={this.behavior.scrolling.bind(this, items)}
											 onItemsRendered={this.behavior.refreshItemsRendered.bind(this, items)}
											 height={height} width={width} itemCount={10000} itemSize={60}>
								{this.behavior.renderItem.bind(this, items)}
							</ReactWindowList>
						)}
					</InfiniteLoader>
				)}
			</AutoSizer>
		);
	};

	itemView = (item) => {
		return React.createElement(Elements[item.tp], item.pl);
	};
}

export default withStyles(styles, { withTheme: true })(List);