import React from "react";
import {withStyles} from '@material-ui/core/styles';
import AbstractSelectorComboBox from "../../../gen/displays/components/AbstractSelectorComboBox";
import SelectorComboBoxNotifier from "../../../gen/displays/notifiers/SelectorComboBoxNotifier";
import SelectorComboBoxRequester from "../../../gen/displays/requesters/SelectorComboBoxRequester";
import Select, {components} from "react-select";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Theme from 'app-elements/gen/Theme';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import classnames from 'classnames';
import OutlinedInput from '@material-ui/core/OutlinedInput';
import InputLabel from '@material-ui/core/InputLabel';

export const SelectorComboBoxTextViewStyles = {
	control: (provided, state) => ({
		...provided,
		background: 'transparent',
		border: '0',
		minHeight: '20px',
	}),
	valueContainer: (provided, state) => ({
		...provided,
		padding: '0 6px',
	}),
	input: (provided, state) => ({
		...provided,
		margin: '0px',
	}),
	indicatorSeparator: () => ({
		display: 'none',
	}),
	indicatorsContainer: (provided, state) => ({
		...provided,
		paddingRight: '8px !important'
	}),
};

export function selectorComboBoxStyles(theme) {
	return {
		control: (provided, state) => ({
			...provided,
			":hover": {
				border: "1px solid black"
			},
		}),
		valueContainer: (provided, state) => ({
			...provided,
			padding: "0 13px",
		}),
		singleValue: (provided, state) => ({
			...provided,
			color: theme.isDark() ? 'white' : '#333',
		}),
		multiValue: (provided, state) => ({
			...provided,
			borderRadius:'4px',
			margin: "0 8px 0 0",
			background: state.isDisabled ? "none" : provided.background
		}),
		multiValueRemove: (provided, state) => ({
			...provided,
			cursor: 'pointer',
			color: Theme.get().palette.primary.main,
			display: state.isDisabled ? 'none' : 'inherit',
			':hover': {
				backgroundColor: Theme.get().palette.primary.main,
				color: 'white',
			},
		}),
		indicatorsContainer: (provided, state) => ({
			...provided,
			display: state.isDisabled ? 'none' : 'inherit',
			paddingRight: '8px !important',
		}),
		placeholder: (provided, state) => ({
			...provided,
			display: state.isDisabled ? 'none' : 'inherit',
		}),
		menu: provided => ({ ...provided, zIndex: 9999 }),
		option: (styles, { data, isDisabled, isFocused, isSelected }) => {
			return {
				...styles,
				color: isDisabled ? '#ccc' : (isSelected ? (theme.isDark() ? "black" : "white") : (theme.isDark() ? "white" : "black")),
			};
		},
	};
};

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
	},
	multiValueLabel : {
		pointerEvents:'all',
		cursor:'pointer',
		color: theme.isDark() ? 'white' : 'black',
		padding:'4px 6px 4px 6px',
		//fontSize:'14pt',
		textOverflow:'ellipsis',
		whiteSpace:'nowrap',
		boxSizing:'border-box',
		overflow:'hidden',
	},
	multiValueLabelReadonly : {
		padding:'4px 12px 4px 0px',
		cursor: 'default'
	},
});

class SelectorComboBox extends AbstractSelectorComboBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorComboBoxNotifier(this);
		this.requester = new SelectorComboBoxRequester(this);
		this.state = {
			multipleSelection: this.props.multipleSelection != null ? this.props.multipleSelection : false,
			...this.state
		};
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const { classes, theme } = this.props;
		const items = this.items();
		const multiple = this.state.multipleSelection;
		const label = this.props.label;
		const value = this.selection(items);
		const color = this.state.readonly ? theme.palette.grey.A700 : theme.isDark() ? "#ffffffb3" : "#0000008a";
		const isDark = Theme.get().isDark();
		const styles = this.props.view === "TextView" ? { ...selectorComboBoxStyles(Theme.get()), ...SelectorComboBoxTextViewStyles } : { ...selectorComboBoxStyles(Theme.get()) };
		const readonlyClass = this.state.readonly ? "readonly" : "";
		const labelClass = label == null || label === "" ? "no-label" : undefined;
		const viewClass = this.props.view === "FilterView" ? "filter-view" : this.props.view === "TextView" ? "text-view" : undefined;
		const containerClasses = classnames(classes.container, "selector selector-combo-box", readonlyClass, labelClass, viewClass)

		return (
			<div id={this.props.id + "-container"} className={containerClasses} style={{...this.style()}}>
				{this.renderTraceConsent()}
				<div className="MuiFormControl-root MuiTextField-root" style={{width:"100%"}}>
					<label className="MuiFormLabel-root MuiInputLabel-root MuiInputLabel-formControl MuiInputLabel-animated MuiInputLabel-shrink MuiInputLabel-outlined Mui-focused Mui-focused" data-shrink="true">
						{label != null && label !== "" ? this.translate(label) : ""}
					</label>

					<div className="MuiInputBase-root MuiOutlinedInput-root MuiInputBase-formControl MuiInputBase-adornedEnd" style={{width:"100%"}}>
						<OutlinedInput style={{display:"none"}}></OutlinedInput>
						<InputLabel style={{display:"none"}}></InputLabel>

						<Select isMulti={multiple} isDisabled={this.state.readonly} isSearchable
								ref={!this.state.readonly ? this.selectorRef : undefined}
								closeMenuOnSelect={!multiple} autoFocus={this.props.focused}
								placeholder={this.selectMessage()} options={items}
								className="basic-multi-select" classNamePrefix="select"
								components={{
									Option: this.renderOption.bind(this),
									MultiValueLabel: this.renderMultiValueLabel.bind(this)
								}}
								menuPlacement="auto" maxMenuHeight={this.props.maxMenuHeight} value={value}
								filterOption={this.handleFilter.bind(this)}
								onChange={this.handleChange.bind(this)}
								onFocus={this.handleFocus.bind(this)}
								onBlur={this.handleBlur.bind(this)}
								onMenuOpen={this.handleOpen.bind(this)}
								noOptionsMessage={() => this.translate("No options")}
								styles={styles}
								theme={(theme) => ({
									...theme,
									colors: {
										...theme.colors,
										text: isDark ? 'blue' : theme.colors.text,
										primary: isDark ? "gray" : theme.colors.primary,//Border and Background dropdown color
										primary25: isDark ? "gray" : theme.colors.primary25,//Background hover dropdown color
										primary50: isDark ? "gray" : theme.colors.primary50,//after select dropdown option
										primary75: isDark ? "gray" : theme.colors.primary75,//after select dropdown option
										neutral0: isDark ? "#222" : theme.colors.neutral0,//Background color
										neutral5: isDark ? "#222" : theme.colors.neutral5,//Background color
										neutral10: isDark ? "#777" : theme.colors.neutral10,//Background color
										neutral20: isDark ? "#444" : theme.colors.neutral20,//Border before select
										neutral30: isDark ? "#777" : theme.colors.neutral30,//Hover border
										neutral40: isDark ? "white" : theme.colors.neutral40,//No options color
										neutral50: isDark ? "#F4FFFD" : theme.colors.neutral50,//Select color
										neutral60: isDark ? "white" : theme.colors.neutral60,//arrow icon when click select
										neutral70: isDark ? "white" : theme.colors.neutral60,//arrow icon when click select
										neutral80: isDark ? "#F4FFFD" : theme.colors.neutral80,//Text color
										neutral90: isDark ? "#F4FFFD" : theme.colors.neutral90,//Text color
									},
								})}
						/>

						<fieldset className="PrivateNotchedOutline-root-46 MuiOutlinedInput-notchedOutline">
							{(label != null && label !== "") &&
								<legend className="PrivateNotchedOutline-legendLabelled-48 PrivateNotchedOutline-legendNotched-49"><span>{this.translate(label)}</span></legend>
							}
						</fieldset>
					</div>
				</div>

			</div>
		);
	};

	items = () => {
		var children = this.children();
		const result = React.Children.map(children, (option, i) => {
			return {value: this._name(option), label: this._label(option), item: option}
		});
		const selection = this.state.selection;
		for (let i = 0; i < selection.length; i++) {
			if (this.option(result, selection[i]) == null)
				result.unshift({value: selection[i], label: selection[i], item: selection[i], disabled: true});
		}
		return result;
	};

	renderOption = (options) => {
		const {data, isDisabled, ...props} = options;
		const item = data.item;
		const {classes} = this.props;
		return !isDisabled ? (
			<components.Option {...props} className={classes.container}>{item}</components.Option>
		) : null;
	};

	renderMultiValueLabel = (props) => {
		const { classes } = this.props;
		const theme = Theme.get();
		const color = theme.isDark() ? "#ffffffb3" : "black";
		const background = this.state.readonly ? "none !important" : "inherited";
		return (<a className={classnames(classes.multiValueLabel, this.state.readonly ? classes.multiValueLabelReadonly : undefined)} style={{color:color,background:background}}>{props.data.label}</a>);
	};

	handleFilter = (candidate, condition) => {
		const split = condition.toLowerCase().split(" ");
		const label = candidate.label != null ? candidate.label.toLowerCase() : "";
		const value = candidate.value != null ? candidate.value.toLowerCase() : "";
		for (var i=0; i<split.length; i++) {
			if (label.indexOf(split[i]) == -1 && value.indexOf(split[i]) == -1) return false;
		}
		return true;
	};

	handleChange = (selectedOptions) => {
		const multi = this.state.multipleSelection;
		const selection = multi ? selectedOptions.map(s => s.value) : [ selectedOptions.value ];
		this.trace(selection);
		this.requester.updateSelection(selection);
		this.setState({ selection: selection });
	};

	handleOpen = () => {
		this.requester.opened();
	};

	handleFocus = () => {
		const element = document.getElementById(this.props.id + "-container");
		if (element.className.indexOf(" focus") !== -1) return;
		element.className += " focus";
	};

	handleBlur = () => {
		const element = document.getElementById(this.props.id + "-container");
		if (element.className.indexOf("focus") === -1) return;
		element.className = element.className.replace(" focus", "");
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
		return this.translate(label != null ? label : "no label");
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
DisplayFactory.register("SelectorComboBox", withStyles(styles, { withTheme: true })(SelectorComboBox));