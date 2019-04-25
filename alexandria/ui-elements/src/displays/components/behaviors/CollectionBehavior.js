import React from "react";
import * as Elements from "app-elements/gen/Displays";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import TablePagination from '@material-ui/core/TablePagination';
import {FixedSizeList as ReactWindowList} from "react-window";
import InfiniteLoader from 'react-window-infinite-loader';

const CollectionBehavior = (collection) => {
    const PaginationHeight = 56;
    collection._widths = {};
    const self = { collection };

    window.addEventListener("resize", () => self.refreshDelayed());

    self.renderCollection = (height, width) => {
        const items = self.items();
        const navigable = collection.props.navigable;
        if (navigable == null) return self.renderInfiniteList(items, height, width);
        return (
            <React.Fragment>
                {navigable === "Top" ? self.renderPagination() : undefined}
                {self.renderList(items, height - PaginationHeight, width, null, null)}
                {navigable === "Bottom" ? self.renderPagination() : undefined}
            </React.Fragment>
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
        const page = self.collection.state.page;

        return (
            <div style={{height:PaginationHeight + "px"}}>
                <TablePagination style={{position:"absolute",right:"0"}}
                                 rowsPerPageOptions={[20, 40, 60]}
                                 component="div"
                                 count={itemCount}
                                 rowsPerPage={pageSize}
                                 page={page}
                                 backIconButtonProps={{'aria-label': 'Previous Page'}}
                                 nextIconButtonProps={{'aria-label': 'Next Page'}}
                                 onChangePage={self.handlePage.bind(self)}
                                 onChangeRowsPerPage={self.handlePageSize.bind(self)}/>
            </div>
        );
    };

    self.notifyItemsRendered = (instances, itemsWindow) => {
        self.collection.rendering = true;
        self.collection.timeout = window.setTimeout(() => {
            self.collection.requester.notifyItemsRendered({
                items: self.itemsIds(instances, itemsWindow.overscanStartIndex, itemsWindow.overscanStopIndex),
                visible: self.itemsIds(instances, itemsWindow.visibleStartIndex, itemsWindow.visibleStopIndex)
            });
            self.collection.rendering = false;
        }, 50);
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
        else view = self.scrollingView(width, classes);
        return (<div style={style} key={index}>{view}</div>);
    };

    self.refreshItemsRendered = (items, callback, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex }) => {
        self.collection.itemsWindow = { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex };
        if (self.collection.timeout != null) window.clearTimeout(self.collection.timeout);
        self.notifyItemsRendered(items, self.collection.itemsWindow);
        if (callback != null) callback(self.collection.itemsWindow);
    };

    self.scrolling = (instances, { scrollDirection, scrollOffset, scrollUpdateWasRequested }) => {
        if (self.collection.scrollingTimeout != null) window.clearTimeout(self.collection.scrollingTimeout);
        if (self.collection.rendering) return;
        self.collection.scrollingTimeout = window.setTimeout(() => {
            if (self.collection.itemsWindow == null) return;
            self.refreshItemsRendered(instances, null, self.collection.itemsWindow);
        }, 300);
    };

    self.scrollingView = (width, classes) => {
        return (<div style={{width:width,margin:"0 10px"}} className={classes.scrolling}/>);
    };

    self.itemView = (item, classes, index) => {
        return (<div className={classNames(classes.itemView, "layout horizontal center")}>{React.createElement(Elements[item.tp], item.pl)}</div>);
    };

    self.loadMoreItems = (items, startIndex, stopIndex) => {
        if (self.collection.moreItemsTimeout != null) window.clearTimeout(self.collection.moreItemsTimeout);
        self.collection.moreItemsTimeout = window.setTimeout(() => self.collection.requester.loadMoreItems({ start: startIndex, stop: stopIndex }), 100);
        self.collection.moreItemsCallback = new Promise(resolve => resolve());
        return self.collection.moreItemsCallback;
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

    self.items = () => {
        return self.collection.instances("rows");
    };

    self.refreshDelayed = () => {
        if (self.refreshTimeout != null) window.clearTimeout(self.refreshTimeout);
        self.refreshTimeout = window.setTimeout(() => self.refresh(), 80);
    };

    self.refresh = () => {
        console.log("refresh");
        if (self.collection.itemsWindow == null) return;
        const items = self.items();
        return self.refreshItemsRendered(items, null, self.collection.itemsWindow);
    };

    return {
        renderCollection: (height, width) => {
            return self.renderCollection(height, width);
        },
        refresh: () => {
            self.refresh();
        }
    };
};

export default CollectionBehavior;
