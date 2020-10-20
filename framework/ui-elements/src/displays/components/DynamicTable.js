import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDynamicTable from "../../../gen/displays/components/AbstractDynamicTable";
import DynamicTableNotifier from "../../../gen/displays/notifiers/DynamicTableNotifier";
import DynamicTableRequester from "../../../gen/displays/requesters/DynamicTableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { Table, TableHead, TableBody, TableRow, TableCell, Typography, Dialog,
         DialogActions, DialogContent, DialogTitle, Checkbox, IconButton, FormControlLabel } from '@material-ui/core';
import Clear from '@material-ui/icons/Clear';
import classNames from "classnames";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import AutoSizer from 'react-virtualized-auto-sizer';
import BaseDialog from "./BaseDialog";
import {CollectionStyles} from "./Collection";
import 'alexandria-ui-elements/res/styles/layout.css';

export const DynamicTableStyles = theme => ({
	...CollectionStyles(theme),
    table : {
        marginBottom: '20px',
    },
    headerCell : {
    },
    rowLabel : {
        border:'0',
        textAlign:'right',
    },
    rowActions : {
        border:'0',
        textAlign:'left',
    },
    rowAction : {
        color: theme.palette.primary.main,
        cursor: 'pointer',
    },
    rowCell : {
        textAlign:'right',
    },
    label: {
        color: theme.palette.grey.A700,
        marginRight: "5px"
    },
    headerView : {
        borderBottom: "1px solid #ddd",
    },
    withoutScroller : {
        width: "100%"
    },
    withScroller : {
        width: "calc(100% - 15px)"
    },
    itemView : {
        borderBottom: "1px solid #ddd",
        height: "100%",
        '&:hover' : {
            background: '#ddd'
        },
        '&:hover $selector' : {
            display: 'block'
        }
    },
    selectAll : {
        display: "none",
        position: "absolute",
        top: "0",
        left: "0",
    },
    dialogHeader : {
        background: theme.palette.primary.main,
        color: 'white',
        padding: '3px 24px',
    },
    rowRelativeValue : {
        width:'65px',
        display:'inline-block',
        color:'#777',
    },
});

export class EmbeddedDynamicTable extends AbstractDynamicTable {

	constructor(props) {
		super(props);
		this.notifier = new DynamicTableNotifier(this);
		this.requester = new DynamicTableRequester(this);
		this.header = React.createRef();
		this.state = {
		    sections: [],
		    open: false,
		    section: null,
		    row: null,
            showRelativeValues: false,
		    ...this.state,
		};
	};

    render() {
        if (this.state.sections.length <= 0) return this.renderEmpty();
        return (
            <div>
                {this.renderToggleRelativeValues()}
                {this.state.sections.map((s, index) => this.renderTable(s, index))}
                {this.renderDialog()}
            </div>
        );
    };

    renderToggleRelativeValues = () => {
        return (
            <div className="layout horizontal end-justified" style={{marginBottom:'5px'}}>
                <FormControlLabel control={<Checkbox checked={this.state.showRelativeValues} onChange={this.handleToggleRelativeValues.bind(this)} name="toggleRelativeValues" color="primary"/>} label={this.translate("Show percentages")}/>
            </div>
        );
    };

    renderTable = (section, index) => {
        const { classes } = this.props;
        return (
            <Table size='small' className={classes.table} key={index}>
                <TableHead>{this.renderHeader(section)}</TableHead>
                <TableBody>{this.renderBody(section)}</TableBody>
            </Table>
        );
    };

    renderHeader = (section) => {
        const sectionsArray = this.treeToArray(section);
        const labelSize = this.maxLabelSize(section);
        if (this.state.sections.length == 1 && sectionsArray[0].length == 1 && sectionsArray[0][0].sections.length == 0)
            sectionsArray.shift();
        return (<React.Fragment>{sectionsArray.map((list, index) => this.renderHeaderRow(list, labelSize, index))}</React.Fragment>);
    };

    renderHeaderRow = (sections, rowLabelWidth, index) => {
        const { classes } = this.props;
        return (
            <TableRow key={index}>
                <TableCell className={classes.rowLabel} style={{width:rowLabelWidth+"px"}}></TableCell>
                {sections.map((section, index) => this.renderHeaderCell(section, index))}
            </TableRow>
        );
    };

    renderHeaderCell = (section, index) => {
        const { classes } = this.props;
        const colSpan = this.childrenColumnsCount(section);
        const color = section.color;
        const backgroundColor = section.backgroundColor;
        const fontSize = section.fontSize + "pt";
        const textAlign = section.textAlign != null ? section.textAlign : "center";
        return (
            <TableCell className={classNames(classes.rowCell, classes.headerCell)}
                       colSpan={colSpan}
                       style={{color:color,backgroundColor:backgroundColor,fontSize:fontSize,textAlign:textAlign}}
                       key={index}>
                {section.label}
            </TableCell>
        );
    };

    renderBody = (section) => {
        const sections = this.leafSections(section);
        if (sections.length <= 0) return;
        const countRows = sections[0].rows.length;
        var result = []
        for (var i=0; i<countRows; i++) result.push(this.renderBodyRow(section, sections, i));
        return (<React.Fragment>{result}</React.Fragment>);
    };

    renderBodyRow = (mainSection, sections, rowIndex) => {
        const { classes } = this.props;
        const rowLabel = this.rowLabel(sections, rowIndex);
        const style = this.isRowHighlighted(sections, rowIndex) ? { fontWeight: "bold"} : {};
        return (
            <TableRow key={rowIndex}>
                <TableCell className={classes.rowLabel} style={style}>
                    <a className={classes.rowAction} onClick={this.handleShowItems.bind(this, mainSection, rowLabel)}>{rowLabel}</a>
                </TableCell>
                {sections.map((section, index) => this.renderBodyCells(section, rowIndex, index))}
            </TableRow>
        );
    };

    renderBodyCells = (section, rowIndex, idx) => {
        return (<React.Fragment>{section.rows[rowIndex].cells.map((c, index) => this.renderBodyCell(c, index))}</React.Fragment>);
    };

    renderBodyCell = (cell, index) => {
        const { classes } = this.props;
        const style = cell.highlighted ? { fontWeight: "bold"} : {};
        const relative = cell.relative !== "-1" && this.state.showRelativeValues ? cell.relative : undefined;
        return (
            <TableCell key={index} className={classes.rowCell} style={style}>
                {cell.absolute}
                {relative !== undefined && <span className={classes.rowRelativeValue}>&nbsp;{relative}%</span>}
            </TableCell>
        );
    };

    renderDialog = () => {
        const { classes } = this.props;
        const selectable = this.props.selection != null;
        const multiple = this.allowMultiSelection();
        const headerHeight = this.header.current != null ? this.header.current.offsetHeight : 0;
        const minHeight = this.props.itemHeight * this.state.itemCount;
        const height = this.container.current != null ? this.container.current.offsetHeight : 0;
        const headerClass = height <= minHeight ? classes.withScroller : classes.withoutScroller;
        const sectionLabel = this.state.section != null ? this.state.section.label : "";

        return (
            <Dialog open={this.state.open} onEntered={this.handleOpen.bind(this)} onClose={this.handleClose.bind(this)} aria-labelledby="form-dialog-title" fullScreen TransitionComponent={BaseDialog.Transition}>
                <DialogTitle id="form-dialog-title" className={classes.dialogHeader}>
                    <div className="layout horizontal center">
                        <div className="layout horizontal flex" style={{color:'white'}}><Typography variant="h4">{this.translate("Items of") + " " + sectionLabel + " " + this.translate("in") + " " + this.state.row}</Typography></div>
                        <div className="layout horizontal end-justified"><IconButton onClick={this.handleClose.bind(this)}><Clear fontSize="large" style={{color:"white"}}/></IconButton></div>
                    </div>
                </DialogTitle>
                <DialogContent style={{height:'100%',width:'100%'}}>
                    <div ref={this.container} style={{height:"100%",width:"100%"}} className="layout vertical flex">
                        { ComponentBehavior.labelBlock(this.props) }
                        <div ref={this.header} className={classNames(classes.headerView, headerClass, "layout horizontal center", selectable && multiple ? classes.selectable : {})} style={{position:"relative"}}>
                            <div className={classNames(classes.selectAll, selectable ? classes.selectable : {})}><Checkbox className={classes.selector} onChange={this.handleCheck.bind(this)} /></div>
                            {this.props.children}
                        </div>
                        <div className="layout flex" style={{width:"100%",height:"calc(100% - " + headerHeight + "px)"}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width))}</AutoSizer></div>
                    </div>
                </DialogContent>
            </Dialog>
        );
    };

    handleShowItems = (section, row) => {
        this.setState({open:true, section:section, row:row});
    };

    handleOpen = () => {
        this.requester.showItems({open: true, section: this.state.section.label, row: this.state.row});
    };

	handleClose = () => {
        this.setState({open:false});
    };

    handleCheck = () => {
        this.requester.selectAll();
    };

    maxLabelSize = (section) => {
        const sections = this.leafSections(section);
        if (sections.length <= 0) return 250;
        const countRows = sections[0].rows.length;
        var result = 0;
        for (var i=0; i<countRows; i++) result = Math.max(result, (sections[0].rows[i].label.length * 10) + 20);
        return result;
    };

    treeToArray = (section) => {
        const result = [];
        this.registerItemInArray(section, result, 0);
        return result;
    };

    registerItemInArray = (item, itemsArray, level) => {
        if (itemsArray[level] == null) itemsArray[level] = [];
        itemsArray[level].push(item);
        if (item.sections == null) item.sections = [];
        for (var i=0; i<item.sections.length; i++) this.registerItemInArray(item.sections[i], itemsArray, level+1);
        if (item.columns == null) item.columns = [];
        for (var i=0; i<item.columns.length; i++) {
            item.columns[i].color = "black";
            item.columns[i].backgroundColor = "transparent";
            item.columns[i].fontSize = 11;
            item.columns[i].textAlign = "right !important";
            this.registerItemInArray(item.columns[i], itemsArray, level+1);
        }
    }

    leafSections = (section) => {
        const result = [];
        this.leafSectionsOf(section, result);
        return result;
    };

    leafSectionsOf = (section, result) => {
        if (section.sections.length <= 0) result.push(section);
        for (var i=0; i<section.sections.length; i++) this.leafSectionsOf(section.sections[i], result);
    };

    childrenColumnsCount = (section) => {
        var count = section.columns.length;
        for (var i=0; i<section.sections.length; i++) count += this.childrenColumnsCount(section.sections[i]);
        return count;
    };

    rowLabel = (sections, rowIndex) => {
        if (sections.length <= 0) return "";
        const section = sections[0];
        return rowIndex < section.rows.length ? section.rows[rowIndex].label : "";
    }

    isRowHighlighted = (sections, rowIndex) => {
        if (sections.length <= 0) return false;
        const section = sections[0];
        return rowIndex < section.rows.length ? section.rows[rowIndex].highlighted : false;
    }

    renderEmpty = () => {
        const noItemsMessage = this.props.noItemsMessage != null ? this.props.noItemsMessage : "No elements";
        return (<Typography style={{height:'100%',width:'100%',padding:"10px 0",fontSize:'13pt'}} className="layout horizontal center-center">{this.translate(noItemsMessage)}</Typography>);
    };

    sections = (sections) => {
        this.setState({sections});
    };

    handleToggleRelativeValues = () => {
        this.setState({showRelativeValues: !this.state.showRelativeValues});
    };

}

class DynamicTable extends EmbeddedDynamicTable {
    constructor(props) {
        super(props);
    }
}

export default withStyles(DynamicTableStyles, { withTheme: true })(DynamicTable);
DisplayFactory.register("DynamicTable", withStyles(DynamicTableStyles, { withTheme: true })(DynamicTable));