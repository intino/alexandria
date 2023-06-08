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
        this.behavior.signBatch(documents, this._batchSuccessCallback.bind(this), this._failureCallback.bind(this));
    };

    _batchSuccessCallback = (signatures) => {
        this.success = true;
        this.requester.batchSuccess(this._readSignaturesResult(signatures));
        this.setState({ readonly: false });
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