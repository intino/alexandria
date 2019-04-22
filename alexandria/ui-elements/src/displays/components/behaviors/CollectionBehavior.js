import React from "react";

const CollectionBehavior = (collection, itemView) => {
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
            if (i < start || i > end) continue;
            result.push(instances[i].pl.id);
        }
        return result;
    };

    self.renderItem = (items, { index, style }) => {
        const item = items[index];
        const { classes } = collection.props;
        const width = self.width(index);
        return (<div style={style} key={index}>{item == null ? self.scrollingView(width, classes) : itemView(item)}</div>);
    };

    self.refreshItemsRendered = (instances, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex }) => {
        console.log({ overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex });
        collection.itemsWindow = { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex };
        if (collection.timeout != null) window.clearTimeout(collection.timeout);
        self.notifyItemsRendered(instances, collection.itemsWindow);
    };

    self.scrolling = (instances, { scrollDirection, scrollOffset, scrollUpdateWasRequested }) => {
        if (collection.scrollingTimeout != null) window.clearTimeout(collection.scrollingTimeout);
        if (collection.rendering) return;
        collection.scrollingTimeout = window.setTimeout(() => {
            if (collection.itemsWindow == null) return;
            self.refreshItemsRendered(instances, collection.itemsWindow);
        }, 300);
    };

    self.scrollingView = (width, classes) => {
        return (<div style={ { width: width }} className={classes.scrolling}/>);
    };

    self.nextPage = (items, startIndex, stopIndex) => {
        console.log("more items " + startIndex + " - " + stopIndex);
        collection.moreItemsCallback = new Promise(resolve => {
            self.refreshItemsRendered(items);
            resolve();
        });
        // collection.requester.nextPage(startIndex, stopIndex);
        return collection.moreItemsCallback;
    };

    return {
        renderItem: (items, { index, style }) => {
            return self.renderItem(items, { index, style });
        },

        nextPage : (startIndex, endIndex) => {
            return self.nextPage(startIndex, endIndex);
        },

        refreshItemsRendered: (instances, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex }) => {
            return self.refreshItemsRendered(instances, { overscanStartIndex, overscanStopIndex, visibleStartIndex, visibleStopIndex });
        },

        scrolling: (instances, { scrollDirection, scrollOffset, scrollUpdateWasRequested }) => {
            return self.scrolling(instances, { scrollDirection, scrollOffset, scrollUpdateWasRequested });
        },
    };
};

export default CollectionBehavior;
