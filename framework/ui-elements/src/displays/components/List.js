import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AutoSizer from 'react-virtualized-auto-sizer';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import {collectionPalette, CollectionStyles} from "./Collection";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export const ListStyles = theme => ({
	...CollectionStyles(theme),
	itemView : {
		height: "100%",
		padding: "2px 12px",
		borderRadius: "14px",
		transition: "background-color 140ms ease, box-shadow 140ms ease",
		'&:hover' : {
			background: collectionPalette(theme).rowHoverBackground
		},
		'&:hover $selector' : {
			opacity: 1,
			pointerEvents: "auto"
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
		const { classes } = this.props;
		return (<div ref={this.container} className={classes.collectionViewport + " flex"} style={{width:"100%",position:'relative',borderRadius:"18px"}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width))}</AutoSizer></div>);
	};

}

class List extends EmbeddedList {
    constructor(props) {
        super(props);
    }
}

export default withStyles(ListStyles, { withTheme: true })(List);
DisplayFactory.register("List", withStyles(ListStyles, { withTheme: true })(List));
