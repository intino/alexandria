import React from "react";
import {
    Button,
    Checkbox,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    FormControlLabel,
    IconButton,
    Link,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow
} from '@mui/material';
import {ArrowDownward, ArrowUpward} from '@mui/icons-material';
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractGrid from "../../../gen/displays/components/AbstractGrid";
import GridNotifier from "../../../gen/displays/notifiers/GridNotifier";
import GridRequester from "../../../gen/displays/requesters/GridRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import 'alexandria-ui-elements/res/styles/grid.css';
import 'alexandria-ui-elements/res/styles/layout.css';
import history from "alexandria-ui-elements/src/util/History";
import MaterialIcon from "./MaterialIcon";
import Icon from "./Icon";
import {dialogActionButtonStyles} from "./ButtonStyles";
import Select from "react-select";
import {RiseLoader} from "react-spinners";
import Theme from "app-elements/gen/Theme";
import classNames from "classnames";
import {selectorComboBoxStyles, SelectorComboBoxTextViewStyles} from "./SelectorComboBox";
import {collectionPalette} from "./Collection";
import {linkPalette} from "./ThemeTokens";

const GridSelectorStyles = {
    valueContainer: (provided, state) => ({
        ...provided,
        fontSize: '9pt',
    }),
};

const styles = theme => ({
    gridSurface: {
        background: collectionPalette(theme).viewportBackground,
        border: `1px solid ${collectionPalette(theme).borderColor}`,
        borderRadius: "18px",
        boxSizing: "border-box",
        position: "absolute",
        inset: 0,
        display: "block",
        minWidth: 0,
        minHeight: 0,
        maxWidth: "100%",
        overflow: "hidden",
    },
    gridTable: {
        background: theme.palette.mode === "dark" ? "rgba(11, 18, 27, 0.92)" : "#ffffff",
        borderCollapse: "separate",
        "& .MuiTableHead-root": {
            background: theme.palette.mode === "dark" ? "rgba(18, 28, 42, 0.82)" : "rgba(239, 246, 255, 0.78)",
            position: "sticky",
            top: 0,
            zIndex: 4,
            backdropFilter: "blur(12px)",
        },
        "& .MuiTableHead-root .MuiTableRow-root": {
            background: theme.palette.mode === "dark" ? "rgba(18, 28, 42, 0.82)" : "rgba(239, 246, 255, 0.78)",
            position: "sticky",
            top: 0,
            zIndex: 4,
        },
        "& .MuiTableCell-root": {
            padding: "2px 8px",
            fontSize: "inherit",
            borderBottomColor: collectionPalette(theme).rowBorderColor,
            background: "transparent",
            color: theme.palette.mode === "dark" ? "rgba(255,255,255,0.88)" : "inherit",
        },
        "& .MuiTableHead-root .MuiTableCell-root": {
            background: theme.palette.mode === "dark" ? "rgba(18, 28, 42, 0.82)" : "rgba(239, 246, 255, 0.78)",
            color: theme.palette.mode === "dark" ? "rgba(241,245,249,0.96)" : "inherit",
            borderBottomColor: collectionPalette(theme).borderColor,
        },
        "& .MuiTableCell-stickyHeader": {
            position: "sticky",
            top: 0,
            zIndex: 3,
            background: theme.palette.mode === "dark" ? "rgba(18, 28, 42, 0.82)" : "rgba(239, 246, 255, 0.78)",
            color: theme.palette.mode === "dark" ? "rgba(241,245,249,0.96)" : "inherit",
            backdropFilter: "blur(12px)",
        },
        "& .MuiTableHead-root .MuiCheckbox-root": {
            color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.84)" : undefined,
        },
        "& .MuiTableBody-root .MuiCheckbox-root": {
            color: theme.palette.mode === "dark" ? "rgba(203,213,225,0.82)" : undefined,
        },
        "& .MuiCheckbox-root.Mui-checked": {
            color: theme.palette.primary.main,
        },
        "& .MuiTableHead-root .MuiTableCell-paddingCheckbox": {
            background: theme.palette.mode === "dark" ? "rgba(18, 28, 42, 0.82)" : "rgba(239, 246, 255, 0.78)",
        },
        "& .MuiTableBody-root .MuiTableCell-paddingCheckbox": {
            background: theme.palette.mode === "dark" ? "rgba(15, 23, 42, 0.72)" : "transparent",
        },
    },
    gridRow: {
        "&:hover": {
            backgroundColor: `${collectionPalette(theme).rowHoverBackground} !important`,
        },
        "& td": {
            borderBottomColor: collectionPalette(theme).rowBorderColor,
        },
    },
    link : {
        color: linkPalette(theme).color,
        textDecoration: 'none',
        whiteSpace: "nowrap",
        '&:hover' : { textDecoration: 'none', opacity: 0.88, color: linkPalette(theme).hoverColor },
    },
    columnSelector : {
        width: '275px',
        marginLeft: '5px',
    },
    columnsAction : {
        color: linkPalette(theme).color,
        marginRight: '15px',
        fontSize: '9pt',
        cursor: 'pointer',
        display: 'inline-block',
        textDecoration: 'none',
        fontWeight: 500,
        '&:hover': {
            color: linkPalette(theme).hoverColor,
        },
    },
    toolbarAction : {
        marginRight: "10px",
        cursor: "pointer",
        display: "inline-block",
        color: linkPalette(theme).color,
        textDecoration: "none",
        fontWeight: 500,
        '&:hover': {
            color: linkPalette(theme).hoverColor,
        },
    },
    toolbarActionDisabled : {
        cursor: "default",
        color: theme.palette.grey.primary
    },
    resizeHandle: {
        position: "absolute",
        top: 0,
        right: "-4px",
        width: "8px",
        height: "100%",
        cursor: "col-resize",
        zIndex: 2,
        userSelect: "none",
        touchAction: "none",
        "&::after": {
            content: "\"\"",
            position: "absolute",
            top: "20%",
            bottom: "20%",
            left: "3px",
            width: "2px",
            borderRadius: "999px",
            background: theme.palette.mode === "dark" ? "rgba(148,163,184,0.28)" : "rgba(15,23,42,0.12)",
            transition: "background-color 120ms ease",
        },
        "&:hover::after": {
            background: theme.palette.primary.main,
        }
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
        this.gridCanvasRef = React.createRef();
        this.numericColumnCache = {};
        this.numericColumnCacheRows = null;
        this.numericColumnCacheRowsLength = null;
        this.pendingRows = [];
        this.handleWindowResize = this.resize.bind(this);
        this.gridViewportContainerRef = React.createRef();
        this.columnResizeHandleRef = React.createRef();
        this.state = {
            ...this.state,
            key: 0,
            name: this.props.id,
            contentCleared: false,
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
            columnsOrdering: [],
            visibleColumns: [],
            maxColumnSize: 350,
            minColumnSize: 60,
            columnWidths: {},
            viewportWidth: 0,
            viewportHeight: 0,
        };
    };

    componentDidMount() {
        super.componentDidMount();
        window.addEventListener('resize', this.handleWindowResize);
        this.observeViewportContainer();
    };

    componentDidUpdate() {
        window.setTimeout(() => this.restoreHorizontalScroll(), 10);
        if (!this.state.loading) window.setTimeout(() => this.loadNextPageIfRequired(), 1000);
    };

    componentWillUnmount() {
        window.removeEventListener('resize', this.handleWindowResize);
        window.removeEventListener('pointermove', this.handleColumnResizeMove);
        window.removeEventListener('pointerup', this.handleColumnResizeEnd);
        window.removeEventListener('pointercancel', this.handleColumnResizeEnd);
        window.removeEventListener('blur', this.handleColumnResizeEnd);
        if (this.viewportResizeObserver != null) this.viewportResizeObserver.disconnect();
        super.componentWillUnmount();
    };

    render() {
        const theme = Theme.get();
        if (!this.state.visible) return (<React.Fragment/>);
        const componentStyle = this.style() != null ? this.style() : {};
        const rootStyle = {
            position:'relative',
            display:'flex',
            flexDirection:'column',
            flex:'1 1 0',
            alignSelf:'stretch',
            boxSizing:'border-box',
            minHeight:0,
            minWidth:0,
            maxWidth:'100%',
            overflow:'hidden',
            width: componentStyle.width != null ? componentStyle.width : '100%',
            height: componentStyle.height != null ? componentStyle.height : '100%',
            ...componentStyle,
            ...(this.props.style != null ? this.props.style : {})
        };
        const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
        return (
            <div className={isDark ? "alexandria-grid dark self-stretch" : "alexandria-grid self-stretch"} style={rootStyle}>
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
        const gridCanvas = this.gridCanvasRef.current;
        const rowHeight = this.rowHeight();
        var top = rowHeight * idx;
        if (gridCanvas != null) gridCanvas.scrollTop = top;
    };

    saveHorizontalScroll = (defaultValue) => {
        var gridCanvas = this.gridCanvasRef.current;
        this.horizontalScroll = gridCanvas != null ? gridCanvas.scrollLeft : (defaultValue != null ? defaultValue : 0);
    };

    restoreHorizontalScroll() {
        if (this.horizontalScroll == null) return;
        var gridCanvas = this.gridCanvasRef.current;
        if (gridCanvas != null) gridCanvas.scrollLeft = this.horizontalScroll;
    };

    renderGrid = () => {
        const showCheckbox = this.props.selection != null && this.allowMultiSelection();
        const { classes } = this.props;
        const selectorColumnsDisabled = this.selectorColumns().length <= 0;
        const columns = this.columns();
        const theme = Theme.get();
        const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
        const palette = collectionPalette(theme);
        return (
            <div key={this.state.key} className="layout vertical flex self-stretch" style={{height:'100%',width:'100%',flex:'1 1 0',alignSelf:'stretch',boxSizing:'border-box',minHeight:0,minWidth:0,maxWidth:'100%',overflow:'hidden'}}>
                {this.props.showToolbar &&
                    <div style={{flex:'0 0 auto', width:'100%', marginBottom:'8px'}}>
                        <div className="layout horizontal flex center">
                            {this.selectorColumns().length > 1 && <div><a className={classes.columnsAction} onClick={this.handleOpenColumnsDialog.bind(this)} disabled={selectorColumnsDisabled}>{this.translate("Show columns...")}</a></div>}
                            {this.renderGroupBySelector()}
                            {this.renderGroupByModes()}
                            {this.renderGroupByOptions()}
                            {this.renderColumnsDialog()}
                        </div>
                    </div>
                }
                <div ref={this.gridViewportContainerRef} style={{flex:'1 1 0',minHeight:0,minWidth:0,width:'100%',maxWidth:'100%',boxSizing:'border-box',position:'relative',overflow:'hidden'}}>
                    <div
                        className={classes.gridSurface}
                        ref={this.gridCanvasRef}
                        onScroll={this.handleScroll.bind(this)}
                        style={{
                            overflow:'auto',
                            overscrollBehavior:'contain',
                            width:'100%',
                            height:'100%',
                            minWidth:0,
                            minHeight:0,
                            maxWidth:'100%',
                            maxHeight:'100%'
                        }}
                    >
                        {this.state.contentCleared || this.state.rows.length === 0 ? this.emptyRowsView() :
                            <div style={{display:'inline-block', minWidth:'100%', width:'max-content', maxWidth:'none', verticalAlign:'top'}}>
                                <Table size="small" stickyHeader className={classes.gridTable} style={{minWidth:'100%', width:'max-content', background:isDark ? palette.viewportBackground : undefined}}>
                                    <TableHead>
                                        <TableRow style={isDark ? { background: palette.headerBackground } : undefined}>
                                            {showCheckbox &&
                                                <TableCell padding="checkbox" style={isDark ? { background: palette.headerBackground, color: "rgba(241,245,249,0.96)" } : undefined}>
                                                    <Checkbox
                                                        checked={this.isAllRowsSelected()}
                                                        indeterminate={!this.isAllRowsSelected() && this.state.selectedIndexes.length > 0}
                                                        onChange={this.handleToggleAllRows.bind(this)}
                                                        sx={isDark ? {
                                                            color: "rgba(226,232,240,0.84)",
                                                            backgroundColor: "transparent",
                                                            "&.Mui-checked": { color: theme.palette.primary.main },
                                                            "&.MuiCheckbox-indeterminate": { color: theme.palette.primary.main }
                                                        } : undefined}
                                                    />
                                                </TableCell>
                                            }
                                            {columns.map((column, idx) => this.renderHeaderCell(column, idx))}
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {this.state.rows.map((row, rowIndex) => this.renderGridRow(columns, row, rowIndex, showCheckbox))}
                                    </TableBody>
                                </Table>
                            </div>
                        }
                    </div>
                </div>
            </div>
        );
    };

    observeViewportContainer = () => {
        const container = this.gridViewportContainerRef.current;
        if (container == null) return;
        if (this.viewportResizeObserver == null && typeof ResizeObserver !== "undefined") {
            this.viewportResizeObserver = new ResizeObserver(() => this.updateViewportSize());
            this.viewportResizeObserver.observe(container);
        }
        this.updateViewportSize();
    };

    updateViewportSize = () => {
        const container = this.gridViewportContainerRef.current;
        if (container == null) return;
        const viewportWidth = container.clientWidth;
        const viewportHeight = container.clientHeight;
        if (viewportWidth === this.state.viewportWidth && viewportHeight === this.state.viewportHeight) return;
        this.setState({ viewportWidth, viewportHeight });
    };

    renderHeaderCell = (column, idx) => {
        const { classes } = this.props;
        const active = this.state.sortColumn === column.key;
        const direction = this.state.sortDirection;
        const theme = Theme.get();
        const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
        const palette = collectionPalette(theme);
        const numericColumnInfo = this._getNumericColumnInfo(column.key);
        const rightAligned = this._isRightAlignedColumn(column, numericColumnInfo) || column.type === "Integer";
        return (
            <TableCell
                key={column.key}
                onClick={column.sortable ? this.handleHeaderSortClick.bind(this, column) : undefined}
                style={{
                    cursor: column.sortable ? 'pointer' : 'default',
                    minWidth: column.width,
                    width: column.width,
                    whiteSpace: 'nowrap',
                    textAlign: rightAligned ? 'right' : undefined,
                    position: 'relative',
                    ...(isDark ? { background: palette.headerBackground, color: "rgba(241,245,249,0.96)" } : {})
                }}
            >
                <div className={classNames("layout horizontal center", rightAligned ? "end-justified" : undefined)} style={rightAligned ? { width: "100%" } : undefined}>
                    <div>{column.headerRenderer != null ? column.headerRenderer : column.name}</div>
                    {active && direction === 'ASC' && <ArrowUpward fontSize="inherit" style={{marginLeft:'4px'}}/>}
                    {active && direction === 'DESC' && <ArrowDownward fontSize="inherit" style={{marginLeft:'4px'}}/>}
                </div>
                <div
                    className={classes.resizeHandle}
                    ref={this.columnResizeHandleRef}
                    onPointerDown={this.handleColumnResizeStart.bind(this, column)}
                    onPointerMove={this.handleColumnResizeMove}
                    onPointerUp={this.handleColumnResizeEnd}
                    onPointerCancel={this.handleColumnResizeEnd}
                    onDoubleClick={this.handleColumnResizeReset.bind(this, column)}
                    onClick={(event) => event.stopPropagation()}
                />
            </TableCell>
        );
    };

    renderGridRow = (columns, row, rowIndex, showCheckbox) => {
        const { classes } = this.props;
        const theme = Theme.get();
        const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
        return (
            <TableRow key={rowIndex} hover className={classes.gridRow}>
                {showCheckbox &&
                    <TableCell padding="checkbox" style={isDark ? { background: "rgba(15, 23, 42, 0.72)" } : undefined}>
                        <Checkbox
                            checked={this.state.selectedIndexes.indexOf(rowIndex) !== -1}
                            onChange={this.handleToggleRowSelection.bind(this, rowIndex)}
                            sx={isDark ? {
                                color: "rgba(203,213,225,0.82)",
                                backgroundColor: "transparent",
                                "&.Mui-checked": { color: theme.palette.primary.main }
                            } : undefined}
                        />
                    </TableCell>
                }
                {columns.map((column) => this.renderGridBodyCell(column, row, rowIndex))}
            </TableRow>
        );
    };

    renderGridBodyCell = (column, row, rowIndex) => {
        const theme = Theme.get();
        const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
        const value = row[column.key];
        return (
            <TableCell
                key={`${rowIndex}-${column.key}`}
                onClick={this.handleRowClick.bind(this, rowIndex, row, column)}
                style={{
                    minWidth: column.width,
                    width: column.width,
                    backgroundColor: isDark ? "rgba(15, 23, 42, 0.72)" : undefined
                }}
            >
                {column.formatter({ row, value })}
            </TableCell>
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
        const styles = { ...selectorComboBoxStyles(Theme.get()), ...SelectorComboBoxTextViewStyles, ...GridSelectorStyles };
        return (
            <Select className={classes.columnSelector} isClearable={true}
                    placeholder={this.translate("Group by")} options={this.selectorColumns().filter(c => c.label != "" && c.type != "Icon" && c.type != "MaterialIcon")}
                    value={this.state.groupBy} onChange={this.handleSelectGroupBy.bind(this)} styles={styles}/>
        );
    };

    renderGroupByModes = () => {
        if (this.state.groupBy == null || this.state.groupByOptions.length == 0 || this.state.modes.length == 0) return (<React.Fragment/>);
        const acceptedType = this.selectorColumns()[this.findColumn(this.state.groupBy.name)].type;
        const modes = this.state.modes.filter(m => m.acceptedTypes.indexOf(acceptedType) != -1).map((mode, idx) => { return { value: mode.name, label: mode.name, index: idx }});
        const styles = { ...selectorComboBoxStyles(Theme.get()), ...SelectorComboBoxTextViewStyles, ...GridSelectorStyles };
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
        const styles = { ...selectorComboBoxStyles(Theme.get()), ...SelectorComboBoxTextViewStyles, ...GridSelectorStyles };
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
        const allSelected = this.isAllColumnsSelected();
        const noneSelected = this.isAllColumnsUnselected();
        const noConfiguration = this.state.visibleColumns.length === 0 && this.state.columnsOrdering.length === 0;
        return (
            <Dialog open={this.state.openColumnsDialog} onClose={this.handleCloseColumnsDialog.bind(this)}>
                <DialogTitle id="alert-dialog-title">{this.translate("Show columns")}</DialogTitle>
                <DialogContent style={{minWidth:"450px"}}>
                    <div className="layout horizontal">
                        <div className="layout horizontal flex">
                            <a className={classNames(classes.toolbarAction, allSelected ? classes.toolbarActionDisabled : "")} onClick={this.handleSelectAllColumns.bind(this)} disabled={allSelected}>{this.translate("All")}</a>
                            <a className={classNames(classes.toolbarAction, noneSelected ? classes.toolbarActionDisabled : "")} onClick={this.handleUnselectAllColumns.bind(this)} disabled={noneSelected}>{this.translate("None")}</a>
                            <a className={classes.toolbarAction} onClick={this.handleInvertColumnsSelection.bind(this)} disabled={false}>{this.translate("Invert")}</a>
                        </div>
                        <div className="layout horizontal end-justified">
                            <a className={classNames(classes.toolbarAction, noConfiguration ? classes.toolbarActionDisabled : "")} onClick={this.handleResetColumns.bind(this)} disabled={allSelected}>{this.translate("Reset")}</a>
                        </div>
                    </div>
                    <div style={{overflow:'auto',height:'350px'}}>
                        <DialogContentText id="alert-dialog-description">{this.renderColumnsCheckboxes()}</DialogContentText>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button sx={dialogActionButtonStyles} onClick={this.handleCloseColumnsDialog.bind(this)} color="primary" autoFocus>{this.translate("Close")}</Button>
                </DialogActions>
            </Dialog>
        );
    };

    isAllColumnsSelected = () => {
        const columns = this.selectorColumns();
        for (var i=0; i<columns.length; i++) {
            if (!this.state.visibleColumns[columns[i].index]) return false;
        }
        return true;
    };

    isAllColumnsUnselected = () => {
        const columns = this.selectorColumns();
        for (var i=0; i<columns.length; i++) {
            if (this.state.visibleColumns[columns[i].index]) return false;
        }
        return true;
    };

    renderColumnsCheckboxes = () => {
        const result = [];
        const columns = this.selectorColumns();
        for (let i=0; i<columns.length; i++) {
            if (columns[i].label === "") continue;
            result.push(this.renderColumnCheckbox(columns[i]));
        }
        return result;
    };

    renderColumnCheckbox = (column) => {
        return (
            <div className="layout horizontal center" onMouseOver={this.handleShowColumnToolbar.bind(this, column)} onMouseLeave={this.handleHideColumnToolbar.bind(this, column)}>
                <div className="layout horizontal flex">
                    <FormControlLabel control={<Checkbox checked={this.isColumnVisible(column.originalIndex)} onChange={this.handleToggleColumn.bind(this, column.originalIndex)} color="primary" name={column.index}/>} label={this.translate(column.label)}/>
                </div>
                <div className="layout horizontal center end-justified" id={this.props.id + column.index + "_toolbar"} style={{display:'none',marginRight:'10px'}}>
                    <IconButton onClick={this.handleMoveUpColumn.bind(this, column)} size="small"><ArrowUpward/></IconButton>
                    <IconButton onClick={this.handleMoveDownColumn.bind(this, column)} size="small"><ArrowDownward/></IconButton>
                </div>
            </div>
        );
    };

    handleShowColumnToolbar = (column) => {
        const element = document.getElementById(this.props.id + column.index + "_toolbar");
        if (element == null) return;
        element.style.display = "block";
    };

    handleHideColumnToolbar = (column) => {
        const element = document.getElementById(this.props.id + column.index + "_toolbar");
        if (element == null) return;
        element.style.display = "none";
    };

    handleMoveUpColumn = (column) => {
        const current = this.state.columnsOrdering.length !== 0 ? this.state.columnsOrdering : this.state.columns;
        const newOrdering = current.map((c, idx) => {
            const index = c.index != null ? c.index : idx;
            if (index === column.index) return {name: c.name, index: index - 1};
            if (index === column.index - 1) return {name: c.name, index: index + 1};
            return {name: c.name, index: index};
        });
        this.saveState("columnsOrdering", newOrdering);
        this.requester.updateColumnsOrdering(newOrdering);
    };

    handleMoveDownColumn = (column) => {
        const current = this.state.columnsOrdering.length !== 0 ? this.state.columnsOrdering : this.state.columns;
        const newOrdering = current.map((c, idx) => {
            const index = c.index != null ? c.index : idx;
            if (index === column.index) return {name: c.name, index: index + 1};
            if (index === column.index + 1) return {name: c.name, index: index - 1};
            return {name: c.name, index: index};
        });
        this.saveState("columnsOrdering", newOrdering);
        this.requester.updateColumnsOrdering(newOrdering);
    };

    isColumnVisible = (index) => {
        return this.state.visibleColumns[index] == null || this.state.visibleColumns[index] === true;
    };

    handleResetColumns = () => {
        this.saveState("visibleColumns", []);
        this.saveState("columnsOrdering", []);
        this.requester.updateVisibleColumns([]);
        this.requester.updateColumnsOrdering([]);
    };

    handleSelectAllColumns = () => {
        const columns = this.selectorColumns();
        const visibleColumns = this.state.visibleColumns;
        for (var i=0; i<columns.length; i++) visibleColumns[columns[i].index] = true;
        this.saveState("visibleColumns", this._visibleColumns(visibleColumns));
        this.requester.updateVisibleColumns(this._visibleColumns(visibleColumns));
    };

    handleUnselectAllColumns = () => {
        const columns = this.selectorColumns();
        const visibleColumns = this.state.visibleColumns;
        for (var i=0; i<columns.length; i++) visibleColumns[columns[i].index] = false;
        this.saveState("visibleColumns", this._visibleColumns(visibleColumns));
        this.requester.updateVisibleColumns(this._visibleColumns(visibleColumns));
    };

    handleInvertColumnsSelection = () => {
        const columns = this.selectorColumns();
        const visibleColumns = this.state.visibleColumns;
        for (var i=0; i<columns.length; i++) visibleColumns[columns[i].index] = !visibleColumns[columns[i].index];
        this.saveState("visibleColumns", this._visibleColumns(visibleColumns));
        this.requester.updateVisibleColumns(this._visibleColumns(visibleColumns));
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

    columnsWidths = () => {
        const result = {};
        const columns = this.state.columns;
        const gridCanvas = this.gridCanvasRef.current;
        const viewportWidth = this.state.viewportWidth > 0 ? this.state.viewportWidth : (gridCanvas != null ? gridCanvas.clientWidth : 0);
        const configuredWidths = this.state.columnWidths || {};

        for (let i=0; i<columns.length; i++)
            result[columns[i].name] = columns[i].type === "Icon" || columns[i].type === "MaterialIcon" ? 29 : this.state.rows.length > 0 ? this.getWidth(columns[i].label, 10) : undefined;

        for (let i=0; i<this.state.rows.length; i++) {
            for (let j=0; j<columns.length; j++) {
                if (columns[j].type === "Icon" || columns[j].type === "MaterialIcon") continue;
                const width = columns[j].width != -1 ? columns[j].width : Math.max(this.getWidth(this.rowValue(this.state.rows[i][columns[j].name]), 9), result[columns[j].name]);
                result[columns[j].name] = Math.min(width, this.state.maxColumnSize);
            }
        }

        for (let i=0; i<columns.length; i++) {
            const configuredWidth = configuredWidths[columns[i].name];
            if (configuredWidth != null) result[columns[i].name] = configuredWidth;
        }

        let totalWidth = 0;
        let lastVisibleColumn = null;
        for (let j=0; j<columns.length; j++) {
            if (this.state.visibleColumns.length > 0 && this.state.visibleColumns.length === columns.length && !this.state.visibleColumns[j]) continue;
            totalWidth += result[columns[j].name];
            if (configuredWidths[columns[j].name] == null && columns[j].type !== "Number" && columns[j].type !== "Date" && columns[j].type !== "Icon" && columns[j].type !== "MaterialIcon") lastVisibleColumn = columns[j];
        }

        if (viewportWidth > totalWidth && lastVisibleColumn != null)
            result[lastVisibleColumn.name] = result[lastVisibleColumn.name] + viewportWidth - totalWidth - (result[lastVisibleColumn.name]/3);

        return result;
    };

    columns = () => {
        const widths = this.columnsWidths();
        return this._sortColumns(this.state.columns.filter((column, idx) => this.isColumnVisible(idx))).map((column, idx) => ({
            key: column.name,
            name: column.label,
            type : column.type,
            filterable: true, editable: false,
            sortable: column.sortable, draggable: false,
            resizable: true, frozen: column.fixed, width: widths[column.name],
            headerRenderer : this.columnRenderer(column, idx),
            formatter : this.rowFormatter.bind(this, column),
            cellClass: "column-type-" + column.type
        }));
    };

    getWidth = (value, factor) => {
        if (value == null) return 0;
        return String(value).length * (factor != null ? factor : 6);
    }

    selectorColumns = () => {
        const originalIndexes = {};
        for (let i=0; i<this.state.columns.length; i++) originalIndexes[this.state.columns[i].name] = i;
        return this._sortColumns(this.state.columns).map((column, idx) => ({
            value: column.name,
            label: this.translate(column.label),
            type: column.type,
            width: column.width,
            index: idx,
            originalIndex: originalIndexes[column.name]
        }));
    };

    _sortColumns = (columns) => {
        if (this.state.columnsOrdering.length === 0) return columns;
        const columnsMap = Object.fromEntries(columns.map(col => [col.name, col]));
        return this.state.columnsOrdering
            .slice()
            .sort((a, b) => a.index - b.index)
            .map(o => columnsMap[o.name])
            .filter(Boolean);
    }

    columnRenderer = (column, idx) => {
        const type = column.type;
        const style = { display: 'inline-block' };
        const numericColumnInfo = this._getNumericColumnInfo(column.name);
        if (this._isRightAlignedColumn(column, numericColumnInfo)) style.float = 'right';
        return (<div style={style}>{this.translate(column.label)}</div>);
    };

    _toNumber = (value) => {
        if (value == null || value === '') return null;
        const normalized = String(value).replace(/\./g, '').replace(',', '.');
        const number = Number(normalized);
        return Number.isNaN(number) ? null : number;
    };

    _isRightAlignedColumn = (column, numericColumnInfo) => {
        return numericColumnInfo.isNumericColumn || column.type === "Number" || column.type === "Date";
    };

    _getNumericColumnInfo = (columnName) => {
        const rows = this.state.rows;
        if (this.numericColumnCacheRows !== rows || this.numericColumnCacheRowsLength !== rows.length) {
            this.numericColumnCache = {};
            this.numericColumnCacheRows = rows;
            this.numericColumnCacheRowsLength = rows.length;
        }
        if (this.numericColumnCache[columnName] != null) return this.numericColumnCache[columnName];
        if (rows.length === 0) {
            const info = {
                hasNumericValues: false,
                isNumericColumn: false,
                isIntegerColumn: false,
            };
            this.numericColumnCache[columnName] = info;
            return info;
        }

        const sample = rows.slice(0, 50);
        const sampledValues = sample
            .map(row => this.rowValue(row[columnName]))
            .filter(value => value != null && value !== '');
        const numericValues = sampledValues
            .map(value => this._toNumber(value))
            .filter(value => value != null);

        const info = {
            hasNumericValues: numericValues.length > 0,
            isNumericColumn: sampledValues.length > 0 && numericValues.length === sampledValues.length,
            isIntegerColumn: numericValues.length > 0 && numericValues.length === sampledValues.length && numericValues.every(Number.isInteger),
        };

        this.numericColumnCache[columnName] = info;
        return info;
    };

    _numberLocale = () => {
        if (typeof Application !== "undefined" && Application.configuration != null && Application.configuration.language != null) {
            return Application.configuration.language;
        }
        if (typeof document !== "undefined" && document.configuration != null && document.configuration.language != null) {
            return document.configuration.language;
        }
        if (typeof navigator !== "undefined" && navigator.language != null) return navigator.language;
        return undefined;
    };

    _formatIntegerValue = (value, number) => {
        return value != null ? value.replace(",00", "") : value;
        const integerNumber = Math.trunc(number);
        const locale = this._numberLocale();
        return new Intl.NumberFormat(locale, { maximumFractionDigits: 0, useGrouping: "always" }).format(integerNumber);
    };

    _formatNumericValue = (value, column, numericColumnInfo) => {
        const number = this._toNumber(value);
        if (number == null) return value;
        if (numericColumnInfo.isIntegerColumn) return this._formatIntegerValue(value, number);
        return value;
    };

    rowFormatter = (column, data) => {
        const { classes } = this.props;
        const type = column.type;
        const color = this.rowColor(data.value);
        const value = this.rowValue(data.value);
        const style = this.cellStyle(column, data);
        const numericColumnInfo = this._getNumericColumnInfo(column.name);
        const formattedValue = this._formatNumericValue(value, column, numericColumnInfo);
        if (type === "Icon") return (<Icon icon={value} color={color}/>);
        else if (type === "MaterialIcon") return (<MaterialIcon icon={value} color={color}/>);
        else if (type === "Link" && data.row.selectable) return (<Link className={classNames(classes.link)} style={style} component="button" onClick={this.handleCellClick.bind(this, column, data)}>{value}</Link>);
        else if (this._isRightAlignedColumn(column, numericColumnInfo)) return (<div style={{textAlign:'right',...style}}>{formattedValue}</div>);
        return (<div style={style}>{value}</div>);
    };

    cellStyle = (column, data) => {
        const style = { zIndex: 1 };
        const color = this.rowColor(data.value);
        const backgroundColor = this.rowBackgroundColor(data.value);
        if (column.textColor !== undefined || color !== undefined) style.color = color !== undefined ? color : column.textColor;
        if (column.backgroundColor !== undefined || backgroundColor !== undefined) {
            style.backgroundColor = backgroundColor !== undefined ? backgroundColor : column.backgroundColor;
            style.borderRadius = "6px";
            style.display = "inline-block";
            style.padding = "0px 10px";
        }
        return style;
    };

    handleRowClick = (rowIndex, data, gridColumn, e) => {
        if (!data.selectable) return;
        if (e != null && e.nativeEvent != null && e.nativeEvent.isTrusted === false) return;
        if (e != null && e.target != null) {
            const tagName = e.target.tagName != null ? e.target.tagName.toLowerCase() : "";
            if (tagName === "input" || tagName === "button" || tagName === "a") return;
        }
        const columnIndex = this.findColumn(gridColumn.key);
        const sourceColumn = this.state.columns[columnIndex];
        if (sourceColumn == null) return;
        const dataValue = data[sourceColumn.name];
        const value = this.rowValue(dataValue);
        const address = this.rowAddress(dataValue);
        if (address != null) history.push(address, {});
        this.requester.cellClick({ column: sourceColumn.name, columnIndex: columnIndex, row: value, rowIndex: rowIndex });
    };

    linkColumns = () => {
        return this.state.columns.filter(c => c.type === "Link");
    };

    handleCellClick = (column, data, e) => {
        if (e != null && e.nativeEvent != null && e.nativeEvent.isTrusted === false) return;
        e.stopPropagation();
        const index = this.rowIndex(data.value);
        const value = this.rowValue(data.value);
        const address = this.rowAddress(data.value);
        const columnIndex = this.columnIndex(column.name);
        if (address != null) history.push(address, {});
        this.requester.cellClick({ column: column.name, columnIndex: columnIndex, row: value, rowIndex: index });
    };

    handleScroll = (event) => {
        this.saveHorizontalScroll();
        const target = event != null ? event.target : null;
        if (target == null) return;
        if (this.state.rows.length === 0) return;
        const hasMore = this.state.rows.length < this.state.itemCount;
        const nearBottom = target.scrollTop + target.clientHeight >= target.scrollHeight - 100;
        if (!hasMore || !nearBottom || this.loadingNextPage) return;
        const nextPage = this.pageOf(this.state.rows.length);
        if (this.lastLoadedPage[nextPage]) return;
        this.lastLoadedPage[nextPage] = true;
        this.loadingNextPage = true;
        this.requester.loadNextPage();
    };

    loadNextPageIfRequired = () => {
        if (this.loadingNextPage) return;
        if (this.state.rows.length === 0) return;
        const hasMore = this.state.rows.length < this.state.itemCount;
        const scrollableTarget = this.gridCanvasRef.current;
        if (scrollableTarget == null || scrollableTarget.offsetHeight === 0) {
            window.setTimeout(() => this.loadNextPageIfRequired(), 100);
            return;
        }
        const scrollIsVisible = scrollableTarget.scrollHeight - 100 > scrollableTarget.offsetHeight;
        if (!hasMore || scrollIsVisible) return;
        this.loadingNextPage = true;
        this.requester.loadNextPage();
    };

    rowInfo = (index, value, address, color, backgroundColor) => {
        return index + "##" + (value != null ? value : "") + "##" + (address != null ? address : "") + "##" + (color != null ? color : "") + "##" + (backgroundColor != null ? backgroundColor : "");
    };

    rowParts = (value) => {
        if (value == null) return [];
        if (Array.isArray(value)) return value;
        if (typeof value === "string") return value.split("##");
        return [undefined, value];
    };

    rowIndex = (value) => {
        const info = this.rowParts(value);
        return info.length > 0 ? info[0] : undefined;
    };

    rowValue = (value) => {
        const info = this.rowParts(value);
        if (info.length > 1) return info[1] !== "" ? info[1] : undefined;
        return value;
    };

    rowAddress = (value) => {
        const info = this.rowParts(value);
        return info.length > 2 && info[2] !== "" ? info[2] : undefined;
    };

    rowColor = (value) => {
        const info = this.rowParts(value);
        return info.length > 3 && info[3] !== "" ? info[3] : undefined;
    };

    rowBackgroundColor = (value) => {
        const info = this.rowParts(value);
        return info.length > 4 && info[4] !== "" ? info[4] : undefined;
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
        if (this.state.columns.length === 0) {
            this.pendingRows = this.pendingRows.concat(newRows);
            return;
        }
        const columns = this.state.columns;
        this.setState((prevState) => {
            const rows = [...prevState.rows];
            let offset = rows.length;
            for (let i=0; i<newRows.length; i++) {
                let row = { selectable: newRows[i].selectable };
                for (let j=0; j<columns.length; j++) {
                    const address = newRows[i].cells[j].address != null ? newRows[i].cells[j].address : null;
                    const color = newRows[i].cells[j].color != null ? newRows[i].cells[j].color : null;
                    const backgroundColor = newRows[i].cells[j].backgroundColor != null ? newRows[i].cells[j].backgroundColor : null;
                    row[columns[j].name] = this.rowInfo(i+offset, newRows[i].cells[j].value, address, color, backgroundColor);
                }
                rows.push(row);
            }
            const page = this.pageOf(rows.length-1);
            this.lastLoadedPage[page] = true;
            this.loadingNextPage = false;
            return { rows: rows, loading: false, contentCleared: false };
        }, () => window.setTimeout(() => this.loadNextPageIfRequired(), 0));
    };

    pageOf = (index) => {
        if (index == null || index < 0) return 0;
        return Math.floor(index / this.state.pageSize);
    };

    renderColumn(column, index) {
        return (<div>{this.translate(column.label)}</div>);
    };

    refreshSelection = (indexes) => {
        const newIndexes = indexes.map(r => parseInt(r));
        this.setState({selectedIndexes: newIndexes});
    };

    handleRowsSelected = rows => {
        if (rows == null || rows.length === 0) return;
        const indexes = this.state.selectedIndexes.concat(rows.map(r => r.rowIdx));
        this.setState({selectedIndexes: indexes});
        this.requester.selection(indexes);
    };

    handleRowsDeselected = rows => {
        if (rows == null || rows.length === 0) return;
        let rowIndexes = rows.map(r => r.rowIdx);
        const indexes = this.state.selectedIndexes.filter(i => rowIndexes.indexOf(i) === -1);
        this.setState({selectedIndexes: indexes});
        this.requester.selection(indexes);
    };

    refreshInfo = (info) => {
        const cookieState = this.getCookie(this.cookieName(info.name));
        const columnWidths = cookieState != null && cookieState.columnWidths != null ? cookieState.columnWidths : this.state.columnWidths;
        this.setState({
            columns: info.columns,
            modes: info.modes,
            name: info.name,
            columnWidths: columnWidths != null ? columnWidths : {},
        }, () => {
            if (this.pendingRows.length === 0) return;
            const pendingRows = this.pendingRows;
            this.pendingRows = [];
            this.addRows(pendingRows);
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

    refreshColumnsOrdering = (value) => {
        const index = this.lastRow > 0 ? this.lastRow - 1 : 0;
        this.setState({columnsOrdering: value}, () => {
            window.dispatchEvent(new Event('resize'));
            this.scrollToRow(index);
            window.setTimeout(() => this.loadNextPageIfRequired(), 0);
        });
    };

    refreshVisibleColumns = (value) => {
        const index = this.lastRow > 0 ? this.lastRow - 1 : 0;
        this.setState({visibleColumns: this.visibleColumnsArrayOf(value)}, () => {
            window.dispatchEvent(new Event('resize'));
            this.scrollToRow(index);
            window.setTimeout(() => this.loadNextPageIfRequired(), 0);
        });
    };

    visibleColumnsArrayOf = (value) => {
        const result = [];
        for (let i=0; i<value.length; i++) result.push(value[i].visible);
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
        this.loadingNextPage = false;
        this.pendingRows = [];
        this.horizontalScroll = 0;
        this.numericColumnCache = {};
        this.numericColumnCacheRows = null;
        this.numericColumnCacheRowsLength = null;
        this.setState((prevState) => ({
            rows: [],
            selectedIndexes: [],
            itemCount: 0,
            loading: false,
            contentCleared: true,
            key: prevState.key + 1,
        }));
    };

    resize = () => {
        this.updateViewportSize();
        this.setState({key: this.state.key+1});
    };

    handleColumnResizeStart = (column, event) => {
        event.preventDefault();
        event.stopPropagation();
        if (event.currentTarget != null && event.currentTarget.setPointerCapture != null && event.pointerId != null) {
            event.currentTarget.setPointerCapture(event.pointerId);
        }
        this.saveHorizontalScroll(this.horizontalScroll);
        this.resizingColumn = {
            name: column.key,
            startX: event.clientX,
            startWidth: column.width,
            pointerId: event.pointerId,
        };
        window.addEventListener('pointermove', this.handleColumnResizeMove);
        window.addEventListener('pointerup', this.handleColumnResizeEnd);
        window.addEventListener('pointercancel', this.handleColumnResizeEnd);
        window.addEventListener('blur', this.handleColumnResizeEnd);
    };

    handleColumnResizeMove = (event) => {
        if (this.resizingColumn == null) return;
        const { name, startX, startWidth } = this.resizingColumn;
        const delta = event.clientX - startX;
        const width = Math.max(this.state.minColumnSize, Math.min(this.state.maxColumnSize, startWidth + delta));
        this.setState((prevState) => ({
            columnWidths: {
                ...prevState.columnWidths,
                [name]: width,
            }
        }));
    };

    handleColumnResizeEnd = () => {
        if (this.resizingColumn == null) return;
        if (this.columnResizeHandleRef.current != null && this.columnResizeHandleRef.current.hasPointerCapture != null) {
            try {
                const pointerId = this.resizingColumn.pointerId;
                if (pointerId != null && this.columnResizeHandleRef.current.hasPointerCapture(pointerId)) this.columnResizeHandleRef.current.releasePointerCapture(pointerId);
            } catch (e) {
            }
        }
        window.removeEventListener('pointermove', this.handleColumnResizeMove);
        window.removeEventListener('pointerup', this.handleColumnResizeEnd);
        window.removeEventListener('pointercancel', this.handleColumnResizeEnd);
        window.removeEventListener('blur', this.handleColumnResizeEnd);
        const { name } = this.resizingColumn;
        this.resizingColumn = null;
        if (this.state.columnWidths[name] != null) this.saveState("columnWidths", this.state.columnWidths);
    };

    handleColumnResizeReset = (column, event) => {
        event.preventDefault();
        event.stopPropagation();
        const key = column.key;
        this.setState((prevState) => {
            const columnWidths = { ...(prevState.columnWidths || {}) };
            delete columnWidths[key];
            return { columnWidths };
        }, () => this.saveState("columnWidths", this.state.columnWidths));
    };

    rowHeight = () => {
        const firstRow = this.gridCanvasRef.current != null ? this.gridCanvasRef.current.querySelector('tbody tr') : null;
        return firstRow != null ? firstRow.offsetHeight : 35;
    };

    handleHeaderSortClick = (column) => {
        const currentDirection = this.state.sortColumn === column.key ? this.state.sortDirection : null;
        let nextDirection = 'ASC';
        if (currentDirection === 'ASC') nextDirection = 'DESC';
        else if (currentDirection === 'DESC') nextDirection = 'NONE';
        if (nextDirection === 'NONE') {
            this.sortColumns(column.key, null);
            return;
        }
        this.sortColumns(column.key, nextDirection);
    };

    handleToggleRowSelection = (rowIndex, event) => {
        if (event != null && event.nativeEvent != null && event.nativeEvent.isTrusted === false) return;
        const checked = event.target.checked;
        const indexes = checked
            ? this.state.selectedIndexes.concat(rowIndex).filter((value, index, self) => self.indexOf(value) === index)
            : this.state.selectedIndexes.filter(i => i !== rowIndex);
        this.setState({selectedIndexes: indexes});
        this.requester.selection(indexes);
    };

    handleToggleAllRows = (event) => {
        if (event != null && event.nativeEvent != null && event.nativeEvent.isTrusted === false) return;
        const checked = event.target.checked;
        const indexes = checked ? this.state.rows.map((row, index) => index) : [];
        this.setState({selectedIndexes: indexes});
        this.requester.selection(indexes);
    };

    isAllRowsSelected = () => {
        return this.state.rows.length > 0 && this.state.selectedIndexes.length === this.state.rows.length;
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Grid));
DisplayFactory.register("Grid", withStyles(styles, { withTheme: true })(withSnackbar(Grid)));
