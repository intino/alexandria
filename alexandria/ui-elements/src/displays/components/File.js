import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileNotifier from "../../../gen/displays/notifiers/FileNotifier";
import FileRequester from "../../../gen/displays/requesters/FileRequester";
import {withStyles} from "@material-ui/core";
import classNames from 'classnames';
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	value: {
		minHeight: "100px",
		minWidth: "100px"
	},
	message : {
		color: theme.palette.secondary.main
	}
});

class File extends AbstractFile {
	state = {
		value : this.props.value,
		mimeType : null
	};

	constructor(props) {
		super(props);
		this.notifier = new FileNotifier(this);
		this.requester = new FileRequester(this);
	};

	render() {
		const file = this.state.value != null ? this.state.value + (this.state.value.indexOf("?") != -1 ? "&" : "?") + "embedded=true" : undefined;

		if (file === undefined) return (<React.Fragment/>);

		const { classes } = this.props;
		const notAvailable = this.translate("Preview not available");
		if (!this._isPdf()) return (<div style={this.style()} className={classNames(classes.message, "layout horizontal center-center")}><div>{notAvailable}</div></div>);

		const notSupportedMessage = this.translate("It appears your application is not configured to display PDF files. No worries, just");
		const notSupportedLinkMessage = this.translate("click here to download the PDF file.");
		return (
			<object className={classes.value} style={this.style()} data={file} type="application/pdf">
				<div className="layout horizontal center-center">
					<p>{notSupportedMessage}</p>&nbsp;<a href={file} target="_blank">{notSupportedLinkMessage}</a>
				</div>
			</object>
		);
	};

	_isPdf = () => {
		return this.state.mimeType != null && this.state.mimeType === "application/pdf";
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		result.width = this.props.width != null ? this.props.width : "100%";
		result.height = this.props.height != null ? this.props.height : "100%";
		return result;
	};

	refresh = (info) => {
		this.setState({ "value" : info.value, "mimeType": info.mimeType });
	};
}

export default withStyles(styles, { withTheme: true })(File);