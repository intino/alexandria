import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTimeline from "../../../gen/displays/components/AbstractTimeline";
import TimelineNotifier from "../../../gen/displays/notifiers/TimelineNotifier";
import TimelineRequester from "../../../gen/displays/requesters/TimelineRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { ArrowDropDown, ArrowDropUp, Close, Settings } from "@material-ui/icons";
import { Typography, Dialog, DialogActions, DialogContent, DialogContentText,
         DialogTitle, Button, Slide, IconButton, AppBar, FormControlLabel, Checkbox } from '@material-ui/core';
import { withSnackbar } from 'notistack';
import classnames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import Highcharts from 'highcharts/highstock';
import HighchartsReact from 'highcharts-react-official';
import Delayer from 'alexandria-ui-elements/src/util/Delayer';
import { DndProvider } from 'react-dnd'
import { HTML5Backend } from 'react-dnd-html5-backend'
import TimelineMeasurement from './timeline/measurement'

const styles = theme => ({
    measurement : { position:'relative', borderRadius:'3px', minWidth:'150px', padding:'5px', background:'white', margin: '5px 5px' },
    value : { marginRight: '2px', fontSize: '35pt', lineHeight: 1},
    unit : { marginTop: '3px', fontSize: '14pt', color: theme.palette.grey.A700, paddingLeft: '5px' },
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
		    openConfiguration : false,
		    history : { visible: true, from: null, to: null, data: [] },
		    measurement : null,
		    measurements: [],
		    measurementsVisibility : {},
		    measurementsSorting : {},
		}
	};

	static SlideTransition = React.forwardRef(function Transition(props, ref) {
        return <Slide direction="up" ref={ref} {...props} />;
    });

    setup = (info) => {
        const measurementsVisibility = this.getCookie(info.name + "_visibility") != null ? this.getCookie(info.name + "_visibility") : this.state.measurementsVisibility;
        const measurementsSorting = this.getCookie(info.name + "_sorting") != null ? this.getCookie(info.name + "_sorting") : this.state.measurementsSorting;
        this.setState({ measurements: info.measurements, measurementsVisibility: measurementsVisibility, measurementsSorting: measurementsSorting, sourceName: info.name });
    };

    refreshMeasurementsVisibility = (visibility) => {
        this.setState({measurementsVisibility: visibility});
    };

    refreshMeasurementsSorting = (sorting) => {
        this.setState({measurementsSorting: sorting});
    };

    refreshMeasurementsSorting = (sorting) => {
        this.setState({measurementsSorting: sorting});
    };

    showHistoryDialog = (history) => {
        this.setState({history: { visible:true, from: history.from, to: history.to, data: [] }});
    };

    render() {
        return (
            <DndProvider backend={HTML5Backend}>
                <div className="layout horizontal">
                    {this.renderMeasurements()}
                    {this.renderHistoryDialog()}
                    {this.renderConfigurationDialog()}
                    {this.renderCookieConsent()}
                </div>
            </DndProvider>
        );
    };

    renderMeasurements = () => {
        return this.sortedAndVisibleMeasurements().map((m, idx) => this.renderMeasurement(m, idx));
    };

    sortedAndVisibleMeasurements = () => {
        const measurements = this.state.measurements;
        const sorting = this.state.measurementsSorting;
        if (sorting != null) measurements.sort(this.sortingComparator);
        return measurements.filter(m => this.isMeasurementVisible(m));
    };

    sortingComparator = (m1, m2) => {
        const m1Position = this.state.measurementsSorting[m1.name] != null ? this.state.measurementsSorting[m1.name].position : 0;
        const m2Position = this.state.measurementsSorting[m2.name] != null ? this.state.measurementsSorting[m2.name].position : 0;
        if (m1Position < m2Position ) return -1;
        if (m1Position > m2Position ) return 1;
        return 0;
    }

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

    openConfigurationDialog = () => {
        this.setState({openConfiguration: true});
    };

    handleCloseConfigurationDialog = () => {
        this.setState({openConfiguration: false});
    };

    renderMeasurement = (measurement, idx) => {
        return (<TimelineMeasurement style={{margin:'5px'}}
                                     measurement={measurement}
                                     key={measurement.name}
                                     index={idx}
                                     id={measurement.name}
                                     classes={this.props.classes}
                                     openHistory={this.openHistory.bind(this, measurement)}
                                     moveMeasurement={this.moveMeasurement.bind(this)}
        />);
    };

    renderHistory = (history, measurement) => {
        return (
            <div style={{width:'100%',height:'100%'}} ref={this.historyContainer}>
                <HighchartsReact highcharts={Highcharts} constructorType={'stockChart'} options={this.historyOptions(history, measurement)} />
            </div>
        );
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
	};

	historyWidth = () => {
	    if (this.containerWidth != null) return this.containerWidth;
	    const result = this.historyContainer.current != null ? this.historyContainer.current.offsetWidth : "600";
	    if (this.historyContainer.current != null) this.containerWidth = result;
	    return result;
	};

	moveMeasurement = (dragIndex, hoverIndex) => {
        const measurements = this.state.measurements;
        var measurement = measurements[dragIndex];
        measurements.splice(dragIndex, 1);
        measurements.splice(hoverIndex, 0, measurement);
        this.setState({measurements: measurements, measurementsSorting: this.saveMeasurementsSorting()});
    };

    renderConfigurationDialog = () => {
        const { classes } = this.props;
        return (
            <React.Fragment>
                <div className="layout horizontal start"><IconButton onClick={this.openConfigurationDialog.bind(this)}><Settings/></IconButton></div>
                <Dialog open={this.state.openConfiguration} onClose={this.handleCloseConfigurationDialog.bind(this)}>
                    <DialogTitle id="alert-dialog-title">{this.translate("Measurements")}</DialogTitle>
                    <DialogContent>
                      <DialogContentText id="alert-dialog-description">
                        {this.renderMeasurementsVisibility()}
                      </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                      <Button onClick={this.handleCloseConfigurationDialog.bind(this)} color="primary" autoFocus>{this.translate("Close")}</Button>
                    </DialogActions>
                </Dialog>
            </React.Fragment>
        );
    };

    renderMeasurementsVisibility = () => {
        return this.state.measurements.map((m, index) => this.renderMeasurementVisibility(m, index));
    };

    renderMeasurementVisibility = (measurement, index) => {
        return (
            <div>
                <FormControlLabel control={<Checkbox checked={this.isMeasurementVisible(measurement)}
                                  onChange={this.handleToggleMeasurementVisibility.bind(this, measurement)}
                                  color="primary" name={measurement.name}/>} label={measurement.label}/>
            </div>
        );
    };

    isMeasurementVisible = (measurement) => {
        const name = measurement.name;
        const visibility = this.state.measurementsVisibility;
        return visibility[name] == null || visibility[name] === true;
    };

    handleToggleMeasurementVisibility = (measurement) => {
        const name = measurement.name;
        if (this.state.measurementsVisibility[name] == null) this.state.measurementsVisibility[name] = true;
        this.state.measurementsVisibility[name] = !this.state.measurementsVisibility[name];
        this.updateCookie(this.state.measurementsVisibility, this.state.sourceName + "_visibility");
        this.setState({measurementsVisibility: this.state.measurementsVisibility});
        this.requester.measurementsVisibility(this.state.measurementsVisibility);
    };

    saveMeasurementsSorting = () => {
        const measurements = this.state.measurements;
        const measurementsSorting = {};
        for (var i=0; i<measurements.length; i++) measurementsSorting[measurements[i].name] = { name: measurements[i].name, position: i };
        this.updateCookie(measurementsSorting, this.state.sourceName + "_sorting");
        this.requester.measurementsSorting(measurementsSorting);
        return measurementsSorting;
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Timeline));
DisplayFactory.register("Timeline", withStyles(styles, { withTheme: true })(withSnackbar(Timeline)));