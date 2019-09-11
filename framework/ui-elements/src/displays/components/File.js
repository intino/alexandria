import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileNotifier from "../../../gen/displays/notifiers/FileNotifier";
import FileRequester from "../../../gen/displays/requesters/FileRequester";
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import 'alexandria-ui-elements/res/styles/layout.css';
import Block from "./Block";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Typography from '@material-ui/core/Typography';

const styles = theme => ({
	label: {
		color: theme.palette.grey.primary,
		marginRight: "5px"
	},
	value: {
		minHeight: "300px",
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
		const { classes } = this.props;
		const file = this.state.value != null ? this.state.value + (this.state.value.indexOf("?") != -1 ? "&" : "?") + "embedded=true" : undefined;
		const variant = this.variant("body1");
		const label = ComponentBehavior.label(this.props);
		const labelBlock = (label !== undefined) ? <Typography variant={variant} className={classes.label}>{label}</Typography> : undefined;

		if (file === undefined) return (<React.Fragment/>);

		const notAvailable = this.translate("Preview not available");
		const notSupportedMessage = this.translate("It appears your application is not configured to display PDF files. No worries, just");
		const notSupportedLinkMessage = this.translate("click here to download the PDF file.");

		return (
			<Block layout="horizontal">
				{ labelBlock }
				{!this._isPdf() && <div style={this.style()} className={classNames(classes.message, "layout horizontal center-center")}><div>{notAvailable}</div></div> }
				{this._isPdf() &&
					<object className={classes.value} style={this.style()} data={file} type="application/pdf">
						<div className="layout horizontal center-center">
							<p>{notSupportedMessage}</p>&nbsp;<a href={file} target="_blank">{notSupportedLinkMessage}</a>
						</div>
					</object>
				}
			</Block>
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
DisplayFactory.register("File", withStyles(styles, { withTheme: true })(File));