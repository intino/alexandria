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
	drawerShell: {
		position: 'relative',
		overflow: 'visible',
		display: 'flex',
		alignSelf: 'stretch',
		minHeight: 0,
		height: '100%',
		maxHeight: '100vh',
	},
	drawerShellLeft: {
		justifyContent: 'flex-start',
	},
	drawerShellRight: {
		justifyContent: 'flex-end',
	},
	drawer: {
		flexShrink: 0,
		whiteSpace: 'nowrap',
		overflow: 'visible',
		display: 'flex',
		alignSelf: 'stretch',
		height: '100%',
		minHeight: '100%',
		maxHeight: '100vh',
	},
	drawerPaper: {
		position: 'relative',
		overflowX: 'visible',
		alignSelf: 'stretch',
		height: '100%',
		maxHeight: '100%',
		minHeight: '100%',
		boxSizing: 'border-box',
		background: theme.palette.mode === 'dark'
			? 'linear-gradient(180deg, rgba(24,31,45,0.96) 0%, rgba(15,21,32,0.98) 100%)'
			: 'linear-gradient(180deg, rgba(255,255,255,0.98) 0%, rgba(246,249,253,0.98) 100%)',
		backdropFilter: 'blur(18px)',
		boxShadow: 'none',
	},
	drawerPaperLeft: {
		borderTopLeftRadius: 0,
		borderBottomLeftRadius: 0,
		borderTopRightRadius: 0,
		borderBottomRightRadius: 0,
	},
	drawerPaperRight: {
		borderTopLeftRadius: 0,
		borderBottomLeftRadius: 0,
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
		boxShadow: 'none',
	},
	drawerInner: {
		position: 'relative',
		display: 'flex',
		flexDirection: 'column',
		width: '100%',
		height: '100%',
		minHeight: 0,
		overflow: 'visible',
	},
	drawerContent: {
		position: 'relative',
		display: 'flex',
		flexDirection: 'column',
		flex: 1,
		width: '100%',
		height: '100%',
		minHeight: 0,
		overflow: 'hidden',
		'& > *': {
			minHeight: 0,
		},
		'& > *:last-child': {
			flex: 1,
			minHeight: 0,
		},
	},
	resizeHandle: {
		position: 'absolute',
		top: theme.spacing(2),
		bottom: theme.spacing(2),
		width: '0.8rem',
		display: 'flex',
		alignItems: 'center',
		justifyContent: 'center',
		cursor: 'col-resize',
		touchAction: 'none',
		userSelect: 'none',
		zIndex: 2,
		background: 'transparent',
		opacity: 0.9,
		transition: 'background-color 0.2s ease, opacity 0.2s ease',
		'&::before': {
			content: '""',
			position: 'absolute',
			top: 0,
			bottom: 0,
			left: '50%',
			width: '1px',
			transform: 'translateX(-50%)',
			background: 'color-mix(in srgb, var(--drawer-resize-color, #94a3b8) 24%, transparent)',
			opacity: 0.5,
			transition: 'opacity 0.2s ease, background-color 0.2s ease',
		},
		'&:hover, &$resizeHandleActive': {
			opacity: 1,
		},
		'&:hover::before, &$resizeHandleActive::before': {
			opacity: 0.9,
			background: 'color-mix(in srgb, var(--drawer-resize-color, #94a3b8) 40%, transparent)',
		},
	},
	resizeHandleLeft: {
		left: '100%',
	},
	resizeHandleRight: {
		right: '100%',
	},
	resizeHandleIndicator: {
		position: 'relative',
		zIndex: 1,
		width: '0.32rem',
		height: '2.8rem',
		borderRadius: '999px',
		background: 'color-mix(in srgb, var(--drawer-resize-color, #94a3b8) 78%, white 22%)',
		boxShadow: '0 1px 0 rgba(255, 255, 255, 0.18) inset, 0 4px 14px rgba(15, 23, 42, 0.16)',
		transition: 'transform 0.2s ease, box-shadow 0.2s ease, background-color 0.2s ease',
	},
	resizeHandleActive: {},
	resizeHandleIndicatorActive: {
		transform: 'scale(1.04)',
		boxShadow: '0 1px 0 rgba(255, 255, 255, 0.22) inset, 0 8px 18px rgba(15, 23, 42, 0.22)',
	},
});

class BlockDrawer extends AbstractBlockDrawer {

	constructor(props) {
		super(props);
		this.notifier = new BlockDrawerNotifier(this);
		this.requester = new BlockDrawerRequester(this);
		this.paperRef = React.createRef();
		this.handleRef = React.createRef();
		this.resizeState = null;
		this.state = {
    		opened: false,
	    	temporaryOpened: false,
	    	dragging: false,
	    	hoveringResizeHandle: false,
	    	resizedWidth: null,
	    	...this.state
		}
	};

	componentWillUnmount() {
		this.removeResizeListeners();
	}

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
		const {classes} = this.props;
		const opened = this._isOpened();
		const anchor = this._anchor();
		const baseDrawerClass = this.props.variant === "PersistentAndMini" ? (opened ? classes.drawerOpen : classes.drawerClose) : undefined;
		const drawerClass = this.state.dragging ? undefined : baseDrawerClass;
		const shellAnchorClass = anchor === "right" ? classes.drawerShellRight : classes.drawerShellLeft;
		const anchorClass = anchor === "right" ? classes.drawerPaperRight : classes.drawerPaperLeft;
		const paperClass = this.props.variant === "PersistentAndMini" ? classNames(classes.drawerPaper, anchorClass, drawerClass) : drawerClass;
		return (
			<div className={classNames(classes.drawerShell, shellAnchorClass)} style={this.shellStyle()}>
				<Drawer style={this.style()} open={opened}
						anchor={anchor}
						variant={this._variant()}
						className={drawerClass && classNames(classes.drawer, drawerClass)}
						slotProps={{
							docked: { style: { alignSelf: "stretch", height: "100%", minHeight: "100%" } },
							paper: { ref: this.paperRef, className: paperClass, style: this.paperStyle() }
						}}
						onClose={this.toggleDrawer.bind(this)}
						onMouseOver={this.handleMouseOver.bind(this)}
						onMouseOut={this.handleMouseOut.bind(this)}
						>
							<div className={classes.drawerInner}>
								<div className={classes.drawerContent}>
									{this.props.children}
								</div>
							</div>
						</Drawer>
				{this.renderResizeHandle(anchor)}
			</div>
		);
	};

	renderResizeHandle = (anchor) => {
		if (!this._isResizable()) return null;
		const {classes} = this.props;
		const handleClass = anchor === "right" ? classes.resizeHandleRight : classes.resizeHandleLeft;
		const activeClass = this.state.dragging ? classes.resizeHandleActive : undefined;
		const indicatorClass = this.state.dragging ? classes.resizeHandleIndicatorActive : undefined;
		return (
			<div
				ref={this.handleRef}
				className={classNames(classes.resizeHandle, handleClass, activeClass)}
				style={{"--drawer-resize-color": this._handleColor()}}
				onMouseEnter={this.handleResizeHandleEnter}
				onMouseLeave={this.handleResizeHandleLeave}
				onPointerDown={this.handleResizeStart}
				onPointerMove={this.handleResizeMove}
				onPointerUp={this.handleResizeEnd}
				onPointerCancel={this.handleResizeEnd}
			>
				<div className={classNames(classes.resizeHandleIndicator, indicatorClass)}/>
			</div>
		);
	};

	_anchor = () => {
		return this.props.position != null ? this.props.position.toLowerCase() : "left";
	};

	handleMouseOver = () => {
	    if (this.state.dragging || this.state.hoveringResizeHandle) return;
	    Delayer.execute(this, () => this.setState({temporaryOpened: true}), 5000);
	};

	handleMouseOut = () => {
	    if (this.state.dragging || this.state.hoveringResizeHandle) return;
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

	handleResizeStart = (event) => {
		if (!this._isResizable()) return;
		event.preventDefault();
		event.stopPropagation();
		Delayer.stop(this);
		if (event.currentTarget != null && event.currentTarget.setPointerCapture != null && event.pointerId != null) {
			event.currentTarget.setPointerCapture(event.pointerId);
		}
		const width = this._resolvedWidthValue();
		this.resizeState = { originX: event.clientX, width, pointerId: event.pointerId };
		this.setState({dragging: true, resizedWidth: width});
		window.addEventListener("pointermove", this.handleResizeMove);
		window.addEventListener("pointerup", this.handleResizeEnd);
		window.addEventListener("pointercancel", this.handleResizeEnd);
		window.addEventListener("blur", this.handleResizeEnd);
	};

	handleResizeHandleEnter = () => {
		Delayer.stop(this);
		if (this.props.variant === "PersistentAndMini" && !this._isOpened()) this.setState({temporaryOpened: true});
		this.setState({hoveringResizeHandle: true});
	};

	handleResizeHandleLeave = () => {
		if (this.state.dragging) return;
		this.setState({hoveringResizeHandle: false}, () => {
			if (this.props.variant === "PersistentAndMini") this.handleMouseOut();
		});
	};

	handleResizeMove = (event) => {
		if (this.resizeState == null) return;
		const delta = event.clientX - this.resizeState.originX;
		const direction = this._anchor() === "right" ? -1 : 1;
		const width = this._clampWidth(this.resizeState.width + (delta * direction));
		this.setState({resizedWidth: width});
	};

	handleResizeEnd = () => {
		const width = this.state.resizedWidth;
		if (this.handleRef.current != null && this.handleRef.current.hasPointerCapture != null) {
			try {
				const pointerId = this.resizeState != null ? this.resizeState.pointerId : undefined;
				if (pointerId != null && this.handleRef.current.hasPointerCapture(pointerId)) this.handleRef.current.releasePointerCapture(pointerId);
			} catch (e) {
			}
		}
		this.removeResizeListeners();
		if (width != null) this.updateCookie(width, this._widthCookieName());
		this.resizeState = null;
		this.setState({dragging: false}, () => {
			if (this.props.variant === "PersistentAndMini" && !this.state.hoveringResizeHandle) this.handleMouseOut();
		});
	};

	removeResizeListeners = () => {
		window.removeEventListener("pointermove", this.handleResizeMove);
		window.removeEventListener("pointerup", this.handleResizeEnd);
		window.removeEventListener("pointercancel", this.handleResizeEnd);
		window.removeEventListener("blur", this.handleResizeEnd);
	};

	style() {
		let result = super.style();
		if (result == null) result = {};
		if (this._checkWidthDefined()) result.width = this._resolvedWidthStyle();
		if (this._heightDefined()) result.height = this.props.height;
		else if (this._fillsHeight()) result.height = "100%";
		if (this.props.variant === "PersistentAndMini" || this.props.variant === "Permanent") {
			result.position = "relative";
			result.zIndex = 0;
		}
		return result;
	};

	shellStyle = () => {
		const result = {};
		if (this._checkWidthDefined()) result.width = this._resolvedWidthStyle();
		if (this._heightDefined()) result.height = this.props.height;
		else if (this._fillsHeight()) result.height = "100%";
		result.maxHeight = "100vh";
		return result;
	};

	paperStyle = () => {
		const result = this.style() != null ? {...this.style()} : {};
		if (this._checkWidthDefined()) result.width = this._resolvedWidthStyle();
		if (!this._heightDefined() && this._fillsHeight()) result.height = "100%";
		result.maxHeight = "100vh";
		if (this.props.variant === "PersistentAndMini") {
			const borderColor = Theme.get().palette.mode === "dark"
				? "rgba(148, 163, 184, 0.16)"
				: "rgba(148, 163, 184, 0.18)";
			result.borderLeft = "1px solid " + borderColor;
			result.borderRight = "1px solid " + borderColor;
			result.borderTop = "0";
			result.borderBottom = "0";
		}
		return result;
	};

	_checkWidthDefined = () => {
		let defined = this._widthDefined();
		return defined && this.props.variant !== "PersistentAndMini" ? defined : this._isOpened();
	};

	_fillsHeight = () => {
		return this.props.variant === "PersistentAndMini" || this.props.variant === "Permanent";
	};

	_isResizable = () => {
		const variant = this.props.variant;
		return this._isOpened() && this._isHorizontalDrawer() && variant !== null;
	};

	_isHorizontalDrawer = () => {
		const anchor = this._anchor();
		return anchor === "left" || anchor === "right";
	};

	_handleColor = () => {
		const theme = Theme.get();
		const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
		const defaultColor = isDark ? "black" : "#aaa";
		if (this.props.color == null) return defaultColor;
		if (isDark) return this.props.darkColor != null ? this.props.darkColor : defaultColor;
		return this.props.color != null ? this.props.color : defaultColor;
	};

	_widthCookieName = () => {
		return this.props.id + "_drawer_width";
	};

	_resolvedWidthStyle = () => {
		if (this.state.resizedWidth != null) return this.state.resizedWidth;
		if (this._widthDefined()) return this.props.width;
		const cookieWidth = this.getCookie(this._widthCookieName());
		if (cookieWidth != null && cookieWidth !== "") return this._clampWidth(parseFloat(cookieWidth));
		if (this.paperRef.current != null && this.paperRef.current.offsetWidth > 0) return this._clampWidth(this.paperRef.current.offsetWidth);
		return 320;
	};

	_resolvedWidthValue = () => {
		if (this.state.resizedWidth != null) return this.state.resizedWidth;
		if (this._widthDefined()) return this._clampWidth(this._parseWidth(this.props.width));
		const cookieWidth = this.getCookie(this._widthCookieName());
		if (cookieWidth != null && cookieWidth !== "") return this._clampWidth(parseFloat(cookieWidth));
		if (this.paperRef.current != null && this.paperRef.current.offsetWidth > 0) return this._clampWidth(this.paperRef.current.offsetWidth);
		return 320;
	};

	_parseWidth = (value) => {
		if (typeof value === "number") return value;
		if (typeof value !== "string") return 320;
		if (value.endsWith("%")) return (window.innerWidth * parseFloat(value)) / 100;
		const parsed = parseFloat(value.replace("px", ""));
		return Number.isNaN(parsed) ? 320 : parsed;
	};

	_clampWidth = (value) => {
		const min = 180;
		const max = Math.max(min, window.innerWidth - 96);
		return Math.min(max, Math.max(min, value));
	};

	_isOpened = () => {
	    return this.state.temporaryOpened ? this.state.temporaryOpened : this.state.opened;
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(BlockDrawer));
DisplayFactory.register("BlockDrawer", withStyles(styles, { withTheme: true })(withSnackbar(BlockDrawer)));
