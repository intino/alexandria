import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileEditableNotifier from "../../../gen/displays/notifiers/FileEditableNotifier";
import FileEditableRequester from "../../../gen/displays/requesters/FileEditableRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Block from "./Block";
import ProgressBar from "./ProgressBar";
import Theme from "app-elements/gen/Theme";
import {DropzoneArea} from 'material-ui-dropzone';
import {Box} from "@material-ui/core";
import {Add} from "@material-ui/icons";
import 'alexandria-ui-elements/res/styles/components/fileeditable/styles.css';
import 'alexandria-ui-elements/res/styles/layout.css';
import {withStyles} from '@material-ui/core/styles';
import NumberUtil from 'alexandria-ui-elements/src/util/NumberUtil';
import classnames from 'classnames';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import ComponentBehavior from "./behaviors/ComponentBehavior";

const styles = theme => ({
    info: {
        color: theme.isDark() ? '#799deb' : '#2563EB',
        margin: '2px 10px 0 2px'
    },
    errorMessage: {
        margin: "5px 0 0",
        background: "#fdecec",
        color: "#e13939",
        padding: "2px 10px",
        borderRadius: "6px",
    },
	dropzoneText: {
		background: "red",
	},
	readonlyDropzone: {
		position: "absolute",
		top: 0,
		left: 0,
		width: "100%",
		height: "100%",
		zIndex: 10,
		opacity: 0.05
	},
	pasteInput: {
	    border: theme.isDark() ? "1px solid #ffffffde" : "1px solid #555",
	    borderRadius: "14px",
	    marginTop: "5px",
	    padding: "14px 10px",
		background: '#f5ffff',
		"&:hover": {
			background: "#d2f1fe",
		},
		"&:focus": {
			border: theme.isDark() ? "1px solid #ffffffde" : "1px solid #00000023",
			background: "#d2f1fe",
		}
	},
	readonlyPasteInput: {
		border: theme.isDark() ? "1px solid #ffffffde" : "1px solid #d7d7d7",
		background: 'none !important',
		"&::placeholder": {
			color: "#00000030 !important"
		},
	},
	empty : {
		fontSize: '12pt',
	},
	link : {
		cursor: 'pointer',
		width: '70px',
		textAlign: 'center',
		marginRight: '10px',
		fontSize: '11pt',
		color: theme.palette.primary.main,
	},
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
            allowedTypes: this.props.allowedTypes,
            maxSize: this.props.maxSize,
			downloadDelegated: false,
            uploadingFiles: {},
            key: 0,
            errorMessage: null,
        };
	};

	handleChange(e) {
	    this.saveFile(e.target.files[0], e.target.value);
	};

	handleClear(e) {
	    this.saveFile(null, null);
	};

	handleDownload(e) {
		if (this.state.downloadDelegated) this.requester.notifyDownload();
		else this.requester.downloadFile();
	};

	saveFile(file, value) {
	    const fileId = file != null ? this._newUploadingFile(file) : null;

	    if (file != null && this._isMaxSizeExceeded(file)) {
	        this._showFileExceededMessage(file);
	        return;
	    }

	    this.requester.notifyUploading();
		this.requester.notifyChange(file, (progress) => {
		    if (fileId == null) return;
		    this._handleFileProgress(fileId, progress);
        });
		this.setState({ value: value, errorMessage: null });

		if (this.props.progress && !this._isUploadingFiles()) {
			this._resetDropzone();
		}
    };

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
        const { classes, progress } = this.props;
        const theme = Theme.get();
		const label = this.props.label !== "" ? this.props.label : undefined;
		const width = this.props.width != null ? this.props.width : "auto";
		const height = this.props.height != null ? this.props.height : "auto";
		const color = this.state.readonly ? theme.palette.grey.A700 : theme.isDark() ? "#ffffffb3" : "#0000008a";
		return (
			<Block layout="vertical" style={{...this.style(),width:width,height:height}} className="file-editable">
				<div className={classnames("layout ", this._isInlineComponent() ? "horizontal center" : "vertical")}>
					{ ComponentBehavior.labelBlock(this.props, "body1", { marginRight: '15px', color: theme.palette.grey.primary, fontSize: this._isInlineComponent() ? "14pt" : "9pt"}) }
					{this._renderPreview()}
					{this._renderComponent()}
					{this._renderInfoMessage()}
					{this._renderErrorMessage()}
				</div>
				{(progress && this._isUploadingFiles()) && this._renderProgress()}
			</Block>
		);
	};

    _renderInfoMessage = () => {
        const maxSize = this.state.maxSize;
        if (maxSize == null || maxSize == -1) return (<React.Fragment/>);
        const { classes } = this.props;
        return (<div className={classes.info}>{this.translate("Maximum size: %s").replace("%s", this._formatFileSize(this.state.maxSize))}</div>);
    };

    _renderErrorMessage = () => {
        if (this.state.errorMessage == null) return (<React.Fragment/>);
        const { classes } = this.props;
	    return (<div className={classes.errorMessage}>{this.state.errorMessage}</div>);
    };

	_renderPreview = () => {
	    if (!this.props.preview || this.state.value == null) return (<React.Fragment/>);
	    return (<div style={{...this.style(),marginBottom:'10px'}}>{this.renderInstances()}</div>);
	};

	_renderComponent = () => {
	    const { dropZone, pasteZone } = this.props;

	    const result = [];
		if (dropZone || pasteZone) {
			if (dropZone) result.push(this._renderDropZone());
			if (pasteZone) result.push(this._renderPasteZone());
		}
	    else result.push(this._renderInput());

		return result;
	};

	_isInlineComponent = () => {
		const { dropZone, pasteZone, preview } = this.props;
		return !dropZone && !pasteZone && !preview;
	};

	handlePaste = (e) => {
        const clipboardData = e.clipboardData || window.clipboardData;
        const file = clipboardData.files.length > 0 ? clipboardData.files[0] : null;
        if (file == null) return;
        this.saveFile(file, file.name);
	};

	_renderDropZone = () => {
		const { dropZoneLimit } = this.props;
		const maxSize = this.state.maxSize;
		const { classes } = this.props;
	    return (
			<div style={{position:'relative'}}>
				{this.state.readonly && <div className={classes.readonlyDropzone}></div>}
				<DropzoneArea
					key={this.state.key}
					Icon={Add}
					acceptedFiles={this._allowedTypes()}
					dropzoneText={!this.state.readonly ? this.translate("Drag and drop a file here or click") : ""}
					fileObjects={this.state.files}
					dropzoneClass={this.state.readonly ? "fileeditable-dropzone-readonly" : "fileeditable-dropzone" }
					dropzoneParagraphClass="fileeditable-dropzone-paragraph"
					dropzoneIconClass="fileeditable-dropzone-icon"
					filesLimit={ dropZoneLimit || 1 }
					maxFileSize={maxSize != null && maxSize !== -1 ? maxSize : 20971520000}
					showPreviews={false}
					showPreviewsInDropzone={this.state.value != null}
					useChipsForPreview
					previewGridProps={{container: { spacing: 1, direction: 'row' }}}
					previewText={this.translate("Selected file")}
					showAlerts={true}
					getDropRejectMessage={(file) => this._errorMessage(file)}
					showFilenames={true}
					onDelete={(file) => { this.saveFile(null, null) }}
					onChange={(files) => { for (let i=0; i<files.length; i++) this.saveFile(files[i], files[i].name); }}
				/>
			</div>
        );
	};

	_isMaxSizeExceeded = (file) => {
	    const maxSize = this.state.maxSize;
	    if (maxSize == null || maxSize === -1) return false;
	    return file.size > maxSize;
	};

	_showFileExceededMessage = (file) => {
	    this.setState({errorMessage: this._errorMessage(file)});
	};

	_errorMessage = (file) => {
	    const exceeded = this._isMaxSizeExceeded(file);
	    if (exceeded) return this.translate("File is too big. Maximum size is set to %s").replace("%s", this._formatFileSize(this.state.maxSize));
	    else return this.translate("Could not upload file");
	}

	_renderPasteZone = () => {
	    if (!this.props.pasteZone) return (<React.Fragment/>);
		const { classes } = this.props;
        return (
            <input className={classnames(classes.pasteInput, this.state.readonly ? classes.readonlyPasteInput : undefined)}
                   placeholder={!this.state.readonly ? this.translate("Paste content here from clipboard") : ""}
                   disabled={this.state.readonly ? true : undefined}
                   onPaste={this.handlePaste.bind(this)} value="" ></input>
        );
	};

	_renderInput = () => {
        return (
            <React.Fragment>
                {(this.state.readonly || this.state.value != null) && this._renderInputValue()}
                {(!this.state.readonly && this.state.value == null) && this._renderInputField()}
            </React.Fragment>
        );
	};

	_renderInputValue = () => {
		const { classes } = this.props;
	    const empty = this.state.value == null || this.state.value === "";
	    const readonly = this.state.readonly;
	    return (
	        <div className="layout horizontal center">
				{(readonly && empty && this._isInlineComponent()) && <div className={classes.empty}>{this.translate("No attachment")}</div>}
				{!empty && <a className={classes.link} onClick={this.handleDownload.bind(this)}>{this.translate("Download")}</a>}
				{(!empty && !readonly) && <a className={classes.link} onClick={this.handleClear.bind(this)}>{this.translate("Remove")}</a>}
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
	    const allowedTypes = this._allowedTypes();
		const { classes } = this.props;
		const inputId = this.props.id + "-file-input";
	    return (
			<React.Fragment>
				{!this.state.readonly && <label htmlFor={inputId}><a className={classes.link}>{this.translate("Select")}</a></label>}
				<input id={inputId} style={{display:"none"}} ref={this.inputRef} type="file" disabled={this.state.readonly ? true : undefined}
					   onChange={this.handleChange.bind(this)} value="" accept={allowedTypes != null ? allowedTypes.toString() : undefined}></input>
			</React.Fragment>
		);
    };

	_allowedTypes = () => {
	    if (this.state.allowedTypes == null || this.state.allowedTypes.length == 0) return undefined;
	    let result = [];
	    if (this._containsType("Image")) result.push("image/*");
	    if (this._containsType("Audio")) result.push("audio/*");
	    if (this._containsType("Video")) {
	        result.push("video/*");
	        result.push("video/mkv");
	    }
	    if (this._containsType("Application")) result.push("application/*");
	    if (this._containsType("Text")) result.push("text/*");
	    if (this._containsType("Xml")) result.push(".xml");
	    if (this._containsType("Html")) result.push("text/html");
	    if (this._containsType("Pdf")) result.push("application/pdf");
	    if (this._containsType("Jar")) result.push("application/java-archive");
	    if (this._containsType("Zip")) {
	        result.push("application/zip");
	        result.push("application/octet-stream");
	    }
	    if (this._containsType("Excel")) {
	        result.push("application/vnd.ms-excel");
	        result.push("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    }
	    for (var i=0; i<this.state.allowedTypes.length; i++) {
	        if (this.state.allowedTypes[i].substr(0, 1) === ".") result.push(this.state.allowedTypes[i]);
	    }
	    return result;
	};

	_containsType = (type) => {
	    for (let i=0; i<this.state.allowedTypes.length; i++) {
	        if (this.state.allowedTypes[i] == type) return true;
	    }
	    return false;
	};

	_renderProgress = () => {
	    const { classes } = this.props;
	    const { uploadingFiles } = this.state;
	    return (
            <Box>
                {Object.entries(uploadingFiles).map(([fileId, fileData]) => {
                    if (this._isMaxSizeExceeded({size: fileData.fileSize})) return (<React.Fragment/>);
                    else return (
                        <ProgressBar
                            key={fileId}
                            label={fileData.fileName}
                            info={this._formatFileSize(fileData.fileSize)}
                            progress={fileData.progress}
                        />
                    )
                })}
            </Box>
        );
	};

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

	refreshMaxSize = (maxSize) => {
		this.setState({ maxSize });
	};

	refreshDownloadDelegated = (downloadDelegated) => {
		this.setState({ downloadDelegated });
	};

    _newUploadingFile(file) {
        const fileId = `${file.name}-${Date.now()}`;
        this.setState(prevState => {
            const uploadingFiles = Object.assign({}, prevState.uploadingFiles);
            uploadingFiles[fileId] = {
                fileName: file.name,
                fileSize: file.size,
                progress: 0
            };
            return { uploadingFiles };
        });
        return fileId;
    };

    _handleFileProgress(fileId, progress) {
        this.setState(prevState => {
            const updatedFiles = Object.assign({}, prevState.uploadingFiles);
            if (updatedFiles[fileId].progress < 100 && progress >= 100) {
                setTimeout(() => {
                    this._handleFileComplete(fileId);
                }, 1000);
            }
            updatedFiles[fileId] = Object.assign({}, prevState.uploadingFiles[fileId], { progress });
            return { uploadingFiles: updatedFiles };
        });
    };

    _handleFileComplete = (fileId) => {
        this.setState(prevState => {
            const newFiles = Object.assign({}, prevState.uploadingFiles);
            delete newFiles[fileId];
            return { uploadingFiles: newFiles };
        });
    };

    _formatFileSize = (bytes) => {
      const units = ['bytes', 'KB', 'MB', 'GB', 'TB'];
      let i = 0;
      while (bytes >= 1024 && i < units.length - 1) {
        bytes /= 1024;
        i++;
      }
      return `${NumberUtil.format(bytes, bytes % 1 === 0 ? '0' : '0.0')} ${units[i]}`;
    };

	_isUploadingFiles = () => {
        return Object.keys(this.state.uploadingFiles).length > 0;
	};

	_resetDropzone = () => {
		window.setTimeout(() => {
			this.setState({ key: this.state.key+1 });
		}, 0);
	};
}

export default withStyles(styles, { withTheme: true })(FileEditable);
DisplayFactory.register("FileEditable", withStyles(styles, { withTheme: true })(FileEditable));