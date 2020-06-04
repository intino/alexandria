import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileEditableNotifier from "../../../gen/displays/notifiers/FileEditableNotifier";
import FileEditableRequester from "../../../gen/displays/requesters/FileEditableRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Block from "./Block";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import Theme from "app-elements/gen/Theme";
import { DropzoneArea } from 'material-ui-dropzone';

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
	    this.saveFile(e.target.files[0], e.target.value);
	};

	saveFile(file, value) {
	    this.requester.notifyUploading();
		this.requester.notifyChange(file);
		this.setState({ value: value });
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const label = this.props.label !== "" ? this.props.label : undefined;
		const theme = Theme.get();
		return (
			<Block layout="vertical center flex" style={this.style()}>
				{ ComponentBehavior.labelBlock(this.props, "body1", { color: theme.palette.grey.primary, marginRight: '5px' }) }
				{this._renderComponent()}
			</Block>
		);
	};

	_renderComponent = () => {
	    if (this.props.dropZone) return this._renderDropZone();
	    return this._renderInput();
	};

	_renderDropZone = () => {
	    return (
	        <DropzoneArea
	            acceptedFiles={this._allowedTypes()}
	            dropzoneText={this.translate("Drag and drop a file here or click")}
                filesLimit={1}
                maxFileSize={this.props.maxSize != null ? this.props.maxSize : 30000000}
	            onChange={(files) => {
	                if (files.length <= 0) return;
	                if (files.length > 1) return;
	                this.saveFile(files[0], files[0].name);
            }}/>
        );
	};

	_renderInput = () => {
        return (
            <input type="file" value={this.state.value} disabled={this.state.readonly ? true : undefined}
                   onChange={this.handleChange.bind(this)}></input>
        );
	};

	_allowedTypes = () => {
	    if (this.props.allowedTypes == null) return ['image/*', 'video/*', 'application/*', 'text/*'];
	    let result = [];
	    if (this._containsType("Image")) result.push("image/*");
	    if (this._containsType("Video")) result.push("video/*");
	    if (this._containsType("Application")) result.push("application/*");
	    if (this._containsType("Text")) result.push("text/*");
	    if (this._containsType("Xml")) result.push(".xml");
	    if (this._containsType("Html")) result.push("text/html");
	    if (this._containsType("Pdf")) result.push("application/pdf");
	    return result;
	};

	_containsType = (type) => {
	    for (let i=0; i<this.props.allowedTypes.length; i++) {
	        if (this.props.allowedTypes[i] == type) return true;
	    }
	    return false;
	}

	refresh = (value) => {
		this.setState({ "value": value });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};
}

DisplayFactory.register("FileEditable", FileEditable);