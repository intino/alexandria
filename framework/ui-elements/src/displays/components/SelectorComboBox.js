import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSelectorComboBox from "../../../gen/displays/components/AbstractSelectorComboBox";
import SelectorComboBoxNotifier from "../../../gen/displays/notifiers/SelectorComboBoxNotifier";
import SelectorComboBoxRequester from "../../../gen/displays/requesters/SelectorComboBoxRequester";
import * as Ui from '../../../gen/Displays'
import * as Elements from "app-elements/gen/Displays";
import Select, { components } from "react-select";

const styles = theme => ({});

class SelectorComboBox extends AbstractSelectorComboBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorComboBoxNotifier(this);
		this.requester = new SelectorComboBoxRequester(this);
	};

	render() {
		const { classes } = this.props;
		const items = React.Children.map(this.props.children, (option, i) => { return { value: this._label(option), label: this._label(option), item: option }});
		return (
			<div className={classes.container} style={this.style()}>
				<Select isMulti={this.props.multipleSelection} isSearchable closeMenuOnSelect={false}
						placeholder={this.selectMessage()} options={items}
						className="basic-multi-select" classNamePrefix="select"
						components={{ Option: this.renderOption.bind(this)}}
						onChange={this.handleChange.bind(this)}
				/>
			</div>
		);
	};

	renderOption = (options) => {
		const { data, isDisabled, ...props } = options;
		const item = data.item;
		return !isDisabled ? (
			<components.Option {...props}>{item}</components.Option>
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
		return option.props.label != null && option.props.label !== "" ? option.props.label : this.translate("no label");
	}

}

export default withStyles(styles, { withTheme: true })(SelectorComboBox);