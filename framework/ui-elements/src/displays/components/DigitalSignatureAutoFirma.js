import React from "react";
import { Snackbar, Dialog, DialogTitle, DialogContent, DialogActions, Button, IconButton, List, ListItem, ListItemText } from "@material-ui/core";
import { Close } from "@material-ui/icons";
import { withStyles } from '@material-ui/core/styles';
import AbstractDigitalSignatureAutoFirma from "../../../gen/displays/components/AbstractDigitalSignatureAutoFirma";
import DigitalSignatureAutoFirmaNotifier from "../../../gen/displays/notifiers/DigitalSignatureAutoFirmaNotifier";
import DigitalSignatureAutoFirmaRequester from "../../../gen/displays/requesters/DigitalSignatureAutoFirmaRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import AutoFirmaBehavior from "./autofirma/AutoFirmaBehavior";
import StringUtil from 'alexandria-ui-elements/src/util/StringUtil';

const styles = theme => ({});

class DigitalSignatureAutoFirma extends AbstractDigitalSignatureAutoFirma {

	constructor(props) {
		super(props);
		this.notifier = new DigitalSignatureAutoFirmaNotifier(this);
		this.requester = new DigitalSignatureAutoFirmaRequester(this);
		this.behavior = new AutoFirmaBehavior(this);
		this.initialized = false;
        this.checkProtocol = false;
		this.state = {
		    ...this.state,
		    format: "XAdES",
		    open: false,
		    openDownload: false,
		    signing: false,
		};
	};

	componentDidMount() {
        this.behavior.initSignatory();
        this.initialized = true;
        this.checkProtocol = false;
	};

    render() {
        const signing = this.state.signing;
        const disabled = this.state.data == null || this.state.signing;
        return (
            <React.Fragment>
                <div layout="horizontal" style={this.style()}>
                    <Button disabled={disabled} variant="outlined" size="small" onClick={this.handleSign.bind(this)}>{this.translate(signing ? "Signing..." : "Sign")}</Button>
                </div>
                <Snackbar
                  open={this.state.open}
                  onClose={this.handleClose.bind(this)}
                  message={this.translate("@firma application not found in your computer.")}
                  action={this.renderToolbar()}
                />
                {this.renderDownloadDialog()}
            </React.Fragment>
        );
    };

    setup = (data) => {
        this.behavior.setDownloadUrl(data.downloadUrl);
        this.behavior.setStorageUrl(data.storageUrl);
        this.behavior.setRetrieveUrl(data.retrieveUrl);
    };

    format = (format) => {
        this.setState({ format });
    };

    renderDownloadDialog = () => {
        return (
            <Dialog open={this.state.openDownload} onClose={this.closeDownloadDialog.bind(this)}>
                <DialogTitle id="alert-dialog-title">{this.translate("Select linux distribution")}</DialogTitle>
                <DialogContent>
                    <List component="nav">
                        <ListItem button onClick={this.handleDownloadDistribution.bind(this, "linux_deb")}><ListItemText>Debian</ListItemText></ListItem>
                        <ListItem button onClick={this.handleDownloadDistribution.bind(this, "linux_rpm")}><ListItemText>Red Hat</ListItemText></ListItem>
                    </List>
                </DialogContent>
                <DialogActions>
                  <Button onClick={this.closeDownloadDialog.bind(this)} color="primary">{this.translate("Close")}</Button>
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

    handleSign = () => {
        this.setState({ signing: true });
        if (this.state.isDocument) this._signDocument();
        else this._sign();
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

    _sign = () => {
        this.behavior.sign(this.state.data, this.state.format, this._successCallback.bind(this), this._failureCallback.bind(this));
    };

    _signDocument = () => {
        this.behavior.signDocument(this.state.data, this._successCallback.bind(this), this._failureCallback.bind(this));
    };

    _successCallback = (signature, certificate) => {
        this.requester.success({signature: signature, certificate: certificate});
        this.setState({ signing: false });
    };

    _failureCallback = (errorCode, errorMessage) => {
        this.requester.failure({ code: errorCode, message: errorMessage});
        this.setState({ signing: false });
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(DigitalSignatureAutoFirma));
DisplayFactory.register("DigitalSignatureAutoFirma", withStyles(styles, { withTheme: true })(withSnackbar(DigitalSignatureAutoFirma)));