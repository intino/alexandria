import React from "react";
import { TextField } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import { withSnackbar } from 'notistack';
import AbstractRangeSlider from "../../../gen/displays/components/AbstractRangeSlider";
import RangeSliderNotifier from "../../../gen/displays/notifiers/RangeSliderNotifier";
import RangeSliderRequester from "../../../gen/displays/requesters/RangeSliderRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import BaseSlider from "./BaseSlider";
import Delayer from "../../util/Delayer";

const styles = theme => ({...BaseSlider.Styles(theme)});

class RangeSlider extends AbstractRangeSlider {

	constructor(props) {
		super(props);
		this.notifier = new RangeSliderNotifier(this);
		this.requester = new RangeSliderRequester(this);
	};

	render() {
		return this.renderComponent();
	};

	getValue = () => {
	    const selected = this.state.selected;
		return selected != null && selected.value !== undefined ? [selected.value, selected.to] : [0, 0];
	};

	getFormattedValue = () => {
	    const selected = this.state.selected;
		return selected != null && selected.value !== undefined ? selected.formattedValue + " " + this.translate("to") + " " + selected.formattedTo : 0 + " " + this.translate("to") + " " + 0;
	};

	renderValueEditor = () => {
		const range = this.state.range;
		return (
		    <React.Fragment>
                <TextField format={this.variant("body1")} type="number"
                           value={this.state.editorValue} onChange={this.handleValueEditorChange.bind(this)} onKeyPress={this.handleValueEditorKeyPress.bind(this)}
                           style={{width:'100%'}} autoFocus={true}
                           inputProps={{
                               min: range.min !== -1 ? range.min : undefined,
                               max: range.max !== -1 ? range.max : undefined,
                               step: 1
                           }}/>
                <TextField format={this.variant("body1")} type="number"
                           value={this.state.editorTo} onChange={this.handleToEditorChange.bind(this)} onKeyPress={this.handleValueEditorKeyPress.bind(this)}
                           style={{width:'100%',marginTop:'10px'}} autoFocus={true}
                           inputProps={{
                               min: range.min !== -1 ? range.min : undefined,
                               max: range.max !== -1 ? range.max : undefined,
                               step: 1
                           }}/>
            </React.Fragment>
		);
	};

	showValueDialog = () => {
	    this.setState({openValueDialog: true, editorValue: this.state.selected != null ? this.state.selected.value : this.state.range.min, editorTo: this.state.selected != null ? this.state.selected.to : this.state.range.min+1 });
	};

	handleToEditorChange = (e) => {
	    this.setState({editorTo: e.target.value});
	};

	handleValueChange = () => {
		this.requester.update({from: this.state.editorValue, to: this.state.editorTo});
		this.setState({openValueDialog: false});
	};

	handleMoved = (e, newValue) => {
	    const minDistance = this.props.minimumDistance;
	    const activeThumb = this.state.selected != null && Math.abs(this.state.selected.value - newValue[0]) >= 1 ? 0 : 1;
	    let value = newValue;
        if (minDistance != null && (newValue[1] - newValue[0] < minDistance)) {
          if (activeThumb === 0) {
            const clamped = Math.min(newValue[0], 100 - minDistance);
            value = [clamped, clamped + minDistance];
          } else {
            const clamped = Math.max(newValue[1], minDistance);
            value = [clamped - minDistance, clamped];
          }
        }
		this.requester.moved({from: value[0], to: value[1]});
		this.setState({value});
	};

	handleChange = (e, value) => {
		Delayer.execute(this, () => this.requester.update({from: value[0], to: value[1]}));
		this.setState({value});
	};

	handleFormattedValue = (value, index) => {
	    const selected = this.state.selected;
	    if (selected == null) return value;
	    return selected.value == value ? selected.formattedValue : selected.formattedTo;
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(RangeSlider));
DisplayFactory.register("RangeSlider", withStyles(styles, { withTheme: true })(withSnackbar(RangeSlider)));