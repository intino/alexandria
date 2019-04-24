import React from "react";
import * as Elements from "app-elements/gen/Displays";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';

const CollectionBehavior = (collection) => {
    var pageSize = 0;
    collection._widths = {};
    const self = {};

    self.notifyItemsRendered = (instances, itemsWindow) => {
        collection.rendering = true;
        collection.timeout = window.setTimeout(() => {
            collection.requester.notifyItemsRendered({
                items: self.itemsIds(instances, itemsWindow.overscanStartIndex, itemsWindow.overscanStopIndex),
                visible: self.itemsIds(instances, itemsWindow.visibleStartIndex, itemsWindow.visibleStopIndex)
            });
            collection.rendering = false;
        }, 50);
    };

    self.width = (index) => {
        const max = 70;
        const min = 50;
        if (collection._widths[index] == null)
            collection._widths[index] = Math.floor(Math.random()*(max-min+1)+min) + "%";
        return collection._widths[index];
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
        const { classes } = collection.props;
        const width = self.width(index);
        var view = null;
        if (item != null) view = isScrolling ? self.scrollingView(width, classes) : self.itemView(item, classes, index);
        else view = self.scrollingView(width, classes);
        return (<div style={style} key={index}>{view}</div>);
    };

    self.refreshItemsRendered = (instances, callback, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex }) => {
        collection.itemsWindow = { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex };
        if (collection.timeout != null) window.clearTimeout(collection.timeout);
        self.notifyItemsRendered(instances, collection.itemsWindow);
        if (callback != null) callback(collection.itemsWindow);
    };

    self.scrolling = (instances, { scrollDirection, scrollOffset, scrollUpdateWasRequested }) => {
        if (collection.scrollingTimeout != null) window.clearTimeout(collection.scrollingTimeout);
        if (collection.rendering) return;
        collection.scrollingTimeout = window.setTimeout(() => {
            if (collection.itemsWindow == null) return;
            self.refreshItemsRendered(instances, null, collection.itemsWindow);
        }, 300);
    };

    self.scrollingView = (width, classes) => {
        return (<div style={{width:width,margin:"0 10px"}} className={classes.scrolling}/>);
    };

    self.itemView = (item, classes, index) => {
        return (<div className={classNames(classes.itemView, "layout horizontal center")}>{React.createElement(Elements[item.tp], item.pl)}</div>);
    };

    self.moreItems = (items, startIndex, stopIndex) => {
        if (collection.moreItemsTimeout != null) window.clearTimeout(collection.moreItemsTimeout);
        collection.moreItemsTimeout = window.setTimeout(() => collection.requester.moreItems({ start: startIndex, stop: stopIndex }), 100);
        collection.moreItemsCallback = new Promise(resolve => resolve());
        return collection.moreItemsCallback;
    };

    self.pageSize = (size) => {
        pageSize = size;
    };

    return {
        renderItem: (items, { index, isScrolling, style }) => {
            return self.renderItem(items, { index, isScrolling, style });
        },

        pageSize: (size) => {
            self.pageSize(size);
        },

        moreItems : (items, startIndex, endIndex) => {
            return self.moreItems(items, startIndex, endIndex);
        },

        refreshItemsRendered: (instances, callback, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex }) => {
            return self.refreshItemsRendered(instances, callback, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex });
        },

        scrolling: (instances, { scrollDirection, scrollOffset, scrollUpdateWasRequested }) => {
            return self.scrolling(instances, { scrollDirection, scrollOffset, scrollUpdateWasRequested });
        },
    };
};

export default CollectionBehavior;
