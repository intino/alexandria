import React from "react";
import { OutlinedInput, Popover, InputAdornment, Icon } from '@material-ui/core';
import { KeyboardArrowDown } from '@material-ui/icons';
import { withStyles } from '@material-ui/core/styles';
import AbstractSelectorCollectionBox from "../../../gen/displays/components/AbstractSelectorCollectionBox";
import SelectorCollectionBoxNotifier from "../../../gen/displays/notifiers/SelectorCollectionBoxNotifier";
import SelectorCollectionBoxRequester from "../../../gen/displays/requesters/SelectorCollectionBoxRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import classNames from 'classnames';
import { withSnackbar } from 'notistack';
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	trigger : {
	    width: '100% !important',
	    background: 'white'
	},
	popover : {
	    marginTop: '10px'
	},
	content : {
	    padding: '10px'
	},
	search : {
		padding: "0 10px",
		marginBottom: "5px"
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
            readonly: this.props.readonly,
		    selected: null,
		    trigger: null,
		}
	};

	render() {
		const { classes } = this.props;
		const opened = this.state.trigger != null;
	    const trigger = this.state.trigger;
	    const height = this._height() + "px";
	    const width = this._width() + "px";
		const label = this.props.label;
	    return (
            <React.Fragment>
                {label != null && label !== "" ? <div className={classes.label}>{label}</div> : undefined }
                <OutlinedInput className={classes.trigger} fullWidth variant="outlined" margin="dense"
                           value={this.state.selected} autoFocus
                           onClick={this.handleOpen.bind(this)}
                           onChange={this.handleChange.bind(this)}
                           inputRef={input => input && input.focus()}
                           readOnly={this.state.readonly}
                           style={this.style()}
                           endAdornment={
                                <InputAdornment position="end">
                                    <Icon edge="end"><KeyboardArrowDown /></Icon>
                                </InputAdornment>
                           }
                           ref={this.triggerComponent}>
               </OutlinedInput>
                <Popover className={classes.popover} open={opened} anchorEl={trigger != null ? trigger : undefined} onClose={this.handleClose.bind(this)}
                    anchorOrigin={{
                      vertical: 'bottom',
                      horizontal: 'center',
                    }}
                    transformOrigin={{
                      vertical: 'top',
                      horizontal: 'center',
                    }}>
                    <div className={classNames("layout vertical flexible", classes.content)} style={{width:width,height:height,...this.style()}}>
                        <div className={classes.search}>{this.renderInstances()}</div>
                        <div className="layout vertical flex" style={{height:'calc(100% - 55px)'}}>{this.props.children}</div>
                    </div>
                </Popover>
            </React.Fragment>
        );
    };

	refreshSelected = (value) => {
	    this.setState({ selected: value, trigger: null });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

    open = () => {
        this.setState({ trigger : this.triggerComponent.current});
        this.requester.opened();
    };

    close = () => {
        this.setState({ trigger: null });
    };

    handleChange = (e) => {
        this.requester.search(e.value);
    };

    handleOpen = (e) => {
        if (this.state.readonly) return;
        if (this.state.trigger != null) return;
        this.open();
    };

    handleClose = () => {
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
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorCollectionBox));
DisplayFactory.register("SelectorCollectionBox", withStyles(styles, { withTheme: true })(withSnackbar(SelectorCollectionBox)));