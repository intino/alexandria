import React from "react";
import { withStyles } from '@material-ui/core/styles';
import InfiniteLoader from 'react-window-infinite-loader';
import { FixedSizeList } from 'react-window';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";
import * as Elements from "app-elements/gen/Displays";

const styles = theme => ({});

class List extends AbstractList {
	state = {
		countItems : -1
	};

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
	};

	render() {
		const instances = this.instances();
		const count = instances.length;
		return (
			<FixedSizeList useIsScrolling height={150} width={300} itemCount={count} itemSize={100}>{this._renderItem.bind(this, instances)}</FixedSizeList>
		);
	};

	componentDidUpdate = () => {
		this.requester.notifyItemsRendered();
	};

	_renderItem = (instances, { index, isScrolling, style }) => {
		const instance = instances[index];
		return (<div style={style} key={index}>{isScrolling ? "scrolling" : React.createElement(Elements[instance.tp], instance.pl)}</div>);
	};
}

export default withStyles(styles, { withTheme: true })(List);