import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractSelectorComboBox from "../../../gen/displays/components/AbstractSelectorComboBox";
import SelectorComboBoxNotifier from "../../../gen/displays/notifiers/SelectorComboBoxNotifier";
import SelectorComboBoxRequester from "../../../gen/displays/requesters/SelectorComboBoxRequester";
import Select, {components} from "react-select";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Theme from 'app-elements/gen/Theme';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import classnames from 'classnames';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import {fieldPalette} from "./FieldStyles";

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

export const SelectorComboBoxFilterViewStyles = {
	control: (provided, state) => ({
		...provided,
		minHeight: '38px',
		height: '38px',
		borderRadius: '19px',
	}),
	valueContainer: (provided, state) => ({
		...provided,
		height: '36px',
		padding: '0 13px',
	}),
	input: (provided, state) => ({
		...provided,
		margin: '0px',
		paddingTop: 0,
		paddingBottom: 0,
	}),
	indicatorsContainer: (provided, state) => ({
		...provided,
		height: '36px',
		paddingRight: '8px !important'
	}),
};

export function selectorComboBoxStyles(theme) {
	const palette = fieldPalette(theme);
	const controlBackground = palette.dark ? "rgba(44, 57, 72, 0.86)" : palette.background;
	const controlHoverBackground = palette.dark ? "rgba(49, 64, 80, 0.92)" : palette.hoverBackground;
	const menuBackground = palette.dark ? "rgba(23, 29, 38, 0.98)" : palette.background;
	const optionHoverBackground = palette.dark ? "rgba(148, 163, 184, 0.12)" : palette.hoverBackground;
	const optionSelectedBackground = palette.dark ? "rgba(144, 202, 249, 0.18)" : palette.focusBackground;
	const optionSelectedColor = palette.dark ? "rgba(255,255,255,0.96)" : palette.textColor;
	return {
		control: (provided, state) => ({
			...provided,
			background: controlBackground,
			borderRadius: "16px",
			boxShadow: "none",
			minHeight: "0",
			height: "52px",
			borderColor: palette.borderColor,
			cursor: state.isDisabled ? "default" : provided.cursor,
			":hover": {
				background: state.isDisabled ? controlBackground : controlHoverBackground,
				borderColor: state.isDisabled ? palette.borderColor : palette.hoverBorderColor,
			},
		}),
		valueContainer: (provided, state) => ({
			...provided,
			padding: "0 13px",
			height: "50px",
		}),
		singleValue: (provided, state) => ({
			...provided,
			color: palette.textColor,
		}),
		input: (provided) => ({
			...provided,
			margin: 0,
			paddingTop: 0,
			paddingBottom: 0,
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
			color: theme.palette.primary.main,
			display: state.isDisabled ? 'none' : 'inherit',
			':hover': {
				backgroundColor: theme.palette.primary.main,
				color: 'white',
			},
		}),
		indicatorsContainer: (provided, state) => ({
			...provided,
			display: state.isDisabled ? 'none' : 'inherit',
			height: "50px",
			paddingRight: '8px !important',
		}),
		placeholder: (provided, state) => ({
			...provided,
			display: state.isDisabled ? 'none' : 'inherit',
			color: palette.placeholderColor,
		}),
		menu: provided => ({ ...provided, zIndex: 9999 }),
		menuList: (provided) => ({
			...provided,
			background: menuBackground,
		}),
		option: (styles, { data, isDisabled, isFocused, isSelected }) => {
			return {
				...styles,
				backgroundColor: isSelected ? optionSelectedBackground : (isFocused ? optionHoverBackground : menuBackground),
				color: isDisabled ? palette.disabledText : (isSelected ? optionSelectedColor : palette.textColor),
				"&:active": {
					backgroundColor: palette.dark ? "rgba(144, 202, 249, 0.24)" : palette.focusBackground,
				},
			};
		},
		menuPortal: (provided) => ({
			...provided,
			zIndex: 16000,
		}),
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
		color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.84)" : "#0000008a",
		marginBottom: "5px",
	},
	multiValueLabel : {
		pointerEvents:'all',
		cursor:'pointer',
		color: theme.palette.mode === "dark" ? 'rgba(226,232,240,0.92)' : 'black',
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

		const { classes } = this.props;
		const runtimeTheme = Theme.get();
		const theme = runtimeTheme != null ? runtimeTheme : this.props.theme;
		const items = this.items();
		const multiple = this.state.multipleSelection;
		const label = this.props.label;
		const value = this.selection(items);
		const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
		const styles = this.props.view === "TextView"
			? { ...selectorComboBoxStyles(theme), ...SelectorComboBoxTextViewStyles }
			: this.props.view === "FilterView"
				? { ...selectorComboBoxStyles(theme), ...SelectorComboBoxFilterViewStyles }
				: { ...selectorComboBoxStyles(theme) };
		const readonlyClass = this.state.readonly ? "readonly" : "";
		const labelClass = label == null || label === "" ? "no-label" : undefined;
		const viewClass = this.props.view === "FilterView" ? "filter-view" : this.props.view === "TextView" ? "text-view" : undefined;
		const darkClass = isDark ? "dark" : undefined
		const menuPortalTarget = typeof document !== "undefined" ? document.body : null;
		const containerClasses = classnames(classes.container, "selector selector-combo-box", readonlyClass, labelClass, viewClass, darkClass);
		const labelClasses = classnames(
			"MuiFormLabel-root",
			"MuiFormLabel-sizeSmall",
			"MuiFormLabel-filled",
			"MuiInputLabel-root",
			"MuiInputLabel-formControl",
			"MuiInputLabel-animated",
			"MuiInputLabel-shrink",
			"MuiInputLabel-sizeSmall",
			"MuiInputLabel-outlined",
			this.state.focused ? "Mui-focused" : undefined,
			isDark ? "dark" : undefined
		);
		const inputClasses = classnames(
			"MuiInputBase-root",
			"MuiInputBase-sizeSmall",
			"MuiOutlinedInput-root",
			"MuiOutlinedInput-sizeSmall",
			"MuiInputBase-formControl",
			"MuiInputBase-adornedEnd",
			this.state.focused ? "Mui-focused" : undefined,
			this.state.readonly ? "Mui-disabled" : undefined,
			labelClass,
			viewClass,
			darkClass
		);

		return (
			<div id={this.props.id + "-container"} className={containerClasses} style={{...this.style()}}>
				{this.renderTraceConsent()}
				<div className="MuiFormControl-root MuiTextField-root" style={{width:"100%"}}>
					<label className={labelClasses} data-shrink="true">
						{label != null && label !== "" ? this.translate(label) : ""}
					</label>

					<div className={inputClasses} style={{width:"100%"}}>
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
								menuPortalTarget={menuPortalTarget}
								menuPosition="fixed"
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
										text: isDark ? "#e2e8f0" : theme.colors.text,
										primary: isDark ? "#90caf9" : theme.colors.primary,
										primary25: isDark ? "rgba(27,36,47,0.98)" : theme.colors.primary25,
										primary50: isDark ? "rgba(16,43,71,0.92)" : theme.colors.primary50,
										primary75: isDark ? "#90caf9" : theme.colors.primary75,
										neutral0: isDark ? "#141b24" : theme.colors.neutral0,
										neutral5: isDark ? "#141b24" : theme.colors.neutral5,
										neutral10: isDark ? "#1b242f" : theme.colors.neutral10,
										neutral20: isDark ? "rgba(148,163,184,0.28)" : theme.colors.neutral20,
										neutral30: isDark ? "rgba(191,219,254,0.42)" : theme.colors.neutral30,
										neutral40: isDark ? "rgba(226,232,240,0.62)" : theme.colors.neutral40,
										neutral50: isDark ? "#cbd5e1" : theme.colors.neutral50,
										neutral60: isDark ? "#e2e8f0" : theme.colors.neutral60,
										neutral70: isDark ? "#e2e8f0" : theme.colors.neutral60,
										neutral80: isDark ? "#f8fafc" : theme.colors.neutral80,
										neutral90: isDark ? "#f8fafc" : theme.colors.neutral90,
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
		const color = theme != null && theme.palette != null && theme.palette.mode === "dark" ? "#ffffffb3" : "black";
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
