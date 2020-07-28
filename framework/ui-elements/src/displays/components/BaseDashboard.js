import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseDashboard from "../../../gen/displays/components/AbstractBaseDashboard";
import BaseDashboardNotifier from "../../../gen/displays/notifiers/BaseDashboardNotifier";
import BaseDashboardRequester from "../../../gen/displays/requesters/BaseDashboardRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

export default class BaseDashboard extends AbstractBaseDashboard {

	constructor(props) {
		super(props);
		this.notifier = new BaseDashboardNotifier(this);
		this.requester = new BaseDashboardRequester(this);
	};

	showLoading = () => {
	};
}
