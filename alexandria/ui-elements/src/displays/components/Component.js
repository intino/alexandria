import React from "react";
import AlexandriaDisplay from "../Display";
import Theme from "../../../gen/Theme";
import ComponentNotifier from "../notifiers/ComponentNotifier";
import {Spinner} from "../../../gen/Displays";

export default class Component extends AlexandriaDisplay {
    state = {
        loading: true
    };

    constructor(props) {
        super(props);
        this.notifier = new ComponentNotifier(this);
    };

    style() {
        let style = this._addFormats();
        style = this._addSpacing(style);
        return style;
    };

    renderDynamicLoaded(components) {
        const theme = Theme.get();
        return (
            <React.Fragment>
                {this.state.loading ? <div style={{position:'absolute',top:'50%',left:'43%'}}><Spinner mode="Rise" color={theme.palette.secondary.main} loading={this.state.loading}/></div> : undefined }
                <div style={ { visibility: this.state.loading ? "hidden" : "" } }>{components}</div>
            </React.Fragment>
        );
    };

    refreshLoading = (loading) => {
        this.setState({ loading: loading });
    };

    _addFormats() {
        const { format } = this.props;
        const formats = format != null ? format.split(" ") : [];
        if (formats.length <= 0) return undefined;
        const theme = Theme.get();
        const result = {};
        formats.forEach(f => {
            let style = theme.formats[f];
            if (style == null) return;
            for (let rule in style) result[rule] = style[rule];
        });
        return result;
    };

    applyStyles = (styles, to) => {
        if (styles == null) return;
        for (let rule in styles) to[rule] = styles[rule];
    };

    _addSpacing(style) {
        if (style == null) style = {};
        if (this.props.spacingstyle == null) return style;
        const spacingStyle = this.props.spacingstyle;
        style.marginBottom = spacingStyle.bottom + "px";
        style.marginRight = spacingStyle.right + "px";
        return style;
    };

    _widthDefined = () => {
        return this.props.width != null && this.props.width.indexOf("-1") === -1;
    };

    _heightDefined = () => {
        return this.props.height != null && this.props.height.indexOf("-1") === -1;
    };

}