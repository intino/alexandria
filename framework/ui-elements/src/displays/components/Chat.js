import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractChat from "../../../gen/displays/components/AbstractChat";
import ChatNotifier from "../../../gen/displays/notifiers/ChatNotifier";
import ChatRequester from "../../../gen/displays/requesters/ChatRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { TextField, Dialog, DialogTitle, DialogContent, DialogActions, IconButton, Popover } from "@material-ui/core";
import { Send, Add, Chat as ChatIcon, Clear } from '@material-ui/icons';
import Moment from 'react-moment';
import classnames from 'classnames';
import InnerHTML from 'dangerously-set-html-content'
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import SaveAltIcon from '@material-ui/icons/SaveAlt';
import {emojify} from 'react-emojione';
import 'alexandria-ui-elements/res/styles/layout.css';
import 'alexandria-ui-elements/res/styles/components/chat/styles.css';
import { BeatLoader } from "react-spinners";
import { marked } from "marked";
import { DropzoneArea } from 'material-ui-dropzone';

const styles = theme => ({
    container : {
        width: "100%",
        height: "100%",
    },
    messagesContainer : {
        overflow: "auto",
        height: "0",
        padding: "0 5px 5px",
        width: "100%",
        height: "calc(100% - 97px)",
        background: theme.isLight() ? "white" : "#303030",
    },
    messagesContainerNoHeader : {
        height: "calc(100% - 47px)",
    },
    startReached : {
        padding: "10px",
        color: "grey",
    },
    day : {
        padding: "3px 15px",
        background: theme.isLight() ? "#dfdfdf" : "#535353",
        fontSize: "9pt",
        borderRadius: "10px",
        marginBottom: "5px"
    },
    date : {
        color: "#888",
        fontSize: "8pt",
        marginTop: "4px",
        marginLeft: "10px",
    },
    outgoing : {
        background: theme.isLight() ? "#e0f7dd" : "#3f603a",
        padding: "4px 10px",
        borderRadius: "13px",
    },
    incoming : {
        background: theme.isLight() ? "#dde4f7" : "#445073",
        padding: "4px 10px",
        borderRadius: "13px"
    },
    content : {
        maxWidth: "450px",
        background: "none",
        border: "0",
        padding: "3px 5px",
        fontSize: "11pt",
    },
    input : {
        marginRight: "10px",
        fontSize: "11pt",
        width: "100%",
        border: theme.isLight() ? "1px solid #888" : "1px solid white",
        borderRadius : "3px",
        paddingLeft: "4px"
    },
    file : {
        color: theme.palette.secondary.main
    },
    fileValue: {
        minHeight: "300px",
        minWidth: "100px"
    },
    header : {
        background: theme.isLight() ? "#efefef" : "black",
        padding: "10px",
        fontSize: "12pt",
        height: "44px",
    },
    toolbar : {
        background: theme.isLight() ? "#efefef" : "black",
        padding: "10px"
    },
    label : {
        marginTop: "10px",
        fontSize: "11pt"
    },
});

class Chat extends AbstractChat {

	constructor(props) {
		super(props);
		this.notifier = new ChatNotifier(this);
		this.requester = new ChatRequester(this);
		this.messagesContainer = React.createRef();
		this.attachmentContainer = React.createRef();
		this.assistantTrigger = React.createRef();
		this.state = {
		    ...this.state,
		    label: this.props.label,
		    message: null,
		    messages: [],
		    scroll: -1,
		    startReached: false,
		    chatLayerOpened: false,
		    addAttachmentDialogOpened: false,
		    attachment: { file: null, description: null},
		    images: { incoming: null, outgoing: null, loading: null }
		};
	};

    componentDidMount() {
        super.componentDidMount();
        window.addEventListener('resize', this.resize.bind(this));
        this.resize();
    };

	render() {
	    this.lastDay = null;
	    if (!this.state.visible) return (<React.Fragment/>);
	    return (
	        <React.Fragment>
	            {this.props.view == "Floating" && this.renderFloatingLayer() }
	            {this.props.view != "Floating" && this.renderDefaultLayer() }
            </React.Fragment>
        );
	};

	renderDefaultLayer = () => {
	    window.setTimeout(() => this.resize(), 50);
        return this.renderContent();
	};

	renderFloatingLayer = () => {
	    return (
	        <div>
                <IconButton ref={this.assistantTrigger} style={{marginRight:'5px'}} size="large" aria-label={this.state.title} color="inherit" onClick={this.handleOpenChatLayer.bind(this)}><ChatIcon fontSize="large"/></IconButton>
                {this.renderChatLayer()}
            </div>
	    );
	};

	renderContent = () => {
	    const { classes } = this.props;
	    window.setTimeout(e => this.setupContainer(), 100);
	    const containerClassName = this.isHeaderVisible() ? classes.messagesContainer : classnames(classes.messagesContainer, classes.messagesContainerNoHeader);
	    return (
	        <div className={classnames("layout vertical flex intino-chat", classes.container)}>
	            {this.renderHeader()}
                <div ref={this.messagesContainer} className={containerClassName} onScroll={this.handleScroll.bind(this)} style={{height:'1px'}}>
                    {(this.state.startReached && this.state.messages.length > 0) && <div className={classnames("layout vertical center-center", classes.startReached)}>{this.translate("No more previous messages")}</div>}
                    {this.state.messages.map((m, index) => this.renderMessage(m, index))}
                </div>
                {this.renderToolbar()}
            </div>
        );
	};

	renderHeader = () => {
	    const { classes } = this.props;
	    if (!this.isHeaderVisible()) return (<React.Fragment/>);
	    return (<div className={classes.header}>{this.state.label}</div>);
	};

	isHeaderVisible = () => {
	    if (this.props.view == "Floating") return true;
	    return this.state.label != null && this.state.label != "";
	};

	renderMessage = (message, index) => {
	    return (
	        <div style={{marginBottom:"5px"}}>
	            {this.renderMessageDay(message)}
	            {message.direction == "Outgoing" && this.renderOutgoingMessage(message, index)}
	            {message.direction == "Incoming" && this.renderIncomingMessage(message, index)}
            </div>
        );
	};

	renderMessageDay = (message) => {
	    var date = new Date(message.date).toDateString();
	    if (this.lastDay == date) return (<React.Fragment/>);
	    const { classes } = this.props;
		const language = window.Application.configuration.language;
	    this.lastDay = date;
	    return (<div className="layout vertical center"><Moment className={classnames("layout vertical center-center", classes.day)} format="ll" date={message.date} locale={language}/></div>);
	};

	renderOutgoingMessage = (message, index) => {
	    const { classes } = this.props;
        return this.renderMessageWith(message, index, "end-justified", classes.outgoing);
	};

	renderIncomingMessage = (message, index) => {
	    const { classes } = this.props;
        return this.renderMessageWith(message, index, "", classes.incoming);
	};

	renderMessageWith = (message, index, blockClazz, clazz) => {
	    const { classes } = this.props;
		const language = window.Application.configuration.language;
	    const images = this.state.images;
	    const showLoading = this.loadingDefined() && this.processing() && index == this.state.messages.length-1;
	    return (
	        <div className={classnames("layout horizontal start", blockClazz)}>
                {showLoading && <img src={images.loading} style={{height:"24px",width:"24px",margin:"5px"}}/> }
                {(!showLoading && message.direction == "Incoming" && images.incoming != null) && <img src={images.incoming} style={{height:"24px",width:"24px",margin:"5px"}}/> }
	            <div className={classnames("layout horizontal end", clazz)}>
	                <div>
	                    <div className={classes.content}>{this.renderMessageContent(message, index)}</div>
	                    {this.renderMessageAttachments(message, index)}
	                </div>
                    <Moment className={classes.date} format="HH:mm" ago date={message.date} locale={language}/>
	            </div>
                {message.direction == "Outgoing" && images.outgoing != null && <img src={images.outgoing} style={{height:"24px",width:"24px",margin:"0 5px"}}/> }
	        </div>
	    );
	};

	loadingDefined = (index) => {
	    return this.state.images.loading != null;
	};

	processing = () => {
	    const messages = this.state.messages;
	    if (this.props.messageFlow == "Continuous") return false;
	    return this.state.loading || (messages.length > 0 && messages[messages.length-1].active);
    };

	loadingDefined = () => {
	    return this.state.images.loading != null;
	};

	renderMessageContent = (message, index) => {
	    this.refreshEvents();
	    if (message.content == null || message.content === "") return (<BeatLoader size={8} />);
	    return (<InnerHTML id={this.props.id + "-" + index + "-html"} html={emojify(this.parse(message.content), { output: 'unicode' })} allowRerender={true}/>);
	};

	parse = (content) => {
	    const hasHtml = new RegExp("<\\/?[a-z][^>]*>").test(content);
	    return hasHtml ? content : marked.parse(content);
	};

	renderMessageAttachments = (message, index) => {
	    if (message.attachments.length == 0) return (<React.Fragment/>);
	    return (
	        <div style={{padding:"10px",width:'400px',height:'400px'}}>
	            {message.attachments.map((attachment, index) => this.renderMessageAttachment(attachment, index))}
	        </div>
	    );
	};

	renderMessageAttachment = (attachment, index) => {
		const notSupportedMessage = this.translate("It appears your application is not configured to display PDF files. No worries, just");
		const notSupportedLinkMessage = this.translate("click here to download file");
	    return (
	        <div style={{width:'400px', height:'340px'}}>
				{!this._isRecognizedFormat(attachment) && this._renderNotRecognizedFormat(attachment)}
				{this._isPdf(attachment) && this._renderPdf(attachment)}
				{this._isImage(attachment) && this._renderImage(attachment)}
	        </div>
        );
	};

	renderToolbar = () => {
	    const { classes } = this.props;
	    return (
	        <React.Fragment>
                <div className={classnames(classes.toolbar, "layout horizontal center")}>
                    <IconButton disabled={this.processing()} style={{marginRight:'5px'}} size="small" aria-label={this.translate("Add")} color="inherit" onClick={this.handleOpenAttachmentDialog.bind(this)}><Add fontSize="small"/></IconButton>
                    <TextField onKeyUp={this.handleMessageKeyUp.bind(this)} onChange={this.handleMessageChange.bind(this)} autoFocus={true} value={this.state.message} format={this.variant("body1")} placeholder={this.translate("Write a message...")} type="text" className={classes.input} multiline={false} InputProps={{ readOnly: this.processing(), disableUnderline: true }}></TextField>
                    <IconButton disabled={this.processing()} size="small" aria-label={this.translate("Send")} color="inherit" onClick={this.handleSendMessage.bind(this)}><Send fontSize="small"/></IconButton>
                </div>
                {this.renderAttachmentDialog()}
            </React.Fragment>
	    );
	};

	renderAttachmentDialog = () => {
	    const { classes } = this.props;
        return (
            <Dialog open={this.state.addAttachmentDialogOpened} onClose={this.handleCloseAttachmentDialog.bind(this)}>
                <DialogTitle id="alert-dialog-title">{this.translate("Add attachment")}</DialogTitle>
                <DialogContent>
                    <div className="layout vertical flex center" style={{overflow:"auto",width:"100%",height:"100%"}}>
                        <div style={{width:"400px",height:"100%"}}>
                            <DropzoneArea
                                dropzoneText={this.translate("Drag and drop a file here or click")}
                                fileObjects={[this.state.attachment]}
                                dropzoneClass="fileeditable-dropzone"
                                dropzoneParagraphClass="fileeditable-dropzone-paragraph"
                                filesLimit={1}
                                maxFileSize={this.props.maxSize != null ? this.props.maxSize : 300000000}
                                showPreviews={false}
                                showPreviewsInDropzone={true}
                                useChipsForPreview
                                previewGridProps={{container: { spacing: 1, direction: 'row' }}}
                                previewText={this.translate("Selected file")}
                                showAlerts={false}
                                showFilenames={true}
                                onDelete={(file) => { this.handleAttachmentFileChange(null, null) }}
                                onChange={(files) => { for (var i=0; i<files.length;i++) this.handleAttachmentFileChange(files[i], files[i].name); }}
                            />
                            <TextField format={this.variant("body1")} label={this.translate("Description")} type="text"
                                       onChange={this.handleAttachmentDescriptionChange.bind(this)}
                                       style={{marginTop:'10px',width:'100%'}}
                                       value={this.state.attachment.description}
                                       multiline={true} rows={3}>
                            </TextField>
                        </div>
                    </div>
                </DialogContent>
                <DialogActions>
                  <Button onClick={this.handleCloseAttachmentDialog.bind(this)} color="primary">{this.translate("Cancel")}</Button>
                  <Button disabled={this.processing()} onClick={this.handleSendAttachment.bind(this)} color="primary" variant="contained">{this.translate("Add")}</Button>
                </DialogActions>
            </Dialog>
        );
	};

	renderChatLayer = () => {
	    const { classes } = this.props;
	    if (this.state.chatLayerOpened) window.setTimeout(() => this.resize(), 100);
	    return (
	        <Popover open={this.state.chatLayerOpened}
					 anchorEl={this.assistantTrigger.current}
					 anchorOrigin={{vertical: 'top',horizontal: 'center',}}
                     transformOrigin={{vertical: 'bottom',horizontal: 'right',}}
                     onClose={this.handleCloseChatLayer.bind(this)}
                     style={{marginLeft:"20px"}}
                     disableRestoreFocus>
                <div ref={this.popover} className="layout vertical flexible" style={{width:"100%",height:"100%",position:"relative"}}>
                    <IconButton style={{position:'absolute',right:'0'}} size="medium" aria-label={this.translate("Close")} color="inherit" onClick={this.handleCloseChatLayer.bind(this)}><Clear fontSize="medium"/></IconButton>
                    <div style={{height:this.height(),width:this.width()}}>{this.renderContent()}</div>
                </div>
            </Popover>
	    );
	};

	handleOpenChatLayer = () => {
	    this.setState({chatLayerOpened: true});
    };

	handleCloseChatLayer = () => {
	    this.setState({chatLayerOpened: false});
    };

	handleOpenAttachmentDialog = () => {
	    this.setState({ addAttachmentDialogOpened: true, attachment: { file: null, description: null }});
	};

	handleCloseAttachmentDialog = () => {
	    this.setState({ addAttachmentDialogOpened: false });
	};

	handleSendAttachment = () => {
	    this.setState({ addAttachmentDialogOpened: false, loading: true });
	    this.requester.sendAttachment(this.state.attachment.description != null ? this.state.attachment.description : "");
	};

	handleSendMessage = () => {
	    this.sendMessage(this.state.message);
	};

	refresh = (info) => {
	    this.setState({label: info.label, messages: info.messages, scroll: -1, loading: false, images: { incoming: info.incomingImage, outgoing: info.outgoingImage, loading: info.loadingImage }});
	};

	open = () => {
	    this.setState({chatLayerOpened: true});
	};

	refreshEvents = () => {
	    var widget = this;
        window.sendMessage = (message, label) => { widget.sendMessage(message, label) };
    };

	messagesStartReached = () => {
	    this.setState({startReached:true, scroll: 0});
	};

	addPreviousMessages = (newMessages) => {
	    var messages = this.state.messages;
	    for (var i=newMessages.length-1; i>=0; i--) messages.unshift(newMessages[i]);
	    this.setState({messages: messages, scroll: 0});
	};

	addMessages = (newMessages) => {
	    var messages = this.state.messages;
	    newMessages.forEach(message => messages.push(message));
	    this.setState({messages: messages, scroll: -1,loading:false});
	};

	addMessagePart = (content) => {
	    var messages = this.state.messages;
	    if (messages.length <= 0) return;
	    messages[messages.length-1].content += content;
	    this.setState({messages: messages,loading:true});
	};

	closeMessage = () => {
	    var messages = this.state.messages;
	    if (messages.length <= 0) return;
	    messages[messages.length-1].active = false;
	    this.setState({messages: messages, loading: false});
	};

	setupContainer = (resize) => {
	    if (this.messagesContainer.current == null) return;
	    var container = this.messagesContainer.current;
	    const height = container.parentNode.getBoundingClientRect().height-52;
        if (height > 0 && (container.style.height == 0 || resize)) container.style.height = height + "px";
	    if (container.offsetHeight > 20) container.scrollTop = this.state.scroll == -1 ? container.scrollHeight : 0;
        else container.scrollTop = this.state.scroll == -1 ? container.scrollHeight : 0;
	};

	resize = () => {
	    if (this.resizeTimer != null) window.clearTimeout(this.resizeTimer);
	    this.resizeTimer = window.setTimeout(() => this.doResize(), 50);
	};

	doResize = () => {
	    var container = this.messagesContainer.current;
	    if (container == null) return;
        container.style.height = "100px";
        this.setupContainer(true)
	};

	handleScroll = (e) => {
	    const value = e.target.scrollTop;
	    if (value > 30) return;
	    if (this.state.startReached) return;
	    if (this.previousTimeout != null) window.clearTimeout(this.previousTimeout);
	    this.previousTimeout = window.setTimeout(e => this.requester.previousMessages(), 100);
	};

	_isRecognizedFormat = (attachment) => {
	    return this._isPdf(attachment) || this._isImage(attachment);
	};

	_isPdf = (attachment) => {
		return attachment.mimeType != null && attachment.mimeType === "application/pdf";
	};

	_isImage = (attachment) => {
		return attachment.mimeType != null && attachment.mimeType.startsWith("image/");
	};

	_renderNotRecognizedFormat = (attachment) => {
		const { classes } = this.props;

		const notAvailable = this.translate("No preview available");
		const downloadTitle = this.translate("Download");

	    return (
            <div style={{height:"100%", width:"100%"}} className="layout vertical flex">
                <div style={{height:"50px", background:"#26282B"}}></div>
                <div style={{background:"#414447"}} className="layout vertical flex">
                    <div className={classnames(classes.file, "layout vertical center-center")}>
                        <div style={{padding:"50px 70px",background:"#4C494C",borderRadius:"10px",fontSize:"12pt",boxShadow:"3px 3px 25px black"}} className="layout vertical center-center">
                            <div style={{marginBottom:"10px",fontSize:"15pt",color:"white"}}>{notAvailable}</div>
                            <Button variant="contained" color="primary" onClick={this._downloadAttachment.bind(this, attachment)}><SaveAltIcon style={{marginRight:"5px"}}/>{downloadTitle}</Button>
                        </div>
                    </div>
                </div>
            </div>
	    );
	};

	_renderPdf = (attachment) => {
		const { classes } = this.props;
		var url = this._attachmentUrl(attachment);

		const notSupportedMessage = this.translate("It appears your application is not configured to display PDF files. No worries, just");
		const notSupportedLinkMessage = this.translate("click here to download file");

	    return (
            <object style={{width:"420px",height:"350px"}} className={classes.fileValue} data={url} type="application/pdf" download={attachment.filename}>
                <a href={url}>{attachment.filename}</a>
                <div className="layout horizontal center-center">
                    <p>{notSupportedMessage}</p>&nbsp;<a href={url} target="_blank">{notSupportedLinkMessage}</a>
                </div>
            </object>
	    );
	}

	_renderImage = (attachment) => {
		const { classes } = this.props;
		const downloadTitle = this.translate("Download");
		const imageId = this.props.id + "_" + Math.random();
		window.setTimeout(() => this._adjustImageSize(imageId), 10);
		return (
		    <div style={{width:'100%',height:'100%'}} ref={this.attachmentContainer}>
                <div style={{height:"100%", width:"100%"}} className="layout vertical flex">
                    <div style={{height:"50px", background:"#26282B"}} className="layout horizontal start-justified">
                        <Button style={{margin:'8px'}} variant="contained" color="primary" onClick={this._downloadAttachment.bind(this, attachment)}><SaveAltIcon style={{marginRight:"5px"}}/>{downloadTitle}</Button>
                    </div>
                    <div style={{background:"#414447"}} className="layout vertical flex">
                        <div className={classnames(classes.file, "layout vertical center-center")}>
                            <div className="layout vertical center-center">
                                <img id={imageId} src={attachment.url} title={attachment.filename} style={{display:"none"}}/>
                            </div>
                        </div>
                    </div>
                </div>
		    </div>
        );
    }

	_adjustImageSize = (imageId) => {
	    const width = this.attachmentContainer.current.offsetWidth - 20;
	    const height = this.attachmentContainer.current.offsetHeight - 20;
	    const element = document.getElementById(imageId);
	    element.style.display = "block";
	    element.style.width = width + "px";
	    if (element.offsetHeight > height) {
	        element.style.width = "100%";
	        element.style.height = height + "px";
	    }
	};

	_downloadAttachment = (attachment) => {
        var link = document.createElement('a');
        if (typeof link.download === 'string') {
            document.body.appendChild(link); // Firefox requires the link to be in the body
            link.download = attachment.filename;
            link.target = "_blank";
            link.href = attachment.url;

            link.click();
            document.body.removeChild(link); // remove the link when done
        } else {
            location.replace(attachment.url);
        }
	};

    _attachmentUrl = (attachment) => {
        var url = attachment.url;
        url += (url.indexOf("?") != -1 ? "&" : "?") + "embedded=true";
        url += "&label=" + attachment.filename;
        return url;
    };

    handleAttachmentFileChange = (file, name) => {
        const attachment = this.state.attachment;
        attachment.file = { value: file, name: name };
		this.requester.uploadAttachment(file);
		this.setState({ attachment });
    };

    handleAttachmentDescriptionChange = (e) => {
        const attachment = this.state.attachment;
        attachment.description = e.target.value;
		this.setState({ attachment });
    };

    handleMessageChange = (e) => {
        this.setState({message: e.target.value});
    };

    handleMessageKeyUp = (e) => {
        if (e.keyCode != 13) return;
        this.sendMessage(e.target.value);
    };

    sendMessage = (message, displayMessage) => {
        if (message == null || message == "") return;
        this.setState({message: "", loading: true});
        this.requester.sendMessage({message: message, displayMessage: displayMessage});
    };

    width = () => {
		return this._widthDefined() ? this.props.width : "600px";
    };

    height = () => {
		return this._heightDefined() ? this.props.height : "500px";
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Chat));
DisplayFactory.register("Chat", withStyles(styles, { withTheme: true })(withSnackbar(Chat)));