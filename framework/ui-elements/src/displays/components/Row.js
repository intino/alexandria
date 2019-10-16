import React from "react";
import AbstractRow from "../../../gen/displays/components/AbstractRow";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Row extends AbstractRow {

	constructor(props) {
		super(props);
	};

	renderRow(row) {
		let style = { height:"100%"};
		if (this.state.color != null) style.backgroundColor = this.state.color;
		return (
			<div className="layout horizontal center flex" style={style}>{row}</div>
		);
	};

}

DisplayFactory.register("Row", Row);