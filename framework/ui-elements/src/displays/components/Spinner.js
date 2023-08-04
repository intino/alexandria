import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { withSnackbar } from 'notistack';
import AbstractSpinner from "../../../gen/displays/components/AbstractSpinner";
import SpinnerNotifier from "../../../gen/displays/notifiers/SpinnerNotifier";
import SpinnerRequester from "../../../gen/displays/requesters/SpinnerRequester";
import { BarLoader, CircleLoader, HashLoader, RingLoader, RiseLoader } from "react-spinners";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({});

class Spinner extends AbstractSpinner {

	constructor(props) {
		super(props);
		this.notifier = new SpinnerNotifier(this);
		this.requester = new SpinnerRequester(this);
		this.state = {
		    ...this.state,
		}
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const mode = this.props.mode != null ? this.props.mode : "Rise";
		const color = this.color();

		if (mode === "Bar") return (<BarLoader color={color} width={this._size(300)} loading={true}/>);
		else if (mode === "Circle") return (<CircleLoader size={this._size(50)} color={color} loading={true}/>);
		else if (mode === "Hash") return (<HashLoader size={this._size(50)} color={color} loading={true}/>);
		else if (mode === "Ring") return (<RingLoader size={this._size(60)} color={color} loading={true}/>);
		else if (mode === "Rise") return (<RiseLoader size={this._size(15)} color={color} loading={true}/>);
	};

	_size = (defaultSize) => {
	    return this.props.size != 0 && this.props.size !== undefined ? this.props.size : defaultSize;
	};

	color = () => {
		if (this.props.color == null || this.props.color === "") return undefined;
		if (this.props.color === "primary") return Theme.get().palette.primary.main;
		else if (this.props.color === "secondary") return Theme.get().palette.secondary.main;
		return this.props.color;
	}

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Spinner));
DisplayFactory.register("Spinner", withStyles(styles, { withTheme: true })(withSnackbar(Spinner)));