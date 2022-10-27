import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseSorting from "../../../gen/displays/components/AbstractBaseSorting";
import BaseSortingNotifier from "../../../gen/displays/notifiers/BaseSortingNotifier";
import BaseSortingRequester from "../../../gen/displays/requesters/BaseSortingRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

export const BaseSortingStyles = theme => ({
	link : {
		color: theme.palette.primary.main,
		cursor: "pointer"
	},
});

export default class BaseSorting extends AbstractBaseSorting {

	constructor(props) {
		super(props);
		this.notifier = new BaseSortingNotifier(this);
		this.requester = new BaseSortingRequester(this);
	};

	handleToggle = () => {
		this.requester.toggle();
	};

	refreshSelection = (selection) => {
	};

}