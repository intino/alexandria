import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractOpenPopover from "../../../gen/displays/components/AbstractOpenPopover";
import OpenPopoverNotifier from "../../../gen/displays/notifiers/OpenPopoverNotifier";
import OpenPopoverRequester from "../../../gen/displays/requesters/OpenPopoverRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import Actionable from "./Actionable";

const styles = theme => ({});

class OpenPopover extends AbstractOpenPopover {
    static CloseDelay = 300;

	constructor(props) {
		super(props);
		this.notifier = new OpenPopoverNotifier(this);
		this.requester = new OpenPopoverRequester(this);
		this.closeTimeout = null;
		this.lastPointerPosition = null;
		this.isTriggerHovered = false;
		this.isPopoverHovered = false;
		this.isPopoverFocused = false;
		this.state = {
		    ...this.state,
		    triggerId : null,
		};
	};

	componentDidMount() {
		super.componentDidMount();
		document.addEventListener("alexandria-popover-enter", this.handlePopoverEnter);
		document.addEventListener("alexandria-popover-leave", this.handlePopoverLeave);
		document.addEventListener("alexandria-popover-focus", this.handlePopoverFocus);
		document.addEventListener("alexandria-popover-blur", this.handlePopoverBlur);
		document.addEventListener("mousemove", this.handleDocumentMouseMove);
	};

	componentWillUnmount() {
		document.removeEventListener("alexandria-popover-enter", this.handlePopoverEnter);
		document.removeEventListener("alexandria-popover-leave", this.handlePopoverLeave);
		document.removeEventListener("alexandria-popover-focus", this.handlePopoverFocus);
		document.removeEventListener("alexandria-popover-blur", this.handlePopoverBlur);
		document.removeEventListener("mousemove", this.handleDocumentMouseMove);
		this.clearCloseTimeout();
		if (super.componentWillUnmount != null) super.componentWillUnmount();
	};

    refreshTriggerId = (triggerId) => {
		if (triggerId == null) {
			this.clearCloseTimeout();
			this.isTriggerHovered = false;
			this.isPopoverHovered = false;
			this.isPopoverFocused = false;
		}
        this.setState({triggerId});
    };

    triggerId = () => {
        if (this.state.triggerId != null) return this.state.triggerId;
        return this.props.id;
    };

	clickEvent = () => {
	    return this.props.triggerEvent === "MouseClick" ? this.handleClick.bind(this) : this.handleVoidClick.bind(this);
	};

	mouseEnterEvent = () => {
	    return this.props.triggerEvent === "MouseOver" ? this.handleTriggerEnter : null;
	};

	mouseLeaveEvent = () => {
	    return this.props.triggerEvent === "MouseOver" ? this.handleTriggerLeave : null;
	};

	openPopover = (e) => {
	    this.clearCloseTimeout();
		this.isPopoverHovered = false;
		this.isPopoverFocused = false;
        this.execute();
	};

	closePopover = (e) => {
		if (this.props.triggerEvent !== "MouseOver") {
			this.requester.closePopover();
			return;
		}
		this.scheduleClosePopover();
	};

	handleTriggerEnter = (event) => {
		this.updatePointerPosition(event);
		this.isTriggerHovered = true;
		this.evaluateCloseState();
		this.openPopover(event);
	};

	handleTriggerLeave = (event) => {
		this.isTriggerHovered = false;
		this.updatePointerPosition(event);
		if (this.isLeavingTowardActivePopover(event)) return;
		this.evaluateCloseState();
	};

	clearCloseTimeout = () => {
		if (this.closeTimeout == null) return;
		window.clearTimeout(this.closeTimeout);
		this.closeTimeout = null;
	};

	scheduleClosePopover = () => {
		this.clearCloseTimeout();
		if (this.isPopoverActive()) return;
		this.closeTimeout = window.setTimeout(() => {
			this.closeTimeout = null;
			this.syncActiveStateFromDom();
			if (this.isPopoverActive()) return;
			this.requester.closePopover();
		}, OpenPopover.CloseDelay);
	};

	isPopoverActive = () => {
		return this.isTriggerHovered || this.isPopoverHovered || this.isPopoverFocused;
	};

	syncActiveStateFromDom = () => {
		const trigger = document.getElementById(this.triggerId());
		const popover = this.getPopoverElement();
		this.isTriggerHovered = this.isPointerInsideElement(trigger);
		this.isPopoverHovered = this.isPointerInsideElement(popover);
		this.isPopoverFocused = popover != null && popover.contains(document.activeElement);
	};

	handleDocumentMouseMove = (event) => {
		this.updatePointerPosition(event);
		if (this.state.triggerId == null || this.props.triggerEvent !== "MouseOver") return;
		this.evaluateCloseState();
	};

	updatePointerPosition = (event) => {
		if (event == null || event.clientX == null || event.clientY == null) return;
		this.lastPointerPosition = { x: event.clientX, y: event.clientY };
	};

	isPointerInsideElement = (element) => {
		if (element == null || this.lastPointerPosition == null) return false;
		const rect = element.getBoundingClientRect();
		const { x, y } = this.lastPointerPosition;
		return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
	};

	evaluateCloseState = () => {
		this.syncActiveStateFromDom();
		if (this.isPopoverActive()) this.clearCloseTimeout();
		else this.scheduleClosePopover();
	};

	matchesPopoverEvent = (event) => {
		return event.detail != null && event.detail.triggerId === this.triggerId();
	};

	getPopoverElement = () => {
		return document.querySelector(`[data-popover-trigger-id="${this.triggerId()}"]`);
	};

	isLeavingTowardActivePopover = (event) => {
		const nextTarget = event.relatedTarget;
		if (nextTarget == null) return false;
		const popover = this.getPopoverElement();
		return popover != null && popover.contains(nextTarget);
	};

	handlePopoverEnter = (event) => {
		if (!this.matchesPopoverEvent(event)) return;
		this.updatePointerPosition(event);
		this.isPopoverHovered = true;
		this.evaluateCloseState();
	};

	handlePopoverLeave = (event) => {
		if (!this.matchesPopoverEvent(event)) return;
		this.updatePointerPosition(event);
		this.isPopoverHovered = false;
		this.evaluateCloseState();
	};

	handlePopoverFocus = (event) => {
		if (!this.matchesPopoverEvent(event)) return;
		this.isPopoverFocused = true;
		this.evaluateCloseState();
	};

	handlePopoverBlur = (event) => {
		if (!this.matchesPopoverEvent(event)) return;
		this.isPopoverFocused = false;
		this.evaluateCloseState();
	};

	handleVoidClick = (e) => {
	    e.stopPropagation();
	    return false;
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(OpenPopover));
DisplayFactory.register("OpenPopover", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(OpenPopover)));
