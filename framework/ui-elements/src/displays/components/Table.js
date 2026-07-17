import React from "react";
import {Checkbox} from "@mui/material";
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

}

class Table extends EmbeddedTable {
    constructor(props) {
        super(props);
    }
}

export default withStyles(TableStyles, { withTheme: true })(withSnackbar(Table));
DisplayFactory.register("Table", withStyles(TableStyles, { withTheme: true })(withSnackbar(Table)));
