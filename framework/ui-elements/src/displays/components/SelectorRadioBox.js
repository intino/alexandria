import React from "react";
import {withStyles} from '@material-ui/core/styles';
import {FormControl, FormControlLabel, FormLabel, Radio, RadioGroup, Typography} from '@material-ui/core';
import AbstractSelectorRadioBox from "../../../gen/displays/components/AbstractSelectorRadioBox";
import SelectorRadioBoxNotifier from "../../../gen/displays/notifiers/SelectorRadioBoxNotifier";
import SelectorRadioBoxRequester from "../../../gen/displays/requesters/SelectorRadioBoxRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	label: {
		transform: "translate(0px, -2px) scale(0.8) !important"
	}
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
		if (!this.state.visible) return (<React.Fragment/>);
		const { classes } = this.props;
		const children = this.children();
		const label = this.props.label;
		const selected = this.state.selected != null ? this.state.selected : this.props.selected;
		const layout = this.props.layout != null ? "layout " + this.props.layout.toLowerCase() + " wrap" : "layout vertical";
		this._index = -1;
		return (
			<FormControl component="fieldset" variant="outlined" style={{ border: '1px solid rgba(0, 0, 0, 0.23)', borderRadius: '4px', padding: '10px' }}>
				{label != null && label !== "" ? <FormLabel component="legend" className={classes.label}><Typography variant={this.variant("subtitle1")} style={{color:this.props.color}}>{this.translate(label)}</Typography></FormLabel> : undefined }
				<RadioGroup className={layout} value={selected} onChange={this.handleSelect.bind(this)}>
					{React.Children.map(children, (child, i) => { return this.renderItem(child); })}
				</RadioGroup>
			</FormControl>
		);
	};

	renderItem = (item) => {
		return (<FormControlLabel value={this._name(item)} control={<Radio style={{marginLeft:'10px',marginRight:'5px',padding:'0'}}>{item}</Radio>} label={this._label(item)} />);
	};

	_name = (item) => {
		return item.props.name;
	};

	_label = (item) => {
		const label = item.props.label != null && item.props.label !== "" ? item.props.label : item.props.value;
		return this.translate(label != null ? label : "no label");
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