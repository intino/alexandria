import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMultiple from "../../../gen/displays/components/AbstractMultiple";
import MultipleNotifier from "../../../gen/displays/notifiers/MultipleNotifier";
import MultipleRequester from "../../../gen/displays/requesters/MultipleRequester";
import * as Ui from "alexandria-ui-elements/gen/Displays";
import 'alexandria-ui-elements/res/styles/layout.css';

export default class Multiple extends AbstractMultiple {

	constructor(props) {
		super(props);
		this.notifier = new MultipleNotifier(this);
		this.requester = new MultipleRequester(this);
	};

	render() {
		let multiple = this.props.multiple;
		let layout = multiple.layout.toLowerCase();
		let style = (layout === "horizontal") ? { marginRight: "5px", marginBottom: "2px" } : {};
		return (<div className={"layout wrap " + layout}>{this.renderInstances(multiple.instances, this._instanceProps(), style)}</div>);
	};

	_instanceProps = () => {
		var result = {};
		this.copyProps(this.props, result, "multiple,layout");
		return result;
	};

}
