import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { List, ListItem, ListSubheader } from '@material-ui/core';
import AbstractSelectorListBox from "../../../gen/displays/components/AbstractSelectorListBox";
import SelectorListBoxNotifier from "../../../gen/displays/notifiers/SelectorListBoxNotifier";
import SelectorListBoxRequester from "../../../gen/displays/requesters/SelectorListBoxRequester";
import Divider from './Divider';
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({
	label : {
        fontSize: "10pt",
        color: "#0000008a",
        marginBottom: "5px",
    }
});

class SelectorListBox extends AbstractSelectorListBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorListBoxNotifier(this);
		this.requester = new SelectorListBoxRequester(this);
		this.state = {
    		...this.state,
		}
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const { classes, theme } = this.props;
		const label = this.props.label;
		const color = this.state.readonly ? theme.palette.grey.A700 : "inherit";
		return (
		    <div style={this.style()}>
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
		const hidden = this.isHidden(child.props.id);
		if (hidden) return (<React.Fragment/>);
		const className = child.props.className;
		if (className != null && className.indexOf("divider") !== -1) return (<Divider/>);
		if (className != null && className.indexOf("sub-header") !== -1) return (<ListSubheader component="div" style={{margin:'0',padding:'0',height:'30px',lineHeight:'30px'}}>{child.props.name}</ListSubheader>);
		const selected = this.isInSelection(child.props.name);
		const style = selected ? {background:"#ddd"} : {};
		style.display = hidden ? "none" : "block";
		return (<ListItem disabled={this.state.readonly} key={key} style={style} button onClick={this.handleSelect.bind(this, child.props.name)}>{child}</ListItem>);
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