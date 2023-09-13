import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSignDocument from "../../../gen/displays/components/AbstractSignDocument";
import SignDocumentNotifier from "../../../gen/displays/notifiers/SignDocumentNotifier";
import SignDocumentRequester from "../../../gen/displays/requesters/SignDocumentRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Actionable from "./Actionable";
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class SignDocument extends AbstractSignDocument {

	constructor(props) {
		super(props);
		this.notifier = new SignDocumentNotifier(this);
		this.requester = new SignDocumentRequester(this);
	};

	document = (documentData) => {
	    this.setState({ data: documentData });
	};

    sign = (content) => {
        this.requester.signing();
        this.behavior.signDocument(content, this._successCallback.bind(this), this._failureCallback.bind(this));
    };

    signBatch = (documents) => {
        this.requester.signing();
        this._reset();
        this.documentsToSign = documents;
        this.behavior.signDocument(this.documentsToSign[this.signIndex].url, this._batchDocumentSignedCallback.bind(this), this._batchFailureCallback.bind(this));
    };

    _batchDocumentSignedCallback = (signature, certificate) => {
        this.signedDocuments[this.signIndex] = { signature: signature, certificate: certificate };
        this.signIndex++;
        if (this.signIndex >= this.documentsToSign.length) {
            this._batchSuccess();
            return;
        }
        this.behavior.signDocument(this.documentsToSign[this.signIndex].url, this._batchDocumentSignedCallback.bind(this), this._batchFailureCallback.bind(this));
    }

    _batchSuccess = () => {
        this.requester.batchSuccess(this._signatures());
        this._reset();
        this.setState({ readonly: false });
    };

    _batchFailureCallback = (errorCode, errorMessage) => {
        this._failureCallback(errorCode, errorMessage);
        this._reset();
    };

    _reset = () => {
        this.signIndex = 0;
        this.documentsToSign = [];
        this.signedDocuments = [];
    };

    _signatures = () => {
        const result = [];
        for (let i=0; i<this.documentsToSign.length; i++) {
            const signedDocument = this.signedDocuments[i] != null ? this.signedDocuments[i] : { signature: null, certificate: null };
            const info = { id: this.documentsToSign[i].id, signature: signedDocument.signature, certificate: signedDocument.certificate };
            result.push(info);
        }
        return result;
    };

    _readSignaturesResult = (content) => {
        const node = new DOMParser().parseFromString(content, "application/xml");
        const signNodeResults = node.childNodes[0].childNodes;
        const result = [];
        for (let i=0; i<signNodeResults.length; i++) {
            const id = signNodeResults[i].attributes["id"].value;
            const resultInfo = signNodeResults[i].attributes["result"].value;
            result.push({id: id, success: resultInfo === "DONE_AND_SAVED"});
        }
        return result;
    };

}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SignDocument));
DisplayFactory.register("SignDocument", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SignDocument)));