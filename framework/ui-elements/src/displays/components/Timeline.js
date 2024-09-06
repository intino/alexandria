import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTimeline from "../../../gen/displays/components/AbstractTimeline";
import TimelineNotifier from "../../../gen/displays/notifiers/TimelineNotifier";
import TimelineRequester from "../../../gen/displays/requesters/TimelineRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { ArrowDropDown, ArrowDropUp, Close, MoreHoriz } from "@material-ui/icons";
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
import TimelineMagnitude from './timeline/magnitude'
import Theme from "app-elements/gen/Theme";
import 'alexandria-ui-elements/res/styles/components/timeline/styles.css';

const styles = theme => ({
    magnitude : { position:'relative', padding:'5px 0', marginBottom: '1px' },
    scale : { cursor:'pointer',padding:'0 4px' },
    selectedScale : { backgroundColor: theme.palette.primary.main, color: 'white' },
    summaryMagnitude : { minWidth:'60px', padding: '5px 15px 5px 5px' },
    catalogMagnitude : { minWidth:'180px' },
    value : { marginRight: '2px', fontSize: '35pt', lineHeight: 1},
    catalogValue : { fontSize: '25pt' },
    summaryValue : { fontSize: '16pt' },
    unit : { marginTop: '1px', fontSize: '14pt', color: theme.palette.grey.A700 },
    infoUnit : { color: '#555', marginRight: '5px', marginLeft: '1px', fontSize: '8pt' },
    infoValue : { color:'#555', fontSize:'9pt' },
    catalogUnit : { fontSize: '12pt' },
    summaryUnit : { fontSize: '9pt', marginTop: '-1px' },
    catalogTrend : { position: 'absolute', left: '0', top:'0px', marginLeft: '-2px', marginTop: '15px', width:'30px', height:'30px' },
    summaryTrend : { position: 'absolute', left: '0', bottom:'0px', marginLeft: '-4px', marginBottom: '-6px', width:'24px', height:'24px' },
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
		    inside : false,
		    openConfiguration : false,
		    history : { visible: true, from: null, to: null, data: [], relativeValues: { visible: false, active: false } },
		    magnitude : null,
		    scales: [],
		    magnitudes: [],
		    magnitudesVisibility : {},
		    magnitudesSorting : {},
		}
	};

	static SlideTransition = React.forwardRef(function Transition(props, ref) {
        return <Slide direction="up" ref={ref} {...props} />;
    });

    setup = (info) => {
        const magnitudesVisibility = this.getCookie(info.name + "_visibility") != null ? this.getCookie(info.name + "_visibility") : this.state.magnitudesVisibility;
        const magnitudesSorting = this.getCookie(info.name + "_sorting") != null ? this.getCookie(info.name + "_sorting") : this.state.magnitudesSorting;
        this.setState({ scales: info.scales, magnitudes: info.magnitudes, magnitudesVisibility: magnitudesVisibility, magnitudesSorting: magnitudesSorting, sourceName: info.name });
    };

    refreshMagnitudesVisibility = (visibility) => {
        const newVisibility = {};
        for (let i=0; i<visibility.length; i++) newVisibility[visibility[i].name] = visibility[i].visible;
        this.setState({magnitudesVisibility: newVisibility});
    };

    refreshMagnitudesSorting = (sorting) => {
        const newSorting = {};
        for (let i=0; i<sorting.length; i++) newSorting[sorting[i].name] = sorting[i].position;
        this.setState({magnitudesSorting: newSorting});
    };

    refreshMagnitudes = (magnitudes) => {
        this.setState({magnitudes});
    };

    showHistoryDialog = (history) => {
        this.setState({history: { visible:true, from: history.from, to: history.to, data: [], relativeValues: { visible: history.hasRelativeValues, active: this.state.history.relativeValues.active } }});
    };

    render() {
        if (!this.state.visible) return (<React.Fragment/>);
        const layoutClassNames = this.props.mode === "Catalog" ? "layout vertical wrap" : "layout horizontal wrap";
        return (
            <DndProvider backend={HTML5Backend}>
                <div className={layoutClassNames} onMouseEnter={this.handleMouseEnter.bind(this)} onMouseLeave={this.handleMouseLeave.bind(this)}>
                    {this.renderMagnitudes()}
                    {this.renderHistoryDialog()}
                    {this.renderConfigurationDialog()}
                    {this.renderCookieConsent()}
                </div>
            </DndProvider>
        );
    };

    handleMouseEnter = () => {
        this.setState({inside: true});
    };

    handleMouseLeave = () => {
        this.setState({inside: false});
    };

    renderMagnitudes = () => {
        return this.sortedAndVisibleMagnitudes().map((m, idx) => this.renderMagnitude(m, idx));
    };

    sortedAndVisibleMagnitudes = () => {
        const magnitudes = this.state.magnitudes;
        const sorting = this.state.magnitudesSorting;
        if (sorting != null) magnitudes.sort(this.sortingComparator);
        return magnitudes.filter(m => this.isMagnitudeVisible(m));
    };

    sortingComparator = (m1, m2) => {
        const m1Position = this.state.magnitudesSorting[m1.name] != null ? this.state.magnitudesSorting[m1.name] : 0;
        const m2Position = this.state.magnitudesSorting[m2.name] != null ? this.state.magnitudesSorting[m2.name] : 0;
        if (m1Position < m2Position ) return -1;
        if (m1Position > m2Position ) return 1;
        return 0;
    }

    renderHistoryDialog = () => {
        if (this.state.magnitude == null || !this.state.history.visible) return (<React.Fragment/>);
        const { classes } = this.props;
        const hasHistoryData = this.state.history.data.length > 0;
        window.setTimeout(() => {
            if (hasHistoryData) return;
            this.requester.fetch({ magnitude: this.state.magnitude.name, start: this.state.history.from, end: this.state.history.to })
        }, 100);
        return (
            <Dialog fullScreen={true} TransitionComponent={Timeline.SlideTransition} open={this.state.history.visible} onClose={this.handleCloseHistoryDialog.bind(this)}>
                <AppBar className={classes.dialogHeader}>
                    <div className="layout horizontal flex center">
                        <Typography variant="h5">{this.state.magnitude.label}</Typography>
                        <div className="layout horizontal end-justified flex"><IconButton onClick={this.handleCloseHistoryDialog.bind(this)} className={classes.icon}><Close fontSize="large"/></IconButton></div>
                    </div>
                </AppBar>
                <DialogContent style={{marginTop:'60px'}}>
                    <div style={{width:'100%',height:'100%'}} ref={this.historyContainer}>
                        {!hasHistoryData && <div className="layout vertical flex center-center" style={{marginTop:'10%'}}><Typography variant="h5">{this.translate("Not enough data yet")}</Typography></div>}
                        {hasHistoryData && this.renderHistory(this.state.history, this.state.magnitude)}
                    </div>
                </DialogContent>
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

    renderMagnitude = (magnitude, idx) => {
        return (<TimelineMagnitude style={{margin:'5px'}}
                                     magnitude={magnitude}
                                     key={this.props.id + magnitude.name}
                                     index={idx}
                                     id={magnitude.name}
                                     mode={this.props.mode}
                                     classes={this.props.classes}
                                     openHistory={this.openHistory.bind(this, magnitude)}
                                     translate={this.translate.bind(this)}
                                     moveMagnitude={this.moveMagnitude.bind(this)}
        />);
    };

    renderHistory = (history, magnitude) => {
        return (
            <div>
                {this.state.history.relativeValues.visible &&
                    <div className="layout horizontal end-justified">
                        <FormControlLabel control={<Checkbox checked={this.state.history.relativeValues.active} onChange={this.handleToggleRelativeValues.bind(this)} name="toggleRelativeValues" color="primary"/>} label={this.translate("Relative values")}/>
                    </div>
                }
                <HighchartsReact highcharts={Highcharts} constructorType={'stockChart'} options={this.historyOptions(history, magnitude)} />
            </div>
        );
    };

    historyOptions = (history, magnitude) => {
        const height = this.historyHeight();
        const width = this.historyWidth();
        const unit = this.state.history.relativeValues.active ? "%" : magnitude.unit;
        const data = history.data;
        const annotations = history.annotations;
        const fetch = this.fetch.bind(this, magnitude, this.translate.bind(this));
        const translate = this.translate.bind(this);
        const minRange = this.minRange(magnitude);
        const formatDate = this.formatDate.bind(this, magnitude, translate);
        if (data.length <= 0) data.push([new Date(history.to), null]);
        return {
            chart: {
                type: 'spline', zoomType: 'x', height: height, width: width, backgroundColor: 'transparent'
            },

            tooltip: {
                enabled: true,
                formatter: function() {
                    if (this.points == null || this.points[0] == null) return;
                    const index = this.points[0].point.index;
                    let annotation = null;
                    for (let i=0; i<annotations.length; i++) {
                        if (annotations[i].idx == index) {
                            annotation = annotations[i];
                            break;
                        }
                    }
                    const annotationText = annotation != null ? "<div style='color:" + annotation.color + "'>" + annotation.text + "</div><br/>" : "";
                    return annotationText + '<b>' + data[index][2] + (unit != null ? " " + unit : "") + '</b><br/>' + formatDate(this.x);
                },
                shared: true
            },

            navigator: {
                adaptToUpdatedData: false,
                series: [
                    { data: data, id: 'dataseries_nav' },
                    { type: 'flags', data: annotations, onSeries: 'dataseries_nav', shape: 'circlepin', width: 16 },
                ],
                xAxis: {
                    min: history.from,
                    max: history.to,
                },
            },

            scrollbar: { liveRedraw: false },
            title: { text: '' },
            rangeSelector: {
                buttons: [
                    { type: 'hour', count: 24, text: 'H' },
                    { type: 'day', count: 7, text: 'D' },
                    { type: 'week', count: 4, text: 'W' },
                    { type: 'month', count: 12, text: 'M' },
                    { type: 'year', count: 10, text: 'Y'},
                ],
                inputEnabled: false, // it supports only days
                selected: this.selectionIndex(magnitude),
                enabled: false,
            },

            xAxis: {
                events: { afterSetExtremes: fetch },
                minRange: minRange,
                min: history.from,
                max: history.to,
            },

            yAxis: { floor: this.minValue(history, magnitude), min: this.minValue(history, magnitude), max: this.maxValue(history, magnitude) },
            series: [
                { data: data, dataGrouping: { enabled: false }, id: 'dataseries' },
                { type: 'flags', data: annotations, onSeries: 'dataseries', shape: 'circlepin', width: 16 },
            ]
        };
    };

    minRange = (magnitude) => {
        const scale = this.state.scales.length > 0 ? this.state.scales[0] : null;
        if (scale == "Hour") return 3600 * 1000;
        else if (scale == "Day") return 3600 * 1000 * 24;
        else if (scale == "Week") return 3600 * 1000 * 24 * 7;
        else if (scale == "Month") return 3600 * 1000 * 24 * 7 * 30;
        else if (scale == "Year") return 3600 * 1000 * 24 * 7 * 30 * 12;
        return 3600 * 1000;
    };

    selectionIndex = (magnitude) => {
        const scale = magnitude.summary.scale;
        if (scale == "Hour") return 0;
        else if (scale == "Day") return 1;
        else if (scale == "Week") return 2;
        else if (scale == "Month") return 3;
        else if (scale == "Year") return 4;
        return 1;
    };

    formatDate = (magnitude, translate, value) => {
        const scale = magnitude.summary.scale;
        let format = '%Y/%m/%d %H:%M';
        if (scale == "Day") format = '%Y/%m/%d';
        else if (scale == "Week") format = '%Y/%m/%d';
        else if (scale == "Month") format = '%Y/%m';
        else if (scale == "Year") format = '%Y';
        return Highcharts.dateFormat(translate(format), value);
    };

    openHistory = (magnitude) => {
        this.setState({magnitude: magnitude, history: { visible: true, from: null, to: null, data: [], relativeValues: { active: false} } });
        this.requester.openHistory(magnitude.name);
    };

	fetch = (magnitude, translate, e) => {
        this.chart = e.target;
        this.requester.fetch({ magnitude: magnitude.name, start: e.min, end: e.max });
	};

	minValue = (history, magnitude) => {
	    if (this.state.history.relativeValues.active) return 0;
	    const unit = magnitude.unit;
	    if (unit == "ºC") return -50;
	    return magnitude.min != null ? magnitude.min : 0;
	};

	maxValue = (history, magnitude) => {
	    if (this.state.history.relativeValues.active) return 100;
	    return magnitude.max != null ? magnitude.max : null;
	};

	refreshHistory = (dataObjects) => {
	    const history = this.state.history;
	    this.refreshHistoryData(history, dataObjects);
	    this.refreshHistoryAnnotations(history, dataObjects);
	    this.setState({history: history});
	};

	refreshHistoryData = (history, dataObjects) => {
	    history.data = [];
	    for (let i=0; i<dataObjects.length; i++) {
	        const entry = dataObjects[i];
	        if (history.from > entry.date) history.from = entry.date;
	        history.data.push([entry.date, entry.value != null ? parseFloat(entry.value) : null, entry.formattedValue, entry.annotation]);
        }
	};

	refreshHistoryAnnotations = (history, dataObjects) => {
	    history.annotations = [];
	    for (let i=0; i<dataObjects.length; i++) {
	        const entry = dataObjects[i];
	        if (entry.annotation == null) continue;
	        history.annotations.push({ x: entry.date, title: history.annotations.length+1, idx: i, text: this.annotationLabel(entry.annotation), color: entry.annotation.color });
        }
	};

    annotationLabel = (annotation) => {
        let result = "";
        annotation.entries.forEach(a => result += "<span style='color:" + annotation.color + "'>" + a + "</span><br/>");
        return "<div>" + result + "</div>";
    };

	refreshHistoryToolbar = (toolbar) => {
	    this.setState({toolbar});
	};

	historyHeight = () => {
	    const result = this.historyContainer.current != null ? this.historyContainer.current.offsetHeight : "400";
	    if (this.historyContainer.current != null) this.containerHeight = result;
	    return result - (this.state.history.relativeValues.visible ? 40 : 0);
	};

	historyWidth = () => {
	    const result = this.historyContainer.current != null ? this.historyContainer.current.offsetWidth : "600";
	    if (this.historyContainer.current != null) this.containerWidth = result;
	    return result;
	};

	moveMagnitude = (dragIndex, hoverIndex) => {
        const magnitudes = this.state.magnitudes;
        var magnitude = magnitudes[dragIndex];
        magnitudes.splice(dragIndex, 1);
        magnitudes.splice(hoverIndex, 0, magnitude);
        this.setState({magnitudes: magnitudes, magnitudesSorting: this.saveMagnitudesSorting()});
    };

    renderConfigurationDialog = () => {
        const { classes } = this.props;
        const theme = Theme.get();
        const hasMagnitudes = this.state.magnitudes.length > 0;
        const color = this.state.inside ? theme.palette.primary.main : "transparent";
        return (
            <div className="layout horizontal center">
                {(false && hasMagnitudes && this.props.mode === "Summary") && <div className="layout horizontal start"><IconButton onClick={this.openConfigurationDialog.bind(this)} size="small"><MoreHoriz style={{color:color}}/></IconButton></div>}
                <Dialog open={this.state.openConfiguration} onClose={this.handleCloseConfigurationDialog.bind(this)}>
                    <DialogTitle id="alert-dialog-title">{this.translate("Magnitudes")}</DialogTitle>
                    <DialogContent>
                      <DialogContentText id="alert-dialog-description">
                        {this.renderMagnitudesVisibility()}
                      </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                      <Button onClick={this.handleCloseConfigurationDialog.bind(this)} color="primary" autoFocus>{this.translate("Close")}</Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    };

    renderMagnitudesVisibility = () => {
        return this.state.magnitudes.map((m, index) => this.renderMagnitudeVisibility(m, index));
    };

    renderMagnitudeVisibility = (magnitude, index) => {
        return (
            <div>
                <FormControlLabel control={<Checkbox checked={this.isMagnitudeVisible(magnitude)}
                                  onChange={this.handleToggleMagnitudeVisibility.bind(this, magnitude)}
                                  color="primary" name={magnitude.name}/>} label={magnitude.label}/>
            </div>
        );
    };

    isMagnitudeVisible = (magnitude) => {
        const name = magnitude.name;
        const visibility = this.state.magnitudesVisibility;
        return visibility[name] == null || visibility[name] === true;
    };

    handleToggleMagnitudeVisibility = (magnitude) => {
        const magnitudes = this.state.magnitudes;
        const name = magnitude.name;
        if (this.state.magnitudesVisibility[name] == null) this.state.magnitudesVisibility[name] = true;
        this.state.magnitudesVisibility[name] = !this.state.magnitudesVisibility[name];
        this.updateCookie(this.state.magnitudesVisibility, this.state.sourceName + "_visibility");
        this.setState({magnitudesVisibility: this.state.magnitudesVisibility});
        const list = [];
        for (var i=0; i<magnitudes.length; i++) list.push({ name: magnitudes[i].name, visible: this.state.magnitudesVisibility[magnitudes[i].name] != null ? this.state.magnitudesVisibility[magnitudes[i].name] : true });
        this.requester.magnitudesVisibility(list);
    };

    saveMagnitudesSorting = () => {
        const magnitudes = this.state.magnitudes;
        const magnitudesSorting = {};
        for (var i=0; i<magnitudes.length; i++) magnitudesSorting[magnitudes[i].name] = i;
        this.updateCookie(magnitudesSorting, this.state.sourceName + "_sorting");
        const list = [];
        for (var i=0; i<magnitudes.length; i++) list.push({ name: magnitudes[i].name, position: i });
        this.requester.magnitudesSorting(list);
        return magnitudesSorting;
    };

    handleToggleRelativeValues = () => {
        const history = this.state.history;
        history.relativeValues.active = !history.relativeValues.active;
        this.requester.historyWithRelativeValues(history.relativeValues.active);
        this.setState({history});
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Timeline));
DisplayFactory.register("Timeline", withStyles(styles, { withTheme: true })(withSnackbar(Timeline)));