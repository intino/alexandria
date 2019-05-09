import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDownload from "../../../gen/displays/components/AbstractDownload";
import DownloadNotifier from "../../../gen/displays/notifiers/DownloadNotifier";
import DownloadRequester from "../../../gen/displays/requesters/DownloadRequester";
import DownloadDialog from "./operation/DownloadDialog";
import Operation from "./Operation";
import { withSnackbar } from 'notistack';

const styles = theme => ({...Operation.Styles(theme)});

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
		const operation = this.renderOperation();
		return (<React.Fragment>{this.renderDownloadDialog()}{operation}</React.Fragment>);
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