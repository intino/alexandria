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

	constructor(props) {
		super(props);
		this.notifier = new SelectorComboBoxNotifier(this);
		this.requester = new SelectorComboBoxRequester(this);
	};

	render() {
		const { classes } = this.props;
		const items = React.Children.map(this.props.children, (option, i) => { return { value: this._label(option), label: this._label(option), item: option }});
		const multiple = this.props.multipleSelection;
		const label = this.props.label;
		return (
			<div className={classes.container} style={this.style()}>
				{label != null && label !== "" ? <Typography variant={this.variant("subtitle1")}>{label}</Typography> : undefined }
				<Select isMulti={multiple} isSearchable closeMenuOnSelect={multiple ? false : true}
						placeholder={this.selectMessage()} options={items}
						className="basic-multi-select" classNamePrefix="select"
						components={{ Option: this.renderOption.bind(this)}}
						onChange={this.handleChange.bind(this)}/>
			</div>
		);
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

	_label = (option) => {
		const label = option.props.label != null && option.props.label !== "" ? option.props.label : option.props.value;
		return label != null ? label : this.translate("no label");
	}

}

export default withStyles(styles, { withTheme: true })(SelectorComboBox);
DisplayFactory.register("SelectorComboBox", withStyles(styles, { withTheme: true })(SelectorComboBox));