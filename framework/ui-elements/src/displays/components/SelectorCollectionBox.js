import React from "react";
import Select, {components} from "react-select";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractSelectorCollectionBox from "../../../gen/displays/components/AbstractSelectorCollectionBox";
import SelectorCollectionBoxNotifier from "../../../gen/displays/notifiers/SelectorCollectionBoxNotifier";
import SelectorCollectionBoxRequester from "../../../gen/displays/requesters/SelectorCollectionBoxRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Delayer from '../../util/Delayer';
import classnames from 'classnames';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import 'alexandria-ui-elements/res/styles/layout.css';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import BrowserUtil from 'alexandria-ui-elements/src/util/BrowserUtil';
import Theme from "app-elements/gen/Theme";
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import {fieldPalette} from "./FieldStyles";

function selectorCollectionBoxViewStyles(theme) {
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
        singleValue: (provided, state) => ({
            ...provided,
            color: palette.textColor,
        }),
        valueContainer: (provided, state) => ({
            ...provided,
            padding: "0 13px",
            height: "50px",
        }),
        input: (provided) => ({
            ...provided,
            margin: 0,
            paddingTop: 0,
            paddingBottom: 0,
        }),
        menu: provided => ({ ...provided, zIndex: 9999 }),
        multiValue: (provided, state) => ({
            ...provided,
            borderRadius:'10px',
            margin: "0 8px 0 0",
            background: state.isDisabled ? "none" : palette.hoverBackground
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
        }),
        placeholder: (provided, state) => ({
            ...provided,
            display: state.isDisabled ? 'none' : 'inherit',
            color: palette.placeholderColor,
        }),
        menuList: (provided) => ({
            ...provided,
            background: menuBackground,
        }),
        option: (styles, { isDisabled, isFocused, isSelected }) => {
            return {
                ...styles,
                backgroundColor: isSelected ? optionSelectedBackground : (isFocused ? optionHoverBackground : menuBackground),
                color: isDisabled ? palette.disabledText : (isSelected ? optionSelectedColor : palette.textColor),
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
    content : {
        padding: '10px'
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
    other : {
        color: theme.palette.primary.main,
        cursor: "pointer",
        marginTop: "2px",
        marginRight: "2px",
    },
});

const SelectorCollectionBoxMinHeight = 300;
const SelectorCollectionBoxMinWidth = 300;

class SelectorCollectionBox extends AbstractSelectorCollectionBox {

    constructor(props) {
        super(props);
        this.notifier = new SelectorCollectionBoxNotifier(this);
        this.requester = new SelectorCollectionBoxRequester(this);
        this.searchComponent = React.createRef();
        this.triggerComponent = React.createRef();
        this.state = {
            ...this.state,
            multipleSelection: this.props.multipleSelection != null ? this.props.multipleSelection : false,
            opened: false
        }
    };

    render() {
        if (!this.state.visible) return (<React.Fragment/>);

        const { classes } = this.props;
        const runtimeTheme = Theme.get();
        const theme = runtimeTheme != null ? runtimeTheme : this.props.theme;
        const multiple = this.state.multipleSelection;
        const label = this.props.label;
        const items = this.items();
        const value = this.selection(items);
        const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
        const readonlyClass = this.state.readonly ? "readonly" : "";
        const labelClass = label == null || label === "" ? "no-label" : undefined;
        const darkClass = isDark ? "dark" : undefined;
        const menuPortalTarget = typeof document !== "undefined" ? document.body : null;
        const containerClasses = classnames(classes.container, "selector selector-combo-box selector-collection-box", readonlyClass, labelClass, darkClass);
        let selectComponents = { Option: this.renderOption.bind(this), MenuList: this.renderDialog.bind(this), MultiValueLabel: this.renderMultiValueLabel.bind(this) };
        if (this.openOnInput()) selectComponents = { DropdownIndicator:() => null, IndicatorSeparator:() => null, ...selectComponents };
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
            label == null || label === "" ? "no-label" : undefined,
            isDark ? "dark" : undefined
        );

        return (
            <div id={this.props.id + "-container"} className={containerClasses} style={{...this.style()}}>
                {this.renderTraceConsent()}

                <div className="MuiFormControl-root MuiTextField-root" style={{width:"100%",position:'relative'}}>
                    <label className={labelClasses} data-shrink="true">
                        {label != null && label !== "" ? this.translate(label) : ""}
                    </label>

                    <div className={inputClasses} style={{width:"100%"}}>
                        <OutlinedInput style={{display:"none"}}></OutlinedInput>
                        <InputLabel style={{display:"none"}}></InputLabel>

                        <Select ref={this.searchComponent} isMulti={multiple} isDisabled={this.state.readonly} isSearchable
                                closeMenuOnSelect={!multiple} autoFocus={this.props.focused} menuIsOpen={this.state.opened}
                                placeholder={this.selectMessage()}
                                className="basic-multi-select" classNamePrefix="select"
                                components={selectComponents}
                                menuPortalTarget={menuPortalTarget}
                                menuPosition="fixed"
                                value={value} options={items}
                                onChange={this.handleChange.bind(this)}
                                onFocus={this.handleFocus.bind(this)}
                                onBlur={this.handleBlur.bind(this)}
                                onInputChange={this.handleSearch.bind(this)}
                                onMenuOpen={this.handleOpen.bind(this)}
                                onMenuClose={this.handleClose.bind(this)}
                                styles={selectorCollectionBoxViewStyles(theme)}
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
                {this.props.allowOther && !this.state.readonly && <div className="layout vertical end"><a className={classes.other} onClick={this.handleAllowOther.bind(this)}>{label != null ? this.translate("Add") + " " + this.translate(label).toLowerCase() : this.translate("Add other")}</a></div>}
            </div>
        );
    };

    renderOption = (options) => {
        const { data, isDisabled, ...props } = options;
        const item = data.item;
        const { classes } = this.props;
        return !isDisabled ? (
            <components.Option {...props} className={classes.container}><div>{item}</div></components.Option>
        ) : null;
    };

    renderDialog = (props) => {
        const { classes } = this.props;
        const height = this._height() + "px";
        const width = this._width() + "px";
        return (
            <React.Fragment>
                <div className={classnames("layout vertical flexible", classes.content)} style={{width:'100%',height:height,...this.style()}}>
                    <div className={classes.search}>{this.renderInstances()}</div>
                    <div className="layout vertical flex" style={{height:'calc(100% - 55px)'}}>{this.props.children}</div>
                </div>
                <components.MenuList {...props}>
                    <div style={{display:'none'}}>{props.children}</div>
                </components.MenuList>
            </React.Fragment>
        );
    }

    renderMultiValueLabel = (props) => {
        const { classes } = this.props;
        const theme = Theme.get();
        const color = theme != null && theme.palette != null && theme.palette.mode === "dark" ? "#ffffffb3" : "black";
        const background = this.state.readonly ? "none !important" : "inherited";
        return (<a className={classnames(classes.multiValueLabel, this.state.readonly ? classes.multiValueLabelReadonly : undefined)} style={{color:color,background:background}} onClick={this.handleOptionClick.bind(this, props)}>{props.data.value}</a>);
    };

    refreshSelection = (selection) => {
        this.setState({ selection: selection });
        if (this.searchComponent.current == null) return;
        var isFocused = (document.activeElement === this.searchComponent.current);
        if (isFocused && !this.state.multipleSelection) {
            this.searchComponent.current.blur();
            this.searchComponent.current.focus();
        }
    };

    refreshMultipleSelection = (multipleSelection) => {
        this.setState({ multipleSelection });
    };

    open = () => {
        if (this.state.opened) return;
        this.setState({ opened: true }, () => {
            window.requestAnimationFrame(() => this.requester.opened());
        });
    };

    close = () => {
        this.searchComponent.current.blur();
        this.setState({ opened: false });
    };

    handleOptionClick = (props, e) => {
        e.stopPropagation();
        e.preventDefault();
        this.requester.open(props.data.value);
    };

    handleChange = (value, method) => {
        if (this.state.readonly) return;
        switch (method.action) {
            case 'pop-value' : { this.requester.unSelect(method.removedValue.value); break; }
            case 'remove-value' : { this.requester.unSelect(method.removedValue.value); break; }
            case 'clear' : { this.requester.clearSelection(); break; }
        }
    };

    handleSearch = (value) => {
        if (this.openOnInput() && this.condition == null && (value == null || value === "" || value.length < 3)) return;
        this.condition = value;
        this.open();
        Delayer.execute(this, () => this.requester.search(value), 500);
    };

    handleOpen = (e) => {
        if (this.openOnInput() && (this.condition == null || this.condition === "" || this.condition.length < 3)) return;
        if (this.state.readonly) return;
        if (this.state.trigger != null) return;
        this.open();
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

    handleClose = (e) => {
        if (BrowserUtil.isMobile()) window.setTimeout(() => this.close(), 500);
        else this.close();
    };

    handleAllowOther = () => {
        this.requester.selectOther();
    };

    openOnInput = () => {
        return this.props.openMode === "Input";
    };

    style() {
        var result = super.style();
        if (result == null) result = {};
        if (this._widthDefined() && result.width == null) result.width = this.props.width;
        if (this._heightDefined() && result.height == null) result.height = this.props.height;
        return result;
    };

    _height = () => {
        const height = this.triggerComponent.current != null ? this.triggerComponent.current.offsetHeight : SelectorCollectionBoxMinHeight;
        return height > SelectorCollectionBoxMinHeight ? height : SelectorCollectionBoxMinHeight;
    };

    _width = () => {
        const width = this.triggerComponent.current != null ? this.triggerComponent.current.offsetWidth : SelectorCollectionBoxMinWidth;
        return width > SelectorCollectionBoxMinWidth ? width : SelectorCollectionBoxMinWidth;
    };

    items = () => {
        const selection = this.state.selection;
        const result = [];
        for (let i=0; i<selection.length; i++) {
            result.push({ value: selection[i], label: selection[i], item: selection[i] });
        }
        return result;
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

    selectMessage = () => {
        const placeholder = this.props.placeholder;
        return this.translate(placeholder != null && placeholder !== "" ? placeholder : "Select an option");
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorCollectionBox));
DisplayFactory.register("SelectorCollectionBox", withStyles(styles, { withTheme: true })(withSnackbar(SelectorCollectionBox)));
