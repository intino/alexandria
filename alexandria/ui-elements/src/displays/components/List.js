import React from "react";
import { withStyles } from '@material-ui/core/styles';
import InfiniteLoader from 'react-window-infinite-loader';
import { FixedSizeList } from 'react-window';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";

const styles = theme => ({});

const Item = ({ index, isScrolling, style }) => {
	return (<div style={style} key={index}>{isScrolling ? "scrolling" : "Row " + index}</div>);
	// return (<div style={style} key={index}>{React.createElement(Elements[instance.tp], instance.pl)}</div>);//{React.createElement(Elements[instance.tp], instance.pl)}
};

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
		return (
			<FixedSizeList useIsScrolling height={150} width={300} itemCount={100} itemSize={35}>{Item}</FixedSizeList>
		);
	};

	refreshItems = (count) => {
	}
}

export default withStyles(styles, { withTheme: true })(List);