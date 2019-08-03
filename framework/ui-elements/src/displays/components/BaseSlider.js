import React from "react";
import Slider from '@material-ui/core/Slider';
import { withStyles } from '@material-ui/core';
import { Tooltip, MenuItem, Select, IconButton } from '@material-ui/core';
import { PlayCircleFilled, PauseCircleFilled, NavigateBefore, NavigateNext } from '@material-ui/icons';
import AbstractBaseSlider from "../../../gen/displays/components/AbstractBaseSlider";
import Delayer from "../../util/Delayer";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	root: {
		color: theme.palette.primary.main,
		height: 6,
	},
	thumb: {
		height: 16,
		width: 16,
		backgroundColor: '#fff',
		border: '2px solid currentColor',
		marginTop: -6,
		marginLeft: -8,
		'&:focus,&:hover,&$active': {
			boxShadow: 'inherit',
		},
	},
	active: {},
	valueLabel: {
		left: 'calc(-50% - 4px)',
	},
	track: {
		height: 4,
		borderRadius: 2,
	},
	rail: {
		height: 4,
		borderRadius: 2,
	},
});

const StyledSlider = withStyles(styles, { withTheme: true })(Slider);
const StyledTooltip = withStyles(theme => ({
	tooltip: {
		backgroundColor: theme.palette.primary.main,
		color: theme.palette.common.white,
		boxShadow: theme.shadows[1],
		fontSize: 11,
	},
}))(Tooltip);

function ValueLabelComponent(props) {
	const { children, open, value, valueLabelFormat } = props;

	const popperRef = React.useRef(null);
	React.useEffect(() => {
		if (popperRef.current) {
			popperRef.current.update();
		}
	});

	return (
		<StyledTooltip
			PopperProps={{ popperRef }}
			open={open}
			enterTouchDelay={0}
			placement="top"
			title={valueLabelFormat(value)}
		>
			{children}
		</StyledTooltip>
	);
};

export default class BaseSlider extends AbstractBaseSlider {
	state = {
		selected: { value: this.props.value, formattedValue: this.props.value },
		range: this.props.range,
		toolbar: null,
		ordinals: [],
		ordinal: null,
		readonly: this.props.readonly,
	};

	static Styles = theme => ({
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
		const ordinal = this.state.ordinals[0];

		return (<StyledSlider disabled={this.state.readonly} valueLabelDisplay="auto" min={range.min} max={range.max}
							  value={this.getValue()} step={ordinal.step}
							  onChange={this.handleChange.bind(this)}
							  valueLabelFormat={this.handleFormattedValue.bind(this)}
							  ValueLabelComponent={ValueLabelComponent}
		/>);
	};

	renderToolbar = () => {
		const { classes } = this.props;
		return (
			<div className={classNames("layout horizontal", classes.doubleSpacing)}>
				<div className={classNames("layout horizontal center", classes.doubleSpacing)}>
					{this.renderAnimationControls()}
					{this.renderValue()}
				</div>
				<div className="layout horizontal">
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
				{ <IconButton disabled={this.state.readonly || !canPrevious} color="primary" aria-label={this.translate("Before")} onClick={this.handlePrevious.bind(this)} size="small"><NavigateBefore/></IconButton>}
				{ (this.props.animation && !this.state.toolbar.playing) && <IconButton disabled={this.state.readonly} color="primary" aria-label={this.translate("Play")} onClick={this.handlePlay.bind(this)} size="small"><PlayCircleFilled/></IconButton>}
				{ (this.props.animation && this.state.toolbar.playing) && <IconButton disabled={this.state.readonly} color="primary" aria-label={this.translate("Pause")} onClick={this.handlePause.bind(this)} size="small"><PauseCircleFilled/></IconButton>}
				{ <IconButton disabled={this.state.readonly || !canNext} className={classes.spacing} color="primary" aria-label={this.translate("Next")} onClick={this.handleNext.bind(this)} size="small"><NavigateNext/></IconButton>}
			</div>
		);
	};

	renderValue = () => {
		const { theme } = this.props;
		return (<div style={{color: this.state.readonly ? theme.palette.grey.primary : "black"}}>{this.getFormattedValue()}</div>);
	};

	getValue = () => {
		return this.state.selected != null && this.state.selected.value !== -1 ? this.state.selected.value : 0;
	};

	getFormattedValue = () => {
		return this.state.selected != null && this.state.selected.value !== -1 ? this.state.selected.formattedValue : 0;
	};

	renderOrdinals = () => {
		if (this.state.ordinals.length <= 1 || this.state.ordinal == null) return null;
		return (
			<Select value={this.state.ordinal} style={{width:"100%"}} isDisabled={this.state.readonly}
					onChange={this.handleOrdinalChange.bind(this)}>
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

	refreshSelected = (selected) => {
		this.setState({selected});
	};

	refreshToolbar = (toolbar) => {
		this.setState({toolbar});
	};

	refreshRange = (range) => {
		this.setState({range});
	};

	refreshReadonly = (readonly) => {
		this.setState({readonly});
	};

	handleFormattedValue = (value, index) => {
		return this.state.selected != null ? this.state.selected.formattedValue : value;
	};

	handlePrevious = () => { this.requester.previous(); };
	handlePlay = () => { this.requester.play(); };
	handlePause = () => { this.requester.pause(); };
	handleNext = () => { this.requester.next(); };

}