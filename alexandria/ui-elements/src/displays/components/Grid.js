import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AutoSizer from 'react-virtualized-auto-sizer';
import {FixedSizeList as ReactWindowList} from "react-window";
import AbstractGrid from "../../../gen/displays/components/AbstractGrid";
import GridNotifier from "../../../gen/displays/notifiers/GridNotifier";
import GridRequester from "../../../gen/displays/requesters/GridRequester";
import CollectionBehavior from "./behaviors/CollectionBehavior";
import * as Elements from "app-elements/gen/Displays";

const styles = theme => ({
	scrolling: {
		background: "#ddd",
		height: "50%",
		borderRadius: "5px"
	}
});

class Grid extends AbstractGrid {

	constructor(props) {
		super(props);
		this.notifier = new GridNotifier(this);
		this.requester = new GridRequester(this);
		this.behavior = new CollectionBehavior(this, this.itemView.bind(this));
	};

	render() {
		const items = this.instances("rows");
		const count = items.length;

		return (
			<AutoSizer>
				{({ height, width }) => (
					<ReactWindowList useIsScrolling onScroll={this.behavior.scrolling.bind(this, items)} onItemsRendered={this.behavior.refreshItemsRendered.bind(this, items)} height={height} width={width}
									 itemCount={count} itemSize={100}>{this.behavior.renderItem.bind(this, items)}</ReactWindowList>
				)}
			</AutoSizer>
		);
	};

	itemView = (item) => {
		return (<div>{item}</div>)
	};

}

export default withStyles(styles, { withTheme: true })(Grid);