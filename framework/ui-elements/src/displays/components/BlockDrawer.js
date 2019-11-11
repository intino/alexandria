import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Drawer } from '@material-ui/core';
import AbstractBlockDrawer from "../../../gen/displays/components/AbstractBlockDrawer";
import BlockDrawerNotifier from "../../../gen/displays/notifiers/BlockDrawerNotifier";
import BlockDrawerRequester from "../../../gen/displays/requesters/BlockDrawerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import classNames from "classnames";
import Delayer from '../../util/Delayer';

const styles = theme => ({
	drawer: {
		flexShrink: 0,
		whiteSpace: 'nowrap',
	},
	drawerOpen: {
		transition: theme.transitions.create('width', {
			easing: theme.transitions.easing.sharp,
			duration: theme.transitions.duration.enteringScreen,
		}),
	},
	drawerClose: {
		transition: theme.transitions.create('width', {
			easing: theme.transitions.easing.sharp,
			duration: theme.transitions.duration.leavingScreen,
		}),
		overflowX: 'hidden',
		width: theme.spacing(5),
		[theme.breakpoints.up('sm')]: {
			width: theme.spacing(7),
		},
	},
});

class BlockDrawer extends AbstractBlockDrawer {
	state = {
		opened: false,
		temporaryOpened: false
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockDrawerNotifier(this);
		this.requester = new BlockDrawerRequester(this);
	};

	render() {
		const {classes} = this.props;
		const opened = this._isOpened();
		const drawerClass = this.props.variant === "PersistentAndMini" ? (opened ? classes.drawerOpen : classes.drawerClose) : undefined;
		return (
			<Drawer style={this.style()} open={opened}
					anchor={this._anchor()}
					variant={this._variant()}
					className={drawerClass && classNames(classes.drawer, drawerClass)}
					classes={{paper: drawerClass}}
					PaperProps={{style: this.style()}}
					onClose={this.toggleDrawer.bind(this)}
					onMouseOver={this.handleMouseOver.bind(this)}
					onMouseOut={this.handleMouseOut.bind(this)}
					>{this.props.children}</Drawer>
		);
	};

	_anchor = () => {
		return this.props.position != null ? this.props.position.toLowerCase() : "left";
	};

	handleMouseOver = () => {
	    Delayer.execute(this, () => this.setState({temporaryOpened: true}), 1200);
	};

	handleMouseOut = () => {
	    Delayer.execute(this, () => this.setState({temporaryOpened: false}), 300);
	};

	_variant = () => {
		const variant = this.props.variant;
		if (variant == null) return "temporary";
		if (variant === "PersistentAndMini") return "permanent";
		return variant.toLowerCase();
	};

	toggleDrawer = () => {
		this.requester.toggle();
	};

	refresh = (opened) => {
		this.setState({opened});
	};

	style() {
		let result = super.style();
		if (result == null) result = {};
		if (this._checkWidthDefined()) result.width = this.props.width;
		if (this._heightDefined()) result.height = this.props.height;
		if (this.props.variant === "PersistentAndMini" || this.props.variant === "Permanent") {
			result.position = "relative";
			result.zIndex = 0;
		}
		return result;
	};

	_checkWidthDefined = () => {
		let defined = this._widthDefined();
		return defined && this.props.variant !== "PersistentAndMini" ? defined : this._isOpened();
	};

	_isOpened = () => {
	    return this.state.temporaryOpened ? this.state.temporaryOpened : this.state.opened;
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(BlockDrawer));
DisplayFactory.register("BlockDrawer", withStyles(styles, { withTheme: true })(withSnackbar(BlockDrawer)));