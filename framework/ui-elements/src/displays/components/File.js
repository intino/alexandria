import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileNotifier from "../../../gen/displays/notifiers/FileNotifier";
import FileRequester from "../../../gen/displays/requesters/FileRequester";
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import 'alexandria-ui-elements/res/styles/layout.css';
import Block from "./Block";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import SaveAltIcon from '@material-ui/icons/SaveAlt';

const styles = theme => ({
	label: {
		color: theme.palette.grey.primary,
		marginRight: "5px"
	},
	value: {
		minHeight: "300px",
		minWidth: "100px"
	},
	message : {
		color: theme.palette.secondary.main
	}
});

class File extends AbstractFile {
	state = {
		value : this.props.value,
		filename : this.props.filename,
		mimeType : null
	};

	constructor(props) {
		super(props);
		this.notifier = new FileNotifier(this);
		this.requester = new FileRequester(this);
	};

	render() {
		const { classes } = this.props;
		const file = this.state.value != null ? this.state.value + (this.state.value.indexOf("?") != -1 ? "&" : "?") + "embedded=true" : undefined;

		if (file === undefined) return (<React.Fragment/>);

		const notAvailable = this.translate("No preview available");
		const notSupportedMessage = this.translate("It appears your application is not configured to display PDF files. No worries, just");
		const notSupportedLinkMessage = this.translate("click here to download file");
		const downloadTitle = this.translate("Download");

		return (
			<Block layout="horizontal flex">
				{ ComponentBehavior.labelBlock(this.props) }
				{!this._isPdf() &&
				    <div style={{height:"100%", width:"100%"}} className="layout vertical flex">
				        <div style={{height:"50px", background:"#26282B"}}></div>
				        <div style={{background:"#414447"}} className="layout vertical flex">
                            <div style={this.style()} className={classNames(classes.message, "layout vertical center-center")}>
                                <div style={{padding:"50px 70px",background:"#4C494C",borderRadius:"10px",fontSize:"12pt",boxShadow:"3px 3px 25px black"}} className="layout vertical center-center">
                                    <div style={{marginBottom:"10px",fontSize:"15pt",color:"white"}}>{notAvailable}</div>
                                    <Button variant="contained" color="primary" onClick={this._downloadFile.bind(this, file)}><SaveAltIcon style={{marginRight:"5px"}}/>{downloadTitle}</Button>
                                </div>
                            </div>
                        </div>
                    </div>
				}
				{this._isPdf() &&
					<object className={classes.value} style={this.style()} data={file} type="application/pdf" download={this.state.filename}>
						<div className="layout horizontal center-center">
							<p>{notSupportedMessage}</p>&nbsp;<a href={file} target="_blank">{notSupportedLinkMessage}</a>
						</div>
					</object>
				}
			</Block>
		);
	};

	_isPdf = () => {
		return this.state.mimeType != null && this.state.mimeType === "application/pdf";
	};

	_extension = () => {
	    if (this.state.mimeType === "application/vnd.ms-excel") return ".xls";
	    else if (this.state.mimeType === "application/xml") return ".xml";
	    return ".bin";
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		result.width = this.props.width != null ? this.props.width : "100%";
		result.height = this.props.height != null ? this.props.height : "100%";
		return result;
	};

	refresh = (info) => {
		this.setState({ "value" : info.value, "filename": info.filename, "mimeType": info.mimeType });
	};

	_downloadFile = (file) => {
        var link = document.createElement('a');
        if (typeof link.download === 'string') {
            document.body.appendChild(link); // Firefox requires the link to be in the body
            link.download = this._downloadFilename();
            link.target = "_blank";
            link.href = file;

            link.click();
            document.body.removeChild(link); // remove the link when done
        } else {
            location.replace(file);
        }
	};

	_downloadFilename = () => {
        return this.state.filename;
    }
}

export default withStyles(styles, { withTheme: true })(File);
DisplayFactory.register("File", withStyles(styles, { withTheme: true })(File));