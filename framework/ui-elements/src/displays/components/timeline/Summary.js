import React from "react";
import classnames from "classnames";
import { Typography, IconButton } from '@material-ui/core';
import { NavigateBefore, NavigateNext } from "@material-ui/icons";
import Theme from "app-elements/gen/Theme";
import Moment from 'react-moment';
import 'moment-timezone';
import Highcharts from 'highcharts';

const TimelineSummaryStyles = {
    container : { height:'100%',margin:'2px 20px 5px 0' },
    label : { fontSize:'12pt',marginRight:'5px' },
    field : { color:'#555', width: '55px' },
    value : { width: 'calc(100% - 55px)' },
    icon : { height:'20px',width:'20px' },
    unit : { color:'#555', marginLeft:'2px' },
    date : { fontSize: '7pt', marginLeft:'3px', color: '#777' },
};

const TimelineSummary = ({ summary, scales, evolution, width, unit, decimalCount, translate, beforeSummary, nextSummary }) => {
    const theme = Theme.get();
    const handleBefore = (e) => {
        e.stopPropagation();
        beforeSummary(summary);
    };
    const handleNext = (e) => {
        e.stopPropagation();
        nextSummary(summary);
    };
    const average = () => {
        return formattedValue(summary.average.value, null);
    };
    const max = () => {
        return formattedValue(summary.max.value, summary.max.date);
    };
    const min = () => {
        return formattedValue(summary.min.value, summary.min.date);
    };
    const formattedValue = (value, date) => {
        const language = window.Application.configuration.language;
        return (
            <div className="layout vertical flex end-justified">
                <div className="layout horizontal end-justified center flex">
                    {date && <div className="layout horizontal start-justified flex" style={{...TimelineSummaryStyles.date,marginRight:'10px'}}><Moment format="DD/MM/YYYY HH:mm" date={date} locale={language}/></div>}
                    <span>{Highcharts.numberFormat(value,decimalCount)}</span>
                    {formattedUnit()}
                </div>
            </div>
        );
    };
    const formattedUnit = () => {
        return (
            <React.Fragment>
                {unit && <span style={TimelineSummaryStyles.unit}>{unit}</span>}
            </React.Fragment>
        );
    };
    const renderIndicator = (label, value) => {
        return (
            <div className="layout horizontal start" style={{marginBottom:'2px',borderBottom:'1px solid #efefef'}}>
                <Typography style={TimelineSummaryStyles.field} variant="body2">{label}</Typography>
                <div style={TimelineSummaryStyles.value}>{value}</div>
            </div>
        );
    };
    const beforeColor = summary.canBefore ? theme.palette.primary.main : theme.palette.grey.A900;
    const nextColor = summary.canNext ? theme.palette.primary.main : theme.palette.grey.A900;
    return (
        <div style={{width:width + "px",...TimelineSummaryStyles.container}}>
            <div className="layout horizontal start">
                <div className="layout horizontal start flex">
                    <div style={{marginRight:'15px',marginTop:'5px'}}>{scales}</div>
                    <div className="layout vertical"><Typography style={TimelineSummaryStyles.label}>{summary.label}</Typography></div>
                    <div className="layout horizontal">
                        <IconButton disabled={!summary.canBefore} onClick={handleBefore} size="small" style={{color:beforeColor}}><NavigateBefore style={TimelineSummaryStyles.icon}/></IconButton>
                        <IconButton disabled={!summary.canNext} onClick={handleNext} size="small" style={{color:nextColor}}><NavigateNext style={TimelineSummaryStyles.icon}/></IconButton>
                    </div>
                </div>
            </div>
            <div className="layout horizontal">
                {evolution}
                <div style={{width:'100%',marginLeft:'15px',marginTop:'10px'}}>
                    {renderIndicator(translate("Average"), average())}
                    {renderIndicator("Max", max())}
                    {renderIndicator("Min", min())}
                </div>
            </div>
        </div>
    );
};
export default TimelineSummary;