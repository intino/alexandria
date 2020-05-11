import React from "react";
import { withStyles } from '@material-ui/core/styles';
import classNames from "classnames";
import { Radio, FormControl, FormControlLabel, FormLabel, RadioGroup } from '@material-ui/core';
import AbstractSelectorRadioBox from "../../../gen/displays/components/AbstractSelectorRadioBox";
import SelectorRadioBoxNotifier from "../../../gen/displays/notifiers/SelectorRadioBoxNotifier";
import SelectorRadioBoxRequester from "../../../gen/displays/requesters/SelectorRadioBoxRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
});

class SelectorRadioBox extends AbstractSelectorRadioBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorRadioBoxNotifier(this);
		this.requester = new SelectorRadioBoxRequester(this);
        this.state = {
            selected : "",
            ...this.state
        };
	};

	render() {
		const children = this.children();
		const label = this.props.label;
		const selected = this.state.selected != null ? this.state.selected : this.props.selected;
		this._index = -1;
		return (
            <FormControl component="fieldset">
                {label != null && label !== "" ? <FormLabel component="legend"><Typography variant={this.variant("subtitle1")} style={{color:color}}>{label}</Typography></FormLabel> : undefined }
                <RadioGroup value={selected} onChange={this.handleSelect.bind(this)}>
                    {React.Children.map(children, (child, i) => { return this.renderItem(child); })}
                </RadioGroup>
            </FormControl>
		);
	};

	renderItem = (item) => {
        return (<FormControlLabel value={this._name(item)} control={<Radio>{item}</Radio>} label={this._label(item)} />);
	};

	_name = (item) => {
		return item.props.name;
	};

	_label = (item) => {
		const label = item.props.label != null && item.props.label !== "" ? item.props.label : item.props.value;
		return label != null ? label : this.translate("no label");
	};

	handleSelect = (event) => {
		this.requester.select(event.target.value);
	};

	refreshSelected = (selected) => {
		this.setState({selected});
	};
}

export default withStyles(styles, { withTheme: true })(SelectorRadioBox);
DisplayFactory.register("SelectorRadioBox", withStyles(styles, { withTheme: true })(SelectorRadioBox));