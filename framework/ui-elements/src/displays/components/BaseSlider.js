import React from "react";
import Slider from '@material-ui/core/Slider';
import { withStyles } from '@material-ui/core';
import { Tooltip, MenuItem, Select, IconButton, Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField } from '@material-ui/core';
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

	static Styles = theme => ({
		spacing: {
			marginRight: theme.spacing(1),
		},
		doubleSpacing: {
			marginRight: theme.spacing(2),
		},
		leftSpacing: {
			marginLeft: theme.spacing(2),
		},
	});

	constructor(props) {
		super(props);
		this.state = {
			...this.state,
			selected: { value: this.props.value, formattedValue: this.props.value },
			range: this.props.range,
			toolbar: null,
			ordinals: [],
			ordinal: null,
			readonly: this.props.readonly,
			marks: null,
		};
	};

	renderComponent() {
		if (!this.state.visible) return (<React.Fragment/>);

		if (this.state.ordinals.length <= 0)
			return (<div style={this.style()}></div>);

		const range = this.state.range;
		if (range == null)
			return (<div style={this.style()}>{this.translate("No range defined!")}</div>);

		const position = this.props.position;
		return (
			<div style={this.style()} className="layout vertical flex">
				{(position == null || position === "SliderTop") && this.renderSlider()}
				{this.renderToolbar()}
				{position === "SliderBottom" && this.renderSlider()}
			</div>
		);
	};

	renderSlider = () => {
		if (!this.allowSlider()) return (<React.Fragment/>);

		const range = this.state.range;
		const ordinal = this.state.ordinals[0];
		const marks = this.state.marks;

		return (<StyledSlider disabled={this.state.readonly} valueLabelDisplay="auto" min={range.min} max={range.max}
							  value={this.getValue()} step={ordinal.step} marks={marks}
							  disableSwap={this.props.minimumDistance != -1}
							  onChange={this.handleMoved.bind(this)}
							  onChangeCommitted={this.handleChange.bind(this)}
							  valueLabelFormat={this.handleFormattedValue.bind(this)}
							  ValueLabelComponent={ValueLabelComponent}
		/>);
	};

	renderToolbar = () => {
		if (!this.allowToolbar()) return (<React.Fragment/>);
		const { classes } = this.props;
		const display = !this.allowNavigation() || this.ordinalSelectorOnly() ? "none" : "inherit";
		const mainSpacing = !this.navigationOnly() ? classes.doubleSpacing : {};
		return (
			<div className={classNames("layout horizontal", mainSpacing)}>
                <div className={classNames("layout horizontal center")} style={{display:display}}>
                    {this.allowNavigation() && this.renderNavigationControls()}
                    {(!this.navigationOnly() && !this.ordinalSelectorOnly()) && this.renderValue()}
                </div>
				{!this.navigationOnly() && <div className={classNames("layout horizontal", classes.leftSpacing)}>{this.renderOrdinals()}</div>}
			</div>
		);
	};

	renderNavigationControls = () => {
		const toolbar = this.state.toolbar;
		const canPrevious = toolbar != null ? toolbar.canPrevious : false;
		const canNext = toolbar != null ? toolbar.canNext : false;
		const { classes } = this.props;
		return (
			<div className="layout horizontal">
				{ <IconButton disabled={this.state.readonly || !canPrevious} color="primary" aria-label={this.translate("Before")} onClick={this.handlePrevious.bind(this)} size="small"><NavigateBefore/></IconButton>}
				{ (this.props.animation && !this.state.toolbar.playing) && <IconButton disabled={this.state.readonly} color="primary" aria-label={this.translate("Play")} onClick={this.handlePlay.bind(this)} size="small"><PlayCircleFilled/></IconButton>}
				{ (this.props.animation && this.state.toolbar.playing) && <IconButton disabled={this.state.readonly} color="primary" aria-label={this.translate("Pause")} onClick={this.handlePause.bind(this)} size="small"><PauseCircleFilled/></IconButton>}
				{ <IconButton disabled={this.state.readonly || !canNext} color="primary" aria-label={this.translate("Next")} onClick={this.handleNext.bind(this)} size="small"><NavigateNext/></IconButton>}
			</div>
		);
	};

	renderValue = () => {
		const { theme } = this.props;
		return (
		    <React.Fragment>
		        <div style={{color: this.state.readonly ? theme.palette.grey.A700 : "black", cursor: "pointer"}} onClick={this.showValueDialog.bind(this)}>{this.getFormattedValue()}</div>
		        {this.renderValueDialog()}
		    </React.Fragment>
        );
	};

	showValueDialog = () => {
	    this.setState({openValueDialog: true, editorValue: this.state.selected != null ? this.state.selected.value : this.state.range.min });
	};

	renderValueDialog = () => {
		const openValueDialog = this.state.openValueDialog != null ? this.state.openValueDialog : false;
		return (<Dialog onClose={this.handleCloseValueDialog.bind(this)} open={openValueDialog}>
				<DialogTitle onClose={this.handleCloseValueDialog.bind(this)}>{this.translate("Select value")}</DialogTitle>
				<DialogContent style={{position:"relative",overflow:"hidden"}}>
					{this.renderValueEditor()}
				</DialogContent>
				<DialogActions>
					<Button onClick={this.handleCloseValueDialog.bind(this)} color="primary">{this.translate("Cancel")}</Button>
					<Button variant="contained" onClick={this.handleValueChange.bind(this)} color="primary">{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	renderValueEditor = () => {
		const range = this.state.range;
		return (
			<TextField format={this.variant("body1")} type="number"
					   value={this.state.editorValue} onChange={this.handleValueEditorChange.bind(this)} onKeyPress={this.handleValueEditorKeyPress.bind(this)}
					   style={{width:'100%'}} autoFocus={true}
					   inputProps={{
						   min: range.min !== -1 ? range.min : undefined,
						   max: range.max !== -1 ? range.max : undefined,
						   step: 1
					   }}/>
		);
	};

	handleValueEditorChange = (e) => {
	    this.setState({editorValue: e.target.value});
	};

	handleValueEditorKeyPress = (e) => {
	    if (e.key != "Enter") return;
	    this.handleValueChange();
	};

	handleValueChange = () => {
		this.requester.update(this.state.editorValue);
		this.setState({openValueDialog: false});
	};

	handleCloseValueDialog = () => {
	    this.setState({openValueDialog: false});
	};

	getValue = () => {
		return this.state.selected != null && this.state.selected.value !== undefined ? this.state.selected.value : 0;
	};

	getFormattedValue = () => {
		return this.state.selected != null && this.state.selected.value !== undefined ? this.state.selected.formattedValue : 0;
	};

	renderOrdinals = () => {
		if (this.state.ordinals.length <= 1 || this.state.ordinal == null) return null;
		return (
			<Select value={this.state.ordinal} style={{width:"100%"}} isdisabled={this.state.readonly}
					onChange={this.handleOrdinalChange.bind(this)}>
				{this.state.ordinals.map((ordinal, i) => <MenuItem key={i} value={ordinal.name}>{this.translate(ordinal.label)}</MenuItem>)}
			</Select>
		);
	};

	handleMoved = (e, value) => {
		this.requester.moved(value);
		this.setState({value});
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

	refreshMarks = (marks) => {
		this.setState({marks});
	};

	handleFormattedValue = (value, index) => {
		return this.state.selected != null ? this.state.selected.formattedValue : value;
	};

	handlePrevious = () => { this.requester.previous(); };
	handlePlay = () => { this.requester.play(); };
	handlePause = () => { this.requester.pause(); };
	handleNext = () => { this.requester.next(); };

	full = () => {
		return this.props.style == null || this.props.style.toLowerCase() === "full";
	};

	toolbarOnly = () => {
		return this.props.style != null && this.props.style.toLowerCase() === "toolbaronly";
	};

	sliderOnly = () => {
		return this.props.style != null && this.props.style.toLowerCase() === "slideronly";
	};

	navigationOnly = () => {
		return this.props.style != null && this.props.style.toLowerCase() === "navigationonly";
	};

	ordinalSelectorOnly = () => {
		return this.props.style != null && this.props.style.toLowerCase() === "ordinalselectoronly";
	};

    allowToolbar = () => {
        return this.full() || this.allowOrdinalSelector() || this.allowNavigation();
    };

    allowSlider = () => {
        return this.full() || this.sliderOnly();
    };

    allowOrdinalSelector = () => {
        return this.full() || this.toolbarOnly() || this.ordinalSelectorOnly();
    };

    allowNavigation = () => {
        return this.full() || this.toolbarOnly() || this.navigationOnly();
    };
}