import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDynamicTable from "../../../gen/displays/components/AbstractDynamicTable";
import DynamicTableNotifier from "../../../gen/displays/notifiers/DynamicTableNotifier";
import DynamicTableRequester from "../../../gen/displays/requesters/DynamicTableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { Table, TableHead, TableBody, TableRow, TableCell, TableSortLabel, Typography, Dialog,
         DialogActions, DialogContent, DialogContentText, DialogTitle, Checkbox, IconButton, Button, FormControlLabel } from '@material-ui/core';
import {RiseLoader, PulseLoader} from "react-spinners";
import { Clear, ArrowBack, TouchApp } from '@material-ui/icons';
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
    },
    visuallyHidden: {
        border: 0,
        clip: 'rect(0 0 0 0)',
        height: 1,
        margin: -1,
        overflow: 'hidden',
        padding: 0,
        position: 'absolute',
        top: 20,
        width: 1,
    },
    columnsAction : {
        color: theme.palette.primary.main,
        marginRight: '15px',
        fontSize: '11pt',
        cursor: 'pointer',
    },
});

const DynamicTablePageSize = 10;

export class EmbeddedDynamicTable extends AbstractDynamicTable {

	constructor(props) {
		super(props);
		this.notifier = new DynamicTableNotifier(this);
		this.requester = new DynamicTableRequester(this);
		this.header = React.createRef();
		this.container = React.createRef();
		this.dialogContainer = React.createRef();
		this.tableContainer = React.createRef();
		this.state = {
		    ...this.state,
		    traceable : true,
		    name: null,
		    visibleColumns: [],
		    sections: null,
		    open: false,
		    openColumnsDialog: false,
		    openConfirm: false,
		    section: null,
		    row: null,
		    page: 0,
		    column: null,
		    selectRowProvided: false,
            hideZeros: false,
            showRelativeValues: false,
            order: null,
            orderBy: null,
		};
	};

    setup = (info) => {
        let visibleColumns = info.visibleColumns != null && info.visibleColumns.length > 0 ? this.visibleColumnsArrayOf(info.visibleColumns) : null;
        if (visibleColumns == null) visibleColumns = this.getCookie(info.name) ? this.getCookie(info.name) : this.state.visibleColumns;
        this.setState({ itemCount : info.itemCount, pageSize: info.pageSize, name: info.name, visibleColumns: visibleColumns });
    };

    render() {
        if (this.state.sections == null) return this.renderLoading();
        if (this.state.sections.length <= 0) return this.state.loading ? this.renderLoading() : this.renderEmpty();
        window.setTimeout(this.showTableContainer.bind(this), 100);
        return (
            <div ref={this.container}>
                {this.renderToolbar()}
                <div ref={this.tableContainer} style={{width:this.container.current != null ? (this.container.current.offsetWidth-15)+"px" : "100%", overflow:'auto', display:'none', height:'calc(100% - 40px)'}}>
                    {this.renderTable()}
                </div>
                {this.renderConfirmDialog()}
                {this.renderColumnsDialog()}
                {this.renderDialog()}
                {this.renderCookieConsent()}
            </div>
        );
    };

    showTableContainer = () => {
        if (this.tableContainer.current == null) return;
        this.tableContainer.current.style.display = "block";
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
                    <a className={classes.columnsAction} onClick={this.handleOpenColumnsDialog.bind(this)}>{this.translate("Show/hide columns")}</a>
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
        const sections = this.sort(this.state.sections);
        const end = this._endPos();
        let result = [];
        for (let i=0; i<=end; i++) {
            result.push(this.renderSection(sections[i], i));
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
        return this._pageOf(this.state.sections.length);
    };

    renderDetailView = () => {
        const { classes } = this.props;
        const sections = this.sort(this.state.sections);
        return (
            <div className="layout horizontal flex">
                {sections.map((s, index) => this.renderDetailSection(s, index))}
                {sections.length > 1 && this.renderDetailSection(this.createDetailTotalSection(), sections.length+1)}
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
		result.borderColor = section.borderColor;
		result.fontSize = section.fontSize;
		result.columns = section.columns;
		result.rows = [];
		result.sections = [];
		this.copySections(section, result);
		this.aggregateTotalRows(result);
        return result;
    };

    copySections = (section, totalSection) => {
        for (let i=0; i<section.sections.length; i++) {
            const childSection = section.sections[i];
            const childTotalSection = { label: childSection.label, color: childSection.color,
                                        backgroundColor: childSection.backgroundColor,
                                        borderColor: childSection.borderColor,
                                        fontSize: childSection.fontSize,
                                        columns: childSection.columns, rows: [], sections: [] };
            totalSection.sections.push(childTotalSection);
            this.copySections(childTotalSection, childSection);
        }
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
                {visible && <TableCell className={classes.rowLabel} style={{minWidth:rowLabelWidth+"px",width:rowLabelWidth+"px"}}><div></div></TableCell>}
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
        const textAlign = section.textAlign != null ? section.textAlign : "center";
        const selectable = section.selectable;
        const orderBy = this.state.orderBy != null ? this.state.orderBy.label : null;
        const order = this.state.order;
        const className = isMainView || this.state.sections[0] == mainSection ? classNames(classes.rowCell) : classNames(classes.rowCell, classes.detailRowCell);
        if (!isMainView && selectable && index != this.state.column.index) return null;
        const style = {backgroundColor:section.backgroundColor,fontSize:section.fontSize + "pt",textAlign:textAlign,minWidth:'170px'};
        if (section.borderColor !== "transparent") style.borderBottom = "4px solid " + section.borderColor;
        if (isMainView && selectable && !this.isColumnVisible(index)) return null;
        return (
            <TableCell className={className}
                       colSpan={colSpan}
                       style={style}
                       align='right'
                       key={index}>
                {(!selectable || (!isMainView && selectable)) &&
                    <a className="layout horizontal center-center" style={{color:color,whiteSpace:'nowrap',cursor:'pointer'}} onClick={this.handleFilterFirstColumn.bind(this)}>
                        {section.label}{isMainView && <TouchApp style={{marginLeft:'5px'}}/>}
                    </a>
                }
                {isMainView && selectable &&
                    <TableSortLabel active={orderBy === section.label} direction={orderBy === section.label ? order : 'asc'} onClick={this.handleSort.bind(this, section, index)}>
                        <span>{section.label}</span>
                        {orderBy === section.label ? (
                            <span className={classes.visuallyHidden}>{order === 'desc' ? 'sorted descending' : 'sorted ascending'}</span>
                        ) : null}
                    </TableSortLabel>
                }
            </TableCell>
        );
    };

    handleSort = (section, index, e) => {
        const isAsc = this.state.orderBy != null && this.state.orderBy.label === section.label && this.state.orderBy.index === index && this.state.order === 'asc';
        this.setState({ order: isAsc ? 'desc' : 'asc', orderBy: { label: section.label, index: index } });
    };

    sort = (s) => {
        if (this.state.orderBy == null) return s;
        let sections = JSON.parse(JSON.stringify(s));
        for (let i=0; i<sections.length; i++) {
            let section = this.findSectionToSort(sections, i);
            if (section == null) continue;
            let rows = this.sortRows(section.rows);
            sections[i] = this.sortSection(sections[i], rows);
        }
        return sections;
    };

    findSectionToSort = (sections, index) => {
        if (sections.length <= 0) return null;
        const sectionsArray = this.treeToArray(sections[index]);
        const leafSections = sectionsArray.length > 1 ? sectionsArray[sectionsArray.length-2] : [];
        if (leafSections.length <= 0) return null;
        let count = 0;
        for (let i=0; i<leafSections.length; i++) {
            count += leafSections[i].columns.length;
            if (this.state.orderBy.index < count) return leafSections[i];
        }
        return null;
    };

    sortSection = (section, rowsOrder) => {
        for (let i=0; i<section.sections.length; i++) section.sections[i] = this.sortSection(section.sections[i], rowsOrder);
        section.rows = this.sortRowsUsing(section.rows, rowsOrder);
        return section;
    };

    sortRowsUsing = (rows, rowsOrder) => {
        if (rows.length <= 0) return rows;
        let result = [];
        for (let i=0; i<rowsOrder.length; i++) {
            let label = rowsOrder[i].label;
            result.push(this.findRow(rows, label));
        }
        return result;
    };

    findRow = (rows, label) => {
        for (let i=0; i<rows.length; i++)
            if (rows[i].label === label) return rows[i];
        return null;
    };

    sortRows = (rows) => {
      const comparator = this.getComparator();
      const stabilizedThis = rows.map((el, index) => [el, index]);
      stabilizedThis.sort((a, b) => {
        const order = comparator(a[0], b[0]);
        if (order !== 0) return order;
        return a[1] - b[1];
      });
      return stabilizedThis.map((el) => el[0]);
    }

    getComparator = () => {
      return this.state.order === 'desc'
        ? (a, b) => this.descendingComparator(a, b)
        : (a, b) => -this.descendingComparator(a, b);
    };

    descendingComparator = (a, b) => {
        if (a.label === "Total" || b.label === "Total") return 0;
        const indicator = this.state.orderBy.label;
        const aValue = this.cellValue(a, indicator);
        const bValue = this.cellValue(b, indicator);
        if (bValue < aValue) return -1;
        if (bValue > aValue) return 1;
        return 0;
    };

    cellValue = (row, indicator) => {
        for (let i=0; i<row.cells.length; i++)
            if (row.cells[i].label === indicator) return row.cells[i].absolute;
        return 0;
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
        const rowDescription = this.rowDescription(sections, rowIndex);
        const totalRow = this.isTotalRow(sections, rowIndex);
        const style = totalRow ? { fontWeight: "bold"} : {};
        const isMainView = this._isMainView();
        const hideRow = this.state.hideZeros && ((isMainView && this._isZeroRow(sections, rowIndex)) || (!isMainView && this._isZeroRow(this.state.sections, rowIndex)));
        if (hideRow) return (<React.Fragment/>);
        return (
            <TableRow key={rowIndex}>
                {(isMainView || (!isMainView && mainSection == this.state.sections[0])) &&
                    <TableCell className={classes.rowLabel} style={style}>
                        {(!totalRow && isMainView) &&
                            <a className={classes.rowAction} onClick={this.handleShowItems.bind(this, mainSection, rowLabel)} title={rowDescription}>{rowLabel}</a>
                        }
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
        return (<React.Fragment key={rowIndex}>{row.cells.map((c, index) => this.isColumnVisible(offset+index) && (isMainView || (!isMainView && offset+index == this.state.column.index)) ? this.renderBodyCell(mainSection, section, row, c, index) : null)}</React.Fragment>);
    };

    renderBodyCell = (mainSection, section, row, cell, index) => {
        const { classes } = this.props;
        const operator = this.cellOperator(section, cell);
        const metric = this.cellMetric(section, cell);
        const style = cell.isTotalRow ? { fontWeight: "bold" } : {};
        const relative = cell.relative !== "-1" && this.state.showRelativeValues && metric !== "%" ? cell.relative : undefined;
        const title = cell.label + " " + this.translate("in") + " " + row.label + " " + this.translate("in") + " " + section.label;
        const value = operator === "Average" ? cell.absolute : cell.absolute;
        const format = this.cellFormat(section, cell);
        const className = this._isMainView() || this.state.sections[0] == mainSection ? classNames(classes.rowCell) : classNames(classes.rowCell, classes.detailRowCell);
        const isMainView = this._isMainView();
        const isTotalRow = cell.isTotalRow || row.label === "Total";
        return (
            <TableCell title={title} key={index} className={className} style={{whiteSpace:'nowrap',...style}}>
                {!isTotalRow && !isMainView ?
                    <a className={classes.rowAction} onClick={this.handleShowItems.bind(this, mainSection, row.label)}>
                        {NumberUtil.format(cell.absolute, format)}
                        {metric !== "" && <span style={{fontSize:'9pt',marginLeft:'5px',color:'#777'}}>{metric}</span>}
                        {relative !== undefined && <span className={classes.rowRelativeValue}>&nbsp;{relative}<span style={{fontSize:'9pt',marginLeft:'5px',color:'#777'}}>%</span></span>}
                    </a>
                :
                    <React.Fragment>
                        {NumberUtil.format(cell.absolute, format)}
                        {metric !== "" && <span style={{fontSize:'9pt',marginLeft:'5px',color:'#777'}}>{metric}</span>}
                        {relative !== undefined && <span className={classes.rowRelativeValue}>&nbsp;{relative}<span style={{fontSize:'9pt',marginLeft:'5px',color:'#777'}}>%</span></span>}
                    </React.Fragment>
                }
            </TableCell>
        );
    };

    columnOf = (section, cell) => {
        for (let i=0; i<section.columns.length; i++) {
            if (section.columns[i].label === cell.label) return section.columns[i];
        }
        return null;
    };

    cellMetric = (section, cell) => {
        const column = this.columnOf(section, cell);
        return column != null ? column.metric : "";
    };

    cellOperator = (section, cell) => {
        const column = this.columnOf(section, cell);
        return column != null ? column.operator : null;
    };

    cellCountDecimals = (section, cell) => {
        const column = this.columnOf(section, cell);
        return column != null ? column.countDecimals : 0;
    };

    cellFormat = (section, cell) => {
        const operator = this.cellOperator(section, cell);
        const countDecimals = this.cellCountDecimals(section, cell);
        let result = "0,0";
        if (operator === "Average" && countDecimals <= 0) return result + ".00";
        if (countDecimals <= 0) return result;
        result += ".";
        for (let i=0; i<countDecimals; i++) result = result + "0";
        return result;
    };

    renderConfirmDialog = () => {
        return (
            <Dialog open={this.state.openConfirm} onClose={this.handleCloseConfirm.bind(this)}>
                <DialogTitle id="alert-dialog-title">{this.translate("Open selected row")}</DialogTitle>
                <DialogContent>
                  <DialogContentText id="alert-dialog-description">{this.translate("Are you sure to open selected row?")}</DialogContentText>
                </DialogContent>
                <DialogActions>
                  <Button onClick={this.handleCloseConfirm.bind(this)} color="primary">{this.translate("Cancel")}</Button>
                  <Button onClick={this.handleOpenConfirm.bind(this)} color="primary" autoFocus>{this.translate("Accept")}</Button>
                </DialogActions>
            </Dialog>
        );
    };

    renderDialog = () => {
        const { classes } = this.props;
        const selectable = this.props.selection != null;
        const multiple = this.allowMultiSelection();
        const headerHeight = this.header.current != null ? this.header.current.offsetHeight : 0;
        const minHeight = this.props.itemHeight * this.state.itemCount;
        const height = this.dialogContainer.current != null ? this.dialogContainer.current.offsetHeight : 0;
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
                    <div ref={this.dialogContainer} style={{height:"100%",width:"100%"}} className="layout vertical flex">
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

    renderColumnsDialog = () => {
        const { classes } = this.props;

        return (
            <Dialog open={this.state.openColumnsDialog} onClose={this.handleCloseColumnsDialog.bind(this)}>
                <DialogTitle id="alert-dialog-title">{this.translate("Show/hide columns")}</DialogTitle>
                <DialogContent>
                  <DialogContentText id="alert-dialog-description">
                    {this.renderColumnsCheckboxes()}
                  </DialogContentText>
                </DialogContent>
                <DialogActions>
                  <Button onClick={this.handleCloseColumnsDialog.bind(this)} color="primary" autoFocus>{this.translate("Close")}</Button>
                </DialogActions>
            </Dialog>
        );
    };

    renderColumnsCheckboxes = () => {
        const result = [];
        const columns = this._selectorColumns();
        for (let i=0; i<columns.length; i++) result.push(this.renderColumnCheckbox(columns[i]));
        return result;
    };

    renderColumnCheckbox = (column) => {
//        return <FormControlLabel control={<Checkbox checked={this.state.hideZeros} onChange={this.handleToggleHideZeros.bind(this)} name="toggleHideZeros" color="primary"/>} label={this.translate("Hide zeros")}/>
        return (<div><FormControlLabel control={<Checkbox checked={this.isColumnVisible(column.index)} onChange={this.handleToggleColumn.bind(this, column.index)} color="primary" name={column.index}/>} label={column.label}/></div>);
    };

    refreshVisibleColumns = (value) => {
        this.setState({visibleColumns: this.visibleColumnsArrayOf(value)});
    };

    visibleColumnsArrayOf = (value) => {
        const result = [];
        for (let i=0; i<value.length; i++) result[this.findColumn(value[i].name)] = value[i].visible;
        return result;
    };

    findColumn = (name) => {
        const columns = this._selectorColumns();
        for (let i=0; i<columns.length; i++) {
            if (columns[i].label === name) return columns[i].index;
        }
        return -1;
    };

    refreshZeros = (value) => {
        this.setState({hideZeros:!value});
    };

    refreshPercentages = (value) => {
        this.setState({showRelativeValues:value});
    };

    isColumnVisible = (index) => {
        return this.state.visibleColumns[index] == null || this.state.visibleColumns[index] === true;
    };

    handleToggleColumn = (index) => {
        if (this.state.visibleColumns[index] == null) this.state.visibleColumns[index] = true;
        this.state.visibleColumns[index] = !this.state.visibleColumns[index];
        this.updateCookie(this.state.visibleColumns, this.state.name);
        this.setState({visibleColumns: this.state.visibleColumns});
        this.requester.visibleColumns(this._visibleColumns(this.state.visibleColumns));
    };

    handleShowItems = (section, row) => {
        const selectRowProvided = this.state.selectRowProvided;
        const state = selectRowProvided ? {openConfirm:true, section: section, row: row} : {open:true, section: section, row: row};
        this.setState(state);
    };

    handleOpen = () => {
        this.requester.showItems({open: true, section: this.state.section.label, row: this.state.row});
    };

	handleClose = () => {
        this.setState({open:false});
    };

    handleOpenColumnsDialog = () => {
        this.setState({openColumnsDialog:true});
    };

	handleCloseColumnsDialog = () => {
        this.setState({openColumnsDialog:false});
    };

    handleOpenConfirm = () => {
        this.setState({openConfirm:false});
        this.requester.selectRow({ section: this.state.section.label, row: this.state.row });
    };

	handleCloseConfirm = () => {
        this.setState({openConfirm:false});
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
        for (var i=0; i<countRows; i++) result = Math.max(result, (sections[0].rows[i].label.length * 9) + 20);
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
            item.columns[i].borderColor = item.columns[i].color;
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
    };

    rowDescription = (sections, rowIndex) => {
        if (sections.length <= 0) return "";
        const section = sections[0];
        return rowIndex < section.rows.length ? section.rows[rowIndex].description : "";
    };

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
        const value = !this.state.showRelativeValues;
        this.setState({showRelativeValues: value});
        this.requester.showPercentages(value);
    };

    handleToggleHideZeros = () => {
        const value = !this.state.hideZeros;
        this.setState({hideZeros: value});
        this.requester.showZeros(!value);
    };

    handleFilterFirstColumn = () => {
        const columns = this._selectorColumns();
        if (columns.length <= 0) return;
        this.handleFilterColumn(columns[0], columns[0].index);
    };

    handleFilterColumn = (column, index) => {
        this.setState({ column: { index: index, label: column.label }, order: null, orderBy: null });
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

    _visibleColumns = (visibleList) => {
        const columns = this._selectorColumns();
        const result = [];
        for (let i=0; i<columns.length; i++) {
            const visible = visibleList[columns[i].index];
            result.push({name: columns[i].label, visible: visible != null ? visible : true});
        }
        return result;
    }
}

class DynamicTable extends EmbeddedDynamicTable {
    constructor(props) {
        super(props);
    }
}

export default withStyles(DynamicTableStyles, { withTheme: true })(DynamicTable);
DisplayFactory.register("DynamicTable", withStyles(DynamicTableStyles, { withTheme: true })(DynamicTable));