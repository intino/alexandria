import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMicroSite from "../../../gen/displays/components/AbstractMicroSite";
import MicroSiteNotifier from "../../../gen/displays/notifiers/MicroSiteNotifier";
import MicroSiteRequester from "../../../gen/displays/requesters/MicroSiteRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import 'alexandria-ui-elements/res/styles/layout.css';
import {RiseLoader} from "react-spinners";
import { Button } from "@material-ui/core";

const styles = theme => ({
    download: {
        position:'absolute',
        right:'0',
        marginRight:'20px',
        marginTop:'10px',
    },
    downloadContent: {
        position:'absolute',
        right:'0',
        marginRight:'110px',
        marginTop:'10px',
    },
});

class MicroSite extends AbstractMicroSite {

	constructor(props) {
		super(props);
		this.notifier = new MicroSiteNotifier(this);
		this.requester = new MicroSiteRequester(this);
		this.state = {
		    content: null,
		    downloadVisible: this.props.downloadOperations.indexOf("DownloadMicrosite") !== -1,
		    downloadContentVisible: this.props.downloadOperations.indexOf("DownloadContent") !== -1,
		    ...this.state,
		};
	};

    render() {
        const style = "";
        const { classes, theme } = this.props;
        const downloadStyle = this.state.downloadVisible ? { display: 'block' } : { display: 'none' };
        const downloadContentStyle = this.state.downloadContentVisible ? { display: 'block' } : { display: 'none' };
        const downloadContentClassName = this.state.downloadVisible ? classes.downloadContent : classes.download;
        if (this.state.content == null) return (<div className="layout vertical flex center-center" style={{height:'100%',width:'100%'}}><RiseLoader color={theme.palette.secondary.main} loading={true}/></div>);
        return (
            <React.Fragment>
                <Button className={classes.download} style={{marginRight:'10px',...downloadStyle}} size="small" color="primary" onClick={this.handleDownload.bind(this)}>{this.translate("Download")}</Button>
                <Button className={downloadContentClassName} style={downloadContentStyle} size="small" color="primary" onClick={this.handleDownloadContent.bind(this)}>{this.translate("Download content")}</Button>
                <div style={{height:'100%'}} dangerouslySetInnerHTML={{__html: style + this.state.content}}></div>
            </React.Fragment>
        );
    };

    handleDownload = () => {
        this.requester.download();
    };

    handleDownloadContent = () => {
        this.requester.downloadContent();
    };

    renderPage = (content) => {
        this.setState({content: content});
    };

    renderPageNotFound = () => {
        this.setState({content : "<h1 style='height:100%' class='layout vertical flex center-center'>" + this.translate("Page not found") + "</h1>", downloadVisible: false});
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(MicroSite));
DisplayFactory.register("MicroSite", withStyles(styles, { withTheme: true })(withSnackbar(MicroSite)));