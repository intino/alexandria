import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AutoSizer from 'react-virtualized-auto-sizer';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	scrolling: {
		background: "#ddd",
		height: "50%",
		borderRadius: "5px"
	},
	itemView : {
		height: "100%",
		padding: "0 10px",
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

class List extends AbstractList {

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
	};

	render() {
		return (<div className="flex"><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width))}</AutoSizer></div>);
	};

}

export default withStyles(styles, { withTheme: true })(List);