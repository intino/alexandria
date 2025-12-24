import React from "react";
import { Typography, Checkbox } from "@material-ui/core";
import classNames from "classnames";
import TablePagination from '@material-ui/core/TablePagination';
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { enrichDisplayProperties } from 'alexandria-ui-elements/src/displays/Display';
import {RiseLoader,BeatLoader} from "react-spinners";
import Theme from "app-elements/gen/Theme";
import InfiniteScroll from "react-infinite-scroll-component";

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

    self.renderCollection = (height, width, mode, header) => {
        const items = self.items();
        const navigable = collection.props.navigable;

        if (collection.state.loading) return self.renderLoading(height, width);
        if (items.length <= 0) return self.renderEmpty(height, width);

        if (navigable == null) return self.renderInfiniteList(items, height, width, mode, header);
        return (
            <div>
                {navigable === "Top" ? self.renderPagination() : undefined}
                {self.renderList(items, height - PaginationHeight, width, mode, null, null, header)}
                {navigable === "Bottom" ? self.renderPagination() : undefined}
            </div>
        );
    };

    self.renderInfiniteList = (items, height, width, mode, header) => {
        let itemHeight = self.collection.props.itemHeight;
        const hasMore = items.length < self.collection.state.itemCount;
        const scrollableTarget = self.collection.props.id + "_infinite";
        if (width <= 800) itemHeight += (itemHeight/4.5);
        self.notifyItemsRendered(items);
        self.loadingNextPage = false;
        window.setTimeout(() => self.loadNextPageIfRequired(hasMore, scrollableTarget), 1000);
        return (
            <div id={scrollableTarget} style={{ height: height, width: width, overflow: "auto" }}>
                <InfiniteScroll dataLength={items.length} next={self.loadNextPage.bind(self)}
                        scrollThreshold={0.9} hasMore={hasMore} loader={self.renderLoadingMore()}
                        height={height} style={{height:height+"px",width:width+'px'}} scrollableTarget={scrollableTarget}>
                    {header != null && header}
                    {self.renderItems(items, mode, itemHeight, "layout horizontal flex center item")}
                </InfiniteScroll>
            </div>
        );
    };

    self.loadNextPageIfRequired = (hasMore, scrollableTargetId) => {
        if (self.loadingNextPage) return;
        const scrollableTarget = document.getElementById(scrollableTargetId);
        const scrollContainer = scrollableTarget != null && scrollableTarget.childNodes[0] != null ? scrollableTarget.childNodes[0].firstChild : null;
        const scrollIsVisible = scrollContainer == null || scrollContainer.offsetHeight == 0 || scrollContainer.scrollHeight-100 > scrollContainer.offsetHeight;
        if (!hasMore || scrollIsVisible) return;
        self.loadingNextPage = true;
        self.loadNextPage();
    };

    self.requestNewPage = (hasMore, scrollableTarget, height) => {
        const scrollIsVisible = document.getElementById(scrollableTarget).scrollHeight > height;
        const toolbar = document.getElementById(scrollableTarget + "_toolbar");
        toolbar.style.display = hasMore && !scrollIsVisible ? "block" : "none";
    };

    self.renderList = (items, height, width, mode, onItemsRendered, ref, header) => {
        let itemHeight = self.collection.props.itemHeight;
        let itemWidth = self.collection.props.itemWidth != null ? self.collection.props.itemWidth : width;
        let offsetHeight = self.collection.props.itemWidth != null ? -30 : 0;
        const itemCount = self.collection.props.navigable == null ? self.collection.state.itemCount : self.collection.state.pageSize;
        const hasMore = self.collection.props.navigable == null ? items.length < itemCount : false;
        const scrollableTarget = self.collection.props.id + "_infinite";
        self.notifyItemsRendered(items);
        return (
            <div id={scrollableTarget} style={{ height: height, width: width, overflow: "auto" }}>
                <InfiniteScroll dataLength={items.length} next={self.loadNextPage.bind(self)}
                        scrollThreshold={0.9} hasMore={hasMore} loader={self.renderLoadingMore()}
                        height={height} style={{height:(height+offsetHeight)+"px",width:itemWidth+'px',overflowX:self.collection.props.itemWidth!=null?"hidden":"auto"}} scrollableTarget={scrollableTarget}>
                    {header != null && header}
                    {self.renderItems(items, mode, itemHeight, "")}
                </InfiniteScroll>
            </div>
        );
    };

    self.renderPagination = () => {
        const itemCount = self.collection.state.itemCount;
        const pageSize = self.collection.state.pageSize;
        const defaultPageSize = self.collection.defaultPageSize != null ? self.collection.defaultPageSize : self.collection.state.pageSize;
        const page = self.collection.state.page;
        const pageSizes = [defaultPageSize];//[defaultPageSize, defaultPageSize*2, defaultPageSize*3];

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

    self.renderLoadingMore = () => {
        const theme = Theme.get();
        return (<div style={{marginTop:'10px',marginLeft:'10px'}}><BeatLoader color={theme.palette.secondary.main} loading={true}/></div>);
    };

    self.renderEmpty = (height, width) => {
        const noItemsMessage = self.collection.props.noItemsMessage != null ? self.collection.props.noItemsMessage : "No elements";
        return (<Typography style={{height, width, padding:"10px 0",fontSize:'13pt',paddingTop:'100px'}} className="layout horizontal center-justified">{self.collection.translate(noItemsMessage)}</Typography>);
    };

    self.notifyItemsRenderedDelayed = (items) => {
        if (self.collection.itemsRenderedTimeout != null) window.clearTimeout(self.collection.itemsRenderedTimeout);
        self.collection.itemsRenderedTimeout = window.setTimeout(() => self.notifyItemsRendered(items), 50);
    };

    self.notifyItemsRendered = (instances) => {
        const pageSize = self.collection.state.pageSize;
        const countInstances = instances.length;
        const instancesToRefresh = countInstances < pageSize ? instances : instances.slice(instances.length-pageSize);
        const ids = self.itemsIds(instancesToRefresh);
        if (self.collection.requester.notifyItemsRendered != null) self.collection.requester.notifyItemsRendered({items: ids, visible: ids});
    };

    self.width = (index) => {
        const max = 70;
        const min = 50;
        if (self.collection._widths[index] == null)
            self.collection._widths[index] = Math.floor(Math.random()*(max-min+1)+min) + "%";
        return self.collection._widths[index];
    };

    self.itemsIds = (instances) => {
        var result = [];
        for (var i=0; i<instances.length; i++) {
            if (instances[i] == null) continue;
            result.push(instances[i].pl.id);
        }
        return result;
    };

    self.renderItems = (items, mode, itemHeight, customItemClasses) => {
        const theme = Theme.get();
        const background = theme.isDark() ? "#999" : "#efefef";
        const sections = self.sections(items);
        const index = [-1];
        const result = [];
        for (const [section, elements] of sections) {
            const content = elements.map((i, idx) => {
                index[0]++;
                return self.renderItem(items, mode, { index: index[0], isScrolling: false, customClasses: customItemClasses, itemHeight: itemHeight });
            });
            if (section != "__default") result.push(<div><Typography variant="h6" style={{paddingLeft:'10px', marginTop:'10px',marginBottom:'10px',background:background}}>{section}</Typography></div>);
            result.push(mode != "Column" ? content : <div className="layout horizontal wrap">{content}</div>);
        }
        return result;
    };

    self.sections = (items) => {
        const sections = new Map();

        for (let i = 0; i < items.length; i++) {
            const section = items[i] != null && items[i].pl.section != null ? items[i].pl.section : "__default";
            if (!sections.has(section)) sections.set(section, []);
            sections.get(section).push(items[i]);
        }

        return sections;
    };

    self.renderItem = (items, mode, properties) => {
        const classes = "layout vertical" + (mode != "Column" ? " flex" : "");
        return (
            <div className={classes} style={{minHeight:properties.itemHeight,position:'relative'}}>
                {self.renderItemContent(items, mode, properties)}
            </div>
        );
    };

    self.renderItemContent = (items, mode, { index, isScrolling, style, customClasses }) => {
        const item = items[index];
        const { classes } = self.collection.props;
        const width = self.width(index);
        var view = null;

        if (item != null) view = isScrolling ? self.scrollingView(width, classes) : self.itemView(item, classes, index);
        else view = isScrolling ? self.scrollingView(width, classes) : (<div style={style} key={index} className={classNames(classes.itemView, "layout horizontal center")}>&nbsp;</div>);

        var selectable = self.collection.props.selection != null;
        var multiple = self.allowMultiSelection();
        var selecting = self.selection.length > 0;
        const id = item != null ? item.pl.id : undefined;
        const classNamesValue = classNames(classes.itemView, customClasses, selectable && multiple ? classes.selectable : undefined, selecting ? classes.selecting : undefined);
        const finalStyle = selectable && !multiple && self.isItemSelected(item) ? { ...style, ...self.getSelectedStyleRules() } : style;
        const widthStyle = self.collection.props.itemWidth != null ? {width: self.collection.props.itemWidth + "px"} : {};
        return (
            <React.Fragment>
                <div id={self.elementId(id)} style={{...finalStyle,...widthStyle}} key={index} onClick={selectable && !multiple ? self.handleSelect.bind(self, id) : undefined} className={classNamesValue}>
                    {/*{multiple ? <Checkbox checked={self.isItemSelected(item)} className={classes.selector} onChange={self.handleSelect.bind(self, id)} /> : undefined}*/}
                    {multiple ? <CollectionBehaviorCheckbox checked={self.isItemSelected(item)} classes={classes} onCheck={self.handleSelect.bind(self, id)} /> : undefined}
                    {view}
                </div>
            </React.Fragment>
        );
    };

    self.allowMultiSelection = () => {
        return self.collection.allowMultiSelection();
    };

    self.scrollingView = (width, classes) => {
        return (<div style={{width:width,margin:"0 10px"}} className={classes.scrolling}/>);
    };

    self.itemView = (item, classes, index) => {
        enrichDisplayProperties(item);
        return React.createElement(DisplayFactory.get(item.tp), item.pl);
    };

    self.loadNextPage = () => {
        self.collection.requester.loadNextPage();
    };

    self.isItemSelected = (item) => {
        if (item == null) return false;
        return self.selection.indexOf(item.pl.id) !== -1;
    };

    self.updateSelection = (item) => {
        const multiple = self.allowMultiSelection();
        var selection = self.selection;
        var index = self.selection.indexOf(item);
        if (index !== -1) {
            self.removeSelectedStyle(self.element(selection[index]));
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
        const multiple = self.allowMultiSelection();

        if (!selectable) return;

        const prevSelectionCount = self.collection.selectionCount != null ? self.collection.selectionCount : 0;
        const selection = self.updateSelection(item);
        if (multiple) self.collection.selectionCount = selection.length;

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
        const navigable = collection.props.navigable;
        return navigable != null ? self.nonNullItems() : self.collection.instances("rows");
    };

    self.nonNullItems = () => {
        const items = self.collection.instances("rows");
        const result = [];
        for (var i=0; i<items.length; i++) {
            if (items[i] != null) result.push(items[i]);
        }
        return result;
    };

    self.refreshDelayed = () => {
        if (self.refreshTimeout != null) window.clearTimeout(self.refreshTimeout);
        self.refreshTimeout = window.setTimeout(() => self.refresh(), 80);
    };

    self.refresh = () => {
        self.notifyItemsRendered(self.items);
    };

    self.refreshSelection = (selection) => {
        if (self.selection != null && self.selection.length > 0) self.removeSelectedStyle(self.element(self.selection[0]));
        self.selection = selection;
        var selectable = self.collection.props.selection != null;
        var multiple = self.allowMultiSelection();
        if (!selectable || multiple) return;
        if (self.selection != null && self.selection.length > 0) {
            const element = self.element(self.selection[0]);
            if (element == null) return;
            self.addSelectedStyle(element);
            if (element.scrollIntoView) element.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    };

    self.getSelectedStyleRules = () => {
        const theme = Theme.get();
        return {
            border: "1px solid " + theme.palette.secondary.main,
            borderRadius: "5px",
            background: theme.isDark() ? "transparent" : "white"
        };
    };

    self.element = (id) => {
        return $(self.collection.container.current).find("#" + self.elementId(id)).get(0);
    };

    self.addSelectedStyle = (element) => {
        if (element == null) return;
        self.addSelectedStyleRules(element.style);
    };

    self.addSelectedStyleRules = (style) => {
        if (style == null) return;
        const theme = Theme.get();
        style.border = "1px solid " + theme.palette.secondary.main;
        style.borderRadius = "5px";
        style.background = theme.isDark() ? "transparent" : "white";
        return style;
    };

    self.removeSelectedStyle = (element) => {
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
        renderCollection: (height, width, mode, header) => {
            return self.renderCollection(height, width, mode, header);
        },
        renderLoading: () => {
            return self.renderLoading();
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