import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractEventline from "../../../gen/displays/components/AbstractEventline";
import EventlineNotifier from "../../../gen/displays/notifiers/EventlineNotifier";
import EventlineRequester from "../../../gen/displays/requesters/EventlineRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { Typography, Paper } from "@material-ui/core";
import { Adjust } from '@material-ui/icons';
import { withSnackbar } from 'notistack';
import EventlineToolbar from './eventline/toolbar';
import classnames from 'classnames';

const styles = theme => ({
    eventHorizontal : { padding:'5px 10px',marginBottom:'10px',minWidth:'200px',marginLeft:'15px' },
    eventVertical : { padding:'5px 10px',marginBottom:'10px',minWidth:'200px' },
    groupBlockHorizontal : { padding:'0 10px 0 10px' },
    groupBlockVertical : { padding:'0 10px 20px 10px' },
    groupIconDashHorizontal : {borderTop:'1px dashed',width:'calc(100% - 18px)',position:'absolute',marginTop:'18px',marginLeft:'18px'},
    groupIconDashVertical : {borderLeft:'1px dashed',height:'100%',marginLeft:'3px'},
});

class Eventline extends AbstractEventline {

	constructor(props) {
		super(props);
		this.notifier = new EventlineNotifier(this);
		this.requester = new EventlineRequester(this);
		this.container = React.createRef();
		this.groups = React.createRef();
		this.resetScroll = true;
		this.lastScrollInfo = null;
		this.state = {
		    ...this.state,
		    arrangement: this.props.arrangement,
		    toolbar: { label: '', canNext: false, canPrevious: false },
		    events: [],
		}
	};

	setup = (info) => {
        this.setState({ label: info.label, toolbar: info.toolbar, events: info.events });
	};

	resetEvents = (events) => {
	    this.resetScroll = true;
	    this.setState({events: []});
	};

	addEventsBefore = (events) => {
	    let currentEvents = this.state.events;
	    events = events.concat(currentEvents);
	    this.resetScroll = true;
	    this.lastScrollInfo = { left: this.groups.current.offsetLeft, left: this.groups.current.offsetTop, width: this.groups.current.scrollWidth, height: this.groups.current.scrollHeight };
	    this.setState({events: events});
	};

	addEventsAfter = (events) => {
	    let currentEvents = this.state.events;
	    currentEvents = currentEvents.concat(events);
	    this.lastScrollInfo = null;
	    this.setState({events: currentEvents});
	};

	refreshToolbar = (toolbar) => {
	    this.setState({toolbar});
	};

    render() {
        if (!this.state.visible) return (<React.Fragment/>);
        const { classes } = this.props;
        const arrangement = this.state.arrangement.toLowerCase();
        const flex = this.state.arrangement.toLowerCase() == "horizontal" ? "flex" : "";
        const width = this.container.current != null ? this.container.current.offsetWidth : 100;
        const widthValue = this.container.current != null ? width + "px" : width + "%";
        const height = this.container.current != null ? this.container.current.offsetHeight : 100;
        const heightValue = this.container.current != null ? height + "px" : height + "%";
        const groupsStyle = this.state.arrangement.toLowerCase() == "horizontal" ? {overflowX:'scroll',width:widthValue} : {overflowY:'scroll',height:((this.groups.current != null && this.groups.current.offsetHeight != null) ? this.groups.current.offsetHeight : heightValue)};
        window.setTimeout(() => {
            if (this.groups.current == null || !this.resetScroll) return;
            this.resetScroll = false;
            const scrollInfo = arrangement == "horizontal" ? {left:this.lastScrollInfo != null ? this.groups.current.scrollLeft+(this.groups.current.scrollWidth-this.lastScrollInfo.width) : 200} : {top:this.lastScrollInfo != null ? this.groups.current.scrollTop+(this.groups.current.scrollHeight-this.lastScrollInfo.height) : 200};
            this.groups.current.scrollTo(scrollInfo);
        }, 40);
        return (
            <div className="layout vertical" style={this.style()} ref={this.container}>
                {this.renderToolbar({fontWeight:'300'})}
                {this.state.events.length == 0 && <Typography variant="body1">{this.translate("No events found")}</Typography>}
                {this.state.events.length > 0 &&
                    <div ref={this.groups} className={"layout " + arrangement + " " + flex} style={groupsStyle} onScroll={this.handleScroll.bind(this, width, height)}>
                        {this.renderBeforeScroller()}
                        {this.renderEventsGroups()}
                    </div>
                }
            </div>
        );
    };

    renderToolbar = (style) => {
        return (
            <EventlineToolbar
                label={{title:this.state.label, style:style}}
                toolbar={this.state.toolbar}
                onFirst={this.first.bind(this)}
                onPrevious={this.previous.bind(this)}
                onNext={this.next.bind(this)}
                onLast={this.last.bind(this)}
                translate={this.translate.bind(this)}
                classes={this.props.classes}
            />
        );
    };

    renderEventsGroups = () => {
        const events = this.state.events;
        return (
            <React.Fragment>
                {events.map((eventGroup, index) => this.renderEventsGroup(eventGroup, index))}
            </React.Fragment>
        );
    };

    renderBeforeScroller = () => {
        const horizontalArrangement = this.state.arrangement.toLowerCase() == "horizontal"
        if (horizontalArrangement && this.state.toolbar.page == 0) return;
        if (!horizontalArrangement && this.state.toolbar.page == this.state.toolbar.countPages-1) return;
        const style = this.state.arrangement.toLowerCase() == "horizontal" ? {minWidth:'200px',display:'block'} : {minHeight:'200px',display:'block'};
        return (<div style={style}>&nbsp;</div>);
    };

    renderEventsGroup = (group, index) => {
        const { classes } = this.props;
        const arrangement = this.state.arrangement.toLowerCase() == "horizontal" ? "vertical" : "horizontal";
        const blockClass = this.state.arrangement.toLowerCase() == "horizontal" ? classes.groupBlockHorizontal : classes.groupBlockVertical;
        return (
            <div id={this.props.id + "groups"} className={"layout " + arrangement} style={{position:'relative'}}>
                <div className={blockClass}>{this.renderGroupHeader(group)}</div>
                <div>{this.renderGroupIcon(group)}</div>
                <div className={blockClass}>{this.renderEvents(group)}</div>
            </div>
        );
    };

    renderGroupHeader = (group) => {
        return (
            <div className="layout vertical center" style={{minWidth:'150px'}}>
                <div style={{fontSize:'14pt'}}>{group.shortDate}</div>
                <div style={{fontSize:'9pt',color:'grey'}}>{group.longDate}</div>
           </div>
        );
    };

    renderGroupIcon = (group) => {
        const arrangement = this.state.arrangement.toLowerCase();
        const { classes } = this.props;
        const clazz = arrangement == "horizontal" ? classes.groupIconDashHorizontal : classes.groupIconDashVertical;
        return (
            <div className={"layout flex center " + arrangement} style={{height:'100%'}}>
                <Adjust fontSize="small"/>
                <div className={clazz}>&nbsp;</div>
            </div>
        );
    };

    renderEvents = (group) => {
        return group.events.map((event, index) => this.renderEvent(event, index));
    };

    renderEvent = (event) => {
        const { classes } = this.props;
        const arrangement = this.state.arrangement.toLowerCase();
        const clazz = arrangement == "horizontal" ? classes.eventHorizontal : classes.eventVertical;
        return (
            <Paper className={clazz}>
                <Typography style={{color:event.color}}>{event.label}</Typography>
                {event.description != null && <Typography variant="body2">{event.description}</Typography>}
            </Paper>
        );
    };

	first = () => { this.requester.first(); };
	previous = () => { this.requester.page(this.state.toolbar.page-1); };
	next = () => { this.requester.page(this.state.toolbar.page+1); };
	last = () => { this.requester.last(); };

    handleScroll = (width, height, e) => {
        const target = e.target;
        const horizontalArrangement = this.state.arrangement.toLowerCase() == "horizontal";
        if (this.scrollTimeout != null) window.clearTimeout(this.scrollTimeout);
        this.scrollTimeout = window.setTimeout(() => {
            const threshold = horizontalArrangement ? width * 0.1 : height * 0.1;
            const requestPreviousPage = horizontalArrangement ? target.scrollLeft < 200 : (target.scrollHeight - height - target.scrollTop) <= threshold;
            const requestNextPage = horizontalArrangement ? (target.scrollWidth - width - target.scrollLeft) <= threshold : target.scrollTop < 200;
            if (requestPreviousPage) this.requester.previous();
            else if (requestNextPage) this.requester.next();
        }, 100);
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Eventline));
DisplayFactory.register("Eventline", withStyles(styles, { withTheme: true })(withSnackbar(Eventline)));