import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import classNames from "classnames";
import {List, ListItemButton, ListSubheader} from '@mui/material';
import AbstractSelectorListBox from "../../../gen/displays/components/AbstractSelectorListBox";
import SelectorListBoxNotifier from "../../../gen/displays/notifiers/SelectorListBoxNotifier";
import SelectorListBoxRequester from "../../../gen/displays/requesters/SelectorListBoxRequester";
import Divider from './Divider';
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import Theme from "app-elements/gen/Theme";
import {fieldPalette} from "./FieldStyles";

const styles = theme => ({
	label : {
        fontSize: "10pt",
        color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.78)" : "#0000008a",
        marginBottom: "5px",
	},
	item: {
		border: "1px solid transparent",
		borderRadius: "16px",
		boxSizing: "border-box"
	},
	hovered: {
		background: `color-mix(in srgb, ${theme.palette.primary.main} 10%, ${theme.palette.mode === "dark" ? "#1e293b" : "white"}) !important`,
		color: theme.palette.mode === "dark" ? theme.palette.primary.light : theme.palette.primary.dark
	},
	selected: {
		background: `color-mix(in srgb, ${theme.palette.primary.main} 10%, ${theme.palette.mode === "dark" ? "#1e293b" : "white"}) !important`,
		borderColor: theme.palette.primary.main,
		color: theme.palette.mode === "dark" ? theme.palette.primary.light : theme.palette.primary.dark
	}
});

class SelectorListBox extends AbstractSelectorListBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorListBoxNotifier(this);
		this.requester = new SelectorListBoxRequester(this);
		this.state = {
    		...this.state,
			hovered: null,
		}
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const { classes, theme } = this.props;
		const label = this.props.label;
		const palette = fieldPalette(theme);
		const color = this.state.readonly ? theme.palette.grey.A700 : palette.textColor;
		return (
		    <div style={{...this.style(), ...this.highlightBackgroundStyle(), ...this.highlightStyle()}}>
                {label != null && label !== "" ? <div className={classes.label} style={{color:color}}>{this.translate(label)}</div> : undefined }
                {this.renderChildren()}
		    </div>
		);
	};

	renderChildren = () => {
		const children = this.children();
        if (children.length <= 0) return (<div></div>);
        return (
            <List>
                {React.Children.map(children, (child, i) => { return this.renderChild(child, i); })}
            </List>
        );
    };

	renderChild = (child, key) => {
		const { classes } = this.props;
		const hidden = this.isHidden(child.props.id);
		if (hidden) return (<React.Fragment/>);
		const className = child.props.className;
		const theme = Theme.get();
		if (className != null && className.indexOf("divider") !== -1) return (<Divider/>);
		if (className != null && className.indexOf("sub-header") !== -1) return (<ListSubheader component="div" style={{margin:'0',padding:'0',height:'30px',lineHeight:'30px',background:(theme != null && theme.palette != null && theme.palette.mode === "dark") ? 'rgba(15,23,42,0.72)' : 'white', color:(theme != null && theme.palette != null && theme.palette.mode === "dark") ? 'rgba(226,232,240,0.78)' : 'inherit'}}>{child.props.name}</ListSubheader>);
		const selected = this.isInSelection(child.props.name);
		const style = {};
		style.display = hidden ? "none" : "block";
		return (<ListItemButton disabled={this.state.readonly} key={key} style={style} className={classNames(classes.item, selected ? classes.selected : undefined, this.state.hovered === child.props.name && !selected ? classes.hovered : undefined)} onClick={this.handleSelect.bind(this, child.props.name)}
			onMouseEnter={() => this.setState({hovered: child.props.name})}
			onMouseLeave={() => this.setState({hovered: null})}
			onFocus={() => this.setState({hovered: child.props.name})}
			onBlur={() => this.setState({hovered: null})}>{child}</ListItemButton>);
	};

	handleSelect = (name) => {
		const multi = this.props.multipleSelection;
	    const selection = multi ? this.updateSelection(name) : [ name ];
		this.requester.updateSelection(selection);
		this.setState({ selection: selection });
	};

	refreshSelection = (value) => {
		this.setState({ selection: value });
	};

	refreshHiddenOptions = (options) => {
		this.setState({ hiddenOptions: options });
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorListBox));
DisplayFactory.register("SelectorListBox", withStyles(styles, { withTheme: true })(withSnackbar(SelectorListBox)));
