import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { FixedSizeList as ReactWindowList, areEqual } from 'react-window';
import AutoSizer from 'react-virtualized-auto-sizer';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";
import * as Elements from "app-elements/gen/Displays";

const styles = theme => ({
	scrolling: {
		background: "#ddd",
		height: "50%",
		borderRadius: "5px"
	}
});

class List extends AbstractList {
	state = {
		countItems : -1
	};

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
		this.container = React.createRef();
		this._widths = {};
	};

	render() {
		const instances = this.instances();
		const count = instances.length;

		return (
			<AutoSizer>
				{({ height, width }) => (
					<ReactWindowList useIsScrolling onItemsRendered={this.handleItemsRendered.bind(this, instances)} height={height} width={width}
									 itemCount={count} itemSize={100}>{this._renderItem.bind(this, instances)}</ReactWindowList>
				)}
			</AutoSizer>
		);
	};

	_renderItem = (instances, { index, isScrolling, style }) => {
		const instance = instances[index];
		const id = instance.pl.id;
		const { classes } = this.props;
		const width = this._width(id);
		return (<div style={style} key={index}>{isScrolling ? <div style={ { width: width }} className={classes.scrolling}></div> : React.createElement(Elements[instance.tp], instance.pl)}</div>);
	};

	_width = (id) => {
		const max = 70;
		const min = 50;
		if (this._widths[id] == null)
			this._widths[id] = Math.floor(Math.random()*(max-min+1)+min) + "%";
		return this._widths[id];
	};

	handleItemsRendered = (instances, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex }) => {
		if (this.itemsView == null) this.itemsView = { start: Number.MAX_VALUE, end: Number.MIN_VALUE };
		if (visibleStartIndex < this.itemsView.start) this.itemsView.start = visibleStartIndex;
		if (visibleStopIndex > this.itemsView.stop) this.itemsView.stop = visibleStopIndex;
		if (this.timeout != null) window.clearTimeout(this.timeout);
		this.timeout = window.setTimeout(() => {
			this.requester.notifyItemsRendered({ items: this._instancesIds(instances, overscanStartIndex, overscanStopIndex),
														visible: this._instancesIds(instances, visibleStartIndex, visibleStopIndex)
												});
			this.itemsView = null;
		}, 50);
	};

	_instancesIds = (instances, start, end) => {
		var result = [];
		for (var i=0; i<instances.length; i++) {
			if (i < start || i > end) continue;
			result.push(instances[i].pl.id);
		}
		return result;
	}

}

export default withStyles(styles, { withTheme: true })(List);