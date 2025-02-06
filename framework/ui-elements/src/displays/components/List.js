import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AutoSizer from 'react-virtualized-auto-sizer';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import {CollectionStyles} from "./Collection";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export const ListStyles = theme => ({
	...CollectionStyles(theme),
	itemView : {
		height: "100%",
		padding: "0 10px",
		'&:hover' : {
			background: theme.isDark() ? "#444" : "#ddd"
		},
		'&:hover $selector' : {
			display: 'block'
		}
	},
});

export class EmbeddedList extends AbstractList {

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
	};

	render() {
		return (<div ref={this.container} className="flex" style={{width:"100%",position:'relative'}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width))}</AutoSizer></div>);
	};

}

class List extends EmbeddedList {
    constructor(props) {
        super(props);
    }
}

export default withStyles(ListStyles, { withTheme: true })(List);
DisplayFactory.register("List", withStyles(ListStyles, { withTheme: true })(List));