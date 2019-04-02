import React from "react";
import Plot from "react-plotly.js/react-plotly";

export default function ChartPlotly(props) {
    return (<Plot layout={{ autosize: false, width: props.width }} data={props.data}/>);
}