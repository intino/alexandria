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
		position: "relative",
		minWidth: "85px",
		width: "100%",
	},
	readonly : {
		position: "absolute",
		top: "0",
		left: "0",
		width: "100%",
		height: "100%",
		zIndex: "1",
	},
	label : {
        fontSize: "10pt",
        color: "#0000008a",
        marginBottom: "5px",
    }
});

class SelectorComboBox extends AbstractSelectorComboBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorComboBoxNotifier(this);
		this.requester = new SelectorComboBoxRequester(this);
        this.state = {
            selection: this.traceValue() ? this.traceValue() : [],
            multipleSelection: this.props.multipleSelection != null ? this.props.multipleSelection : false,
            ...this.state
        };
	};

	render() {
		const { classes, theme } = this.props;
		const items = this.items();
		const multiple = this.state.multipleSelection;
		const label = this.props.label;
		const value = this.selection(items);
		const color = this.state.readonly ? theme.palette.grey.A700 : "inherit";

		return (
			<div className={classes.container} style={this.style()}>
                {this.renderTraceConsent()}
				{label != null && label !== "" ? <div className={classes.label} style={{color:color}}>{label}</div> : undefined }
				<Select isMulti={multiple} isDisabled={this.state.readonly} isSearchable
						closeMenuOnSelect={!multiple} autoFocus={this.props.focused}
						placeholder={this.selectMessage()} options={items}
						className="basic-multi-select" classNamePrefix="select"
						components={{ Option: this.renderOption.bind(this)}}
						menuPlacement="auto" value={value}
						onChange={this.handleChange.bind(this)}
						onMenuOpen={this.handleOpen.bind(this)}/>
			</div>
		);
	};

	items = () => {
		var children = this.children();
		return React.Children.map(children, (option, i) => { return { value: this._name(option), label: this._label(option), item: option }});
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
		const multi = this.state.multipleSelection;
		const selection = multi ? selectedOptions.map(s => s.value) : [ selectedOptions.value ];
		this.trace(selection);
		this.requester.updateSelection(selection);
		this.setState({ selection: selection });
	};

	handleOpen = () => {
		this.requester.refresh();
	};

	selectMessage = () => {
		const placeholder = this.props.placeholder;
		return this.translate(placeholder != null && placeholder !== "" ? placeholder : "Select an option");
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

	refreshMultipleSelection = (multipleSelection) => {
		this.setState({ multipleSelection });
	};

	selection = (options) => {
		const multiple = this.state.multipleSelection;
		const selectedOptions = this.state.selection.map(s => this.option(options, s));
		return multiple ? selectedOptions : (selectedOptions.length > 0 ? selectedOptions[0] : "");
	};

	option = (options, key) => {
		for (var i=0; i<options.length; i++) {
			if (options[i].value === key || options[i].label === key) return options[i];
		}
		return null;
	};
}

export default withStyles(styles, { withTheme: true })(SelectorComboBox);
DisplayFactoryM.register("SelectorComboBox", withStyles(styles, { withTheme: true })(SelectorComboBox));