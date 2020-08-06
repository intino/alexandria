import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { FormControl, FormGroup, FormControlLabel, Checkbox } from '@material-ui/core';
import AbstractSelectorCheckBox from "../../../gen/displays/components/AbstractSelectorCheckBox";
import SelectorCheckBoxNotifier from "../../../gen/displays/notifiers/SelectorCheckBoxNotifier";
import SelectorCheckBoxRequester from "../../../gen/displays/requesters/SelectorCheckBoxRequester";
import Divider from './Divider';
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
    options : {
        maxHeight: "200px",
        overflow: 'auto'
    },
	label : {
        fontSize: "10pt",
        color: "#0000008a",
        marginBottom: "5px",
    }
});

class SelectorCheckBox extends AbstractSelectorCheckBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorCheckBoxNotifier(this);
		this.requester = new SelectorCheckBoxRequester(this);
        this.state = {
            selection: this.traceValue() ? this.traceValue() : (this.props.selected != null ? [ this.props.selected ] : []),
            ...this.state
        };
	};

	render() {
		const { classes, theme } = this.props;
		const label = this.props.label;
		const color = this.state.readonly ? theme.palette.grey.primary : "inherit";
		return (
		    <div style={this.style()}>
                {label != null && label !== "" ? <div className={classes.label} style={{color:color}}>{label}</div> : undefined }
                {this.renderChildren()}
		    </div>
		);
	};

	renderChildren = () => {
		const { classes } = this.props;
		const children = this.children();
		const layout = this.props.layout != null ? "layout " + this.props.layout.toLowerCase() + " wrap" : "layout vertical";
		const multi = this.props.multipleSelection;
		if (children.length <= 0) return (<div></div>);
		return (
            <FormControl className={classes.options} component="fieldset" disabled={this.state.readonly}>
                <FormGroup className={layout} style={this.style()} value={this.state.selection}>
                    {React.Children.map(children, (child, i) => { return this.renderChild(child, i); })}
                </FormGroup>
            </FormControl>
		);
	};

	renderChild = (child, key) => {
		const className = child.props.className;
		const selected = this.isInSelection(child.props.name);
		if (className != null && className.indexOf("divider") !== -1) return (<Divider/>);
		return (<FormControlLabel value={this._name(child)} control={<Checkbox checked={selected}>{child}</Checkbox>} label={this._label(child)} onChange={this.handleChange.bind(this, this._name(child))}/>);
	};

	_name = (item) => {
		return item.props.name;
	};

	_label = (item) => {
		const label = item.props.label != null && item.props.label !== "" ? item.props.label : item.props.value;
		return label != null ? label : this.translate("no label");
	};

	handleChange = (name) => {
	    const selection = this.updateSelection(name);
		this.requester.updateSelection(selection);
		this.setState({ selection: selection });
	};

	refreshSelection = (value) => {
		this.setState({ selection: value });
	};

	_size = () => {
		const size = this.state.size != null ? this.state.size : this.props.size;
		return size != null ? size.toLowerCase() : "small";
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorCheckBox));
DisplayFactory.register("SelectorCheckBox", withStyles(styles, { withTheme: true })(withSnackbar(SelectorCheckBox)));