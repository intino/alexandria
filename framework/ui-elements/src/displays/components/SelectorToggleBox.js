import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { ToggleButtonGroup, ToggleButton } from '@material-ui/lab';
import AbstractSelectorToggleBox from "../../../gen/displays/components/AbstractSelectorToggleBox";
import SelectorToggleBoxNotifier from "../../../gen/displays/notifiers/SelectorToggleBoxNotifier";
import SelectorToggleBoxRequester from "../../../gen/displays/requesters/SelectorToggleBoxRequester";
import Divider from './Divider';
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class SelectorToggleBox extends AbstractSelectorToggleBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorToggleBoxNotifier(this);
		this.requester = new SelectorToggleBoxRequester(this);
        this.state = {
            selection: this.traceValue() ? this.traceValue() : (this.props.selected != null ? [ this.props.selected ] : []),
            readonly: this.props.readonly,
            ...this.state
        };
	};

	render() {
		const children = this.children();
		const multi = this.props.multipleSelection;
		if (children.length <= 0) return (<div></div>);
		return (
            <ToggleButtonGroup exclusive={!multi} orientation={this.props.layout.toLowerCase()}
                               style={this.style()} size={this._size()}
                               value={this.state.selection} onChange={this.handleChange.bind(this)}>
				{React.Children.map(children, (child, i) => { return this.renderChild(child, i); })}
            </ToggleButtonGroup>
		);
	};

	renderChild = (child, key) => {
		const className = child.props.className;
		if (className != null && className.indexOf("divider") !== -1) return (<Divider/>);
		return (<ToggleButton value={child.props.name} key={key}>{child}</ToggleButton>);
	};

	handleChange = (e, newSelection) => {
		const multi = this.props.multipleSelection;
	    const selection = multi ? newSelection : [ newSelection ];
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

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorToggleBox));
DisplayFactory.register("SelectorToggleBox", withStyles(styles, { withTheme: true })(withSnackbar(SelectorToggleBox)));