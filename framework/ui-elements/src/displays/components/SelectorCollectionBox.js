import React from "react";
import Select, { components } from "react-select";
import { withStyles } from '@material-ui/core/styles';
import AbstractSelectorCollectionBox from "../../../gen/displays/components/AbstractSelectorCollectionBox";
import SelectorCollectionBoxNotifier from "../../../gen/displays/notifiers/SelectorCollectionBoxNotifier";
import SelectorCollectionBoxRequester from "../../../gen/displays/requesters/SelectorCollectionBoxRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Delayer from '../../util/Delayer';
import classNames from 'classnames';
import { withSnackbar } from 'notistack';
import 'alexandria-ui-elements/res/styles/layout.css';
import BrowserUtil from 'alexandria-ui-elements/src/util/BrowserUtil';
import Theme from "app-elements/gen/Theme";

function selectorCollectionBoxViewStyles(theme) {
    return {
        singleValue: (provided, state) => ({
            ...provided,
            color: theme.isDark() ? 'white' : '#333',
        }),
        menu: provided => ({ ...provided, zIndex: 9999 }),
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
        }),
        placeholder: (provided, state) => ({
            ...provided,
            display: state.isDisabled ? 'none' : 'inherit',
        }),
        option: (styles, { data, isDisabled, isFocused, isSelected }) => {
            return {
              ...styles,
              color: isDisabled ? '#ccc' : (isSelected ? (theme.isDark() ? "black" : "white") : (theme.isDark() ? "white" : "black"))
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
        color: "#0000008a",
        marginBottom: "5px",
    },
    multiValueLabel : {
        pointerEvents:'all',
        cursor:'pointer',
        color: theme.isDark() ? 'white' : 'black',
        padding:'3px 6px 3px 6px',
        fontSize:'85%',
        textOverflow:'ellipsis',
        whiteSpace:'nowrap',
        boxSizing:'border-box',
        overflow:'hidden',
        borderRadius:'2px',
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

		const { classes, theme } = this.props;
		const multiple = this.state.multipleSelection;
		const label = this.props.label;
		const items = this.items();
		const value = this.selection(items);
		const color = this.state.readonly ? theme.palette.grey.A700 : theme.isDark() ? "#ffffffb3" : "#0000008a";
        const isDark = theme.isDark();
        let components = components= { Option: this.renderOption.bind(this), MenuList: this.renderDialog.bind(this), MultiValueLabel: this.renderMultiValueLabel.bind(this) };
        if (this.openOnInput()) components = { DropdownIndicator:() => null, IndicatorSeparator:() => null, ...components };

	    return (
			<div className={classes.container} style={this.style()}>
                {this.renderTraceConsent()}
				{label != null && label !== "" ? <div className={classes.label} style={{color:color}}>{this.translate(label)}</div> : undefined }
				<Select ref={this.searchComponent} isMulti={multiple} isDisabled={this.state.readonly} isSearchable
						closeMenuOnSelect={!multiple} autoFocus={this.props.focused} menuIsOpen={this.state.opened}
						placeholder={this.selectMessage()}
						className="basic-multi-select" classNamePrefix="select"
                        components={components}
						value={value} options={items}
						onChange={this.handleChange.bind(this)}
						onInputChange={this.handleSearch.bind(this)}
						onMenuOpen={this.handleOpen.bind(this)}
						onMenuClose={this.handleClose.bind(this)}
						styles={selectorCollectionBoxViewStyles(theme)}
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
                <div className={classNames("layout vertical flexible", classes.content)} style={{width:'100%',height:height,...this.style()}}>
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
	    const color = this.state.readonly ? theme.palette.grey.A700 : theme.isDark() ? "#ffffffb3" : "black";
        return (<a className={classes.multiValueLabel} style={{color:color}}onClick={this.handleOptionClick.bind(this, props)}>{props.data.value}</a>);
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
        this.setState({ opened: true });
        this.requester.opened();
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