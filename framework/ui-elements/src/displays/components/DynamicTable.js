import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDynamicTable from "../../../gen/displays/components/AbstractDynamicTable";
import DynamicTableNotifier from "../../../gen/displays/notifiers/DynamicTableNotifier";
import DynamicTableRequester from "../../../gen/displays/requesters/DynamicTableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { Table, TableHead, TableBody, TableRow, TableCell, Typography, Dialog,
         DialogActions, DialogContent, DialogTitle, Checkbox, IconButton, FormControlLabel } from '@material-ui/core';
import {RiseLoader, PulseLoader} from "react-spinners";
import { Clear, ArrowBack } from '@material-ui/icons';
import classNames from "classnames";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import AutoSizer from 'react-virtualized-auto-sizer';
import BaseDialog from "./BaseDialog";
import {CollectionStyles} from "./Collection";
import 'alexandria-ui-elements/res/styles/layout.css';
import Select from "react-select";
import NumberUtil from "alexandria-ui-elements/src/util/NumberUtil";

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
    columnAction : {
        color: theme.palette.primary.main,
        cursor: 'pointer',
    },
    rowCell : {
        textAlign:'right',
        borderTop: '1px solid #e0e0e0',
        borderRight: '1px solid #e0e0e0',
        borderLeft: '1px solid #e0e0e0',
    },
    detailRowCell : {
        borderLeft: '0',
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
        color:'#007143',
    },
    columnSelector : {
        width: '250px',
        marginLeft: '5px',
    },
    showMoreSections : {
        textAlign:'center',
        width:'100%',
        border:'1px solid #ddd',
        display:'block',
        padding:'10px',
        cursor:'pointer',
        color:'white',
        background:'#006ab0',
    }
});

const DynamicTablePageSize = 10;

export class EmbeddedDynamicTable extends AbstractDynamicTable {

	constructor(props) {
		super(props);
		this.notifier = new DynamicTableNotifier(this);
		this.requester = new DynamicTableRequester(this);
		this.header = React.createRef();
		this.container = React.createRef();
		this.state = {
		    sections: null,
		    open: false,
		    section: null,
		    row: null,
		    page: 0,
		    column: null,
		    selectRowProvided: false,
            hideZeros: false,
            showRelativeValues: false,
		    ...this.state,
		};
	};

    render() {
        if (this.state.sections == null) return this.renderLoading();
        if (this.state.sections.length <= 0) return this.state.loading ? this.renderLoading() : this.renderEmpty();
        return (
            <div ref={this.container}>
                {this.renderToolbar()}
                <div style={{width:this.container.current != null ? this.container.current.offsetWidth+"px" : "100%", overflow:'auto', height:'calc(100% - 40px)'}}>
                    {this.renderTable()}
                </div>
                {this.renderDialog()}
            </div>
        );
    };

    renderToolbar = () => {
        const { classes, theme } = this.props;
        return (
            <div className="layout horizontal center flex" style={{width:'100%',marginBottom:'5px'}}>
                <div className="layout horizontal">
                    {this.state.column &&
                        <div className="layout horizontal center">
                            <IconButton color='primary' onClick={this.handleBack.bind(this)}><ArrowBack/></IconButton>
                            <Select className={classes.columnSelector} isSearchable
                                    placeholder={this.translate("Select other column")} options={this._selectorColumns()}
                                    value={this._selectorColumn(this.state.column.label, this.state.column.index)}
                                    onChange={this.handleSelectColumn.bind(this)}/>
                        </div>
                    }
                </div>
                <div className="layout horizontal center-center flex">
                    {this.state.loading && <PulseLoader color={theme.palette.secondary.main} size={8} loading={true}/>}
                </div>
                <div className="layout horizontal end-justified center">
                    <FormControlLabel control={<Checkbox checked={this.state.hideZeros} onChange={this.handleToggleHideZeros.bind(this)} name="toggleHideZeros" color="primary"/>} label={this.translate("Hide zeros")}/>
                    <FormControlLabel control={<Checkbox checked={this.state.showRelativeValues} onChange={this.handleToggleRelativeValues.bind(this)} name="toggleRelativeValues" color="primary"/>} label={this.translate("Show percentages")}/>
                </div>
            </div>
        );
    };

    renderTable = () => {
        return this._isMainView() ? this.renderMainView() : this.renderDetailView();
    };

    renderMainView = () => {
        const end = this._endPos();
        let result = [];
        for (let i=0; i<=end; i++) {
            result.push(this.renderSection(this.state.sections[i], i));
        };
        if (this.state.page < this._lastPage()) result.push(this.renderShowMoreSections());
        return result;
    };

    renderShowMoreSections = () => {
        const { classes } = this.props;
        return (<a className={classes.showMoreSections} onClick={this.handleShowMoreSections.bind(this)}>{this.translate("Show more sections...")}</a>);
    };

    _startPos = () => {
        return this.state.page * DynamicTablePageSize;
    };

    _endPos = () => {
        const endPos = (this.state.page * DynamicTablePageSize) + DynamicTablePageSize - 1;
        return endPos < this.state.sections.length ? endPos : this.state.sections.length-1;
    };

    _pageOf = (current) => {
        const count = this.state.sections.length;
		if (current == 0) return 0;
		return Math.floor(current / DynamicTablePageSize) + (count % DynamicTablePageSize > 0 ? 1 : 0) - 1;
    };

    _lastPage = () => {
        return this._pageOf(this.state.sections.length-1);
    };

    renderDetailView = () => {
        const { classes } = this.props;
        return (
            <div className="layout horizontal flex">
                {this.state.sections.map((s, index) => this.renderDetailSection(s, index))}
                {this.state.sections.length > 1 && this.renderDetailSection(this.createDetailTotalSection(), this.state.sections.length+1)}
            </div>
        );
    };

    createDetailTotalSection = () => {
        if (this.state.sections.length <= 0) return null;
        const section = this.state.sections[0];
        const result = {};
        result.label = "Total";
		result.color = section.color;
		result.backgroundColor = section.backgroundColor;
		result.fontSize = section.fontSize;
		result.columns = section.columns;
		result.rows = [];
		result.sections = section.sections;
		this.aggregateTotalRows(result);
        return result;
    };

    aggregateTotalRows = (totalSection) => {
        const sections = this.state.sections;
        for (let i=0; i<sections.length; i++) this.aggregateSection(sections[i], totalSection);
        this.applyTotalSectionOperator(totalSection);
    };

    aggregateSection = (section, totalSection) => {
        for (let i=0; i<section.sections.length; i++) this.aggregateSection(section.sections[i], totalSection.sections[i]);
        for (let i=0; i<section.rows.length; i++) this.aggregateSectionRow(section, section.rows[i], this.sectionRow(totalSection, i));
    };

    sectionRow = (section, i) => {
        if (section.rows.length > i) return section.rows[i];
        let result = { cells: [], isTotalRow: true };
        section.rows.push(result);
        return result;
    };

    aggregateSectionRow = (section, row, totalRow) => {
        totalRow.label = row.label;
        for (let i=0; i<row.cells.length; i++) {
            const cell = this.rowCell(totalRow, i);
            cell.label = row.cells[i].label;
            cell.absolute += row.cells[i].absolute;
            cell.relative = parseFloat(cell.relative + row.cells[i].relative).toFixed(2);
        }
    };

    rowCell = (row, i) => {
        if (row.cells.length > i) return row.cells[i];
        let result = { label: "", absolute: 0, relative: 0, isTotalRow: false, highlighted: false};
        row.cells.push(result);
        return result;
    };

    applyTotalSectionOperator = (section) => {
        for (let i=0; i<section.sections.length; i++) this.applyTotalSectionOperator(section.sections[i]);
        for (let i=0; i<section.rows.length; i++) this.applyTotalRowOperator(section, section.rows[i]);
    };

    applyTotalRowOperator = (section, row) => {
        for (let i=0; i<row.cells.length; i++) {
            const cell = row.cells[i];
            const operator = this.cellOperator(section, cell);
            cell.absolute = operator === "Average" ? (cell.absolute / this.state.sections.length) : cell.absolute;
        }
    };

    renderDetailSection = (section, index) => {
        const classNames = "layout vertical" + (this.state.sections.length <= 1 ? "" : " flex");
        if (index !== 0 && this.state.hideZeros && this._isZeroSection(section)) return (<React.Fragment/>);
        return (<div className={classNames}>{this.renderSection(section, index)}</div>);
    };

    renderSection = (section, index) => {
        const { classes } = this.props;
        const isMainView = this._isMainView();
        if ((isMainView || (!isMainView && index != 0)) && this.state.hideZeros && this._isZeroSection(section)) return (<React.Fragment/>);
        return (
            <Table size='small' className={classes.table} key={index}>
                <TableHead>{this.renderHeader(section, index)}</TableHead>
                <TableBody>{this.renderBody(section, index)}</TableBody>
            </Table>
        );
    };

    renderHeader = (section, index) => {
        const sectionsArray = this.treeToArray(section);
        const labelSize = this.maxLabelSize(section);
        const isMainView = this._isMainView();
        if (this.state.sections.length == 1 && sectionsArray[0].length == 1 && sectionsArray[0][0].sections.length == 0)
            sectionsArray.shift();
        return (
            <React.Fragment>
                {sectionsArray.map((list, index) => {
                    if (index == sectionsArray.length-1 && !isMainView) return null;
                    return this.renderHeaderRow(section, list, labelSize, index);
                })}
            </React.Fragment>
        );
    };

    renderHeaderRow = (mainSection, sections, rowLabelWidth, index) => {
        const { classes } = this.props;
        const isMainView = this._isMainView();
        const visible = isMainView || (!isMainView && mainSection == this.state.sections[0]);
        return (
            <TableRow key={index}>
                {visible && <TableCell className={classes.rowLabel}><div style={{minWidth:rowLabelWidth+"px",width:rowLabelWidth+"px"}}></div></TableCell>}
                {sections.map((section, index) => isMainView || (!isMainView && this.isSelectedColumnIn(section, sections)) ? this.renderHeaderCell(mainSection, section, index) : null)}
            </TableRow>
        );
    };

    isSelectedColumnIn = (section, sections) => {
        let offset = 0;
        const isTerminal = this.childrenColumnsCount(section) == 0;
        for (let i=0; i<sections.length; i++) {
            if (isTerminal) {
                if (sections[i] == section) return i == this.state.column.index;
            }
            else {
                const childrenCount = this.childrenColumnsCount(sections[i]);
                if (sections[i] == section) return offset <= this.state.column.index && offset+childrenCount-1 >= this.state.column.index;
                offset += childrenCount;
            }
        }
        return false;
    };

    renderHeaderCell = (mainSection, section, index) => {
        const { classes } = this.props;
        const columnCount = this.childrenColumnsCount(section);
        const isMainView = this._isMainView();
        const colSpan = this._isMainView() ? columnCount : (this.isLeafSection(section) && index == this.state.column.index ? 1 : 0);
        const color = section.color;
        const backgroundColor = section.backgroundColor;
        const fontSize = section.fontSize + "pt";
        const textAlign = section.textAlign != null ? section.textAlign : "center";
        const selectable = section.selectable;
        const className = isMainView || this.state.sections[0] == mainSection ? classNames(classes.rowCell) : classNames(classes.rowCell, classes.detailRowCell);
        if (!isMainView && selectable && index != this.state.column.index) return null;
        return (
            <TableCell className={className}
                       colSpan={colSpan}
                       style={{backgroundColor:backgroundColor,fontSize:fontSize,textAlign:textAlign}}
                       key={index}>
                {(!selectable || (!isMainView && selectable)) && <span style={{color:color,whiteSpace:'nowrap'}}>{section.label}</span>}
                {isMainView && selectable && <a className={classes.columnAction} onClick={this.handleFilterColumn.bind(this, section, index)}>{section.label}</a>}
            </TableCell>
        );
    };

    renderBody = (section, index) => {
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
        const totalRow = this.isTotalRow(sections, rowIndex);
        const style = totalRow ? { fontWeight: "bold"} : {};
        const isMainView = this._isMainView();
        const hideRow = this.state.hideZeros && ((isMainView && this._isZeroRow(sections, rowIndex)) || (!isMainView && this._isZeroRow(this.state.sections, rowIndex)));
        if (hideRow) return (<React.Fragment/>);
        return (
            <TableRow key={rowIndex}>
                {(isMainView || (!isMainView && mainSection == this.state.sections[0])) &&
                    <TableCell className={classes.rowLabel} style={style}>
                        {(!totalRow && isMainView) && <a className={classes.rowAction} onClick={this.handleShowItems.bind(this, mainSection, rowLabel)}>{rowLabel}</a>}
                        {(totalRow || !isMainView) && <div>{rowLabel}</div>}
                    </TableCell>
                }
                {sections.map((section, index) => this.renderBodyCells(mainSection, section, rowIndex, index, this.columnOffset(sections, index)))}
            </TableRow>
        );
    };

    _isZeroSection = (section) => {
        for (let i=0; i<section.rows.length; i++) {
            const row = section.rows[i];
            for (let j=0; j<row.cells.length; j++) {
                if (row.cells[j].absolute != 0) return false;
            }
        }
        return true;
    };

    _isZeroRow = (sections, rowIndex) => {
        for (let i=0; i<sections.length; i++) {
            const section = sections[i];
            if (section.sections.length > 0) return this._isZeroRow(section.sections, rowIndex);
            else if (!this._isZeroCells(section, rowIndex)) return false;
        }
        return true;
    };

    _isZeroCells = (section, rowIndex) => {
        const isMainView = this._isMainView();
        const row = section.rows[rowIndex];
        if (row == null) return false;
        for (let j=0; j<row.cells.length; j++) {
            const value = row.cells[j].absolute;
            const checkCell = isMainView || (!isMainView && j == this.state.column.index);
            if (checkCell && value !== 0) return false;
        }
        return true;
    };

    columnOffset = (sections, index) => {
        let result = 0;
        for (let i=0; i<sections.length; i++) {
            if (i == index) return result;
            result += this.childrenColumnsCount(sections[i]);
        }
        return result;
    };

    renderBodyCells = (mainSection, section, rowIndex, idx, offset) => {
        const isMainView = this._isMainView();
        const row = section.rows[rowIndex];
        return (<React.Fragment key={rowIndex}>{row.cells.map((c, index) => (isMainView || (!isMainView && offset+index == this.state.column.index)) ? this.renderBodyCell(mainSection, section, row, c, index) : null)}</React.Fragment>);
    };

    renderBodyCell = (mainSection, section, row, cell, index) => {
        const { classes } = this.props;
        const operator = this.cellOperator(section, cell);
        const metric = this.cellMetric(section, cell);
        const style = cell.isTotalRow ? { fontWeight: "bold" } : {};
        const relative = cell.relative !== "-1" && this.state.showRelativeValues && metric !== "%" ? cell.relative : undefined;
        const title = cell.label + " " + this.translate("in") + " " + row.label + " " + this.translate("in") + " " + section.label;
        const value = operator === "Average" ? cell.absolute : cell.absolute;
        const format = operator === "Average" ? "0,0.00" : "0,0";
        const className = this._isMainView() || this.state.sections[0] == mainSection ? classNames(classes.rowCell) : classNames(classes.rowCell, classes.detailRowCell);
        return (
            <TableCell title={title} key={index} className={className} style={{whiteSpace:'nowrap',...style}}>
                {NumberUtil.format(cell.absolute, format)}
                {metric !== "" && <span style={{fontSize:'9pt',marginLeft:'5px',color:'#777'}}>{metric}</span>}
                {relative !== undefined && <span className={classes.rowRelativeValue}>&nbsp;{relative}<span style={{fontSize:'9pt',marginLeft:'5px',color:'#777'}}>%</span></span>}
            </TableCell>
        );
    };

    cellMetric = (section, cell) => {
        for (let i=0; i<section.columns.length; i++) {
            if (section.columns[i].label === cell.label) return section.columns[i].metric;
        }
        return "";
    };

    cellOperator = (section, cell) => {
        for (let i=0; i<section.columns.length; i++) {
            if (section.columns[i].label === cell.label) return section.columns[i].operator;
        }
        return null;
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
        if (this.state.selectRowProvided) {
            this.requester.selectRow({ section: section.label, row: row });
            return;
        }
        this.setState({open:true, section: section, row:row});
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

    handleShowMoreSections = () => {
        this.setState({page: this.state.page+1});
    };

    openRow = (address) => {
        window.open(address);
    };

    maxLabelSize = (section) => {
        const sections = this.leafSections(section);
        if (sections.length <= 0) return 250;
        const countRows = sections[0].rows.length;
        var result = 0;
        for (var i=0; i<countRows; i++) result = Math.max(result, (sections[0].rows[i].label.length * 7) + 20);
        return result;
    };

    treeToArray = (section) => {
        const result = [];
        const isDetailView = !this._isMainView();
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
            item.columns[i].selectable = true;
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
        if (this.isLeafSection(section)) result.push(section);
        for (var i=0; i<section.sections.length; i++) this.leafSectionsOf(section.sections[i], result);
    };

    isLeafSection = (section) => {
        return section.sections.length == 0;
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

    isTotalRow = (sections, rowIndex) => {
        if (sections.length <= 0) return false;
        const section = sections[0];
        return rowIndex < section.rows.length ? section.rows[rowIndex].isTotalRow : false;
    }

    renderEmpty = () => {
        const noItemsMessage = this.props.noItemsMessage != null ? this.props.noItemsMessage : "No elements";
        return (<Typography style={{height:'100%',width:'100%',padding:"10px 0",fontSize:'13pt',paddingTop:'100px'}} className="layout horizontal center-justified">{this.translate(noItemsMessage)}</Typography>);
    };

    renderLoading = () => {
        const { theme } = this.props;
        return (<div style={{position:'absolute',top:'50%',left:'43%'}}><RiseLoader color={theme.palette.secondary.main} loading={true}/></div>);
    };

    sections = (sections) => {
        this.setState({sections: sections, page: 0});
    };

    selectRowProvided = (value) => {
        this.setState({selectRowProvided: value});
    };

    handleToggleRelativeValues = () => {
        this.setState({showRelativeValues: !this.state.showRelativeValues});
    };

    handleToggleHideZeros = () => {
        this.setState({hideZeros: !this.state.hideZeros});
    };

    handleFilterColumn = (column, index) => {
        this.setState({ column: { index: index, label: column.label } })
    };

    handleSelectColumn = (value) => {
        this.setState({ column : { index: value.index, label: value.label }});
    };

    handleBack = () => {
        this.setState({ column: null })
    };

    _isMainView = () => {
        return this.state.column == null;
    };

    _selectorColumns = () => {
        if (this.state.sections.length <= 0) return [];
        let result = [];
        const sections = this.treeToArray(this.state.sections[0]);
        const leafSections = sections.length > 0 ? sections[sections.length-1] : [];
        if (leafSections.length <= 0) return [];
        return leafSections.map((section, index) => this._selectorColumn(section.label, index));
    };

    _selectorColumn = (label, index) => {
        return { value: label, label: label, index: index };
    };
}

class DynamicTable extends EmbeddedDynamicTable {
    constructor(props) {
        super(props);
    }
}

export default withStyles(DynamicTableStyles, { withTheme: true })(DynamicTable);
DisplayFactory.register("DynamicTable", withStyles(DynamicTableStyles, { withTheme: true })(DynamicTable));