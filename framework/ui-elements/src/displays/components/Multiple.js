import React from "react";
import makeStyles from '@material-ui/core/styles/makeStyles'
import { Button, IconButton, Tooltip, Typography } from '@material-ui/core';
import { Clear } from '@material-ui/icons';
import AbstractMultiple from "../../../gen/displays/components/AbstractMultiple";
import MultipleNotifier from "../../../gen/displays/notifiers/MultipleNotifier";
import MultipleRequester from "../../../gen/displays/requesters/MultipleRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import { enrichDisplayProperties } from "../Display";
import Accordion from '@material-ui/core/Accordion';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Theme from "app-elements/gen/Theme";
import Spinner from "./Spinner"
import 'alexandria-ui-elements/res/styles/components/multiple/styles.css';

export default class Multiple extends AbstractMultiple {

	constructor(props) {
		super(props);
		this.notifier = new MultipleNotifier(this);
		this.requester = new MultipleRequester(this);
		this.state = {
		    ...this.state,
		    readonly: false,
		    expandedItem : false,
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
                <div className={"layout flex " + (wrap ? "wrap " : "") + layout} style={{height:height,...this.style(),marginBottom:'0'}}>
                    {this.renderItems(multiple.instances, this._instanceProps(), style)}
                </div>
                { multiple.editable && this._renderAdd() }
            </div>
        );
	};

	renderItems = (container, props, style) => {
		const collapsed = this.props.multiple.collapsed != null && this.props.multiple.collapsed;
	    return collapsed ? this.renderCollapsed(container, props, style) : this.renderExpanded(container, props, style);
	};

	renderExpanded = (container, props, style) => {
	    return this.renderInstances(container, props, style);
	};

	renderCollapsed = (container, props, style) => {
        let instances = this.instances(container);
        return instances.map((instance, index) => {
            return this.renderAccordion(instance, props, style, index);
        });
	};

	renderAccordion = (instance, props, style, index) => {
	    const theme = Theme.get();
	    const expandedItem = this.state.expandedItem;
	    const id = instance.pl.id;
        enrichDisplayProperties(instance);
        this.copyProps(props, instance.pl);
	    return (
	        <Accordion expanded={expandedItem === id} onChange={this.handleSelect.bind(this, id)}>
                <AccordionSummary expandIcon={<ExpandMoreIcon />} aria-controls="panel1bh-content" id="panel1bh-header">
                    <div className="layout horizontal center flex">
                        <div style={{minWidth:"33%"}}><Typography style={{fontWeight:"bold"}}>{instance.pl.label}</Typography></div>
                        {instance.pl.description != null && instance.pl.description != "" && <Typography style={{color:theme.palette.text.secondary}}>{instance.pl.description}</Typography>}
                    </div>
                </AccordionSummary>
                <AccordionDetails>
                    <div className={"layout flex"}>
                        {expandedItem === id && this.renderInstance(instance, props, style, index)}
                        {expandedItem !== id && this.renderLoading()}
                    </div>
                </AccordionDetails>
            </Accordion>
	    );
	};

    renderInstance = (instance, props, style, index) => {
		const multiple = this.props.multiple;
		const fixedStyle = {...this.style(),...style};
		if (fixedStyle.width == null) fixedStyle.width = 'auto';
        return (
            <div key={index} className="layout horizontal center" style={fixedStyle}>
                <div className="layout flex" style={{...style,...this.style(),height:'100%',marginBottom:'0'}}>{React.createElement(DisplayFactory.get(instance.tp), instance.pl)}</div>
                { multiple.editable && this._removeAllowed(index) && this._renderRemove(index) }
            </div>
        );
    };

    renderLoading = () => {
		return (
		    <div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }>
				<Spinner/>
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

	handleSelect = (panel, event, isExpanded) => {
        this.setState({expandedItem : isExpanded ? panel : false});
        this.requester.select(panel);
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
		if (spacingStyle === undefined) spacingStyle = (multiple.arrangement.toLowerCase() === "horizontal") ? { right: 5, top: 2, bottom: 2 } : { right: 0, top: 0, bottom: 0 };
		return { marginRight: spacingStyle.right + "px", marginTop: spacingStyle.top + "px", marginBottom: spacingStyle.bottom + "px"/*, height: "calc(100% - " + spacingStyle.bottom + "px)"*/ };
	};

	_spacingStyle = (multiple) => {
		let spacingSize = multiple.spacing;
		if (spacingSize === 0) return undefined;
		return { right: spacingSize, bottom: spacingSize };
	};

}

DisplayFactory.register("Multiple", Multiple);