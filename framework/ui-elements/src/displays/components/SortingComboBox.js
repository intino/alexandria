import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSortingComboBox from "../../../gen/displays/components/AbstractSortingComboBox";
import SortingComboBoxNotifier from "../../../gen/displays/notifiers/SortingComboBoxNotifier";
import SortingComboBoxRequester from "../../../gen/displays/requesters/SortingComboBoxRequester";
import {BaseSortingStyles} from "./BaseSorting";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({...BaseSortingStyles(theme)});

class SortingComboBox extends AbstractSortingComboBox {

	constructor(props) {
		super(props);
		this.notifier = new SortingComboBoxNotifier(this);
		this.requester = new SortingComboBoxRequester(this);
	};

}

export default withStyles(styles, { withTheme: true })(SortingComboBox);
DisplayFactory.register("SortingComboBox", withStyles(styles, { withTheme: true })(SortingComboBox));