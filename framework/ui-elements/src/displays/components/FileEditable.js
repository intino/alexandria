import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileEditableNotifier from "../../../gen/displays/notifiers/FileEditableNotifier";
import FileEditableRequester from "../../../gen/displays/requesters/FileEditableRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Block from "./Block";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import Theme from "app-elements/gen/Theme";

export default class FileEditable extends AbstractFile {

	constructor(props) {
		super(props);
		this.notifier = new FileEditableNotifier(this);
		this.requester = new FileEditableRequester(this);
		this.state = {
		    ...this.state,
            value : "",
            readonly : this.props.readonly
        };
	};

	handleChange(e) {
		this.requester.notifyChange(e.target.files[0]);
		this.setState({ value: e.target.value });
	}

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const label = this.props.label !== "" ? this.props.label : undefined;
		const theme = Theme.get();
		return (
			<Block layout="horizontal center" style={this.style()}>
				{ ComponentBehavior.labelBlock(this.props, "body1", { color: theme.palette.grey.primary, marginRight: '5px' }) }
				<input type="file" value={this.state.value} disabled={this.state.readonly ? true : undefined}
					   onChange={this.handleChange.bind(this)}></input>
			</Block>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};
}

DisplayFactory.register("FileEditable", FileEditable);