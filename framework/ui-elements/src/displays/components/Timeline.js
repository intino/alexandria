import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTimeline from "../../../gen/displays/components/AbstractTimeline";
import TimelineNotifier from "../../../gen/displays/notifiers/TimelineNotifier";
import TimelineRequester from "../../../gen/displays/requesters/TimelineRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { ArrowDropDown, ArrowDropUp, Close } from "@material-ui/icons";
import { Typography, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Button, Slide, IconButton, AppBar } from '@material-ui/core';
import { withSnackbar } from 'notistack';
import classnames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import Highcharts from 'highcharts/highstock';
import HighchartsReact from 'highcharts-react-official';
import Delayer from 'alexandria-ui-elements/src/util/Delayer';

const styles = theme => ({
    measurement : { position:'relative', borderRadius:'3px', minWidth:'150px', padding:'5px', background:'white', margin: '5px 5px' },
    value : { marginRight: '2px', fontSize: '35pt' },
    unit : { marginTop: '15px', fontSize: '14pt', color: theme.palette.grey.A700, paddingLeft: '5px' },
    trend : { position: 'absolute', left: '0', top:'0px', marginLeft: '-16px', marginTop: '-15px', width:'40px', height:'40px' },
    increased : { color: '#F44335' },
    decreased : { color: '#4D9A51' },
    dialogHeader : { padding: "2px 15px", },
    icon : { color: "white" },
});

class Timeline extends AbstractTimeline {

	constructor(props) {
		super(props);
		this.notifier = new TimelineNotifier(this);
		this.requester = new TimelineRequester(this);
		this.historyContainer = React.createRef();
		this.state = {
		    ...this.state,
		    history : { visible: true, from: null, to: null, data: [] },
		    measurement : null,
		    measurements: []
		}
	};

	static SlideTransition = React.forwardRef(function Transition(props, ref) {
        return <Slide direction="up" ref={ref} {...props} />;
    });

    refresh = (measurements) => {
        this.setState({measurements: measurements});
    };

    showHistoryDialog = (history) => {
        this.setState({history: { visible:true, from: history.from, to: history.to, data: [] }});
    };

    render() {
        return (
            <div className="layout horizontal">
                {this.renderMeasurements()}
                {this.renderHistoryDialog()}
            </div>
        );
    };

    renderMeasurements = () => {
        const measurements = this.state.measurements;
        return measurements.map(m => this.renderMeasurement(m));
    };

    renderHistoryDialog = () => {
        if (this.state.measurement == null) return (<React.Fragment/>);
        const { classes } = this.props;
        return (
            <Dialog fullScreen={true} TransitionComponent={Timeline.SlideTransition} open={this.state.history.visible} onClose={this.handleCloseHistoryDialog.bind(this)}>
                <AppBar className={classes.dialogHeader}>
                    <div className="layout horizontal flex center">
                        <Typography variant="h5">{this.state.measurement.label}</Typography>
                        <div className="layout horizontal end-justified flex"><IconButton onClick={this.handleCloseHistoryDialog.bind(this)} className={classes.icon}><Close fontSize="large"/></IconButton></div>
                    </div>
                </AppBar>
                <DialogContent style={{marginTop:'60px'}}>{this.renderHistory(this.state.history, this.state.measurement)}</DialogContent>
            </Dialog>
        );
    };

    handleCloseHistoryDialog = () => {
        const history = this.state.history;
        history.visible = false;
        this.setState({history});
    };

    renderMeasurement = (measurement) => {
        const increased = measurement.trend === "Increased";
        const decreased = measurement.trend === "Decreased";
        const { classes } = this.props;
        return (
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
                <Typography variant="body1">{measurement.label}</Typography>
                {this.renderEvolution(measurement, "Summary")}
            </div>
        );
    };

    renderEvolution = (measurement) => {
        return (
            <div style={{width:'100%',height:'100%'}}>
                <HighchartsReact highcharts={Highcharts} options={this.evolutionOptions(measurement)} />
            </div>
        );
    };

    renderHistory = (history, measurement) => {
        return (
            <div style={{width:'100%',height:'100%'}} ref={this.historyContainer}>
                <HighchartsReact highcharts={Highcharts} constructorType={'stockChart'} options={this.historyOptions(history, measurement)} />
            </div>
        );
    };

    evolutionOptions = (measurement) => {
        const decimalCount = measurement.decimalCount;
        const evolution = measurement.evolution;
        const height = 50;
        const width = 150;
        const unit = measurement.unit;
        const positioner = timelineEvolutionDisplaySummaryPositioner;
        const openHistory = this.openHistory.bind(this, measurement);
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

    historyOptions = (history, measurement) => {
        const decimalCount = measurement.decimalCount;
        const height = this.historyHeight();
        const width = this.historyWidth();
        const unit = measurement.unit;
        const data = history.data;
        const fetch = this.fetch.bind(this, measurement, this.translate.bind(this));
        const translate = this.translate.bind(this);
        if (data.length <= 0) data.push([new Date(history.to), null]);
        return {
            chart: {
                type: 'spline', zoomType: 'x', height: height, width: width, backgroundColor: 'transparent'
            },

            tooltip: {
                enabled: true,
                formatter: function() {
                    return '<b>' + Highcharts.numberFormat(this.y,decimalCount) + (unit != null ? " " + unit : "") + '</b><br/>' + Highcharts.dateFormat(translate('%Y/%m/%d %H:%M'), this.x);
                }
            },

            navigator: {
                adaptToUpdatedData: false,
                series: { data: data, },
                xAxis: {
                    min: history.from,
                    max: history.to + (3600 * 1000 * 24), // one day
                },
            },

            scrollbar: { liveRedraw: false },
            title: { text: '' },
            rangeSelector: {
                buttons: [
                    { type: 'hour', count: 1, text: '1h' },
                    { type: 'day', count: 1, text: '1d' },
                    { type: 'month', count: 1, text: '1m' },
                    { type: 'year', count: 1, text: '1y'},
                    { type: 'all', text: this.translate('All')}
                ],
                inputEnabled: false, // it supports only days
                selected: 4 // All
            },

            xAxis: {
                events: { afterSetExtremes: fetch },
                minRange: 3600 * 1000, // one hour
                min: history.from,
                max: history.to,
            },

            yAxis: { floor: 0, min: this.minValue(history, measurement), max: this.maxValue(history, measurement) },
            series: [{ data: data, dataGrouping: { enabled: false } }]
        };
    };

    openHistory = (measurement) => {
        this.setState({measurement: measurement});
        this.requester.openHistory(measurement.name);
    };

	fetch = (measurement, translate, e) => {
	    this.chart = e.target;
	    this.requester.fetch({ measurement: measurement, start: e.min, end: e.max });
	};

	minValue = (history, measurement) => {
	    const unit = measurement.unit;
	    if (unit == "ºC") return -50;
	    return 0;
	};

	maxValue = (history, measurement) => {
	    const unit = measurement.unit;
	    if (unit == "ºC") return 120;
	    else if (unit == "%") return 120;
	    return null;
	};

	historyHeight = () => {
	    if (this.containerHeight != null) return this.containerHeight;
	    const result = this.historyContainer.current != null ? this.historyContainer.current.offsetHeight : "400";
	    if (this.historyContainer.current != null) this.containerHeight = result;
	    return result;
	}

	historyWidth = () => {
	    if (this.containerWidth != null) return this.containerWidth;
	    const result = this.historyContainer.current != null ? this.historyContainer.current.offsetWidth : "600";
	    if (this.historyContainer.current != null) this.containerWidth = result;
	    return result;
	}

}

function timelineEvolutionDisplaySummaryPositioner(labelWidth, labelHeight, point) {
    var chart = this.chart;
    return {
        x: chart.plotLeft + chart.plotSizeX/2 - labelWidth/2,
        y: chart.plotTop + chart.plotSizeY - 4
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Timeline));
DisplayFactory.register("Timeline", withStyles(styles, { withTheme: true })(withSnackbar(Timeline)));