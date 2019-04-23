import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTable from "../../../gen/displays/components/AbstractTable";
import TableNotifier from "../../../gen/displays/notifiers/TableNotifier";
import TableRequester from "../../../gen/displays/requesters/TableRequester";
import AutoSizer from 'react-virtualized-auto-sizer';
import {FixedSizeList as ReactWindowList} from "react-window";
import InfiniteLoader from 'react-window-infinite-loader';
import CollectionBehavior from "./behaviors/CollectionBehavior";
import 'alexandria-ui-elements/res/styles/layout.css';
import * as Elements from "app-elements/gen/Displays";

const styles = theme => ({
	scrolling: {
		background: "#ddd",
		height: "50%",
		borderRadius: "5px"
	},
	row: {
		borderBottom: "1px solid #ddd",
		padding: "10px"
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
		this.behavior = new CollectionBehavior(this, this.itemView.bind(this));
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
							<ReactWindowList ref={ref} onScroll={this.behavior.scrolling.bind(this, items)}
											 onItemsRendered={this.behavior.refreshItemsRendered.bind(this, items, onItemsRendered)}
											 height={height} width={width} itemCount={this.state.itemCount} itemSize={this.props.itemHeight}>
								{this.behavior.renderItem.bind(this, items)}
							</ReactWindowList>
						)}
					</InfiniteLoader>
				)}
			</AutoSizer>
		);
	}

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, pageSize: info.pageSize });
	};

	itemView = (item, index) => {
		// const { classes } = this.props;
		// console.log(row);
		// return (
			{/*<div className={classNames(classes.row, "layout horizontal")}>*/}
				{/*<div style={{width:this._columnWidth(0) + "%"}}>*/}
					// hola
				// </div>
				{/*<div style={{width:this._columnWidth(1) + "%"}}>adios</div>*/}
			// </div>
		// );
		return React.createElement(Elements["CollectionRow"], item.pl);
	};

	_columnWidth = (index) => {
		return this.props.itemsWidth[index];
	}
}

export default withStyles(styles, { withTheme: true })(Table);