import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractUpload from "../../../gen/displays/components/AbstractUpload";
import UploadNotifier from "../../../gen/displays/notifiers/UploadNotifier";
import UploadRequester from "../../../gen/displays/requesters/UploadRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import classNames from "classnames";

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
		    allowedTypes: this.props.allowedTypes
		}
	};

    withWrapper = (content) => {
        const { classes } = this.props;
        return (
	        <React.Fragment>
	            <input type="file" id={this.props.id + "_input"} onChange={this.handleChange.bind(this)} multiple={this.allowMultiple()} hidden disabled={this.state.readonly ? true : undefined} accept={this._allowedTypes()}/>
                <label className={classNames(classes.label, this.state.readonly ? classes.disabled : undefined)} id={this.props.id + "_inputLabel"} disabled={this.state.readonly ? true : undefined} for={this.props.id + "_input"}>{content}</label>
            </React.Fragment>
        );
    };

    allowMultiple = () => {
        return this.props.multipleSelection != null && this.props.multipleSelection;
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
	    for (let i=0; i<files.length; i++) this.requester.add(files[i]);
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
	    return result;
	};

	_containsType = (type) => {
	    for (let i=0; i<this.state.allowedTypes.length; i++) {
	        if (this.state.allowedTypes[i] == type) return true;
	    }
	    return false;
	}

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Upload));
DisplayFactory.register("Upload", withStyles(styles, { withTheme: true })(withSnackbar(Upload)));