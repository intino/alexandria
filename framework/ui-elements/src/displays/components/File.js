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
		mimeType : null,
		data : null
	};

	constructor(props) {
		super(props);
		this.notifier = new FileNotifier(this);
		this.requester = new FileRequester(this);
		this.container = React.createRef();
	};

	render() {
		const { classes } = this.props;
		const file = this._fileUrl();

		if (file === undefined) return (<React.Fragment/>);

		const notAvailable = this.translate("No preview available");
		const notSupportedMessage = this.translate("It appears your application is not configured to display PDF files. No worries, just");
		const notSupportedLinkMessage = this.translate("click here to download file");
		const downloadTitle = this.translate("Download");

		return (
			<Block layout="horizontal flex">
				{ ComponentBehavior.labelBlock(this.props) }
				{(!this._isPdf() && !this._isXml()) &&
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
				{this._isXml() && this._renderXml()}
			</Block>
		);
	};

	_renderXml = () => {
	    const file = this._fileUrl();
		const downloadTitle = this.translate("Download");
	    this.width = this.container.current != null ? this.container.current.offsetWidth + "px" : (this.width != null ? this.width : "100%");
	    this.height = this.container.current != null ? this.container.current.offsetHeight-60 + "px" : (this.height != null ? this.height : "100%");
        if (this.state.data != null) {
            return (
                <div style={{overflow:'auto',height:'100%',width:'100%'}}>
                    <div style={{height:"50px", background:"#26282B"}} className="layout horizontal start-justified">
                        <Button style={{margin:'8px'}} variant="contained" color="primary" onClick={this._downloadFile.bind(this, file)}><SaveAltIcon style={{marginRight:"5px"}}/>{downloadTitle}</Button>
                    </div>
                    <div className="layout vertical flex">
                        <pre style={{width:this.width,height:this.height,border:0,fontSize:'10pt',background:'#eee',padding:'10px',marginTop:'0',overflow:'auto'}}><code>{this.state.data}></code></pre>
                    </div>
                </div>
            );
        }
        return (<div style={{width:'100%',height:'100%'}} ref={this.container}>...{this._loadData()}</div>);
	};

	_isPdf = () => {
		return this.state.mimeType != null && this.state.mimeType === "application/pdf";
	};

	_isXml = () => {
		return this.state.mimeType != null && this.state.mimeType === "application/xml";
	};

	_fileUrl = () => {
	    return this.state.value != null ? this.state.value + (this.state.value.indexOf("?") != -1 ? "&" : "?") + "embedded=true" : undefined;
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