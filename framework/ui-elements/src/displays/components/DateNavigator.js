import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Typography, IconButton } from '@material-ui/core';
import { FirstPage, NavigateBefore, NavigateNext, LastPage } from "@material-ui/icons";
import AbstractDateNavigator from "../../../gen/displays/components/AbstractDateNavigator";
import DateNavigatorNotifier from "../../../gen/displays/notifiers/DateNavigatorNotifier";
import DateNavigatorRequester from "../../../gen/displays/requesters/DateNavigatorRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import 'alexandria-ui-elements/res/styles/layout.css';
import Theme from "app-elements/gen/Theme";
import classnames from "classnames";

const DateNavigatorStyles = {
    label : { fontSize:'12pt', marginRight:'5px' },
    icon : { height:'20px', width:'20px' },
};

const styles = theme => ({
    scale : { cursor:'pointer',padding:'0 4px' },
    selectedScale : { backgroundColor: theme.palette.primary.main, color: 'white' },
});

class DateNavigator extends AbstractDateNavigator {

	constructor(props) {
		super(props);
		this.notifier = new DateNavigatorNotifier(this);
		this.requester = new DateNavigatorRequester(this);
		this.state = {
			...this.state,
			scales: [],
			scale : null,
			selected: null,
			canPrevious : false,
			canNext : false
		}
	};

    render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const theme = Theme.get();
        const previousColor = this.state.canPrevious ? theme.palette.primary.main : theme.palette.grey.A900;
        const nextColor = this.state.canNext ? theme.palette.primary.main : theme.palette.grey.A900;
        return (
            <div className="layout horizontal start center flex">
                <div style={{marginRight:'15px',marginTop:'2px'}}>{this._renderScales()}</div>
                <div className="layout vertical"><Typography style={DateNavigatorStyles.label}>{this.state.selectedLabel}</Typography></div>
                <div className="layout horizontal">
                    <IconButton disabled={!this.state.canPrevious} onClick={this.handleFirst.bind(this)} size="small" style={{color:previousColor}}><FirstPage style={DateNavigatorStyles.icon}/></IconButton>
                    <IconButton disabled={!this.state.canPrevious} onClick={this.handlePrevious.bind(this)} size="small" style={{color:previousColor}}><NavigateBefore style={DateNavigatorStyles.icon}/></IconButton>
                    <IconButton disabled={!this.state.canNext} onClick={this.handleNext.bind(this)} size="small" style={{color:nextColor}}><NavigateNext style={DateNavigatorStyles.icon}/></IconButton>
                    <IconButton disabled={!this.state.canNext} onClick={this.handleLast.bind(this)} size="small" style={{color:nextColor}}><LastPage style={DateNavigatorStyles.icon}/></IconButton>
                </div>
            </div>
        );
    };

    setup = (setup) => {
        const info = setup.info;
        this.setState({ scales: setup.scales, selected: info.selected, selectedLabel: info.selectedLabel, scale: info.scale, canPrevious: info.canPrevious, canNext: info.canNext });
    };

    refresh = (info) => {
        this.setState({ selected: info.selected, selectedLabel: info.selectedLabel, scale: info.scale, canPrevious: info.canPrevious, canNext: info.canNext });
    };

    _renderScales = () => {
        const scales = this.state.scales;
        return (<div>{scales.map((s, idx) => this._renderScale(s, idx==scales.length-1))}</div>);
    };

    _renderScale = (scale, lastScale) => {
        const { classes } = this.props;
        const style = lastScale ? { border:'1px solid #888',borderRight:'1px solid #888' } : { border:'1px solid #888',borderRight:'0' };
        const classNames = scale === this.state.scale ? classnames(classes.scale, classes.selectedScale) : classes.scale;
        const label = scale === "Minute" ? "m" : scale.substring(0,1);
        return (<a onClick={this.handleChangeScale.bind(this, scale)} style={style} className={classNames}>{label}</a>);
    };

    handleFirst = (e) => {
        e.stopPropagation();
        this.requester.first();
    };

    handlePrevious = (e) => {
        e.stopPropagation();
        this.requester.previous();
    };

    handleNext = (e) => {
        e.stopPropagation();
        this.requester.next();
    };

    handleLast = (e) => {
        e.stopPropagation();
        this.requester.last();
    };

    handleChangeScale = (scale, e) => {
        e.stopPropagation();
        this.requester.changeScale(scale);
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(DateNavigator));
DisplayFactory.register("DateNavigator", withStyles(styles, { withTheme: true })(withSnackbar(DateNavigator)));