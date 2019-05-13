import React from "react";
import AbstractCollection from "../../../gen/displays/components/AbstractCollection";
import CollectionNotifier from "../../../gen/displays/notifiers/CollectionNotifier";
import CollectionRequester from "../../../gen/displays/requesters/CollectionRequester";
import CollectionBehavior from "./behaviors/CollectionBehavior";

export default class Collection extends AbstractCollection {
	state = {
		selection: [],
		itemCount: 20,
		pageSize: 20,
		page: 0
	};

	constructor(props) {
		super(props);
		this.notifier = new CollectionNotifier(this);
		this.requester = new CollectionRequester(this);
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