import React from "react";
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

const styles = theme => ({
    ...CollectionStyles(theme),
    label: {
        color: theme.palette.grey.primary,
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
});

class Table extends AbstractTable {

    constructor(props) {
        super(props);
        this.notifier = new TableNotifier(this);
        this.requester = new TableRequester(this);
        this.container = React.createRef();
        this.header = React.createRef();
    };

    render() {
        const { classes } = this.props;
        const selectable = this.props.selection != null;
        const headerHeight = this.header.current != null ? this.header.current.offsetHeight : 0;
        const minHeight = this.props.itemHeight * this.state.itemCount;
        const height = this.container.current != null ? this.container.current.offsetHeight : 0;
        const headerClass = height <= minHeight ? classes.withScroller : classes.withoutScroller;

        return (
            <div ref={this.container} style={{height:"100%",width:"100%"}}>
                { ComponentBehavior.labelBlock(this.props) }
                <div ref={this.header} className={classNames(classes.headerView, headerClass, "layout horizontal", selectable ? classes.selectable : {})}>{this.props.children}</div>
                <div className="layout flex" style={{width:"100%",height:"calc(100% - " + headerHeight + "px)"}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width))}</AutoSizer></div>
            </div>
        );
    }

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Table));
DisplayFactory.register("Table", withStyles(styles, { withTheme: true })(withSnackbar(Table)));