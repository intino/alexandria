import React from "react";
import AbstractCollection from "../../../gen/displays/components/AbstractCollection";
import CollectionNotifier from "../../../gen/displays/notifiers/CollectionNotifier";
import CollectionRequester from "../../../gen/displays/requesters/CollectionRequester";
import CollectionBehavior from "./behaviors/CollectionBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export const CollectionStyles = theme => ({
	scrolling: {
		background: "#ddd",
		height: "50%",
		borderRadius: "5px"
	},
	selectable : {
		paddingLeft: "35px !important"
	},
	selector : {
		position: "absolute",
		left: "-5px"
	},
	selecting : {
		"& $selector" : {
			display: "block"
		}
	}
});

export default class Collection extends AbstractCollection {
	state = {
		selection: [],
		itemCount: 20,
		pageSize: 20,
		page: 0
	};

	constructor(props) {
		super(props);
		this.behavior = new CollectionBehavior(this);
	};

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, pageSize: info.pageSize });
	};

	refresh = () => {
		this.behavior.refresh();
	};

	refreshItemCount = (itemCount) => {
		this.setState({ itemCount });
	};
}

DisplayFactory.register("Collection", Collection);