import React from "react";
import Plot from "react-plotly.js/react-plotly";

export default function ChartPlotly(props) {
    return (<Plot data={props.data}/>);
}