import React, { createRef, useRef, useState } from "react";
import { useDrag, useDrop } from "react-dnd";
import ItemTypes from "./ItemTypes";
import classnames from "classnames";
import { ArrowDropDown, ArrowDropUp, DragIndicator } from "@material-ui/icons";
import { Typography, Icon, Popover } from '@material-ui/core';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import TimelineSummary from './Summary';

const style = {
  margin: "5px",
  cursor: "move"
};

const TimelineMeasurement = ({ scales, measurement, index, id, moveMeasurement, classes, openHistory, mode, translate, beforeSummary, nextSummary, changeScale }) => {
    const ref = useRef(null);
    const [fullView, setFullView] = useState(false);
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
    const renderDistribution = (measurement) => {
        const decimalCount = measurement.decimalCount;
        const style = measurement.distribution.trend === "Lower" ? { color: '#F44335' } : { color: '#4D9A51' };
        return (
            <div style={{width:'150px',height:'100%',margin:'5px'}}>
                <div style={{fontSize:'12pt',marginBottom:'3px'}}>{translate(measurement.distribution.trend)}</div>
                <div className="layout horizontal" style={{marginBottom:'3px'}}>
                    <Typography style={style} className={classnames(classes.value, mode === "Summary" ? classes.summaryValue : classes.detailValue)}>{Highcharts.numberFormat(measurement.distribution.value,decimalCount,',', '.')}</Typography>
                    <Typography style={{fontSize:'9pt'}} className={classes.unit}>%</Typography>
                </div>
                <div style={{fontSize:'10pt'}}>{translate("of the time")}</div>
            </div>
        );
    };
    const renderSummaries = (measurement) => {
        return (
            <div style={{position:"relative"}}>
                {renderScales()}
                {renderSummary(measurement.summary)}
            </div>
        );
    };
    const renderScales = () => {
        return (<div style={{position:'absolute',right:0,marginTop:'10px',marginRight:'20px'}}>{scales.map((s, idx) => renderScale(s, idx==scales.length-1))}</div>);
    };
    const renderScale = (scale, lastScale) => {
        const style = lastScale ? { border:'1px solid #888' } : { border:'1px solid #888',borderRight:'0' };
        const classNames = scale === measurement.summary.scale ? classnames(classes.scale, classes.selectedScale) : classes.scale;
        return (<a onClick={(e) => { e.stopPropagation(); changeScale(scale)} } style={style} className={classNames}>{scale.substring(0,1)}</a>);
    };
    const renderSummary = (summary) => {
        return (<TimelineSummary
            summary={summary} width={300} translate={translate}
            unit={measurement.unit} decimalCount={measurement.decimalCount}
            beforeSummary={beforeSummary} nextSummary={nextSummary}
        />);
    };
    const renderSerie = (measurement) => {
        return (
            <div style={{width:'150px',height:'100%',margin:'5px'}}>
                <div style={{paddingLeft:'5px',fontSize:'12pt'}}>{measurement.serie.name}</div>
                {measurement.serie.values.length > 0 && <HighchartsReact ref={chart} highcharts={Highcharts} options={serieOptions(measurement)} />}
                {measurement.serie.values.length == 0 && <div>No data</div>}
            </div>
        );
    };
    const renderDetail = (measurement) => {
        return (
            <div className="layout horizontal flexible wrap" style={{padding:'5px'}}>
                {renderDistribution(measurement)}
                {renderSummaries(measurement)}
                {renderSerie(measurement)}
            </div>
        );
    };
    const renderDialog = (measurement, anchorRef) => {
        return (
            <Popover
                style={{boxShadow:'none'}}
                anchorEl={anchorRef.current} open={fullView} onClose={() => setFullView(false)}
                anchorOrigin={{vertical: 'bottom',horizontal: 'left'}}>
                {renderDetail(measurement)}
            </Popover>
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
    const serieOptions = (measurement) => {
        const decimalCount = measurement.decimalCount;
        const serie = measurement.serie;
        const height = 80;
        const width = 150;
        const unit = measurement.unit;
        const positioner = summaryPositioner.bind(this, chart);
        return {
            chart: { type: 'spline', height: height, width: width, backgroundColor: 'transparent' },
            title: { text: '' },
            legend: { enabled: false },
            credits: { enabled: false },
            xAxis: { visible: false, type: 'datetime', labels: { overflow: 'justify' }, categories: serie.categories, },
            yAxis: { visible: false, title: { text: '' }, minorGridLineWidth: 0, gridLineWidth: 0, alternateGridColor: null, },
            tooltip: {
                enabled: true,
                positioner: positioner,
                formatter: function() {
                    return '<div style="font-size:6pt;">' + Highcharts.numberFormat(this.y,decimalCount,',', '.') + (unit != null ? unit : "") + '  ' + this.x + "</div>";
                }
            },
            plotOptions: { spline: { lineWidth: 1, states: { hover: { lineWidth: 2 } }, marker: { enabled: false } } },
            series: [{
                name: '',
                data: serie.values,
                cursor: 'pointer',
                point: { events: { click: function(e) { openHistory(); } } },
            }],
            navigation: { menuItemStyle: { fontSize: '10px' } }
        };
    };
    const renderMeasurement = (measurement, mode) => {
        const decimalCount = measurement.decimalCount;
        const style = fullView ? { backgroundColor: 'white', border: '1px dashed #999' } : { border: '1px solid transparent' };
        const width = mode === "Catalog" ? "250px" : "150px";
        return (
            <div style={{width:width,...style}} className={classnames("layout vertical center", classes.measurement, mode === "Summary" ? classes.summaryMeasurement : classes.detailMeasurement)}>
                <div className="layout horizontal" >
                    <Typography className={classnames(classes.value, mode === "Summary" ? classes.summaryValue : classes.detailValue)}>{Highcharts.numberFormat(measurement.value,decimalCount,',', '.')}</Typography>
                    <div className="layout vertical center" style={{position:'relative', height:'24px', marginLeft: '8px'}} >
                        <Typography className={classnames(classes.unit, mode === "Summary" ? classes.summaryUnit : classes.detailUnit)}>{measurement.unit}</Typography>
                        <div>
                            {increased && <ArrowDropUp className={classnames(trendClass, classes.increased)}/>}
                            {decreased && <ArrowDropDown className={classnames(trendClass, classes.decreased)}/>}
                        </div>
                    </div>
                </div>
                <Typography variant="body2">{measurement.label}</Typography>
            </div>
        );
    };
    const opacity = isDragging ? 0 : 1;
    const increased = measurement.trend === "Increased";
    const decreased = measurement.trend === "Decreased";
    const trendClass = mode === "Summary" ? classes.summaryTrend : classes.detailTrend;
    drag(drop(ref));
    return (
        <div ref={ref} style={{ ...style, opacity, position: 'relative' }} data-handler-id={handlerId} onClick={() => setFullView(!fullView)} onMouseLeave={() => setFullView(false)}>
            {mode === "Catalog" && <Icon style={{position:'absolute',zIndex:1}}><DragIndicator/></Icon>}
            <div className="layout horizontal center">
                {renderMeasurement(measurement, mode)}
                {mode === "Summary" && renderDialog(measurement, ref)}
                {mode === "Catalog" && renderDetail(measurement, ref)}
            </div>
        </div>
    );
};
export default TimelineMeasurement;