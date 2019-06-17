import React from "react";
import Slider from '@material-ui/lab/Slider';
import {Fab, MenuItem, Select} from '@material-ui/core';
import { PlayCircleFilled, PauseCircleFilled, NavigateBefore, NavigateNext } from '@material-ui/icons';
import AbstractBaseSlider from "../../../gen/displays/components/AbstractBaseSlider";
import Delayer from "../../util/Delayer";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';

export default class BaseSlider extends AbstractBaseSlider {
	state = {
		value: this.props.value,
		range: this.props.range,
		toolbar: null,
		ordinals: [],
		ordinal: null,
	};

	static Styles = theme => ({
		root: {
			color: '#52af77',
			height: 8,
		},
		thumb: {
			height: 24,
			width: 24,
			backgroundColor: '#fff',
			border: '2px solid currentColor',
			marginTop: -8,
			marginLeft: -12,
			'&:focus,&:hover,&$active': {
				boxShadow: 'inherit',
			},
		},
		active: {},
		valueLabel: {
			left: 'calc(-50% + 4px)',
		},
		track: {
			height: 8,
			borderRadius: 4,
		},
		rail: {
			height: 8,
			borderRadius: 4,
		},
		spacing: {
			marginRight: theme.spacing(1),
		},
		doubleSpacing: {
			marginRight: theme.spacing(2),
		},
	});

	constructor(props) {
		super(props);
	};

	renderComponent() {
		if (this.state.ordinals.length <= 0)
			return (<div style={this.style()}>{this.translate("No ordinals defined!")}</div>);

		const range = this.state.range;
		if (range == null)
			return (<div style={this.style()}>{this.translate("No range defined!")}</div>);

		return (
			<div style={this.style()} className="layout vertical flex">
				{this.renderSlider()}
				{this.renderToolbar()}
			</div>
		);
	};

	renderSlider = () => {
		const range = this.state.range;
		const value = this.state.value != null && this.state.value !== -1 ? this.state.value : 0;
		const ordinal = this.state.ordinals[0];

		return (<Slider valueLabelDisplay="auto" min={range.min} max={range.max}
						value={value} step={ordinal.step}
						onChange={this.handleChange.bind(this)}/>);
	};

	renderToolbar = () => {
		const { classes } = this.props;
		return (
			<div className={classNames("layout horizontal", classes.doubleSpacing)}>
				<div className="layout horizontal center flex">
					{this.renderAnimationControls()}
					{this.renderValue()}
				</div>
				<div className="layout horizontal end-justified">
					{this.renderOrdinals()}
				</div>
			</div>
		);
	};

	renderAnimationControls = () => {
		const toolbar = this.state.toolbar;
		const canPrevious = toolbar != null ? toolbar.canPrevious : false;
		const canNext = toolbar != null ? toolbar.canNext : false;
		const { classes } = this.props;
		return (
			<div className="layout horizontal">
				{ <Fab className={classes.spacing} aria-label={this.translate("Before")} onClick={this.handlePrevious.bind(this)} disabled={!canPrevious} size="small"><NavigateBefore/></Fab>}
				{ (this.props.animation && !this.state.toolbar.playing) && <Fab className={classes.spacing} aria-label={this.translate("Play")} onClick={this.handlePlay.bind(this)} size="small"><PlayCircleFilled/></Fab>}
				{ (this.props.animation && this.state.toolbar.playing) && <Fab className={classes.spacing} aria-label={this.translate("Pause")} onClick={this.handlePause.bind(this)} size="small"><PauseCircleFilled/></Fab>}
				{ <Fab className={classes.spacing} aria-label={this.translate("Next")} onClick={this.handleNext.bind(this)} disabled={!canNext} size="small"><NavigateNext/></Fab>}
			</div>
		);
	};

	renderValue = () => {
		return (<div>soy el valor!</div>);
	};

	renderOrdinals = () => {
		if (this.state.ordinals.length <= 1 || this.state.ordinal == null) return null;
		return (
			<Select value={this.state.ordinal} style={{width:"100%"}} onChange={this.handleOrdinalChange.bind(this)}>
				{this.state.ordinals.map((ordinal, i) => <MenuItem key={i} value={ordinal.name}>{ordinal.label}</MenuItem>)}
			</Select>
		);
	};

	handleChange = (e, value) => {
		Delayer.execute(this, () => this.requester.update(value));
		this.setState({value});
	};

	handleOrdinalChange = (e) => {
		const value = e.target.value;
		this.requester.selectOrdinal(value);
	};

	refreshOrdinals = (ordinals) => {
		this.setState({ordinals});
	};

	refreshSelectedOrdinal = (ordinal) => {
		this.setState({ordinal});
	};

	refreshValue = (value) => {
		this.setState({value});
	};

	refreshToolbar = (toolbar) => {
		this.setState({toolbar});
	};

	handlePrevious = () => { this.requester.previous(); };
	handlePlay = () => { this.requester.play(); };
	handlePause = () => { this.requester.pause(); };
	handleNext = () => { this.requester.next(); };

}