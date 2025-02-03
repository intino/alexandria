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
import { Add, Cancel } from "@material-ui/icons";
import 'alexandria-ui-elements/res/styles/components/fileeditable/styles.css';
import 'alexandria-ui-elements/res/styles/layout.css';
import { withStyles } from '@material-ui/core/styles';

const styles = theme => ({
	dropzoneText: {
		background: "red",
	},
	pasteInput: {
	    border: "1px dashed #0000001f",
	    marginTop: "5px",
	    padding: "10px"
	}
});

class FileEditable extends AbstractFile {

	constructor(props) {
		super(props);
		this.notifier = new FileEditableNotifier(this);
		this.requester = new FileEditableRequester(this);
		this.inputRef = React.createRef();
		this.state = {
		    ...this.state,
            value: null,
            filename: null,
            readonly: this.props.readonly,
            editable: false,
            allowedTypes: this.props.allowedTypes
        };
	};

	handleChange(e) {
	    this.saveFile(e.target.files[0], e.target.value);
	};

	handleClear(e) {
	    this.saveFile(null, null);
	};

	handleDownload(e) {
	    this.requester.downloadFile();
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
                {label != null && label !== "" ? <div style={{color:color,fontSize:"10pt",color:"#0000008a",marginBottom:"5px"}}>{this.translate(label)}</div> : undefined }
				{this._renderPreview()}
				{this._renderComponent()}
			</Block>
		);
	};

	_renderPreview = () => {
	    if (!this.props.preview || this.state.value == null) return (<React.Fragment/>);
	    return (<div style={{...this.style(),marginBottom:'10px'}}>{this.renderInstances()}</div>);
	};

	_renderComponent = () => {
	    const result = [];
	    if (this.props.dropZone && !this.state.readonly) result.push(this._renderDropZone());
	    else if (!this.props.pasteZone) result.push(this._renderInput());
	    if (this.props.pasteZone && !this.state.readonly) result.push(this._renderPasteZone());
	    return result;
	};

	handlePaste = (e) => {
        const clipboardData = e.clipboardData || window.clipboardData;
        const file = clipboardData.files.length > 0 ? clipboardData.files[0] : null;
        if (file == null) return;
        this.saveFile(file, file.name);
	};

	_renderDropZone = () => {
		const { classes } = this.props;
	    return (
            <DropzoneArea
                Icon={Add}
                acceptedFiles={this._allowedTypes()}
                dropzoneText={this.translate("Drag and drop a file here or click")}
                fileObjects={this.state.files}
                dropzoneClass="fileeditable-dropzone"
                dropzoneParagraphClass="fileeditable-dropzone-paragraph"
                filesLimit={1}
                maxFileSize={this.props.maxSize != null ? this.props.maxSize : 20971520000}
                showPreviews={false}
                showPreviewsInDropzone={true}
                useChipsForPreview
                previewGridProps={{container: { spacing: 1, direction: 'row' }}}
                previewText={this.translate("Selected file")}
                showAlerts={false}
                showFilenames={true}
                onDelete={(file) => { this.saveFile(null, null) }}
                onChange={(files) => { for (var i=0; i<files.length;i++) this.saveFile(files[i], files[i].name); }}
            />
        );
	};

	_renderPasteZone = () => {
	    if (!this.props.pasteZone) return (<React.Fragment/>);
		const { classes } = this.props;
        return (
            <input className={classes.pasteInput}
                   placeholder={this.translate("Paste content here from clipboard")}
                   disabled={this.state.readonly ? true : undefined}
                   onPaste={this.handlePaste.bind(this)} value="" ></input>
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
	    const empty = this.state.value == null || this.state.value === "";
	    const readonly = this.state.readonly;
	    return (
	        <div className="layout horizontal center" style={{padding:'0 5px',border:'1px solid #ddd',marginBottom:'4px'}}>
	            <div className="layout vertical flex" style={{marginRight:'10px'}}>{this.filename()}</div>
	            <IconButton title={this.translate("Download")} size="small" color="primary" disabled={empty} onClick={this.handleDownload.bind(this)}><Add/></IconButton>
	            <IconButton title={this.translate("Remove")} size="small" color="primary" disabled={readonly} onClick={this.handleClear.bind(this)}><Cancel/></IconButton>
            </div>
        );
	};

	filename = () => {
	    if (this.state.filename != null) return this.state.filename;
	    const id = this.state.value.substr(this.state.value.lastIndexOf("/")+1);
	    let filename = id;
	    try { filename = atob(id); }
        catch (e) {}
	    filename = filename.indexOf("/") !== -1 ? filename.substr(filename.lastIndexOf("/")+1) : filename;
	    return decodeURIComponent(unescape(filename));
	};

	_renderInputField = () => {
	    return (<input ref={this.inputRef} type="file" disabled={this.state.readonly ? true : undefined}
	                   onChange={this.handleChange.bind(this)} value="" ></input>);
    };

	_allowedTypes = () => {
	    if (this.state.allowedTypes == null || this.state.allowedTypes.length == 0) return undefined;
	    let result = [];
	    if (this._containsType("Image")) result.push("image/*");
	    if (this._containsType("Audio")) result.push("audio/*");
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
	    for (let i=0; i<this.state.allowedTypes.length; i++) {
	        if (this.state.allowedTypes[i] == type) return true;
	    }
	    return false;
	}

	refresh = (info) => {
		this.setState({ value: info.value, filename: info.filename });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	refreshAllowedTypes = (allowedTypes) => {
		this.setState({ allowedTypes });
	};

	refreshFocused = (focused) => {
        if (this.inputRef == null || this.inputRef.current == null) return;
	    window.setTimeout(() => {
            if (this.state.readonly) this.inputRef.current.scrollIntoView();
            else this.inputRef.current.focus();
	    }, 100);
	};
}

export default withStyles(styles, { withTheme: true })(FileEditable);
DisplayFactory.register("FileEditable", withStyles(styles, { withTheme: true })(FileEditable));