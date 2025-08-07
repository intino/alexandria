import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractUpload from "../../../gen/displays/components/AbstractUpload";
import UploadNotifier from "../../../gen/displays/notifiers/UploadNotifier";
import UploadRequester from "../../../gen/displays/requesters/UploadRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import classNames from "classnames";
import ProgressBar from "./ProgressBar";
import { Backdrop, Box, CircularProgress, IconButton } from "@material-ui/core";

const styles = theme => ({
    label : {
        padding: '0',
        margin: '0',
        cursor: 'pointer',
        color: theme.palette.primary.main
    },
    disabled : {
        color: 'grey',
        cursor: 'default',
    },
    backdrop: {
        zIndex: theme.zIndex.drawer + 1,
    },
    progressContainer: {
        marginTop: theme.spacing(2),
        width: '80%',
        maxWidth: '600px',
        minWidth: '250px',
    }
});

class Upload extends AbstractUpload {

	constructor(props) {
		super(props);
		this.labelRef = React.createRef();
		this.notifier = new UploadNotifier(this);
		this.requester = new UploadRequester(this);
		this.state = {
		    ...this.state,
		    allowedTypes: this.props.allowedTypes,
		    uploadingFiles: {},
		    showProgress: false,
		    multipleSelection: this.props.multipleSelection
		}
	};

    withWrapper = (content) => {
        const { classes, progress } = this.props;
        const { uploadingFiles } = this.state;
        const isUploading = Object.entries(uploadingFiles).length > 0;
        return (
	        <React.Fragment>
    	        <div class="layout horizontal center">
                    <input type="file" id={this.props.id + "_input"} onChange={this.handleChange.bind(this)} multiple={this.allowMultiple()} hidden disabled={this.state.readonly ? true : undefined} accept={this._allowedTypes()}/>
                    <label className={classNames(classes.label, this.state.readonly ? classes.disabled : undefined)} id={this.props.id + "_inputLabel"} disabled={this.state.readonly ? true : undefined} for={this.props.id + "_input"}>{content}</label>
                    {progress && isUploading && this._renderProgressIcon()}
                    {progress && this._renderProgressBar()}
                </div>
            </React.Fragment>
        );
    };

    _renderProgressBar = () => {
        const { classes } = this.props;
        const { showProgress, uploadingFiles } = this.state;
        return (
        <Backdrop className={classes.backdrop} open={showProgress} onClick={this.closeProgress}>
            <Box className={classes.progressContainer}>
                {Object.entries(uploadingFiles).map(([fileId, fileData]) => (
                    <ProgressBar
                        key={fileId}
                        label={fileData.fileName}
                        info={this._formatFileSize(fileData.fileSize)}
                        progress={fileData.progress}
                    />
                ))}
            </Box>
        </Backdrop>);
    };

    _renderProgressIcon = () => {
        return (
            <IconButton
                size="small"
                onClick={this.openProgress}
                title={this.translate("Show progress")}
                aria-label={this.translate("Show progress")}
                style={{marginRight:'5px'}}>
                <CircularProgress size={20}/>
            </IconButton>
        );
    };

    openProgress = () => {
        this.setState({ showProgress: true });
    };

    closeProgress = () => {
        this.setState({ showProgress: false });
    };

    refreshMultipleSelection = (value) => {
        this.setState({ multipleSelection: value });
    };

    launch = () => {
        const element = document.getElementById(this.props.id + "_input");
        if (element == null) return;
        element.click();
    };

    allowMultiple = () => {
        return this.state.multipleSelection != null && this.state.multipleSelection;
    };

	openDialog = () => {
        const element = document.getElementById(this.props.id + "_input");
        if (element == null) return;
        element.value = null;
        element.focus();
	};

	refreshAllowedTypes = (allowedTypes) => {
		this.setState({ allowedTypes });
	};

	handleChange = (e) => {
	    const files = e.target.files;
	    this.requester.notifyUploading(files.length);
	    for (let i=0; i<files.length; i++) this._uploadFile(files[i]);
	};

	_uploadFile = (file) => {
	    const fileId = this._newUploadingFile(file);
	    this.requester.add(file, (progress) => {
            this._handleFileProgress(fileId, progress);
        });
	}

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
	    return result;
	};

	_containsType = (type) => {
	    for (let i=0; i<this.state.allowedTypes.length; i++) {
	        if (this.state.allowedTypes[i] == type) return true;
	    }
	    return false;
	};

    _formatFileSize = (bytes) => {
      const units = ['bytes', 'KB', 'MB', 'GB', 'TB'];
      let i = 0;
      while (bytes >= 1024 && i < units.length - 1) {
        bytes /= 1024;
        i++;
      }
      return `${bytes.toFixed(2)} ${units[i]}`;
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
            return {
                uploadingFiles,
                showProgress: true
            };
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
            return {
                uploadingFiles: newFiles,
                showProgress: Object.keys(newFiles).length > 0 ? prevState.showProgress : false,
            };
        });
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Upload));
DisplayFactory.register("Upload", withStyles(styles, { withTheme: true })(withSnackbar(Upload)));