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
	selected : {
        background: "blue"
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

	constructor(props) {
		super(props);
		this.behavior = new CollectionBehavior(this);
        this.container = React.createRef();
        this.state = {
            multiSelection: this.props.selection != null && this.props.selection === "multiple",
            itemCount: 20,
            pageSize: 20,
            page: 0,
            ...this.state
        };
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

	refreshSelection = (selection) => {
		this.behavior.refreshSelection(selection);
	};

	refreshAllowMultiSelection = (value) => {
		this.setState({ multiSelection: value });
	};

	allowMultiSelection = () => {
        if (this.state.multiSelection != null) return this.state.multiSelection;
        return this.props.selection != null && this.props.selection === "multiple";
	};
}

DisplayFactory.register("Collection", Collection);