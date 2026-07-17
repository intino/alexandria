import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {Drawer} from '@mui/material';
import AbstractBlockDrawer from "../../../gen/displays/components/AbstractBlockDrawer";
import BlockDrawerNotifier from "../../../gen/displays/notifiers/BlockDrawerNotifier";
import BlockDrawerRequester from "../../../gen/displays/requesters/BlockDrawerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import classNames from "classnames";
import Delayer from '../../util/Delayer';
import Theme from "app-elements/gen/Theme";

const styles = theme => ({
	drawer: {
		flexShrink: 0,
		whiteSpace: 'nowrap',
	},
	drawerPaper: {
		overflowX: 'hidden',
		background: theme.palette.mode === 'dark'
			? 'linear-gradient(180deg, rgba(24,31,45,0.96) 0%, rgba(15,21,32,0.98) 100%)'
			: 'linear-gradient(180deg, rgba(255,255,255,0.98) 0%, rgba(246,249,253,0.98) 100%)',
		backdropFilter: 'blur(18px)',
		boxShadow: theme.palette.mode === 'dark'
			? '0 18px 40px rgba(0,0,0,0.34)'
			: '0 18px 40px rgba(15, 23, 42, 0.12)',
	},
	drawerPaperLeft: {
		borderTopLeftRadius: 0,
		borderBottomLeftRadius: 0,
		borderTopRightRadius: theme.spacing(3),
		borderBottomRightRadius: theme.spacing(3),
	},
	drawerPaperRight: {
		borderTopLeftRadius: theme.spacing(3),
		borderBottomLeftRadius: theme.spacing(3),
		borderTopRightRadius: 0,
		borderBottomRightRadius: 0,
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
		width: theme.spacing(7),
		boxShadow: theme.palette.mode === 'dark'
			? '0 14px 30px rgba(0,0,0,0.28)'
			: '0 14px 30px rgba(15, 23, 42, 0.10)',
	},
});

class BlockDrawer extends AbstractBlockDrawer {

	constructor(props) {
		super(props);
		this.notifier = new BlockDrawerNotifier(this);
		this.requester = new BlockDrawerRequester(this);
		this.state = {
    		opened: false,
	    	temporaryOpened: false,
	    	...this.state
		}
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
		const {classes} = this.props;
		const opened = this._isOpened();
		const anchor = this._anchor();
		const drawerClass = this.props.variant === "PersistentAndMini" ? (opened ? classes.drawerOpen : classes.drawerClose) : undefined;
		const anchorClass = anchor === "right" ? classes.drawerPaperRight : classes.drawerPaperLeft;
		const paperClass = this.props.variant === "PersistentAndMini" ? classNames(classes.drawerPaper, anchorClass, drawerClass) : drawerClass;
		return (
			<Drawer style={this.style()} open={opened}
					anchor={anchor}
					variant={this._variant()}
					className={drawerClass && classNames(classes.drawer, drawerClass)}
					slotProps={{ paper: { className: paperClass, style: this.paperStyle() } }}
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
	    Delayer.execute(this, () => this.setState({temporaryOpened: true}), 5000);
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
		this.setState({ opened: opened, temporaryOpened: null });
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

	paperStyle = () => {
		const result = this.style() != null ? {...this.style()} : {};
		if (this.props.variant === "PersistentAndMini") {
			result.border = Theme.get().palette.mode === "dark"
				? "1px solid rgba(148, 163, 184, 0.16)"
				: "1px solid rgba(148, 163, 184, 0.18)";
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
