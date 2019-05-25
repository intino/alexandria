import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPageCollection from "../../../gen/displays/components/AbstractPageCollection";
import PageCollectionNotifier from "../../../gen/displays/notifiers/PageCollectionNotifier";
import PageCollectionRequester from "../../../gen/displays/requesters/PageCollectionRequester";

export default class PageCollection extends AbstractPageCollection {

	constructor(props) {
		super(props);
		this.notifier = new PageCollectionNotifier(this);
		this.requester = new PageCollectionRequester(this);
	};

}