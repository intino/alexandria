import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDocumentEditor from "../../../gen/displays/components/AbstractDocumentEditor";
import DocumentEditorNotifier from "../../../gen/displays/notifiers/DocumentEditorNotifier";
import DocumentEditorRequester from "../../../gen/displays/requesters/DocumentEditorRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

export default class DocumentEditor extends AbstractDocumentEditor {

	constructor(props) {
		super(props);
		this.notifier = new DocumentEditorNotifier(this);
		this.requester = new DocumentEditorRequester(this);
	};

    render() {
        return (<div>{this.translate("Indicate editor type by using facets in Konos file")}</div>);
    };

}