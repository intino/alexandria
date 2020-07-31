import React from "react";
import { Typography, Checkbox } from "@material-ui/core";
import classNames from "classnames";
import TablePagination from '@material-ui/core/TablePagination';
import {FixedSizeList as ReactWindowList} from "react-window";
import InfiniteLoader from 'react-window-infinite-loader';
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { enrichDisplayProperties } from 'alexandria-ui-elements/src/displays/Display';
import {RiseLoader} from "react-spinners";
import Theme from "app-elements/gen/Theme";

const CollectionBehaviorCheckbox = (props) => {
    let [checked, setChecked] = React.useState(props.checked ? true : false);
    const handleCheck = (e) => {
        setChecked(e.target.checked);
        if (props.onCheck) props.onCheck(e);
    }
    return (<Checkbox checked={checked} className={props.classes.selector} onChange={handleCheck} />);
}

const CollectionBehavior = (collection) => {
    const PaginationHeight = 56;
    collection._widths = {};
    const self = { collection, selection: [] };

    window.addEventListener("resize", () => self.refreshDelayed());

    self.renderCollection = (height, width) => {
        const items = self.items();
        const navigable = collection.props.navigable;

        if (collection.state.loading) return self.renderLoading(height, width);
        if (items.length <= 0) return self.renderEmpty(height, width);
        if (self.isMultipleSelection()) self.selection = [];

        self.collection.itemsRenderedCalled = false;
        window.setTimeout(() => self.forceNotifyItemsRendered(items), 50);
        if (navigable == null) return self.renderInfiniteList(items, height, width);
        return (
            <div>
                {navigable === "Top" ? self.renderPagination() : undefined}
                {self.renderList(items, height - PaginationHeight, width, null, null)}
                {navigable === "Bottom" ? self.renderPagination() : undefined}
            </div>
        );
    };

    self.renderInfiniteList = (items, height, width) => {
        const threshold = Math.round(self.collection.state.pageSize * 0.8);
        const isItemLoaded = index => !!items[index];

        return (<InfiniteLoader isItemLoaded={isItemLoaded} itemCount={self.collection.state.itemCount}
                                loadMoreItems={self.loadMoreItems.bind(self, items)}
                                threshold={threshold}>
                {({ onItemsRendered, ref }) => (self.renderList(items, height, width, onItemsRendered, ref))}
            </InfiniteLoader>
        );
    };

    self.renderList = (items, height, width, onItemsRendered, ref) => {
        const itemCount = self.collection.props.navigable == null ? self.collection.state.itemCount : self.collection.state.pageSize;
        return (<ReactWindowList useIsScrolling={self.collection.props.scrollingMark} ref={ref} onScroll={self.scrolling.bind(self, items)}
                                 onItemsRendered={self.refreshItemsRendered.bind(self, items, onItemsRendered)}
                                 height={height} width={width} itemCount={itemCount} itemSize={self.collection.props.itemHeight}>
                {self.renderItem.bind(self, items)}
            </ReactWindowList>
        );
    };

    self.renderPagination = () => {
        const itemCount = self.collection.state.itemCount;
        const pageSize = self.collection.state.pageSize;
        const defaultPageSize = self.collection.defaultPageSize != null ? self.collection.defaultPageSize : self.collection.state.pageSize;
        const page = self.collection.state.page;
        const pageSizes = [defaultPageSize, defaultPageSize*2, defaultPageSize*3];

        self.collection.defaultPageSize = defaultPageSize;

        return (
            <div style={{height:PaginationHeight + "px"}}>
                <TablePagination style={{position:"absolute",right:"0"}}
                                 rowsPerPageOptions={pageSizes}
                                 component="div"
                                 count={itemCount}
                                 rowsPerPage={pageSize}
                                 page={page}
                                 backIconButtonProps={{'aria-label': 'Previous Page'}}
                                 nextIconButtonProps={{'aria-label': 'Next Page'}}
                                 onChangePage={self.handlePage.bind(self)}
                                 onChangeRowsPerPage={self.handlePageSize.bind(self)}
                                 labelDisplayedRows={self.displayedRowsLabel.bind(self)}
                                 labelRowsPerPage={self.rowsPerPageLabel()}/>
            </div>
        );
    };

    self.renderLoading = () => {
        const theme = Theme.get();
        return (<div style={{position:'absolute',top:'50%',left:'43%'}}><RiseLoader color={theme.palette.secondary.main} loading={true}/></div>);
    };

    self.renderEmpty = (height, width) => {
        const noItemsMessage = self.collection.props.noItemsMessage != null ? self.collection.props.noItemsMessage : "No elements";
        return (<Typography style={{height, width, padding:"10px 0",fontSize:'13pt'}} className="layout horizontal center-center">{self.collection.translate(noItemsMessage)}</Typography>);
    };

    self.forceNotifyItemsRendered = (items) => {
        if (self.collection.forceTimeout != null) window.clearTimeout(self.collection.forceTimeout);
        self.collection.forceTimeout = window.setTimeout(() => {
            if (self.collection.itemsRenderedCalled) return;
            self.refreshItemsRendered(items, null, self.collection.itemsWindow);
            self.collection.itemsRenderedCalled = true;
        }, 50);
    };

    self.notifyItemsRendered = (instances, itemsWindow) => {
        self.collection.itemsRenderedCalled = true;
        self.collection.rendering = true;
        self.collection.timeout = window.setTimeout(() => {
            self.collection.requester.notifyItemsRendered({
                items: self.itemsIds(instances, itemsWindow.overscanStartIndex, itemsWindow.overscanStopIndex),
                visible: self.itemsIds(instances, itemsWindow.visibleStartIndex, itemsWindow.visibleStopIndex)
            });
            self.collection.rendering = false;
        }, 200);
    };

    self.width = (index) => {
        const max = 70;
        const min = 50;
        if (self.collection._widths[index] == null)
            self.collection._widths[index] = Math.floor(Math.random()*(max-min+1)+min) + "%";
        return self.collection._widths[index];
    };

    self.itemsIds = (instances, start, end) => {
        var result = [];
        for (var i=0; i<instances.length; i++) {
            if (i < start || i > end || instances[i] == null) continue;
            result.push(instances[i].pl.id);
        }
        return result;
    };

    self.renderItem = (items, { index, isScrolling, style }) => {
        const item = items[index];
        const { classes } = self.collection.props;
        const width = self.width(index);
        var view = null;

        if (item != null) view = isScrolling ? self.scrollingView(width, classes) : self.itemView(item, classes, index);
        else view = isScrolling ? self.scrollingView(width, classes) : (<div style={style} key={index} className={classNames(classes.itemView, "layout horizontal center")}>&nbsp;</div>);

        var selectable = self.collection.props.selection != null;
        var multiple = self.isMultipleSelection();
        var selecting = self.selection.length > 0;
        const id = item != null ? item.pl.id : undefined;
        const classNamesValue = classNames(classes.itemView, "layout horizontal center item", selectable && multiple ? classes.selectable : undefined, selecting ? classes.selecting : undefined);
        const finalStyle = selectable && !multiple && self.isItemSelected(item) ? { ...style, ...self.getSelectedStyleRules() } : style;
        return (
            <div id={self.elementId(id)} style={finalStyle} key={index} onClick={selectable && !multiple ? self.handleSelect.bind(self, id) : undefined} className={classNamesValue}>
                {/*{multiple ? <Checkbox checked={self.isItemSelected(item)} className={classes.selector} onChange={self.handleSelect.bind(self, id)} /> : undefined}*/}
                {multiple ? <CollectionBehaviorCheckbox checked={self.isItemSelected(item)} classes={classes} onCheck={self.handleSelect.bind(self, id)} /> : undefined}
                {view}
            </div>
        );
    };

    self.isMultipleSelection = () => {
        return self.collection.props.selection != null && self.collection.props.selection === "multiple";
    };

    self.refreshItemsRendered = (items, callback, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex }) => {
        self.collection.itemsWindow = { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex };
        if (self.collection.timeout != null) window.clearTimeout(self.collection.timeout);
        self.notifyItemsRendered(items, self.collection.itemsWindow);
        if (callback != null) callback(self.collection.itemsWindow);
    };

    self.scrolling = (instances, { scrollDirection, scrollOffset, scrollUpdateWasRequested }) => {
        if (self.collection.scrollingTimeout != null) window.clearTimeout(self.collection.scrollingTimeout);
        // if (self.collection.rendering) return;
        self.collection.scrollingTimeout = window.setTimeout(() => {
            if (self.collection.itemsWindow == null) return;
            self.refreshItemsRendered(instances, null, self.collection.itemsWindow);
        }, 50);
    };

    self.scrollingView = (width, classes) => {
        return (<div style={{width:width,margin:"0 10px"}} className={classes.scrolling}/>);
    };

    self.itemView = (item, classes, index) => {
        enrichDisplayProperties(item);
        return React.createElement(DisplayFactory.get(item.tp), item.pl);
    };

    self.loadMoreItems = (items, startIndex, stopIndex) => {
        if (self.collection.moreItemsTimeout != null) window.clearTimeout(self.collection.moreItemsTimeout);
        self.collection.moreItemsTimeout = window.setTimeout(() => self.collection.requester.loadMoreItems({ start: startIndex, stop: stopIndex }), 150);
        self.collection.moreItemsCallback = new Promise(resolve => resolve());
        return self.collection.moreItemsCallback;
    };

    self.isItemSelected = (item) => {
        if (item == null) return false;
        return self.selection.indexOf(item.pl.id) !== -1;
    };

    self.updateSelection = (item) => {
        const multiple = self.isMultipleSelection();
        var selection = self.selection;
        var index = self.selection.indexOf(item);
        if (index !== -1) {
            self.removeSelectedStyle(selection[index]);
            selection.splice(index, 1);
        }
        else {
            if (multiple) selection.push(item);
            else selection = [item];
        }
        return selection;
    };

    self.handleSelect = (item, e) => {
        e.preventDefault();
        e.stopPropagation();

        const selectable = self.collection.props.selection != null;
        const multiple = self.isMultipleSelection();

        if (!selectable) return;

        const prevSelectionCount = self.collection.selectionCount != null ? self.collection.selectionCount : 0;
        const selection = self.updateSelection(item);
        if (multiple) {
            //if (prevSelectionCount == 0 || (prevSelectionCount > 0 && selection.length <= 0)) self.collection.setState({selection: selection});
            //self.refreshItemsRendered(self.items(), null, self.collection.itemsWindow);
            self.collection.selectionCount = selection.length;
        }

        if (self.collection.selectTimeout != null) window.clearTimeout(self.collection.selectTimeout);
        self.collection.selectTimeout = window.setTimeout(() => self.collection.requester.selection(selection), 50);
    };

    self.handlePage = (e, page) => {
        self.collection.setState({ page });
        self.collection.requester.changePage(page);
    };

    self.handlePageSize = (e) => {
        const size = e.target.value;
        self.collection.setState({ pageSize: size });
        self.collection.requester.changePageSize(size);
    };

    self.displayedRowsLabel = ({from, to, count}) => {
        const language = self.collection.language();
        let message = "${from}-${to} of ${count}";
        if (language === "es") message = "${from}-${to} de ${count}";
        return self.collection.translate(message).replace("${from}", from).replace("${to}", to).replace("${count}", count);
    };

    self.rowsPerPageLabel = () => {
        const language = self.collection.language();
        let message = "Rows per page:";
        if (language === "es") message = "Elementos por página:";
        return self.collection.translate(message);
    };

    self.items = () => {
        return self.collection.instances("rows");
    };

    self.refreshDelayed = () => {
        if (self.refreshTimeout != null) window.clearTimeout(self.refreshTimeout);
        self.refreshTimeout = window.setTimeout(() => self.refresh(), 80);
    };

    self.refresh = () => {
        if (self.collection.itemsWindow == null) return;
        const items = self.items();
        return self.refreshItemsRendered(items, null, self.collection.itemsWindow);
    };

    self.refreshSelection = (selection) => {
        if (self.selection != null && self.selection.length > 0) self.removeSelectedStyle(self.selection[0]);
        self.selection = selection;
        var selectable = self.collection.props.selection != null;
        var multiple = self.isMultipleSelection();
        if (!selectable || multiple) return;
        if (self.selection != null && self.selection.length > 0) self.addSelectedStyle(self.selection[0]);
    };

    self.getSelectedStyleRules = () => {
        return {
            border: "1px solid #3f50b5",
            borderRadius: "5px",
            background: "white"
        };
    };

    self.addSelectedStyleRules = (style) => {
        if (style == null) return;
        style.border = "1px solid #3f50b5";
        style.borderRadius = "5px";
        style.background = "white";
        return style;
    };

    self.addSelectedStyle = (id) => {
        const element = $(self.collection.container.current).find("#" + self.elementId(id)).get(0);
        self.addSelectedStyleRules(element.style);
    };

    self.removeSelectedStyle = (id) => {
        const element = $(self.collection.container.current).find("#" + self.elementId(id)).get(0);
        if (element == null || element.style == null) return;
        element.style.border = "0";
        element.style.borderRadius = "0";
        element.style.background = "transparent";
    };

    self.findSelectedItem = () => {
        const selection = self.selection;
        if (selection == null || selection.length < 0) return null;
        return selection[0];
    }

    self.elementId = (id) => {
        if (id == null) return null;
        return "_" + id.replace(/-/g, "");
    };

    return {
        renderCollection: (height, width) => {
            return self.renderCollection(height, width);
        },
        items : () => {
            return self.items();
        },
        refresh: () => {
            self.refresh();
        },
        refreshSelection: (selection) => {
            self.refreshSelection(selection);
        }
    };
};

export default CollectionBehavior;
