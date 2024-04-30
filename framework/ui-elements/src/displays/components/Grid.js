import React from "react";
import { Link, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Checkbox, Button, FormControlLabel } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import AbstractGrid from "../../../gen/displays/components/AbstractGrid";
import GridNotifier from "../../../gen/displays/notifiers/GridNotifier";
import GridRequester from "../../../gen/displays/requesters/GridRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import DataGrid from "react-data-grid";
import { ToolsPanel } from "react-data-grid-addons";
import 'alexandria-ui-elements/res/styles/grid.css';
import 'alexandria-ui-elements/res/styles/layout.css';
import history from "alexandria-ui-elements/src/util/History";
import MaterialIcon from "./MaterialIcon";
import Icon from "./Icon";
import Select from "react-select";
import {RiseLoader} from "react-spinners";
import Theme from "app-elements/gen/Theme";
import classNames from "classnames";
import { SelectorComboBoxStyles, SelectorComboBoxTextViewStyles } from "./SelectorComboBox";

const GridSelectorStyles = {
    valueContainer: (provided, state) => ({
        ...provided,
        fontSize: '9pt',
    }),
};

const styles = theme => ({
    link : {
        color: theme.palette.primary.main,
        '&:hover' : { textDecoration: 'none' },
    },
    columnSelector : {
        width: '275px',
        marginLeft: '5px',
    },
    columnsAction : {
        color: theme.palette.primary.main,
        marginRight: '15px',
        marginTop: '3px',
        fontSize: '9pt',
        cursor: 'pointer',
        display: 'inline-block',
    },
});

class Grid extends AbstractGrid {

	constructor(props) {
		super(props);
		this.notifier = new GridNotifier(this);
		this.requester = new GridRequester(this);
        this.lastLoadedPage = [];
        this.lastRow = 0;
        this.grid = null;
		this.state = {
		    ...this.state,
		    name: this.props.id,
		    columns: [],
		    modes: [],
		    selectedIndexes: [],
		    rows: [],
		    sortColumn: null,
		    sortDirection: null,
		    groupBy: null,
		    groupByOptions: [],
		    groupByOption: null,
		    groupByMode: null,
		    openColumnsDialog: false,
		    visibleColumns: [],
		};
	};

    render() {
        if (!this.state.visible) return (<React.Fragment/>);
        window.setTimeout(() => this.restoreHorizontalScroll(), 10);
        return (
            <div style={{height:'100%',width:'100%',position:'relative'}}>
                {this.state.loading && this.renderLoading()}
                {this.renderGrid()}
                {this.renderCookieConsent()}
            </div>
        );
    };

    renderLoading = () => {
        if (this.state.rows.length > 0) this.saveHorizontalScroll(this.horizontalScroll);
        const theme = Theme.get();
        return (<div style={{position:'absolute',height:'100%',width:'100%',zIndex:'1'}} className="layout vertical flex center-center"><RiseLoader color={theme.palette.secondary.main} loading={true}/></div>);
    };

    scrollToRow(idx) {
    	var top = this.grid.getRowOffsetHeight() * idx;
    	var gridCanvas = this.grid.getDataGridDOMNode().querySelector('.react-grid-Canvas');
    	if (gridCanvas != null) gridCanvas.scrollTop = top;
    };

    saveHorizontalScroll = (defaultValue) => {
    	var gridCanvas = this.grid.getDataGridDOMNode().querySelector('.react-grid-Canvas');
    	this.horizontalScroll = gridCanvas != null ? gridCanvas.scrollLeft : (defaultValue != null ? defaultValue : 0);
    };

    restoreHorizontalScroll() {
        if (this.horizontalScroll == null) return;
    	var gridCanvas = this.grid.getDataGridDOMNode().querySelector('.react-grid-Canvas');
    	if (gridCanvas != null) gridCanvas.scrollLeft = this.horizontalScroll;
    };

    renderGrid = () => {
        const showCheckbox = this.props.selection != null && this.allowMultiSelection();
        const { classes } = this.props;
        const selectorColumnsDisabled = this.selectorColumns().length <= 0;
        this.loadingNextPage = false;
        window.setTimeout(() => this.loadNextPageIfRequired(), 1000);
        return (
            <DataGrid
                ref={(g) => {this.grid = g;}}
                columns={this.columns()}
                rowGetter={this.handleRowGetter.bind(this)}
                rowsCount={this.state.rows.length}
                emptyRowsView={this.emptyRowsView.bind(this)}
                enableCellSelect={true}
                minColumnWidth={200}
                rowSelection={{
                    showCheckbox: showCheckbox,
                    enableShiftSelect: null,
                    onRowsSelected: this.handleRowsSelected.bind(this),
                    onRowsDeselected: this.handleRowsDeselected.bind(this),
                    selectBy: { indexes: this.state.selectedIndexes }
                }}
                sortColumn={this.state.sortColumn}
                sortDirection={this.state.sortDirection}
                onGridSort={this.sortColumns.bind(this)}
                onRowClick={this.handleRowClick.bind(this)}
                onScroll={this.handleScroll.bind(this)}
                toolbar={
                    <ToolsPanel.AdvancedToolbar>
                        <div className="layout horizontal flex center">
                            <div><a className={classes.columnsAction} onClick={this.handleOpenColumnsDialog.bind(this)} disabled={selectorColumnsDisabled}>{this.translate("Show columns...")}</a></div>
                            {this.renderGroupBySelector()}
                            {this.renderGroupByModes()}
                            {this.renderGroupByOptions()}
                            {this.renderColumnsDialog()}
                        </div>
                    </ToolsPanel.AdvancedToolbar>
                }
            />
        );
    };

    sortColumns = (sortColumn, sortDirection) => {
        const sort = {column: sortColumn, mode: sortDirection};
        this.saveHorizontalScroll();
        this.setState({ sortColumn : sortColumn, sortDirection: sortDirection });
        this.saveState("sort", sort);
        this.requester.sort(sort);
    };

    refreshAllowMultiSelection = (value) => {
        const index = this.lastRow > 0 ? this.lastRow - 1 : 0;
        this.setState({ multiSelection: value });
        window.dispatchEvent(new Event('resize'));
        this.scrollToRow(index);
    };

    emptyRowsView = () => {
        if (this.state.loading) return (<React.Fragment/>);
        return (<div className="layout vertical flex center-center" style={{marginTop:'20px',fontSize:'12pt'}}>{this.translate(this.props.noItemsMessage)}</div>);
    };

    renderGroupBySelector = () => {
        if (this.state.modes.length == 0) return (<React.Fragment/>);
        const { classes } = this.props;
        const styles = { ...SelectorComboBoxStyles, ...SelectorComboBoxTextViewStyles, ...GridSelectorStyles };
        return (
            <Select className={classes.columnSelector} isClearable={true}
                placeholder={this.translate("Group by")} options={this.selectorColumns()}
                value={this.state.groupBy} onChange={this.handleSelectGroupBy.bind(this)} styles={styles}/>
        );
    };

    renderGroupByModes = () => {
        if (this.state.groupBy == null || this.state.groupByOptions.length == 0 || this.state.modes.length == 0) return (<React.Fragment/>);
        const acceptedType = this.selectorColumns()[this.findColumn(this.state.groupBy.name)].type;
        const modes = this.state.modes.filter(m => m.acceptedTypes.indexOf(acceptedType) != -1).map((mode, idx) => { return { value: mode.name, label: mode.name, index: idx }});
        const styles = { ...SelectorComboBoxStyles, ...SelectorComboBoxTextViewStyles, ...GridSelectorStyles };
        const { classes } = this.props;
        return (
            <Select className={classes.columnSelector} isClearable={false}
                placeholder={this.translate("Group by criteria")} options={modes}
                value={this.state.groupByMode} onChange={this.handleSelectGroupByMode.bind(this)}
                styles={styles}/>
        );
    };

    renderGroupByOptions = () => {
        if (this.state.groupByMode == null) return (<React.Fragment/>);
        const options = this.state.groupByOptions.map((option, idx) => { return { value: option, label: option, index: idx }});
        const styles = { ...SelectorComboBoxStyles, ...SelectorComboBoxTextViewStyles, ...GridSelectorStyles };
        const { classes } = this.props;
        return (
            <div>
                {options.length == 0 && <div style={{marginLeft:'10px'}}>{this.translate("No groups available")}</div>}
                {options.length > 0 &&
                    <Select className={classes.columnSelector} isClearable={true}
                        placeholder={this.translate("Group")} options={options}
                        value={this.state.groupByOption} onChange={this.handleSelectGroupByOption.bind(this)}
                        styles={styles}/>
                }
            </div>
        );
    };

    renderColumnsDialog = () => {
        const { classes } = this.props;
        return (
            <Dialog open={this.state.openColumnsDialog} onClose={this.handleCloseColumnsDialog.bind(this)}>
                <DialogTitle id="alert-dialog-title">{this.translate("Show columns")}</DialogTitle>
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
        const columns = this.selectorColumns();
        for (let i=1; i<columns.length; i++) result.push(this.renderColumnCheckbox(columns[i]));
        return result;
    };

    renderColumnCheckbox = (column) => {
        return (<div><FormControlLabel control={<Checkbox checked={this.isColumnVisible(column.index)} onChange={this.handleToggleColumn.bind(this, column.index)} color="primary" name={column.index}/>} label={this.translate(column.label)}/></div>);
    };

    isColumnVisible = (index) => {
        return this.state.visibleColumns[index] == null || this.state.visibleColumns[index] === true;
    };

    handleToggleColumn = (index) => {
        if (this.state.visibleColumns[index] == null) this.state.visibleColumns[index] = true;
        this.state.visibleColumns[index] = !this.state.visibleColumns[index];
        const visibleColumns = this._visibleColumns(this.state.visibleColumns);
        this.saveState("visibleColumns", visibleColumns);
        this.requester.updateVisibleColumns(visibleColumns);
    };

    handleSelectGroupBy = (groupBy) => {
        const acceptedType = groupBy != null ? this.selectorColumns()[this.findColumn(groupBy.value)].type : null;
        const modes = this.state.modes.filter(m => m.acceptedTypes.indexOf(acceptedType) != -1).map((mode, idx) => { return { value: mode.name, label: mode.name, index: idx }});
        const modeNames = [];
        modes.forEach((mode, idx) => modeNames[mode.value] = mode.value);
        const mode = this.state.groupByMode != null && modeNames[this.state.groupByMode.name] != null ? this.state.groupByMode : null;
        this.saveState("groupBy", null);
        this.setState({ groupBy : groupBy != null ? { name: groupBy.value, label: groupBy.label } : null, groupByMode: mode, groupByOption: null });
        this.requester.updateGroupByOptions({ column: groupBy != null ? groupBy.value : null, mode: mode != null ? mode.name : null });
    };

    handleSelectGroupByMode = (mode) => {
        this.setState({groupByMode: mode != null ? { name: mode.value, label: mode.label } : null, groupByOption: null});
        let modeName = mode != null ? mode.value : null;
        this.saveState("groupBy", { column: this.state.groupBy.name, group: null, mode: modeName, group: null, groupIndex: null });
        this.requester.updateGroupByOptions({ column: this.state.groupBy != null ? this.state.groupBy.name : null, mode: modeName });
    };

    handleSelectGroupByOption = (option) => {
        this.setState({groupByOption: option != null ? { name: option.value, label: option.label, index: option.index } : null});
        let group = option != null ? option.value : null;
        let groupIndex = option != null ? option.index : null;
        let mode = this.state.groupByMode != null ? this.state.groupByMode.name : null;
        const groupBy = { column: this.state.groupBy.name, group: group, groupIndex: groupIndex, mode: mode };
        this.saveState("groupBy", groupBy);
        this.requester.groupBy(groupBy);
    };

    columns = () => {
        return this.state.columns.filter((column, idx) => this.isColumnVisible(idx)).map((column, idx) => ({
            key: column.name,
            name: column.label,
            filterable: true, editable: false,
            sortable: column.sortable, draggable: false,
            resizable: true, frozen: column.fixed, width: column.width != -1 ? column.width : undefined,
            headerRenderer : this.columnRenderer(column, idx),
            formatter : this.rowFormatter.bind(this, column)
        }));
    };

    selectorColumns = () => {
        return this.state.columns.map((column, idx) => ({
            value: column.name,
            label: this.translate(column.label),
            type: column.type,
            width: column.width,
            index: idx
        }));
    };

    columnRenderer = (column, idx) => {
        const type = column.type;
        const style = { display: 'inline-block' };
        if (type === "Number" || type === "Date") style.float = 'right';
        return (<div style={style}>{this.translate(column.label)}</div>);
    };

    rowFormatter = (column, data) => {
        const { classes } = this.props;
        const type = column.type;
        const value = this.rowValue(data.value);
        const color = this.rowColor(data.value);
        const style = color !== undefined ? { color: color } : {};
        if (type === "Icon") return (<Icon icon={value} color={color}/>);
        else if (type === "MaterialIcon") return (<MaterialIcon icon={value} color={color}/>);
        else if (type === "Link" && data.row.selectable) return (<Link className={classNames(classes.link)} style={style} component="button" onClick={this.handleCellClick.bind(this, column, data)}>{value}</Link>);
        else if (type === "Number" || type === "Date") return (<div style={{textAlign:'right',...style}}>{value}</div>);
        return (<div style={style}>{value}</div>);
    };

    handleRowClick = (row, data, c, e) => {
        if (!data.selectable) return;
        const columns = this.linkColumns();
        const column = columns.length > 0 ? columns[0] : (this.state.columns.length > 0 ? this.state.columns[0] : null);
        if (column == null) return;
        const dataValue = data[column.name];
        const value = this.rowValue(dataValue);
        const address = this.rowAddress(dataValue);
        if (address != null) history.push(address, {});
        this.requester.cellClick({ column: column.name, columnIndex: 0, row: value, rowIndex: row });
    };

    linkColumns = () => {
        return this.state.columns.filter(c => c.type === "Link");
    };

    handleCellClick = (column, data, e) => {
        e.stopPropagation();
        const index = this.rowIndex(data.value);
        const value = this.rowValue(data.value);
        const address = this.rowAddress(data.value);
        const columnIndex = this.columnIndex(column.name);
        if (address != null) history.push(address, {});
        this.requester.cellClick({ column: column.name, columnIndex: columnIndex, row: value, rowIndex: index });
    };

    handleScroll = (e, a, b) => {
        const page = this.pageOf(e.rowOverscanEndIdx+(this.state.pageSize*0,2));
        if (this.lastLoadedPage[page]) return;
        this.lastLoadedPage[page] = true;
        this.requester.loadNextPage();
    };

    loadNextPageIfRequired = () => {
        if (this.loadingNextPage) return;
        const hasMore = this.state.rows.length < this.state.itemCount;
        const scrollableTarget = this.grid != null && this.grid.getDataGridDOMNode() != null ? this.grid.getDataGridDOMNode().querySelector('.react-grid-Canvas') : null;
        const scrollIsVisible = scrollableTarget == null || scrollableTarget.offsetHeight == 0 || scrollableTarget.scrollHeight > scrollableTarget.offsetHeight;
        if (!hasMore || scrollIsVisible) return;
        this.loadingNextPage = true;
        this.requester.loadNextPage();
    };

    rowInfo = (index, value, address, color) => {
         return index + "##" + (value != null ? value : "") + "##" + (address != null ? address : "") + "##" + (color != null ? color : "");
    };

    rowIndex = (value) => {
        return value.split("##")[0];
    };

    rowValue = (value) => {
        const info = value.split("##");
        return info.length > 1 && info[1] !== "" ? info[1] : undefined;
    };

    rowAddress = (value) => {
        const info = value.split("##");
        return info.length > 2 && info[2] !== "" ? info[2] : undefined;
    };

    rowColor = (value) => {
        const info = value.split("##");
        return info.length > 3 && info[3] !== "" ? info[3] : undefined;
    };

    columnIndex = (columnName) => {
        const columns = this.columns();
        for (let i=0; i<columns.length; i++) {
            if (columns[i].key == columnName) return i;
        }
        return -1;
    };

    handleRowGetter = (i) => {
        const rows = this.state.rows;
        this.lastRow = i;
        return rows[i] || {};
    };

    addRow = (row) => {
        this.addRows([row]);
    };

    addRows = (newRows) => {
        const columns = this.state.columns;
        let rows = this.state.rows;
        let offset = rows.length;
        for (let i=0; i<newRows.length; i++) {
            let row = { selectable: newRows[i].selectable };
            for (let j=0; j<columns.length; j++) {
                const address = newRows[i].cells[j].address != null ? newRows[i].cells[j].address : null;
                const color = newRows[i].cells[j].color != null ? newRows[i].cells[j].color : null;
                row[columns[j].name] = this.rowInfo(i+offset, newRows[i].cells[j].value, address, color);
            }
            rows.push(row);
        }
        const page = this.pageOf(rows.length-1);
        this.lastLoadedPage[page] = true;
        this.setState({ rows: rows });
    };

	pageOf = (index) => {
		return Math.floor(index / this.state.pageSize) + (index % this.state.pageSize > 0 ? 1 : 0);
	};

    renderColumn(column, index) {
        return (<div>{this.translate(column.label)}</div>);
    };

    refreshSelection = (indexes) => {
        const newIndexes = indexes.map(r => parseInt(r));
        this.setState({selectedIndexes: newIndexes});
    };

    handleRowsSelected = rows => {
        const indexes = this.state.selectedIndexes.concat(rows.map(r => r.rowIdx));
        this.setState({selectedIndexes: indexes});
        this.requester.selection(indexes);
    };

    handleRowsDeselected = rows => {
        let rowIndexes = rows.map(r => r.rowIdx);
        const indexes = this.state.selectedIndexes.filter(i => rowIndexes.indexOf(i) === -1);
        this.setState({selectedIndexes: indexes});
        this.requester.selection(indexes);
    };

	refreshInfo = (info) => {
	    this.setState({
	        columns: info.columns,
	        modes: info.modes,
	        name: info.name,
        });
	};

	loadState = (stateName) => {
	    const state = this.getCookie(this.cookieName(stateName));
	    if (state == null) return;
	    this.requester.updateState(state);
	};

	saveState = (property, value) => {
	    const cookieName = this.cookieName(this.state.name);
	    let state = this.getCookie(cookieName);
	    if (state == null) state = {};
	    state[property] = value;
        this.updateCookie(state, cookieName);
	};

	cookieName = (key) => {
	    return key + "_grid";
	};

	refreshItemCount = (itemCount) => {
		this.setState({ itemCount: itemCount });
	};

    handleOpenColumnsDialog = () => {
        this.setState({openColumnsDialog:true});
    };

	handleCloseColumnsDialog = () => {
        this.setState({openColumnsDialog:false});
    };

    refreshSort = (value) => {
        if (value != null && this.state.sortColumn != null && this.state.sortColumn == value.column) return;
        this.grid.handleSort(value.column, value.mode);
        this.setState({ sortColumn: value.column, sortDirection: value.mode });
    };

    refreshGroupBy = (value) => {
        const groupBy = this.selectorColumns()[this.findColumn(value.column)];
        if (groupBy != null && this.state.groupBy != null && this.state.groupBy.name == groupBy.name) return;
        const groupByMode = value.mode != null && value.mode !== "" ? { name: value.mode, label: value.mode } : null;
        const groupByOption = value.group != null && value.group !== "" ? { name: value.group, label: value.group } : null;
        this.setState({ groupBy: { name: groupBy.value, label: groupBy.label }, groupByMode: groupByMode, groupByOption: groupByOption });
    };

    refreshGroupByOptions = (options) => {
        this.setState({groupByOptions: options});
    };

    refreshVisibleColumns = (value) => {
        const index = this.lastRow > 0 ? this.lastRow - 1 : 0;
        this.setState({visibleColumns: this.visibleColumnsArrayOf(value)});
        window.dispatchEvent(new Event('resize'));
        this.scrollToRow(index);
    };

    visibleColumnsArrayOf = (value) => {
        const result = [];
        for (let i=0; i<value.length; i++) result[this.findColumn(value[i].name)] = value[i].visible;
        return result;
    };

    findColumn = (name) => {
        const columns = this.selectorColumns();
        for (let i=0; i<columns.length; i++) {
            if (columns[i].value === name) return columns[i].index;
        }
        return -1;
    };

    _visibleColumns = (visibleList) => {
        const columns = this.selectorColumns();
        const result = [];
        for (let i=0; i<columns.length; i++) {
            const visible = visibleList[columns[i].index];
            result.push({name: columns[i].value, visible: visible != null ? visible : true});
        }
        return result;
    };

    clearContainer = (params) => {
        if (super.clearContainer) super.clearContainer(params);
        this.lastLoadedPage = [];
        this.lastRow = 0;
        this.setState({rows:[]});
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Grid));
DisplayFactory.register("Grid", withStyles(styles, { withTheme: true })(withSnackbar(Grid)));