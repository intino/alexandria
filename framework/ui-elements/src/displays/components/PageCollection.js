import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPageCollection from "../../../gen/displays/components/AbstractPageCollection";
import PageCollectionNotifier from "../../../gen/displays/notifiers/PageCollectionNotifier";
import PageCollectionRequester from "../../../gen/displays/requesters/PageCollectionRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class PageCollection extends AbstractPageCollection {

	constructor(props) {
		super(props);
	};

	setupPageCollection = (info) => {
		this.setup(info);
	};

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, pageSize: info.pageSize });
	};

}

DisplayFactory.register("PageCollection", PageCollection);