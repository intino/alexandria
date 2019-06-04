import React from "react";
import AbstractItem from "../../../gen/displays/components/AbstractItem";
import ItemNotifier from "../../../gen/displays/notifiers/ItemNotifier";
import ItemRequester from "../../../gen/displays/requesters/ItemRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Item extends AbstractItem {

	constructor(props) {
		super(props);
		this.notifier = new ItemNotifier(this);
		this.requester = new ItemRequester(this);
	};

}

DisplayFactory.register("Item", Item);