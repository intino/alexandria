import React from "react";
import AlexandriaDisplay from "../Display";
import Theme from "app-elements/gen/Theme";
import ComponentNotifier from "../notifiers/ComponentNotifier";
import {RiseLoader} from "react-spinners";
import ComponentBehavior from "./behaviors/ComponentBehavior";

export default class Component extends AlexandriaDisplay {

    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            visible: this.props.visible != null ? this.props.visible : true,
            color: this.props.color != null ? this.props.color : null,
            backgroundColor: this.props.backgroundColor != null ? this.props.backgroundColor : null,
            format: this.props.format,
            cssSelectors: this.props.cssSelectors != null ? this.props.cssSelectors.split(",") : [],
            ...this.state
        };
    };

    style() {
        return this.styleOf(this);
    };

    styleOf(element) {
        let style = this._addFormats(element);
        style = this._addSpacing(style);
        return style;
    };

    userMessage = (info) => {
        this.showMessage(info.message, info.type, info.autoHideDuration);
    };

    hideUserMessage = (info) => {
        this.hideMessage();
    };

    renderDynamicLoaded(components) {
        const theme = Theme.get();
        return (
            <React.Fragment>
                {this.state.loading ? <div style={{position:'absolute',top:'50%',left:'43%'}}><RiseLoader color={theme.palette.secondary.main} loading={this.state.loading}/></div> : undefined }
                <div style={ { visibility: this.state.loading ? "hidden" : "" } }>{components}</div>
            </React.Fragment>
        );
    };

    variant = (defaultVariant) => {
        return ComponentBehavior.variant(this.props, defaultVariant);
    };

    refreshPageTitle = (title) => {
        window.document.title = title;
    };

    refreshLoading = (loading) => {
        this.setState({ loading: loading });
    };

    refreshVisibility = (value) => {
        this.setState({ visible: value });
    };

    refreshColor = (color) => {
        this.setState({ color: color });
    };

    refreshDarkColor = (color) => {
        this.setState({ darkColor: color });
    };

    refreshBackgroundColor = (color) => {
        this.setState({ backgroundColor: color });
    };

    refreshDarkBackgroundColor = (color) => {
        this.setState({ darkBackgroundColor: color });
    };

    refreshFormat = (format) => {
        this.setState({ format: format });
    };

    refreshCssSelectors = (value) => {
        this.setState({ cssSelectors: value });
    };

    hiddenClass = () => {
        const hidden = this.props.hidden;
        if (hidden == null || hidden === "Never") return "";
        return "hidden-" + hidden.toLowerCase();
    };

    _addFormats() {
        return this._addFormatsTo(this);
    };

    _addFormatsTo(element) {
        const { format } = element.state;
        const formats = format != null ? format.split(" ") : [];
        if (this.state.readonly) formats.push("readonly");
        if (formats.length <= 0) return undefined;
        const theme = Theme.get();
        const result = {};
        formats.forEach(f => {
            let style = theme.palette.type == "dark" && theme.darkFormats[f] != null ? theme.darkFormats[f] : theme.formats[f];
            if (style == null) return;
            for (let rule in style) result[rule] = style[rule];
        });
        return result;
    };

    cssRuleSelectors = () => {
        return this.state.cssSelectors.join(" ");
    };

    applyStyles = (styles, to) => {
        if (styles == null) return;
        for (let rule in styles) to[rule] = styles[rule];
    };

    _addSpacing(style) {
        if (style == null) style = {};
        if (this.props.spacingstyle == null) return style;
        const spacingStyle = this.props.spacingstyle;
        if (spacingStyle.bottom != null) style.marginBottom = spacingStyle.bottom + "px";
        if (spacingStyle.right != null) style.marginRight = spacingStyle.right + "px";
        return style;
    };

    _widthDefined = () => {
        return this.props.width != null && this.props.width.indexOf("-1") === -1;
    };

    _heightDefined = () => {
        return this.props.height != null && this.props.height.indexOf("-1") === -1;
    };

}