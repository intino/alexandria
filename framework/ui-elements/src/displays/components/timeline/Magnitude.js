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
  margin: "5px 0",
  cursor: "move"
};

const TimelineMagnitude = ({ magnitude, index, id, moveMagnitude, classes, openHistory, mode, translate }) => {
    const ref = useRef(null);
    const [fullView, setFullView] = useState(false);
    const chart = createRef();
    const [{ handlerId }, drop] = useDrop({
        accept: ItemTypes.TIMELINE_MAGNITUDE,
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
            moveMagnitude(dragIndex, hoverIndex);
            item.index = hoverIndex;
        }
    });
    const [{ isDragging }, drag] = useDrag({
        item: () => { return { id, index } },
        type: ItemTypes.TIMELINE_MAGNITUDE,
        collect: monitor => ({
          isDragging: monitor.isDragging()
        })
    });
    const renderCustomView = (magnitude) => {
        const view = magnitude.customView;
        if (view == null) return (<React.Fragment/>);
        return (<div dangerouslySetInnerHTML={{__html: view}}></div>);
    };
    const renderSummaries = (magnitude) => {
        return (
            <div style={{position:"relative"}}>
                {renderSummary(magnitude.summary)}
            </div>
        );
    };
    const renderSummary = (summary) => {
        return (<TimelineSummary
            evolution={renderSerie(magnitude)}
            summary={summary} width={450} translate={translate}
            unit={magnitude.unit}
        />);
    };
    const renderSerie = (magnitude) => {
        return (
            <div style={{width:'150px',height:'100%',margin:'5px'}}>
                {magnitude.serie.values.length > 0 && <HighchartsReact ref={chart} highcharts={Highcharts} options={serieOptions(magnitude)} />}
                {magnitude.serie.values.length == 0 && <div>No data</div>}
            </div>
        );
    };
    const renderDetail = (magnitude) => {
        return (
            <div className="layout horizontal flex wrap end-justified" style={{padding:'5px'}}>
                {renderSummaries(magnitude)}
                {renderCustomView(magnitude)}
            </div>
        );
    };
    const renderDialog = (magnitude, anchorRef) => {
        return (
            <Popover
                style={{boxShadow:'none'}}
                anchorEl={anchorRef.current} open={fullView} onClose={() => setFullView(false)}
                anchorOrigin={{vertical: 'bottom',horizontal: 'left'}}>
                <div style={{marginLeft:'10px'}}>
                    {renderDetail(magnitude)}
                </div>
            </Popover>
        );
    };
    const annotationLabel = (annotation) => {
        let result = "";
        annotation.entries.forEach(a => result += result.length < 200 ? "<span style='color:" + annotation.color + "'>" + a + "</span><br/>" : result.endsWith("...") ? "" : "...");
        return "<div>" + result + "</div>";
    };
    const serieOptions = (magnitude) => {
        const serie = magnitude.serie;
        const height = 80;
        const width = 150;
        const unit = magnitude.percentage != null ? "%" : magnitude.unit;
        const data = valuesWithAnnotations(serie);
        return {
            chart: { type: 'spline', height: height, width: width, backgroundColor: 'transparent' },
            title: { text: '' },
            legend: { enabled: false },
            credits: { enabled: false },
            xAxis: { visible: false, type: 'datetime', labels: { overflow: 'justify' }, categories: serie.categories, },
            yAxis: { visible: false, title: { text: '' }, minorGridLineWidth: 0, gridLineWidth: 0, alternateGridColor: null, },
            tooltip: {
                enabled: true,
                outside: true,
                formatter: function() {
                    const annotation = serie.annotations[this.point.index] != null ? "<div>" + annotationLabel(serie.annotations[this.point.index]) + "</div><br/>" : "";
                    return annotation + '<div style="font-size:6pt;">' + serie.formattedValues[this.point.index] + (unit != null ? unit : "") + '  ' + this.x + "</div>";
                },
            },
            plotOptions: { spline: { lineWidth: 1, states: { hover: { lineWidth: 2 } }, marker: { enabled: false } } },
            series: [{
                name: '',
                data: data,
                cursor: 'pointer',
                point: { events: { click: function(e) { openHistory(); } } },
            }],
            annotations: [{
                labelOptions: { crop: false, overflow: 'allow' },
            }],
            navigation: { menuItemStyle: { fontSize: '10px' } }
        };
    };
    const hasAnnotations = (serie) => {
        for (let i=0; i<serie.annotations.length; i++) {
            if (serie.annotations[i] != null) return true;
        }
        return false;
    };
    const valuesWithAnnotations = (serie) => {
        const result = [];
        for (let i=0; i<serie.annotations.length; i++) {
            const annotation = serie.annotations[i];
            const value = serie.values[i];
            result.push(annotation != null ? { y: value, marker: { symbol: annotation.symbol, fillColor: annotation.color, enabled: true }, text: annotation.entries.join("; ") } : { y: value, marker: null });
        }
        return result;
    };
    const renderValue = (magnitude, mode) => {
        return mode === "Catalog" ? renderCatalogValue(magnitude) : renderSummaryValue(magnitude);
    };
    const renderRange = (magnitude) => {
        if (magnitude.min == null && magnitude.max == null) return (<React.Fragment/>);
        return (
            <React.Fragment>
                {magnitude.min != null && <div className="layout horizontal center"><Typography className={classes.infoValue}>({magnitude.formattedMin}</Typography><Typography className={classes.infoUnit}>{magnitude.unit}</Typography></div>}
                {magnitude.min == null && <div className="layout horizontal center"><Typography className={classes.infoValue}>(-</Typography><Typography style={{color:'#777'}} className={classes.valueInfo}></Typography></div>}
                {magnitude.max != null && <div className="layout horizontal center"><Typography className={classes.infoValue}>{translate("to")} {magnitude.formattedMax}</Typography><Typography className={classes.infoUnit}>{magnitude.unit})</Typography></div>}
                {magnitude.max == null && <div className="layout horizontal center"><Typography className={classes.infoValue}>{translate("to")} -)</Typography></div>}
            </React.Fragment>
        );
    };
    const renderStatus = (magnitude) => {
        const baseStyle = { position:'absolute',width:'16px',height:'16px',border:'3px solid black',borderRadius:'12px',left:'0',marginLeft:'-2px',marginTop:'40px' };
        const visibility = magnitude.status == null ? "hidden" : "visible";
        const color = magnitude.status === "Normal" ? "#4D9A51" : (magnitude.status === "Warning" ? "#F68A1C" : "#D74545");
        return (<div style={{...baseStyle,backgroundColor:color,visibility:visibility}}></div>);
    };
    const renderCatalogValue = (magnitude) => {
        return (
            <div style={{width:"240px",position:'relative'}} className={classnames("layout vertical", classes.magnitude, classes.catalogMagnitude)}>
                <div className="layout horizontal center">
                    <Icon style={{marginLeft:'-4px'}}><DragIndicator/></Icon>
                    <div style={{paddingLeft:'5px',fontSize:'12pt',marginTop:'3px'}}>{magnitude.label}</div>
                </div>
                {renderStatus(magnitude)}
                <div className="layout horizontal" style={{marginLeft:'20px',marginTop:'10px'}}>
                    <Typography className={classnames(classes.value, classes.catalogValue)}>{magnitude.percentage != null ? magnitude.percentage : magnitude.formattedValue}</Typography>
                    <div className="layout vertical center" style={{position:'relative', height:'24px', marginLeft: '5px'}} >
                        <Typography className={classnames(classes.unit, classes.catalogUnit)}>{magnitude.percentage != null ? "%" : magnitude.unit}</Typography>
                    </div>
                </div>
                <div className="layout horizontal wrap" style={{marginLeft:'22px'}}>
                    {magnitude.percentage != null && <div className="layout horizontal center"><Typography className={classes.infoValue}>{magnitude.formattedValue}</Typography><Typography className={classes.infoUnit}>{magnitude.percentage != null ? magnitude.unit : "%"}</Typography></div>}
                    {renderRange(magnitude)}
                </div>
            </div>
        );
    };
    const renderSummaryValue = (magnitude) => {
        const style = fullView ? { backgroundColor: 'white', border: '1px dashed #999' } : { border: '1px solid transparent' };
        return (
            <div style={{width:"150px",...style}} className={classnames("layout vertical", classes.magnitude, classes.summaryMagnitude)}>
                <Typography variant="body2" title={magnitude.label} style={{width:'140px',fontWeight:'bold',textOverflow:'ellipsis',overflow:'hidden',whiteSpace:'nowrap'}}>{magnitude.label}</Typography>
                <div className="layout horizontal" >
                    <Typography className={classnames(classes.value, classes.summaryValue)}>{magnitude.percentage != null ? magnitude.percentage : magnitude.formattedValue}</Typography>
                    <div className="layout vertical center" style={{position:'relative', height:'24px', marginLeft: '5px'}} >
                        <Typography className={classnames(classes.unit, classes.summaryUnit)}>{magnitude.percentage != null ? "%" : magnitude.unit}</Typography>
                    </div>
                </div>
            </div>
        );
    };
    const opacity = isDragging ? 0 : 1;
    const increased = magnitude.trend === "Increased";
    const decreased = magnitude.trend === "Decreased";
    const trendClass = mode === "Summary" ? classes.summaryTrend : classes.catalogTrend;
    drag(drop(ref));
    return (
        <div ref={ref} style={{ ...style, opacity, position: 'relative' }} data-handler-id={handlerId} onClick={() => setFullView(!fullView)} onMouseLeave={() => setFullView(false)}>
            <div className="layout horizontal">
                {renderValue(magnitude, mode)}
                {mode === "Summary" && renderDialog(magnitude, ref)}
                {mode === "Catalog" && renderDetail(magnitude, ref)}
            </div>
        </div>
    );
};
export default TimelineMagnitude;