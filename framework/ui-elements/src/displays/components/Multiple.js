import React from "react";
import Typography from '@material-ui/core/Typography';
import AbstractMultiple from "../../../gen/displays/components/AbstractMultiple";
import MultipleNotifier from "../../../gen/displays/notifiers/MultipleNotifier";
import MultipleRequester from "../../../gen/displays/requesters/MultipleRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Multiple extends AbstractMultiple {

	constructor(props) {
		super(props);
		this.notifier = new MultipleNotifier(this);
		this.requester = new MultipleRequester(this);
	};

	render() {
		let multiple = this.props.multiple;
		let layout = multiple.arrangement.toLowerCase();
		let noItemsMessage = multiple.noItemsMessage;
		let style = this._style(multiple);
		if (noItemsMessage != null && noItemsMessage !== "" && this.instances(multiple.instances).length <= 0) return (<Typography style={{margin:'5px 0'}} variant="body1">{noItemsMessage}</Typography>);
		return (<div className={"layout wrap " + layout}>{this.renderInstances(multiple.instances, this._instanceProps(), style)}</div>);
	};

	_instanceProps = () => {
		var result = {};
		this.copyProps(this.props, result, "multiple,layout");
		return result;
	};

	_style = (multiple) => {
		let spacingStyle = this._spacingStyle(multiple);
		if (spacingStyle === undefined) spacingStyle = (multiple.arrangement.toLowerCase() === "horizontal") ? { right: 5, bottom: 2 } : {};
		return { marginRight: spacingStyle.right + "px", marginBottom: spacingStyle.bottom + "px" };
	};

	_spacingStyle = (multiple) => {
		let spacingSize = multiple.spacing;
		if (spacingSize === 0) return undefined;
		return { right: spacingSize, bottom: spacingSize };
	};

}

DisplayFactory.register("Multiple", Multiple);