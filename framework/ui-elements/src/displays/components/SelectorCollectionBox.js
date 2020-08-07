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
    }
});

const SelectorCollectionBoxMinHeight = 300;
const SelectorCollectionBoxMinWidth = 300;

class SelectorCollectionBox extends AbstractSelectorCollectionBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorCollectionBoxNotifier(this);
		this.requester = new SelectorCollectionBoxRequester(this);
		this.triggerComponent = React.createRef();
		this.state = {
		    ...this.state,
		    selection: this.traceValue() ? this.traceValue() : [],
            multipleSelection: this.props.multipleSelection != null ? this.props.multipleSelection : false,
            opened: false
		}
	};

	render() {
		const { classes, theme } = this.props;
		const multiple = this.state.multipleSelection;
		const label = this.props.label;
		const items = this.items();
		const value = this.selection(items);
		const color = this.state.readonly ? theme.palette.grey.primary : "inherit";

	    return (
			<div className={classes.container} style={this.style()}>
                {this.renderTraceConsent()}
				{label != null && label !== "" ? <div className={classes.label} style={{color:color}}>{label}</div> : undefined }
				<Select isMulti={multiple} isDisabled={this.state.readonly} isSearchable
						closeMenuOnSelect={!multiple} autoFocus={this.props.focused} menuIsOpen={this.state.opened}
						placeholder={this.selectMessage()}
						className="basic-multi-select" classNamePrefix="select"
                        components={{ Option: this.renderOption.bind(this), MenuList: this.renderDialog.bind(this)}}
						value={value} options={items}
						onChange={this.handleChange.bind(this)}
						onInputChange={this.handleSearch.bind(this)}
						onMenuOpen={this.handleOpen.bind(this)}
						onMenuClose={this.handleClose.bind(this)}/>
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

	refreshSelection = (selection) => {
		this.setState({ selection: selection });
	};

	refreshMultipleSelection = (multipleSelection) => {
		this.setState({ multipleSelection });
	};

    open = () => {
        this.setState({ opened: true });
        this.requester.opened();
    };

    close = () => {
        this.setState({ opened: false });
    };

    handleChange = (value, method) => {
        switch (method.action) {
            case 'pop-value' : { this.requester.unSelect(method.removedValue.value); break; }
            case 'remove-value' : { this.requester.unSelect(method.removedValue.value); break; }
            case 'clear' : { this.requester.clearSelection(); break; }
        }
    };

    handleSearch = (value) => {
        this.open();
        Delayer.execute(this, () => this.requester.search(value), 500);
    };

    handleOpen = (e) => {
        if (this.state.readonly) return;
        if (this.state.trigger != null) return;
        this.open();
    };

    handleClose = (e) => {
        this.close();
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