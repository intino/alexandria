import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractEventline from "../../../gen/displays/components/AbstractEventline";
import EventlineNotifier from "../../../gen/displays/notifiers/EventlineNotifier";
import EventlineRequester from "../../../gen/displays/requesters/EventlineRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { Typography, Paper, Popover } from "@material-ui/core";
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
		this.toolbarInfo = [];
		this.lastScrollInfo = null;
		this.disablePageLoading = false;
		this.state = {
		    ...this.state,
		    selectedCategory: null,
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
	    this.toolbarInfo = [];
	    this.setState({events: []});
	};

	addEventsBefore = (events) => {
	    let currentEvents = this.state.events;
	    events = events.concat(currentEvents);
	    this.resetScroll = true;
	    if (this.groups.current != null)
	        this.lastScrollInfo = { left: this.groups.current.offsetLeft, top: this.groups.current.offsetTop, width: this.groups.current.scrollWidth, height: this.groups.current.scrollHeight };
        else
            this.lastScrollInfo = null;
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
        if (this.height == null && this.container.current != null) this.height = this.container.current.offsetHeight;
        const heightValue = this.container.current != null ? this.height + "px" : "100%";
        const groupsStyle = this.state.arrangement.toLowerCase() == "horizontal" ? {overflowX:'auto',width:widthValue,paddingTop:'10px',paddingBottom:'20px'} : {overflowY:'auto',height:((this.container.current != null && this.container.current.offsetHeight != null) ? this.container.current.offsetHeight : heightValue)};
        window.setTimeout(() => {
            if (this.groups.current == null || !this.resetScroll) return;
            this.resetScroll = false;
            const scrollInfo = arrangement == "horizontal" ? {left:this.lastScrollInfo != null ? this.groups.current.scrollLeft+(this.groups.current.scrollWidth-this.lastScrollInfo.width) : 200} : {top:this.lastScrollInfo != null ? this.groups.current.scrollTop(this.groups.current.scrollHeight-this.lastScrollInfo.height) : 200};
            this.groups.current.scrollTo(scrollInfo);
        }, 40);
        return (
            <div className="layout vertical" style={this.style()} ref={this.container}>
                {this.renderToolbar({fontWeight:'300'})}
                {this.state.events.length == 0 && <Typography variant="body1">{this.translate("No events found")}</Typography>}
                {this.state.events.length > 0 &&
                    <div ref={this.groups} className={"layout " + arrangement + " " + flex} style={{marginTop:'5px',...groupsStyle}} onScroll={this.handleScroll.bind(this, width, this.height != null ? this.height : 100)}>
                        {this.renderBeforeScroller()}
                        {this.renderEventsGroups()}
                        {this.renderCategoryEventsDialog()}
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
        const id = this.props.id + "_eventgroup_" + index;
        const isSelected = this.state.selectedElement != null && this.state.selectedElement == id;
        const style = isSelected ? { background: "#fffbd4", cursor: "pointer", paddingTop: arrangement == "horizontal" ? "5px" : "0" } : { cursor: "pointer", paddingTop: arrangement == "horizontal" ? "5px" : "0" };
        this.toolbarInfo[index] = { date: group.date, longDate: group.longDate };
        return (
            <div id={id} className={classnames("layout", arrangement, "eventgroup")} style={{position:'relative',...style}} onClick={this.handleClickEventGroup.bind(this,id,this.toolbarInfo[index])}>
                <div className={blockClass}>{this.renderGroupHeader(group)}</div>
                <div>{this.renderGroupIcon(group)}</div>
                <div className={blockClass}>{this.renderEvents(group, index)}</div>
            </div>
        );
    };

    handleClickEventGroup = (groupId, info, e) => {
        this.updateSelectedInstant({ elementId: groupId, info: info });
        this.requester.update(info.date);
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

    renderEvents = (group, index) => {
        const categoryEvents = this.eventsByCategory(group);
        const result = [];
        for (var category in categoryEvents) {
            const events = categoryEvents[category];
            result.push(this.renderCategoryEvents(category, events));
        }
        return result;
    };

    renderCategoryEvents = (category, events) => {
        const { classes } = this.props;
        const arrangement = this.state.arrangement.toLowerCase();
        const clazz = arrangement == "horizontal" ? classes.eventHorizontal : classes.eventVertical;
        const message = events.length + " " + this.translate(events.length == 1 ? "event" : "events");
        return (
            <Paper className={clazz}>
                <Typography aria-owns={this.state.openCategoryEventsDialog ? this.props.id + "mouse-over-popover" : undefined} aria-haspopup="true"
                           onClick={this.handleOpenCategoryEventsDialog.bind(this, category, events)}
                           style={{cursor:'pointer'}}>
                    {category !== "undefined" && <b>{category}: </b>}{message}
                </Typography>
            </Paper>
        );
    };

    renderCategoryEventsDialog = () => {
        if (this.state.selectedCategory == null) return (<React.Fragment/>);
        return (
            <Popover id={this.props.id + "mouse-over-popover"}
                sx={{pointerEvents: 'none'}}
                open={this.state.openCategoryEventsDialog}
                anchorEl={this.state.selectedCategory.target}
                anchorOrigin={{vertical: 'bottom',horizontal: 'left'}}
                transformOrigin={{vertical: 'top',horizontal: 'left'}}
                onClose={this.handleCloseCategoryEventsDialog.bind(this)}
                disableRestoreFocus>
                <div style={{padding:'10px',minWidth:'300px',minHeight:'100px'}}>
                    {this.state.selectedCategory.events.map((event, eventIndex) => this.renderEvent(event, eventIndex))}
                </div>
            </Popover>
        );
    };

    handleOpenCategoryEventsDialog = (category, events, e) => {
        this.setState({openCategoryEventsDialog:true, selectedCategory: { category: category, events: events, target: e.currentTarget }});
    };

    handleCloseCategoryEventsDialog = () => {
        this.setState({openCategoryEventsDialog:false, selectedCategory: null});
    };

    eventsByCategory = (group) => {
        const result = {};
        group.events.forEach(e => {
            if (result[e.category] == null) result[e.category] = [];
            result[e.category].push(e);
        });
        return result;
    };

    renderEvent = (event, index) => {
        const { classes } = this.props;
        const arrangement = this.state.arrangement.toLowerCase();
        const clazz = arrangement == "horizontal" ? classes.eventHorizontal : classes.eventVertical;
        return (<Typography style={{marginBottom:'5px'}}><b style={{fontSize:'14pt'}}>&#8226; </b> {event.label}</Typography>);
    };

	first = () => { this.requester.first(); };
	previous = () => { this.requester.previous(); };
	next = () => { this.requester.next(); };
	last = () => { this.requester.last(); };

	scrollTo = (date) => {
	    if (this.groups.current == null) return;
	    this.resetScroll = false;
	    this.disablePageLoading = true;
        for (let i=0; i<this.toolbarInfo.length; i++) {
            if (this.toolbarInfo[i].longDate == date) {
                const element = window.document.getElementById(this.props.id + "_eventgroup_" + i);
                const bounding = element.getBoundingClientRect();
                const arrangement = this.state.arrangement.toLowerCase();
                const scrollInfo = arrangement == "horizontal" ? {left:bounding.left} : {top:bounding.top};
                this.groups.current.scrollTo(scrollInfo);
                this.disablePageLoading = true;
                this.updateSelectedInstant({ elementId: element.id, info: this.toolbarInfo[i] });
                break;
            }
        }
	};

	scrollToStart = (highlight) => {
	    if (this.groups.current == null) return;
	    this.resetScroll = false;
	    this.disablePageLoading = true;
	    this.groups.current.scrollTo(0, 0);
	    const events = this.groups.current.querySelectorAll(".eventgroup");
	    if (events.length == 0) return;
        this.updateSelectedInstant({ elementId: highlight ? events[0].id : null, info: this.toolbarInfo[0] });
	};

	scrollToEnd = (highlight) => {
	    if (this.groups.current == null) return;
	    this.resetScroll = false;
	    this.disablePageLoading = true;
	    this.groups.current.scrollTo(this.groups.current.scrollWidth, 0);
	    const events = this.groups.current.querySelectorAll(".eventgroup");
	    if (events.length == 0) return;
        this.updateSelectedInstant({ elementId: highlight ? events[events.length-1].id : null, info: this.toolbarInfo[events.length-1] });
	};

    handleScroll = (width, height, e) => {
        const target = e.target;
        const horizontalArrangement = this.state.arrangement.toLowerCase() == "horizontal";
        if (this.scrollTimeout != null) window.clearTimeout(this.scrollTimeout);
        this.scrollTimeout = window.setTimeout(() => {
            if (this.disablePageLoading) {
                this.disablePageLoading = false;
                return;
            }
            this.disablePageLoading = false;
            this.updateSelectedInstant(this.findCurrentElement());
            const threshold = horizontalArrangement ? width * 0.1 : height * 0.1;
            const requestPreviousPage = horizontalArrangement ? target.scrollLeft < 200 : (target.scrollHeight - height - target.scrollTop) <= threshold;
            const requestNextPage = horizontalArrangement ? (target.scrollWidth - width - target.scrollLeft) <= threshold : target.scrollTop < 200;
            if (requestPreviousPage) this.requester.previousPage();
            else if (requestNextPage) this.requester.nextPage();
        }, 100);
    };

    updateSelectedInstant = (params) => {
        if (params == null || params.info.longDate == null) return;
        const toolbar = this.state.toolbar;
        toolbar.label = params.info.longDate;
        this.setState({toolbar: toolbar, selectedElement: params.elementId});
        this.requester.update(params.info.date);
    };

    findCurrentElement = () => {
        if (this.groups.current == null) return null;
        const horizontalArrangement = this.state.arrangement.toLowerCase() == "horizontal";
        const offset = horizontalArrangement ? this.groups.current.getBoundingClientRect().left : this.groups.current.getBoundingClientRect().top;
        const events = this.groups.current.querySelectorAll(".eventgroup");
        for (let i=0; i<events.length; i++) {
            const eventInfo = events[i].getBoundingClientRect();
            if (horizontalArrangement && eventInfo.left+(eventInfo.width/2)-offset <= 0) continue;
            if (!horizontalArrangement && eventInfo.top+(eventInfo.height/2)-offset <= 0) continue;
            return { elementId: events[i].id, info: this.toolbarInfo[events[i].id.substr(events[i].id.lastIndexOf("_")+1)] };
        }
        return null;
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Eventline));
DisplayFactory.register("Eventline", withStyles(styles, { withTheme: true })(withSnackbar(Eventline)));