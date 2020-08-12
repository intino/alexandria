import React from "react";
import { Checkbox } from "@material-ui/core";
import { withStyles } from '@material-ui/core/styles';
import { withSnackbar } from 'notistack';
import AbstractTable from "../../../gen/displays/components/AbstractTable";
import TableNotifier from "../../../gen/displays/notifiers/TableNotifier";
import TableRequester from "../../../gen/displays/requesters/TableRequester";
import AutoSizer from 'react-virtualized-auto-sizer';
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import Heading from "./Heading";
import { CollectionStyles } from "./Collection";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import Block from "./Block";

export const TableStyles = theme => ({
    ...CollectionStyles(theme),
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

    render() {
        const { classes } = this.props;
        const selectable = this.props.selection != null;
        const multiple = this.allowMultiSelection();
        const headerHeight = this.header.current != null ? this.header.current.offsetHeight : 0;
        const minHeight = this.props.itemHeight * this.state.itemCount;
        const height = this.container.current != null ? this.container.current.offsetHeight : 0;
        const headerClass = height <= minHeight ? classes.withScroller : classes.withoutScroller;

        return (
            <div ref={this.container} style={{height:"100%",width:"100%"}} className="layout vertical flex">
                { ComponentBehavior.labelBlock(this.props) }
                <div ref={this.header} className={classNames(classes.headerView, headerClass, "layout horizontal center", selectable && multiple ? classes.selectable : {})} style={{position:"relative"}}>
                    <div className={classNames(classes.selectAll, selectable ? classes.selectable : {})}><Checkbox className={classes.selector} onChange={this.handleCheck.bind(this)} /></div>
                    {this.props.children}
                </div>
                <div className="layout flex" style={{width:"100%",height:"calc(100% - " + headerHeight + "px)"}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width))}</AutoSizer></div>
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