import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileEditableNotifier from "../../../gen/displays/notifiers/FileEditableNotifier";
import FileEditableRequester from "../../../gen/displays/requesters/FileEditableRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import File from "./File";
import Block from "./Block";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import Theme from "app-elements/gen/Theme";
import { DropzoneArea } from 'material-ui-dropzone';
import { IconButton } from "@material-ui/core";
import { Cancel } from "@material-ui/icons";
import 'alexandria-ui-elements/res/styles/components/fileeditable/styles.css';
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	dropzoneText: {
		background: "red",
	},
});

export default class FileEditable extends AbstractFile {

	constructor(props) {
		super(props);
		this.notifier = new FileEditableNotifier(this);
		this.requester = new FileEditableRequester(this);
		this.state = {
		    ...this.state,
            value : null,
            readonly : this.props.readonly,
            editable : false,
        };
	};

	handleChange(e) {
	    this.saveFile(e.target.files[0], e.target.value);
	};

	handleClear(e) {
	    this.saveFile(null, null);
	};

	saveFile(file, value) {
	    this.requester.notifyUploading();
		this.requester.notifyChange(file);
		this.setState({ value: value });
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
        const { classes } = this.props;
        const theme = Theme.get();
		const label = this.props.label !== "" ? this.props.label : undefined;
		const width = this.props.width != null ? this.props.width : "100%";
		const height = this.props.height != null ? this.props.height : "100%";
		const color = this.state.readonly ? theme.palette.grey.A700 : "inherit";
		return (
			<Block layout="vertical" style={{...this.style(),width:width,height:height}}>
                {label != null && label !== "" ? <div style={{color:color,fontSize:"10pt",color:"#0000008a",marginBottom:"5px"}}>{label}</div> : undefined }
				{this._renderPreview()}
				{this._renderComponent()}
			</Block>
		);
	};

	_renderPreview = () => {
	    if (!this.props.preview) return (<React.Fragment/>);
	    return (<div style={{...this.style(),marginBottom:'10px'}}>{this.renderInstances()}</div>);
	};

	_renderComponent = () => {
	    if (this.props.dropZone && !this.state.readonly) return this._renderDropZone();
	    return this._renderInput();
	};

	_renderDropZone = () => {
		const { classes } = this.props;
	    return (
	        <DropzoneArea
	            acceptedFiles={this._allowedTypes()}
	            dropzoneText={this.translate("Drag and drop a file here or click")}
	            dropzoneClass="fileeditable-dropzone"
	            dropzoneParagraphClass="fileeditable-dropzone-paragraph"
                filesLimit={1}
                maxFileSize={this.props.maxSize != null ? this.props.maxSize : 300000000}
                showPreviewsInDropzone={false}
	            onChange={(files) => {
	                if (files.length <= 0) return;
	                if (files.length > 1) return;
	                this.saveFile(files[0], files[0].name);
            }}/>
        );
	};

	_renderInput = () => {
        return (
            <React.Fragment>
                {this.state.value != null && this._renderInputValue()}
                {this.state.value == null && this._renderInputField()}
            </React.Fragment>
        );
	};

	_renderInputValue = () => {
	    return (
	        <div className="layout horizontal center" style={{padding:'0 5px',border:'1px solid #ddd',marginBottom:'4px'}}>
	            <div className="layout vertical flex" style={{marginRight:'10px'}}>{this.filename()}</div>
	            <IconButton size="small" onClick={this.handleClear.bind(this)}><Cancel/></IconButton>
            </div>
        );
	};

	filename = () => {
	    const id = this.state.value.substr(this.state.value.lastIndexOf("/")+1);
	    let filename = id;
	    try { filename = atob(id); }
        catch (e) {}
	    return filename.indexOf("/") !== -1 ? filename.substr(filename.lastIndexOf("/")+1) : filename;
	};

	_renderInputField = () => {
	    return (<input type="file" disabled={this.state.readonly ? true : undefined}
	                   onChange={this.handleChange.bind(this)}></input>);
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
	    if (this._containsType("Excel")) {
	        result.push("application/vnd.ms-excel");
	        result.push("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    }
	    return result;
	};

	_containsType = (type) => {
	    for (let i=0; i<this.props.allowedTypes.length; i++) {
	        if (this.props.allowedTypes[i] == type) return true;
	    }
	    return false;
	}

	refresh = (info) => {
		this.setState({ value: info.value });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};
}

DisplayFactory.register("FileEditable", FileEditable);