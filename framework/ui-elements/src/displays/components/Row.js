import React from "react";
import AbstractRow from "../../../gen/displays/components/AbstractRow";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {columnWidthStyle, mapTableColumns, TableColumnContext} from "./TableColumnLayout";

export default class Row extends AbstractRow {
    static contextType = TableColumnContext;

	constructor(props) {
		super(props);
	};

	renderRow(row) {
		let style = { height:"100%"};
		if (this.state.color != null) style.backgroundColor = this.state.color;
        const widths = this.context != null ? this.context.widths : null;
        const cells = widths == null ? row : mapTableColumns(row, (child, index) =>
            React.cloneElement(child, {style: {...child.props.style, ...columnWidthStyle(widths[index])}}));
        if (widths != null) style = {...style, ...columnWidthStyle(widths.reduce((sum, width) => sum + width, 0))};
		return (
			<div className="alexandria-table-row layout horizontal center flex" style={style}>{cells}</div>
		);
	};

}

DisplayFactory.register("Row", Row);
