import React from "react";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    IconButton,
    List,
    ListItemButton,
    ListItemText,
    Snackbar
} from "@mui/material";
import {Close} from "@mui/icons-material";
import AbstractSignAction from "../../../gen/displays/components/AbstractSignAction";
import AutoFirmaBehavior from "./autofirma/AutoFirmaBehavior";
import {dialogActionButtonStyles} from "./ButtonStyles";

const styles = theme => ({});

export default class SignAction extends AbstractSignAction {

	constructor(props) {
		super(props);
		this.behavior = new AutoFirmaBehavior(this);
		this.initialized = false;
        this.checkProtocol = false;
		this.state = {
		    ...this.state,
		    data: null,
		    isDocument: false,
		    readonly: false,
		    signMode: "Sign",
		    signFormat: "XAdES",
		    open: false,
		    openDownload: false,
		};
	};

	componentDidMount() {
        this.behavior.initSignatory();
        this.initialized = true;
        this.checkProtocol = false;
	};

	renderActionable = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		return (
			<React.Fragment>
                {this.renderTraceConsent()}
				{this.renderAffirmed()}
				{this.renderSign()}
				{this.renderTrigger()}
                {this.renderAfirmaNotFound()}
                {this.renderDownloadDialog()}
			</React.Fragment>
		);
	};

    renderAfirmaNotFound = () => {
        return (
            <Snackbar
              open={this.state.open}
              onClose={this.handleClose.bind(this)}
              message={this.translate("@firma application not found in your computer.")}
              action={this.renderToolbar()}
            />
        );
    };

    setup = (data) => {
        this.behavior.setDownloadUrl(data.downloadUrl);
        this.behavior.setStorageUrl(data.storageUrl);
        this.behavior.setRetrieveUrl(data.retrieveUrl);
        this.behavior.setBatchPreSignerUrl(data.batchPreSignerUrl);
        this.behavior.setBatchPostSignerUrl(data.batchPostSignerUrl);
    };

    format = (format) => {
        this.setState({ signFormat: format });
    };

    mode = (mode) => {
        this.setState({ signMode: mode });
    };

    refreshReadonly = (value) => {
        this.setState({readonly: value});
    };

    renderDownloadDialog = () => {
        return (
            <Dialog open={this.state.openDownload} onClose={this.closeDownloadDialog.bind(this)}>
                <DialogTitle id="alert-dialog-title">{this.translate("Select linux distribution")}</DialogTitle>
                <DialogContent>
                    <List component="nav">
                        <ListItemButton onClick={this.handleDownloadDistribution.bind(this, "linux_deb")}><ListItemText>Debian</ListItemText></ListItemButton>
                        <ListItemButton onClick={this.handleDownloadDistribution.bind(this, "linux_rpm")}><ListItemText>Red Hat</ListItemText></ListItemButton>
                    </List>
                </DialogContent>
                <DialogActions>
                  <Button sx={dialogActionButtonStyles} onClick={this.closeDownloadDialog.bind(this)} color="primary">{this.translate("Close")}</Button>
                </DialogActions>
            </Dialog>
        );
    };

    renderToolbar = () => {
        return (
            <React.Fragment>
              <Button color="secondary" size="small" onClick={this.handleDownload.bind(this)}>{this.translate("Download")}</Button>
              <Button color="secondary" size="small" onClick={this.handleRetry.bind(this)}>{this.translate("Check again")}</Button>
              <IconButton size="small" aria-label={this.translate("close")} color="inherit" onClick={this.handleClose.bind(this)}>
                <Close fontSize="small" />
              </IconButton>
            </React.Fragment>
        );
    };

    installedSuccessCallback = () => {
        console.log("Signatory application found!");
        this.closeInstallDialog();
    };

    installedFailureCallback = (code, message, intention) => {
        console.log("Signatory application not found!");
        this.openInstallDialog();
        this.intention = intention;
    };

    openInstallDialog = () => {
        this.setState({open:true});
    };

    closeInstallDialog = () => {
        this.setState({open:false});
    };

    openDownloadDialog = () => {
        this.setState({openDownload:true});
    };

    closeDownloadDialog = () => {
        this.setState({openDownload:false});
    };

    handleClose = () => {
        this.closeInstallDialog();
        this.intention = null;
    };

    handleRetry = () => {
        var display = this;
        this.closeInstallDialog();

        this.behavior.checkSignatoryAppInstalled(function() {
            display.intention.successCallback();
        }, function(code, message) {
            display.installedFailureCallback(code, message, widget.intention);
        });
    };

    handleDownload = () => {
        this.behavior.downloadClient();
    };

    handleDownloadDistribution = (distribution) => {
        this.behaviour.doDownloadClientDistribution(distribution);
        this.closeDownloadDialog();
    };

    _successCallback = (signature, certificate) => {
        this.success = true;
        this.requester.success({signature: signature, certificate: certificate});
        this.setState({ readonly: false });
    };

    _failureCallback = (errorCode, errorMessage) => {
        if (errorCode == "java.util.concurrent.TimeoutException") return;
        this.requester.failure({ code: errorCode, message: errorMessage});
        this.setState({ readonly: false });
    };

}
