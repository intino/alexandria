import React from "react";
import AbstractRow from "../../../gen/displays/components/AbstractRow";
import RowNotifier from "../../../gen/displays/notifiers/RowNotifier";
import RowRequester from "../../../gen/displays/requesters/RowRequester";
import 'alexandria-ui-elements/res/styles/layout.css';

export default class Row extends AbstractRow {

	constructor(props) {
		super(props);
		this.notifier = new RowNotifier(this);
		this.requester = new RowRequester(this);
	};

	renderRow(row) {
		return (
			<div className="layout horizontal center flex" style={{height:"100%"}}>{row}</div>
		);
	};

}