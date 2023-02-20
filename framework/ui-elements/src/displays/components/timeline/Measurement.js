import React, { createRef, useRef } from "react";
import { useDrag, useDrop } from "react-dnd";
import ItemTypes from "./ItemTypes";
import classnames from "classnames";
import { ArrowDropDown, ArrowDropUp, DragIndicator } from "@material-ui/icons";
import { Typography, Icon } from '@material-ui/core';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';

const style = {
  margin: "5px",
  backgroundColor: "white",
  cursor: "move"
};

const TimelineMeasurement = ({ measurement, index, id, moveMeasurement, classes, openHistory }) => {
    const ref = useRef(null);
    const chart = createRef();
    const [{ handlerId }, drop] = useDrop({
        accept: ItemTypes.TIMELINE_MEASUREMENT,
        collect(monitor) {
            return {
                handlerId: monitor.getHandlerId(),
            }
        },
        hover(item, monitor) {
            if (!ref.current) return;
            const dragIndex = item.index;
            const hoverIndex = index;
            if (dragIndex === hoverIndex) return;
            const hoverBoundingRect = ref.current.getBoundingClientRect();
            const hoverMiddleY = (hoverBoundingRect.bottom - hoverBoundingRect.top) / 2;
            const hoverMiddleX = (hoverBoundingRect.right - hoverBoundingRect.left) / 2;
            const clientOffset = monitor.getClientOffset();
            const hoverClientY = clientOffset.y - hoverBoundingRect.top;
            const hoverClientX = clientOffset.x - hoverBoundingRect.left;
            const upwards = dragIndex > hoverIndex && hoverClientY > hoverMiddleY;
            const downwards = dragIndex < hoverIndex && hoverClientY < hoverMiddleY;
            const leftwards = dragIndex > hoverIndex && hoverClientX > hoverMiddleX;
            const rightwards = dragIndex < hoverIndex && hoverClientX < hoverMiddleX;
            if (upwards && (leftwards || rightwards)) return;
            if (downwards && (leftwards || rightwards)) return;
            moveMeasurement(dragIndex, hoverIndex);
            item.index = hoverIndex;
        }
    });
    const [{ isDragging }, drag] = useDrag({
        item: () => { return { id, index } },
        type: ItemTypes.TIMELINE_MEASUREMENT,
        collect: monitor => ({
          isDragging: monitor.isDragging()
        })
    });
    const renderEvolution = (measurement) => {
        return (
            <div style={{width:'100%',height:'100%'}}>
                <HighchartsReact ref={chart} highcharts={Highcharts} options={evolutionOptions(measurement)} />
            </div>
        );
    };
    const summaryPositioner = (chartRef, labelWidth, labelHeight, point) => {
        var chart = chartRef.current != null ? chartRef.current.chart : null;
        if (chart == null) return { x: 0, y: 0 };
        return {
            x: chart.plotLeft + chart.plotSizeX/2 - labelWidth/2,
            y: chart.plotTop + chart.plotSizeY - 4
        };
    }
    const evolutionOptions = (measurement) => {
        const decimalCount = measurement.decimalCount;
        const evolution = measurement.evolution;
        const height = 50;
        const width = 150;
        const unit = measurement.unit;
        const positioner = summaryPositioner.bind(this, chart);
        return {
            chart: { type: 'spline', height: height, width: width, backgroundColor: 'transparent' },
            title: { text: '' },
            legend: { enabled: false },
            credits: { enabled: false },
            xAxis: { visible: false, type: 'datetime', labels: { overflow: 'justify' }, categories: evolution.categories, },
            yAxis: { visible: false, title: { text: '' }, minorGridLineWidth: 0, gridLineWidth: 0, alternateGridColor: null, },
            tooltip: {
                enabled: true,
                positioner: positioner,
                formatter: function() {
                    return '<div style="font-size:6pt;">' + Highcharts.numberFormat(this.y,decimalCount) + (unit != null ? unit : "") + '  ' + this.x + "</div>";
                }
            },
            plotOptions: { spline: { lineWidth: 1, states: { hover: { lineWidth: 2 } }, marker: { enabled: false } } },
            series: [{
                name: '',
                data: evolution.serie.values,
                cursor: 'pointer',
                point: { events: { click: function(e) { openHistory(); } } },
            }],
            navigation: { menuItemStyle: { fontSize: '10px' } }
        };
    };
    const opacity = isDragging ? 0 : 1;
    const increased = measurement.trend === "Increased";
    const decreased = measurement.trend === "Decreased";
    drag(drop(ref));
    return (
        <div ref={ref} style={{ ...style, opacity, position: 'relative' }} data-handler-id={handlerId}>
            <Icon style={{position:'absolute',zIndex:1}}><DragIndicator/></Icon>
            <div className={classnames("layout vertical center", classes.measurement)}>
                <div className="layout horizontal" >
                    <Typography className={classes.value}>{measurement.value}</Typography>
                    <div className="layout vertical center" style={{position:'relative'}} >
                        <Typography className={classes.unit}>{measurement.unit}</Typography>
                        <div style={{position:'relative'}}>
                            {increased && <ArrowDropUp className={classnames(classes.trend, classes.increased)}/>}
                            {decreased && <ArrowDropDown className={classnames(classes.trend, classes.decreased)}/>}
                        </div>
                    </div>
                </div>
                <Typography variant="body2">{measurement.label}</Typography>
                {renderEvolution(measurement)}
            </div>
        </div>
    );
};
export default TimelineMeasurement;