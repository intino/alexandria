import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractLibraryTemplateStamp from "../../../gen/displays/components/AbstractLibraryTemplateStamp";
import LibraryTemplateStampNotifier from "../../../gen/displays/notifiers/LibraryTemplateStampNotifier";
import LibraryTemplateStampRequester from "../../../gen/displays/requesters/LibraryTemplateStampRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class LibraryTemplateStamp extends AbstractLibraryTemplateStamp {

	constructor(props) {
		super(props);
		this.notifier = new LibraryTemplateStampNotifier(this);
		this.requester = new LibraryTemplateStampRequester(this);
		this.state = {
		    ...this.state,
		    url : null
		}
	};

	refresh = (url) => {
	    this.setState({url});
	};

	render() {
	    if (!this.state.visible || this.state.url == null) return (<React.Fragment/>);
	    return (<iframe src={this.state.url} width="100%" height="100%" style={{border:"0"}}></iframe>);
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(LibraryTemplateStamp));
DisplayFactory.register("LibraryTemplateStamp", withStyles(styles, { withTheme: true })(withSnackbar(LibraryTemplateStamp)));