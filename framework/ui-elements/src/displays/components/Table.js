import React from "react";
import {Checkbox, IconButton, Tooltip} from "@mui/material";
import {NavigateBefore, NavigateNext} from "@mui/icons-material";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import AbstractTable from "../../../gen/displays/components/AbstractTable";
import TableNotifier from "../../../gen/displays/notifiers/TableNotifier";
import TableRequester from "../../../gen/displays/requesters/TableRequester";
import AutoSizer from 'react-virtualized-auto-sizer';
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import {collectionPalette, CollectionStyles} from "./Collection";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import Theme from 'alexandria-ui-elements/gen/Theme';

export const TableStyles = theme => ({
    ...CollectionStyles(theme),
    label: {
        color: theme.palette.grey.A700,
        marginRight: "5px"
    },
    headerView : {
        background: collectionPalette(theme).headerBackground,
        borderBottom: `1px solid ${collectionPalette(theme).borderColor}`,
        backdropFilter: "blur(6px)",
        color: theme.palette.mode === "dark" ? "rgba(241,245,249,0.96)" : "inherit",
        "& .MuiTableCell-root": {
            padding: "1px 6px",
        },
        "& *": {
            color: theme.palette.mode === "dark" ? "rgba(241,245,249,0.96)" : "inherit",
        },
    },
    withoutScroller : {
        width: "100%"
    },
    withScroller : {
        //width: "calc(100% - 15px)"
        width: "100%"
    },
    itemView : {
        borderBottom: `1px solid ${collectionPalette(theme).rowBorderColor}`,
        height: "100%",
        borderRadius: "14px",
        transition: "background-color 140ms ease, box-shadow 140ms ease",
        "& .MuiTableCell-root": {
            padding: "1px 6px",
        },
        '&:hover' : {
            background: collectionPalette(theme).rowHoverBackground
        },
        '&:hover $selector' : {
            opacity: 1
        }
    },
    rowTooltip: {
        maxWidth: "calc(100vw - 32px)",
        maxHeight: "min(320px, calc(100vh - 48px))",
        padding: "10px 12px",
        boxSizing: "border-box",
        overflowX: "hidden",
        overflowY: "hidden",
        border: theme.palette.mode === "dark" ? "1px solid rgba(148,163,184,0.24)" : "1px solid rgba(15,23,42,0.14)",
        backgroundColor: theme.palette.mode === "dark" ? "rgba(15,23,42,0.98)" : "rgba(255,255,255,0.98)",
        color: theme.palette.mode === "dark" ? "#ffffff" : "#1e293b",
        boxShadow: theme.palette.mode === "dark" ? "0 12px 28px rgba(0,0,0,0.32)" : "0 12px 28px rgba(15,23,42,0.2)",
        "&::-webkit-scrollbar": {
            width: "6px",
        },
        "&::-webkit-scrollbar-thumb": {
            borderRadius: "999px",
            backgroundColor: theme.palette.mode === "dark" ? "rgba(148,163,184,0.42)" : "rgba(71,85,105,0.35)",
        },
    },
    rowTooltipArrow: {
        color: theme.palette.mode === "dark" ? "rgba(15,23,42,0.98)" : "rgba(255,255,255,0.98)",
    },
    rowTooltipEntry: {
        display: "grid",
        gridTemplateColumns: "minmax(110px, 38%) minmax(0, 1fr)",
        gap: "10px",
        alignItems: "start",
        padding: "3px 0",
        lineHeight: 1.35,
    },
    rowTooltipScrollable: {
        maxHeight: "calc(min(320px, calc(100vh - 48px)) - 70px)",
        overflowX: "hidden",
        overflowY: "auto",
        paddingRight: "4px",
        "&::-webkit-scrollbar": {
            width: "6px",
        },
        "&::-webkit-scrollbar-thumb": {
            borderRadius: "999px",
            backgroundColor: theme.palette.mode === "dark" ? "rgba(148,163,184,0.42)" : "rgba(71,85,105,0.35)",
        },
    },
    rowTooltipTitle: {
        marginBottom: "6px",
        color: theme.palette.mode === "dark" ? "#ffffff" : "#1e293b",
        fontSize: "0.84rem",
        fontWeight: 800,
    },
    rowTooltipLabel: {
        color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.7)" : "rgba(51,65,85,0.68)",
        fontSize: "0.75rem",
        fontWeight: 700,
    },
    rowTooltipValue: {
        color: theme.palette.mode === "dark" ? "#ffffff" : "#1e293b",
        fontSize: "0.78rem",
        overflowWrap: "anywhere",
    },
    rowTooltipNavigation: {
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        gap: "8px",
        marginTop: "8px",
        paddingTop: "8px",
        borderTop: theme.palette.mode === "dark" ? "1px solid rgba(148,163,184,0.2)" : "1px solid rgba(15,23,42,0.1)",
    },
    rowTooltipPosition: {
        minWidth: "76px",
        textAlign: "center",
        color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.78)" : "rgba(51,65,85,0.72)",
        fontSize: "0.75rem",
        fontWeight: 700,
    },
    selectAll : {
        display: "none",
        position: "absolute",
        top: "0",
        left: "0",
    }
});

export class EmbeddedTable extends AbstractTable {

    constructor(props) {
        super(props);
        this.notifier = new TableNotifier(this);
        this.requester = new TableRequester(this);
        this.header = React.createRef();
        this.rowTooltipTimer = null;
        this.rowTooltipCloseTimer = null;
        this.state = {...this.state, tooltipItemIndex: null, tooltipAnchorItemIndex: null, tooltipEntries: []};
    };

    componentWillUnmount() {
        if (this.rowTooltipTimer != null) {
            window.clearTimeout(this.rowTooltipTimer);
            this.rowTooltipTimer = null;
        }
        if (this.rowTooltipCloseTimer != null) window.clearTimeout(this.rowTooltipCloseTimer);
        super.componentWillUnmount();
    };

    handleCheck = () => {
        this.requester.selectAll();
    };

    normalizeHeaderChildren = (children, isDark) => {
        if (!isDark) return children;
        return React.Children.map(children, child => {
            if (child == null || typeof child !== "object" || !React.isValidElement(child)) return child;
            const currentStyle = child.props != null && child.props.style != null ? child.props.style : {};
            const nextStyle = {
                ...currentStyle,
                background: "transparent",
                backgroundColor: "transparent",
                color: "rgba(241,245,249,0.96)",
            };
            const nextProps = { style: nextStyle };
            if (child.props != null && child.props.children != null) {
                nextProps.children = this.normalizeHeaderChildren(child.props.children, isDark);
            }
            return React.cloneElement(child, nextProps);
        });
    };

    render() {
        const { classes } = this.props;
        const headerHeight = this.header.current != null ? this.header.current.offsetHeight : 0;
        const header = this.headerContent();

        return (
            <div ref={this.container} style={{height:"100%",width:"100%",position:'relative'}} className={classNames(classes.collectionViewport, "layout vertical flex")}>
                { ComponentBehavior.labelBlock(this.props) }
                <div className="layout flex" style={{width:"100%",height:"calc(100% - " + headerHeight + "px)"}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width, null, header))}</AutoSizer></div>
            </div>
        );
    };

    headerContent = () => {
        const { classes } = this.props;
        const theme = Theme.get();
        const selectable = this.props.selection != null;
        const multiple = this.allowMultiSelection();
        const minHeight = this.props.itemHeight * this.state.itemCount;
        const height = this.container.current != null ? this.container.current.offsetHeight : 0;
        const headerClass = height <= minHeight ? classes.withScroller : classes.withoutScroller;
        const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
        const headerInlineStyle = isDark ? {
            position: "relative",
            background: "var(--alex-collection-header-bg)",
            color: "rgba(241,245,249,0.96)",
            borderBottom: "1px solid var(--alex-collection-border)",
        } : { position: "relative" };
        const normalizedChildren = this.normalizeHeaderChildren(this.props.children, isDark);
        return (
            <div className={classNames("layout vertical flex", isDark ? "dark" : undefined)}>
                <div ref={this.header} className={classNames(classes.headerView, headerClass, "layout horizontal flex center", selectable && multiple ? classes.selectable : {}, isDark ? "alexandria-table-header-dark" : undefined)} style={headerInlineStyle}>
                    <div className="layout horizontal flex center" style={isDark ? {color:"rgba(241,245,249,0.96)"} : undefined}>
                        <div className={classNames(classes.selectAll, selectable ? classes.selectable : {})}><Checkbox className={classes.selector} onChange={this.handleCheck.bind(this)} /></div>
                        {normalizedChildren}
                    </div>
                </div>
            </div>
        );
    }

    wrapCollectionItem = (content, item, index) => {
        if (!this.showTooltipForRows()) return content;
        const { classes } = this.props;
        return (
            <Tooltip
                key={`tooltip-${index}`}
                title={this.renderRowTooltip()}
                placement="bottom-start"
                arrow
                enterDelay={0}
                leaveDelay={0}
                open={this.state.tooltipAnchorItemIndex === index}
                classes={{ tooltip: classes.rowTooltip, arrow: classes.rowTooltipArrow }}
                slotProps={{ tooltip: { style: this.rowTooltipStyle(), onMouseEnter: this.keepRowTooltipOpen, onMouseLeave: this.hideRowTooltip.bind(this, index) } }}
            >
                {React.cloneElement(content, {
                    "data-row-tooltip-index": index,
                    onMouseEnter: this.showRowTooltip.bind(this, index),
                    onMouseOver: this.showRowTooltip.bind(this, index),
                    onMouseLeave: this.hideRowTooltip.bind(this, index),
                })}
            </Tooltip>
        );
    };

    showTooltipForRows = () => {
        return this.props.showTooltipForRows === true || this.props.showTooltipForRows === "true";
    };

    rowTooltipStyle = () => {
        const width = this.container.current != null ? this.container.current.clientWidth : 0;
        return {
            width: width > 0 ? `min(${width}px, calc(100vw - 32px))` : "min(720px, calc(100vw - 32px))",
            maxWidth: "calc(100vw - 32px)",
        };
    };

    showRowTooltip = (index, event) => {
        this.keepRowTooltipOpen();
        if (this.state.tooltipItemIndex === index && this.state.tooltipAnchorItemIndex === index) return;
        if (this.rowTooltipTimer != null) {
            window.clearTimeout(this.rowTooltipTimer);
            this.rowTooltipTimer = null;
        }
        const columns = this.rowTooltipColumns();
        const entries = this.rowTooltipEntries(event.currentTarget, columns);
        if (entries.length === 0) return;
        this.rowTooltipTimer = window.setTimeout(() => {
            this.rowTooltipTimer = null;
            this.setState({tooltipItemIndex: index, tooltipAnchorItemIndex: index, tooltipEntries: entries});
        }, 350);
    };

    hideRowTooltip = (rowIndex) => {
        if (this.rowTooltipTimer != null) {
            window.clearTimeout(this.rowTooltipTimer);
            this.rowTooltipTimer = null;
        }
        const anchorIndex = this.state.tooltipAnchorItemIndex;
        if (anchorIndex == null) return;
        if (this.rowTooltipCloseTimer != null) window.clearTimeout(this.rowTooltipCloseTimer);
        this.rowTooltipCloseTimer = window.setTimeout(() => {
            this.rowTooltipCloseTimer = null;
            this.closeRowTooltip(anchorIndex);
        }, 150);
    };

    keepRowTooltipOpen = () => {
        if (this.rowTooltipCloseTimer != null) {
            window.clearTimeout(this.rowTooltipCloseTimer);
            this.rowTooltipCloseTimer = null;
        }
    };

    closeRowTooltip = (rowIndex) => {
        if (rowIndex != null && this.state.tooltipAnchorItemIndex !== rowIndex) return;
        if (this.rowTooltipTimer != null) {
            window.clearTimeout(this.rowTooltipTimer);
            this.rowTooltipTimer = null;
        }
        if (this.state.tooltipItemIndex != null) this.setState({tooltipItemIndex: null, tooltipAnchorItemIndex: null, tooltipEntries: []});
    };

    rowTooltipItemElements = () => {
        if (this.container.current == null) return [];
        return Array.from(this.container.current.querySelectorAll("[data-row-tooltip-index]"));
    };

    rowTooltipItemPosition = (rowIndex) => {
        return this.rowTooltipItemElements().findIndex((element) => Number(element.dataset.rowTooltipIndex) === rowIndex);
    };

    rowTooltipItemCount = () => this.rowTooltipItemElements().length;

    moveRowTooltip = (direction) => {
        const currentIndex = this.state.tooltipItemIndex;
        const elements = this.rowTooltipItemElements();
        const currentPosition = currentIndex != null ? this.rowTooltipItemPosition(currentIndex) : -1;
        const nextPosition = currentPosition + direction;
        if (currentPosition < 0 || nextPosition < 0 || nextPosition >= elements.length) return;

        const element = elements[nextPosition];
        if (element == null) return;
        const nextIndex = Number(element.dataset.rowTooltipIndex);
        const columns = this.rowTooltipColumns();
        const entries = this.rowTooltipEntries(element, columns);
        this.keepRowTooltipOpen();
        this.setState({tooltipItemIndex: nextIndex, tooltipEntries: entries});
    };

    handleRowTooltipControlMouseDown = (event) => {
        event.preventDefault();
        event.stopPropagation();
        this.keepRowTooltipOpen();
    };

    rowTooltipColumns = () => {
        const headerContent = this.header.current != null ? this.header.current.firstElementChild : null;
        if (headerContent == null) {
            return this.headerLabels()
                .map((label, index) => ({label, index}))
                .filter((column) => column.label != null && column.label !== "");
        }

        return Array.from(headerContent.children)
            .filter((cell) => !cell.classList.contains(this.props.classes.selectAll))
            .map((cell, index) => {
                const bounds = cell.getBoundingClientRect();
                return {label: cell.textContent.trim(), index, left: bounds.left, right: bounds.right};
            })
            .filter((column) => column.label !== "");
    };

    rowTooltipEntries = (element, columns) => {
        const row = element.querySelector(".layout.horizontal.center.flex") || element;
        const cells = Array.from(row.children);
        const dataCells = cells.slice(Math.max(0, cells.length - columns.length));
        const columnsHaveGeometry = columns.some((column) => Number.isFinite(column.left) && Number.isFinite(column.right) && column.right > column.left);

        // Keep the header as the source of truth so empty cells do not remove columns.
        return columns.map((column) => ({
            label: column.label,
            value: this.rowTooltipCellValue(columnsHaveGeometry ? this.rowTooltipCellAtColumn(cells, column) : dataCells[column.index])
        }));
    };

    rowTooltipCellAtColumn = (cells, column) => {
        const columnCenter = (column.left + column.right) / 2;
        const candidates = cells
            .map((cell) => ({cell, bounds: cell.getBoundingClientRect()}))
            .filter(({bounds}) => bounds.width > 0 && bounds.right > column.left && bounds.left < column.right);
        if (candidates.length === 0) return null;

        return candidates
            .sort((first, second) => {
                const firstCenter = (first.bounds.left + first.bounds.right) / 2;
                const secondCenter = (second.bounds.left + second.bounds.right) / 2;
                return Math.abs(firstCenter - columnCenter) - Math.abs(secondCenter - columnCenter);
            })[0].cell;
    };

    rowTooltipCellValue = (cell) => cell != null ? cell.textContent.trim() : "";

    headerLabels = () => {
        const headerContent = this.header.current != null ? this.header.current.firstElementChild : null;
        if (headerContent != null) {
            return Array.from(headerContent.children)
                .filter(cell => !cell.classList.contains(this.props.classes.selectAll))
                .map(cell => cell.textContent.trim());
        }
        return React.Children.toArray(this.props.children).map(this.elementText);
    };

    elementText = (element) => {
        if (element == null || typeof element === "boolean") return "";
        if (typeof element === "string" || typeof element === "number") return String(element);
        if (!React.isValidElement(element)) return "";
        if (element.props.value != null) return String(element.props.value);
        return React.Children.toArray(element.props.children).map(this.elementText).join("");
    };

    renderRowTooltip = () => {
        const { classes } = this.props;
        const currentIndex = this.state.tooltipItemIndex;
        const itemCount = this.rowTooltipItemCount();
        const itemPosition = currentIndex != null ? this.rowTooltipItemPosition(currentIndex) : -1;
        return (
            <div>
                <div className={classes.rowTooltipScrollable}>
                    {currentIndex != null && <div className={classes.rowTooltipTitle}>{`${this.translate("Row")} ${currentIndex + 1}`}</div>}
                    {this.state.tooltipEntries.map((entry, index) =>
                        <div className={classes.rowTooltipEntry} key={`${entry.label}-${index}`}>
                            <span className={classes.rowTooltipLabel}>{entry.label}</span>
                            <span className={classes.rowTooltipValue}>{entry.value}</span>
                        </div>
                    )}
                </div>
                <div className={classes.rowTooltipNavigation} onMouseEnter={this.keepRowTooltipOpen} onMouseDown={this.handleRowTooltipControlMouseDown}>
                    <IconButton
                        size="small"
                        aria-label={this.translate("Previous row")}
                        disabled={itemPosition <= 0}
                        onClick={() => this.moveRowTooltip(-1)}
                    >
                        <NavigateBefore fontSize="small" />
                    </IconButton>
                    <span className={classes.rowTooltipPosition}>{itemPosition >= 0 ? `${itemPosition + 1} / ${itemCount}` : ""}</span>
                    <IconButton
                        size="small"
                        aria-label={this.translate("Next row")}
                        disabled={itemPosition < 0 || itemPosition >= itemCount - 1}
                        onClick={() => this.moveRowTooltip(1)}
                    >
                        <NavigateNext fontSize="small" />
                    </IconButton>
                </div>
            </div>
        );
    };

}

class Table extends EmbeddedTable {
    constructor(props) {
        super(props);
    }
}

export default withStyles(TableStyles, { withTheme: true })(withSnackbar(Table));
DisplayFactory.register("Table", withStyles(TableStyles, { withTheme: true })(withSnackbar(Table)));
