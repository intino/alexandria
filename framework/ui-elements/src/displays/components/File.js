import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileNotifier from "../../../gen/displays/notifiers/FileNotifier";
import FileRequester from "../../../gen/displays/requesters/FileRequester";
import classNames from 'classnames';
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import 'alexandria-ui-elements/res/styles/layout.css';
import Block from "./Block";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Button from '@mui/material/Button';
import SaveAltIcon from '@mui/icons-material/SaveAlt';
import {dialogPrimaryButtonStyles} from "./ButtonStyles";

const styles = theme => ({
	label: {
		color: theme.palette.grey.A700,
		marginRight: "5px"
	},
	value: {
		minHeight: "300px",
		minWidth: "100px"
	},
	message : {
		color: theme.palette.secondary.main
	},
	toolbar: {
		height: "56px",
		background: theme.palette.mode === "dark" ? "rgba(15,23,42,0.82)" : "#f8fafc",
		borderBottom: `1px solid ${theme.palette.mode === "dark" ? "rgba(255,255,255,0.08)" : "rgba(15,23,42,0.08)"}`,
		padding: "0 12px",
	},
	surface: {
		background: theme.palette.mode === "dark" ? "linear-gradient(180deg, rgba(30,41,59,0.92) 0%, rgba(15,23,42,0.92) 100%)" : "linear-gradient(180deg, #f8fbff 0%, #eef4fb 100%)",
		borderRadius: "18px",
		overflow: "hidden",
		border: `1px solid ${theme.palette.mode === "dark" ? "rgba(255,255,255,0.08)" : "rgba(15,23,42,0.08)"}`,
		boxShadow: theme.palette.mode === "dark" ? "0 14px 40px rgba(0,0,0,0.28)" : "0 12px 32px rgba(15,23,42,0.08)",
	},
	emptyState: {
		padding: "50px 70px",
		background: theme.palette.mode === "dark" ? "rgba(15,23,42,0.72)" : "rgba(255,255,255,0.92)",
		borderRadius: "16px",
		fontSize: "12pt",
		boxShadow: theme.palette.mode === "dark" ? "0 12px 30px rgba(0,0,0,0.24)" : "0 12px 24px rgba(15,23,42,0.08)",
		border: `1px solid ${theme.palette.mode === "dark" ? "rgba(255,255,255,0.08)" : "rgba(15,23,42,0.06)"}`,
	},
	emptyTitle: {
		marginBottom: "10px",
		fontSize: "15pt",
		color: theme.palette.mode === "dark" ? "white" : theme.palette.text.primary,
	}
});

class File extends AbstractFile {

	constructor(props) {
		super(props);
		this.notifier = new FileNotifier(this);
		this.requester = new FileRequester(this);
		this.container = React.createRef();
		this.state = {
			...this.state,
			value : this.props.value,
			filename : this.props.filename,
			mimeType : this.props.mimeType,
			data : null
		}
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
		const file = this._fileUrl();

		if (file === undefined) return (<React.Fragment/>);

		return (
			<Block layout="horizontal flex">
				{ ComponentBehavior.labelBlock(this.props, 'body1', this.style()) }
				{!this._isRecognizedFormat() && this._renderNotRecognizedFormat(file)}
				{this._isPdf() && this._renderPdf(file)}
				{this._isImage() && this._renderImage(file)}
				{this._isXml() && this._renderXml(file)}
			</Block>
		);
	};

	_renderNotRecognizedFormat = (file) => {
		const { classes } = this.props;

		const notAvailable = this.translate("No preview available");
		const downloadTitle = this.translate("Download");

		return (
			<div style={{height:"100%", width:"100%"}} className={classNames(classes.surface, "layout vertical flex")}>
				<div className={classNames(classes.toolbar, "layout horizontal center")}></div>
				<div className="layout vertical flex">
					<div style={this.style()} className={classNames(classes.message, "layout vertical center-center")}>
						<div className={classNames(classes.emptyState, "layout vertical center-center")}>
							<div className={classes.emptyTitle}>{notAvailable}</div>
							<Button sx={dialogPrimaryButtonStyles} variant="contained" color="primary" onClick={this._downloadFile.bind(this, file)}><SaveAltIcon style={{marginRight:"5px"}}/>{downloadTitle}</Button>
						</div>
					</div>
				</div>
			</div>
		);
	};

	_renderPdf = (file) => {
		const { classes } = this.props;

		const notSupportedMessage = this.translate("It appears your application is not configured to display PDF files. No worries, just");
		const notSupportedLinkMessage = this.translate("click here to download file");

		return (
			<object className={classes.value} style={this.style()} data={file} type="application/pdf" download={this.state.filename}>
				<a href={this.state.filename}>{this.state.filename}</a>
				<div className="layout horizontal center-center">
					<p>{notSupportedMessage}</p>&nbsp;<a href={file} target="_blank">{notSupportedLinkMessage}</a>
				</div>
			</object>
		);
	}

	_renderImage = (file) => {
		const { classes } = this.props;
		const downloadTitle = this.translate("Download");
		const imageId = this.props.id + "_" + Math.random();
		window.setTimeout(() => this._adjustImageSize(imageId), 10);
		return (
			<div style={{width:'100%',height:'100%'}} ref={this.container}>
				<div className={classNames(classes.surface, "layout vertical flex")} style={{height:"100%", width:"100%"}}>
					<div className={classNames(classes.toolbar, "layout horizontal start-justified center")}>
						<Button sx={dialogPrimaryButtonStyles} style={{margin:'8px'}} variant="contained" color="primary" onClick={this._downloadFile.bind(this, file)}><SaveAltIcon style={{marginRight:"5px"}}/>{downloadTitle}</Button>
					</div>
					<div className="layout vertical flex">
						<div style={this.style()} className={classNames(classes.message, "layout vertical center-center")}>
							<div className="layout vertical center-center">
								<img id={imageId} src={file} title={this.state.filename} style={{display:"none"}}/>
							</div>
						</div>
					</div>
				</div>
			</div>
		);
	}

	_adjustImageSize = (imageId) => {
		const width = this.container.current.offsetWidth - 20;
		const height = this.container.current.offsetHeight - 20;
		const element = document.getElementById(imageId);
		element.style.display = "block";
		element.style.width = width + "px";
		if (element.offsetHeight > height) {
			element.style.width = "100%";
			element.style.height = height + "px";
		}
	};

	_renderXml = (file) => {
		const { classes } = this.props;
		const downloadTitle = this.translate("Download");
		this.width = this.container.current != null ? this.container.current.offsetWidth + "px" : (this.width != null ? this.width : "100%");
		this.height = this.container.current != null ? this.container.current.offsetHeight-60 + "px" : (this.height != null ? this.height : "100%");
		if (this.state.data != null) {
			return (
				<div className={classes.surface} style={{overflow:'auto',height:'100%',width:'100%'}}>
					<div className={classNames(classes.toolbar, "layout horizontal start-justified center")}>
						<Button sx={dialogPrimaryButtonStyles} style={{margin:'8px'}} variant="contained" color="primary" onClick={this._downloadFile.bind(this, file)}><SaveAltIcon style={{marginRight:"5px"}}/>{downloadTitle}</Button>
					</div>
					<div className="layout vertical flex">
						<pre style={{width:this.width,height:this.height,border:0,fontSize:'10pt',background:'transparent',padding:'16px',marginTop:'0',overflow:'auto'}}><code>{this.state.data}</code></pre>
					</div>
				</div>
			);
		}
		return (<div style={{width:'100%',height:'100%'}} ref={this.container}>...{this._loadData()}</div>);
	};

	_isRecognizedFormat = () => {
		return this._isPdf() || this._isXml() || this._isImage();
	};

	_isPdf = () => {
		return this.state.mimeType != null && this.state.mimeType === "application/pdf";
	};

	_isImage = () => {
		return this.state.mimeType != null && this.state.mimeType.startsWith("image/");
	};

	_isXml = () => {
		return this.state.mimeType != null && this.state.mimeType === "application/xml";
	};

	_fileUrl = () => {
		var url = this.state.value != null ? this.state.value : undefined;
		if (url == undefined) return url;
		url += (url.indexOf("?") != -1 ? "&" : "?") + "embedded=true";
		if (this.state.filename != null) url += "&label=" + this.state.filename;
		return url;
	};

	_extension = () => {
		if (this.state.mimeType === "application/vnd.ms-excel") return ".xls";
		else if (this.state.mimeType === "application/xml") return ".xml";
		return ".bin";
	};

	_loadData = () => {
		const file = this._fileUrl();
		if (file === undefined) return;
		var request = this._createHttpRequest();
		request.open("GET", file, true);
		request.send(null);
		const fileWidget = this;
		request.onreadystatechange = function() {
			if (request.readyState == 4) fileWidget.setState({data: request.responseText});
		};
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		result.width = this.props.width != null ? this.props.width : "100%";
		result.height = this.props.height != null ? this.props.height : "100%";
		return result;
	};

	refresh = (info) => {
		this.setState({ "value" : info.value, "filename": info.filename, "mimeType": info.mimeType, data: null });
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

	_createHttpRequest = () => {
		try { return new XMLHttpRequest(); }
		catch (error) {}
		try { return new ActiveXObject("Msxml2.XMLHTTP"); }
		catch (error) {}
		try {return new ActiveXObject("Microsoft.XMLHTTP");}
		catch (error) {}
		throw new Error("Could not create HTTP request object.");
	};

	_downloadFilename = () => {
		return this.state.filename;
	}
}

export default withStyles(styles, { withTheme: true })(File);
DisplayFactory.register("File", withStyles(styles, { withTheme: true })(File));
