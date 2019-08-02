import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Typography from "@material-ui/core/Typography";
import AbstractSelectorComboBox from "../../../gen/displays/components/AbstractSelectorComboBox";
import SelectorComboBoxNotifier from "../../../gen/displays/notifiers/SelectorComboBoxNotifier";
import SelectorComboBoxRequester from "../../../gen/displays/requesters/SelectorComboBoxRequester";
import Select, { components } from "react-select";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	container : {
		position: "relative"
	},
	readonly : {
		position: "absolute",
		top: "0",
		left: "0",
		width: "100%",
		height: "100%",
		zIndex: "1",
	}
});

class SelectorComboBox extends AbstractSelectorComboBox {
	state = {
		selection: [],
		readonly: this.props.readonly
	};

	constructor(props) {
		super(props);
		this.notifier = new SelectorComboBoxNotifier(this);
		this.requester = new SelectorComboBoxRequester(this);
	};

	render() {
		const { classes, theme } = this.props;
		const items = this.items();
		const multiple = this.props.multipleSelection;
		const label = this.props.label;
		const value = this.selection(items);
		const color = this.state.readonly ? theme.palette.grey.primary : "inherit";
		return (
			<div className={classes.container} style={this.style()}>
				{label != null && label !== "" ? <Typography variant={this.variant("subtitle1")} style={{color:color}}>{label}</Typography> : undefined }
				<Select isMulti={multiple} isDisabled={this.state.readonly} isSearchable
						closeMenuOnSelect={!multiple}
						placeholder={this.selectMessage()} options={items}
						className="basic-multi-select" classNamePrefix="select"
						components={{ Option: this.renderOption.bind(this)}}
						value={value}
						onChange={this.handleChange.bind(this)}/>
			</div>
		);
	};

	items = () => {
		var items = this.props.children;

		if (items == null) {
			const instances = this.instances();
			items = [];
			instances.forEach(instance => items.push(React.createElement(DisplayFactory.get(instance.tp), instance.pl)));
		}

		return React.Children.map(items, (option, i) => { return { value: this._name(option), label: this._label(option), item: option }});
	};

	renderOption = (options) => {
		const { data, isDisabled, ...props } = options;
		const item = data.item;
		const { classes } = this.props;
		return !isDisabled ? (
			<components.Option {...props} className={classes.container}>{item}</components.Option>
		) : null;
	};

	handleChange = (selectedOptions) => {
		const multi = this.props.multipleSelection;
		const selection = multi ? selectedOptions.map(s => s.value) : [ selectedOptions.value ];
		this.requester.updateSelection(selection);
		this.setState({ selection: selection });
	};

	selectMessage = () => {
		return this.translate("Select an option");
	};

	_name = (option) => {
		return option.props.name;
	};

	_label = (option) => {
		const label = option.props.label != null && option.props.label !== "" ? option.props.label : option.props.value;
		return label != null ? label : this.translate("no label");
	};

	refreshSelection = (selection) => {
		this.setState({ selection: selection });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	selection = (options) => {
		const multiple = this.props.multipleSelection;
		const selectedOptions = this.state.selection.map(s => this.option(options, s));
		return multiple ? selectedOptions : (selectedOptions.length > 0 ? selectedOptions[0] : undefined);
	};

	option = (options, key) => {
		for (var i=0; i<options.length; i++) {
			if (options[i].value === key || options[i].label === key) return options[i];
		}
		return null;
	};
}

export default withStyles(styles, { withTheme: true })(SelectorComboBox);
DisplayFactory.register("SelectorComboBox", withStyles(styles, { withTheme: true })(SelectorComboBox));