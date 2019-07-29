import React from "react";
import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from "@material-ui/core";
import OptionSelector from "./OptionSelector";
import I18nComponent from "../../I18nComponent";

export default class DownloadDialog extends I18nComponent {

	state = {
		option: this.props.option
	};

	constructor(props) {
		super(props);
	};

	render = () => {
		return (<Dialog onClose={this.close} open={this.props.open}>
					<DialogTitle onClose={this.close}>{this.props.title}</DialogTitle>
					<DialogContent style={{position:"relative",overflow:"hidden"}}>{this.renderOptions()}</DialogContent>
					<DialogActions>
						<Button onClick={this.close} color="primary">{this.translate("Cancel")}</Button>
						<Button variant="contained" onClick={this.handleAccept} color="primary">{this.translate("OK")}</Button>
					</DialogActions>
				</Dialog>
		);
	};

	renderOptions = () => {
		return (<OptionSelector options={this.props.options} onSelect={this.handleOptionChange.bind(this)}/>);
	};

	handleAccept = () => {
		if (!this.valid()) return;
		if (this.props.onAccept != null) this.props.onAccept(this.state.option);
	};

	handleOptionChange(option) {
		this.setState({ option: option });
	};

	valid = () => {
		return this.state.option != null && this.state.option !== "";
	};

	close = () => {
		if (this.props.onClose != null) this.props.onClose();
	};

}