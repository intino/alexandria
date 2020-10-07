import React from "react";
import { Button, IconButton, Tooltip, Typography } from '@material-ui/core';
import { Clear } from '@material-ui/icons';
import AbstractMultiple from "../../../gen/displays/components/AbstractMultiple";
import MultipleNotifier from "../../../gen/displays/notifiers/MultipleNotifier";
import MultipleRequester from "../../../gen/displays/requesters/MultipleRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import ComponentBehavior from "./behaviors/ComponentBehavior";

export default class Multiple extends AbstractMultiple {

	constructor(props) {
		super(props);
		this.notifier = new MultipleNotifier(this);
		this.requester = new MultipleRequester(this);
		this.state = {
		    ...this.state,
		    readonly: false
		};
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const multiple = this.props.multiple;
		const layout = multiple.arrangement.toLowerCase();
		const wrap = multiple.wrap;
		const style = this._style(multiple);
		const height = layout === "horizontal" ? '100%' : 'auto';
		return (
		    <div style={{height:height,...this.style()}}>
                { ComponentBehavior.labelBlock(this.props, "body1", { fontSize:"10pt",color:"#0000008a",marginBottom: "5px" }) }
                <div className={"layout flex " + (wrap ? "wrap " : "") + layout} style={{height:height}}>
                    {this.renderInstances(multiple.instances, this._instanceProps(), style)}
                </div>
                { multiple.editable && this._renderAdd() }
            </div>
        );
	};

    renderInstance = (instance, props, style, index) => {
		const multiple = this.props.multiple;
        return (
            <div key={index} className="layout horizontal center" style={{...style}}>
                <div className="layout flex" style={style}>{React.createElement(DisplayFactory.get(instance.tp), instance.pl)}</div>
                { multiple.editable && this._removeAllowed(index) && this._renderRemove(index) }
            </div>
        );
    };

    _addAllowed = () => {
		const multiple = this.props.multiple;
		return multiple.count == null || multiple.count.max == -1 || this._countItems() < multiple.count.max;
    };

    _removeAllowed = (index) => {
		const multiple = this.props.multiple;
		return multiple.count == null || multiple.count.min == 0 || index >= multiple.count.min;
    };

    _countItems = () => {
        const multiple = this.props.multiple;
        return this.instances(multiple.instances).length;
    };

	_renderAdd = () => {
	    return (
		    <Button style={{marginTop:"10px"}} size="small" color="primary"
		            variant="outlined" disabled={this.state.readonly || !this._addAllowed()} onClick={this.handleAdd.bind(this)}>
				{this.translate("Add")}
			</Button>
	    );
	};

	_renderRemove = (index) => {
	    return (
            <IconButton color="primary" disabled={this.state.readonly} onClick={this.handleRemove.bind(this, index)} size="small">
                <Tooltip title={this.translate("Remove")}><Clear/></Tooltip>
            </IconButton>
	    );
	};

	refreshReadonly = (readonly) => {
	    this.setState({ readonly });
	};

    handleAdd = () => {
        this.requester.add();
    };

	handleRemove = (index) => {
	    this.requester.remove(index);
	};

    _instanceProps = () => {
		var result = {};
		const noItemsMessage = this.props.multiple.noItemsMessage;
		this.copyProps(this.props, result, "multiple,layout,label,format");
		if (noItemsMessage != null && noItemsMessage !== "") result.noItemsMessage = noItemsMessage;
		return result;
	};

	_style = (multiple) => {
		let spacingStyle = this._spacingStyle(multiple);
		if (spacingStyle === undefined) spacingStyle = (multiple.arrangement.toLowerCase() === "horizontal") ? { right: 5, bottom: 2 } : { right: 0, bottom: 0 };
		return { marginRight: spacingStyle.right + "px", marginBottom: spacingStyle.bottom + "px", height: "calc(100% - " + spacingStyle.bottom + "px)" };
	};

	_spacingStyle = (multiple) => {
		let spacingSize = multiple.spacing;
		if (spacingSize === 0) return undefined;
		return { right: spacingSize, bottom: spacingSize };
	};

}

DisplayFactory.register("Multiple", Multiple);