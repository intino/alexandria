import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDocumentEditorCollabora from "../../../gen/displays/components/AbstractDocumentEditorCollabora";
import DocumentEditorCollaboraNotifier from "../../../gen/displays/notifiers/DocumentEditorCollaboraNotifier";
import DocumentEditorCollaboraRequester from "../../../gen/displays/requesters/DocumentEditorCollaboraRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class DocumentEditorCollabora extends AbstractDocumentEditorCollabora {

	constructor(props) {
		super(props);
		this.notifier = new DocumentEditorCollaboraNotifier(this);
		this.requester = new DocumentEditorCollaboraRequester(this);
		this.state = {
			...this.state,
			editorUrl : this.props.editorUrl,
			accessToken : this.props.accessToken,
			documentUrl : null
		};
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		if (this.state.editorUrl == null) return (<div>{this.translate("Editor url is required. Define it by either using konos or inject it in component")}</div>);
		if (this.state.documentUrl == null) return (<div>{this.translate("Indicate document to edit")}</div>);
		const formId = this.props.id + "_form";
		const frameId = this.props.id + "_frame";
		const accessToken = this.state.accessToken;
		const holderId = this.props.id + "_holder";
		const action = this.state.editorUrl + "?WOPISrc=" + this.state.documentUrl + "&lang=es";
		if (this.launchTimer != null) window.clearTimeout(this.launchTimer);
		this.launchTimer = window.setTimeout(() => this._launchEditor(), 5);
	    return (
	        <div layout="vertical flex" style={{width:'100%',height:'100%'}}>
                <form id={formId} name={formId} target={frameId} action={action} method="post">
                    <input name="access_token" value={accessToken} type="hidden" />
                    <input name="access_token_ttl" value="31536000" type="hidden" />
                    <input name="ui_defaults" value="UIMode=compact;TextSidebar=false;TextRuler=false;TextStatusbar=false;" type="hidden"/>
                </form>
                <span id={holderId}></span>
	        </div>
	    );
	};

	refresh = (info) => {
	    this.setState({ editorUrl: info.editorUrl, documentUrl: info.documentUrl, accessToken: info.accessToken });
	};

	_launchEditor = () => {
        var frameHolder = document.getElementById(this.props.id + '_holder');
        var editorFrame = document.createElement('iframe');
        if (frameHolder == null || editorFrame == null) return;
        editorFrame.name = this.props.id + '_frame';
        editorFrame.id = this.props.id + '_frame';
        editorFrame.title = this.state.documentUrl;
        editorFrame.setAttribute('allowfullscreen', 'true');
        editorFrame.setAttribute('sandbox', 'allow-scripts allow-same-origin allow-forms allow-popups allow-top-navigation allow-popups-to-escape-sandbox');
        editorFrame.setAttribute('style', "width:100%;height:100%;border:0");
        frameHolder.appendChild(editorFrame);
        document.getElementById(this.props.id + '_form').submit();
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(DocumentEditorCollabora));
DisplayFactory.register("DocumentEditorCollabora", withStyles(styles, { withTheme: true })(withSnackbar(DocumentEditorCollabora)));