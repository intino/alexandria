import React, { Suspense } from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDownload from "../../../gen/displays/components/AbstractDownload";
import DownloadNotifier from "../../../gen/displays/notifiers/DownloadNotifier";
import DownloadRequester from "../../../gen/displays/requesters/DownloadRequester";
import DownloadDialog from "./actionable/DownloadDialog";
import Actionable from "./Actionable";
import { withSnackbar } from 'notistack';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({...Actionable.Styles(theme)});

class Download extends AbstractDownload {

	constructor(props) {
		super(props);
		this.notifier = new DownloadNotifier(this);
		this.requester = new DownloadRequester(this);
		this.state = {
			...this.state,
			opened : false,
			option: this.props.options != null && this.props.options.length > 0 ? this.props.options[0] : "",
			canAccept: false
		};
	};

	render = () => {
		const actionable = this.renderActionable();
		return (
			<Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
				{this.renderDownloadDialog()}{actionable}
			</Suspense>
		);
	};

	renderDownloadDialog = () => {
		const opened = this.state.opened != null ? this.state.opened : false;
		return (<DownloadDialog open={opened} title={this._title()}
								options={this.props.options} option={this.state.option}
								onClose={this.handleDownloadClose.bind(this)}
								onAccept={this.handleDownloadAccept.bind(this)}/>);
	};

	handleClick(e) {
		const options = this._options();
		const option = options.length > 0 ? options[0] : "";
		this.requester.changeParams(option);
		if (options.length <= 0) this.execute();
		else this.setState({ option: option, opened: true });
	};

	handleDownloadClose = (option) => {
		this.setState({ opened: false });
	};

	handleDownloadAccept = (option) => {
		this.requester.changeParams(option);
		this.execute();
		this.setState({ opened: false });
	};

	_options = () => {
		return this.props.options != null ? this.props.options : [];
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Download));
DisplayFactory.register("Download", withStyles(styles, { withTheme: true })(withSnackbar(Download)));