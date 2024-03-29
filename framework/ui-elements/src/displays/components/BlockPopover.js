import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Popover } from '@material-ui/core';
import AbstractBlockPopover from "../../../gen/displays/components/AbstractBlockPopover";
import BlockPopoverNotifier from "../../../gen/displays/notifiers/BlockPopoverNotifier";
import BlockPopoverRequester from "../../../gen/displays/requesters/BlockPopoverRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import classNames from "classnames";

const styles = theme => ({
    noInteractions: {
        pointerEvents: 'none',
    },
});

class BlockPopover extends AbstractBlockPopover {

	constructor(props) {
		super(props);
		this.notifier = new BlockPopoverNotifier(this);
		this.requester = new BlockPopoverRequester(this);
		this.popover = React.createRef();
		this.state = {
		    triggerId: null,
		    interactionsEnabled: true,
		    ...this.state,
		}
	};

    componentDidMount() {
        super.componentDidMount();
        window.addEventListener('resize', this.relocate.bind(this));
    };

	render() {
		const { classes } = this.props;
		const opened = this.state.triggerId != null;
		const trigger = this.state.triggerId != null ? document.getElementById(this.state.triggerId) : null;
		const drawerClass = this.props.variant === "PersistentAndMini" ? (opened ? classes.drawerOpen : classes.drawerClose) : undefined;
		const className = this.state.interactionsEnabled ? null : classes.noInteractions;
		window.setTimeout(() => this.relocate(), 100);
		return (
			<Popover className={className} open={opened}
			        onClose={this.handleClose.bind(this)}
					anchorEl={trigger != null ? trigger : undefined}
					anchorOrigin={{vertical: this._anchorOriginVertical(),horizontal: this._anchorOriginHorizontal()}}
                    transformOrigin={{vertical: this._transformOriginVertical(), horizontal: this._transformOriginHorizontal()}}
					disableRestoreFocus>
                <div ref={this.popover} className="layout vertical flexible" style={{width:"100%",height:"100%",...this.style()}}>
					{this.props.children}
                </div>
            </Popover>
		);
	};

	refresh = (triggerId) => {
		this.setState({triggerId});
	};

    refreshInteractionsEnabled = (value) => {
        this.setState({interactionsEnabled:value});
    };

	relocate = () => {
	    if (this.popover.current == null) return;
	    this.relocateWidth();
	    this.relocateHeight();
	};

	relocateWidth = () => {
	    if (this.popover.current.parentElement.style.marginLeft != "") this.popover.current.parentElement.style.marginLeft = "";
	    const bounding = this.popover.current.parentElement.getBoundingClientRect();
	    const popoverWidth = bounding.width;
	    const bodyWidth = document.body.offsetWidth;
	    if (bounding.right <= bodyWidth) return;
        this.popover.current.parentElement.style.marginLeft = "-" + (popoverWidth+40) + "px";
	};

	relocateHeight = () => {
	    if (this.popover.current.parentElement.style.marginBottom != "") this.popover.current.parentElement.style.marginBottom = "";
	    const bounding = this.popover.current.parentElement.getBoundingClientRect();
	    const popoverHeight = bounding.height;
	    const bodyHeight = document.body.offsetHeight;
	    if (bounding.bottom <= bodyHeight) return;
        this.popover.current.parentElement.style.marginTop = "-" + (popoverHeight+40) + "px";
	};

	handleClose = () => {
	    this.requester.close();
	};

	_anchorOriginVertical = () => {
	    const position = this.props.position;
	    if (position == "TopLeft") return "top";
	    if (position == "TopCenter") return "top";
	    if (position == "TopRight") return "top";
	    if (position == "RightTop") return "top";
	    if (position == "RightCenter") return "center";
	    if (position == "RightBottom") return "bottom";
	    if (position == "BottomLeft") return "bottom";
	    if (position == "BottomCenter") return "bottom";
	    if (position == "BottomRight") return "bottom";
	    if (position == "LeftTop") return "top";
	    if (position == "LeftCenter") return "center";
	    if (position == "LeftBottom") return "bottom";
	    return "bottom";
	};

	_anchorOriginHorizontal = () => {
	    const position = this.props.position;
	    if (position == "TopLeft") return "left";
	    if (position == "TopCenter") return "center";
	    if (position == "TopRight") return "right";
	    if (position == "RightTop") return "right";
	    if (position == "RightCenter") return "right";
	    if (position == "RightBottom") return "right";
	    if (position == "BottomLeft") return "left";
	    if (position == "BottomCenter") return "center";
	    if (position == "BottomRight") return "right";
	    if (position == "LeftTop") return "left";
	    if (position == "LeftCenter") return "left";
	    if (position == "LeftBottom") return "left";
	    return "center";
	};

	_transformOriginVertical = () => {
	    const position = this.props.position;
	    if (position == "TopLeft") return "bottom";
	    if (position == "TopCenter") return "bottom";
	    if (position == "TopRight") return "bottom";
	    if (position == "RightTop") return "bottom";
	    if (position == "RightCenter") return "center";
	    if (position == "RightBottom") return "top";
	    if (position == "BottomLeft") return "top";
	    if (position == "BottomCenter") return "top";
	    if (position == "BottomRight") return "top";
	    if (position == "LeftTop") return "bottom";
	    if (position == "LeftCenter") return "center";
	    if (position == "LeftBottom") return "top";
	    return "top";
	};

	_transformOriginHorizontal = () => {
	    const position = this.props.position;
	    if (position == "TopLeft") return "center";
	    if (position == "TopCenter") return "center";
	    if (position == "TopRight") return "center";
	    if (position == "RightTop") return "left";
	    if (position == "RightCenter") return "left";
	    if (position == "RightBottom") return "left";
	    if (position == "BottomLeft") return "center";
	    if (position == "BottomCenter") return "center";
	    if (position == "BottomRight") return "center";
	    if (position == "LeftTop") return "right";
	    if (position == "LeftCenter") return "right";
	    if (position == "LeftBottom") return "right";
	    return "center";
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this._widthDefined() && result.width == null) result.width = this.props.width;
		if (this._heightDefined() && result.height == null) result.height = this.props.height;
		return result;
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(BlockPopover));
DisplayFactory.register("BlockPopover", withStyles(styles, { withTheme: true })(withSnackbar(BlockPopover)));