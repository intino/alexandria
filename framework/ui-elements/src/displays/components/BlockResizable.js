import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockResizable from "../../../gen/displays/components/AbstractBlockResizable";
import BlockResizableNotifier from "../../../gen/displays/notifiers/BlockResizableNotifier";
import BlockResizableRequester from "../../../gen/displays/requesters/BlockResizableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Block from "./Block";
import BlockBehavior from "./behaviors/BlockBehavior";
import { PanelGroup, Panel, PanelResizeHandle } from "react-resizable-panels";
import 'alexandria-ui-elements/res/styles/components/blockresizable/styles.css';
import Theme from "app-elements/gen/Theme";

const styles = theme => ({});

class BlockResizable extends AbstractBlockResizable {

	constructor(props) {
		super(props);
		this.notifier = new BlockResizableNotifier(this);
		this.requester = new BlockResizableRequester(this);
		this.panelGroup = React.createRef();
		this.state = {
		    ...this.state,
		    childrenVisibility: {},
		}
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
		return (
			<Block
				layout={this.props.layout}
				width={this.props.width}
				height={this.props.height}
				spacing={this.props.spacing}>
				{this.renderChildren()}
			</Block>
		);
	};

	renderChildren = () => {
		const children = React.Children.map(this.props.children, (child, i) => { return this.renderChild(child, i); });
		return (
		    <PanelGroup ref={this.panelGroup} direction={this._direction()} style={this.style()}>
		        {children}
		    </PanelGroup>
		);
	};

	renderChild = (child, index) => {
	    if (!this._isVisible(child)) return (<React.Fragment/>);
	    return (
            <React.Fragment>
                {index > 0 && <PanelResizeHandle className="ResizeHandle" style={{background:this._color()}}/>}
                <Panel defaultSize={this._size(child)} minSize={this.props.minSize != null ? this.props.minSize : 20}>{this.renderChildElement(child)}</Panel>
            </React.Fragment>
        );
	};

	_isVisible = (child) => {
	    const id = this._id(child);
	    if (this.state.childrenVisibility[id] != null) return this.state.childrenVisibility[id];
	    return child.props.visible !== false;
	};

	_id = (child) => {
	    if (child.props.id.indexOf(".") == -1) return child.props.id;
	    const parts = child.props.id.split(".");
	    return parts[parts.length-1];
	};

	renderChildElement = (child) => {
	    if (child.props.width == null) return child;
	    const result = parseInt(child.props.width.replace("%", "").replace("px", ""));
        let props = {};
        if (this._isHorizontalDirection()) props.width = "auto";
        else props.height = "100%";
        return React.cloneElement(child, { ...child.props, ...props } );
	};

	refreshLayout = (info) => {
	    if (this.panelGroup.current == null) return;
	    this.panelGroup.current.setLayout(info);
	}

	refreshChildVisibility = (info) => {
	    const childrenVisibility = this.state.childrenVisibility;
	    childrenVisibility[info.child] = info.visible;
	    this.setState({childrenVisibility});
	}

	_size = (child) => {
	    const size = this._isHorizontalDirection() ? child.props.width : child.props.height;
	    if (size == null) return -1;
	    return parseInt(size.replace("%", "").replace("px", ""));
	};

	_color = () => {
		const theme = Theme.get();
	    const defaultColor = theme.isDark() ? "black" : "#aaa";
	    if (this.props.color == null) return defaultColor;
	    if (theme.isDark()) return this.props.darkColor != null ? this.props.darkColor : defaultColor;
	    else return this.props.color != null ? this.props.color : defaultColor;
	};

	_direction = () => {
	    return this.props.layout.indexOf("horizontal") !== -1 ? "horizontal" : "vertical";
	};

	_isHorizontalDirection = () => {
	    return this._direction() == "horizontal";
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(BlockResizable));
DisplayFactory.register("BlockResizable", withStyles(styles, { withTheme: true })(withSnackbar(BlockResizable)));