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
	state = {
		loading: true
	};

	constructor(props) {
		super(props);
		this.notifier = new SpinnerNotifier(this);
		this.requester = new SpinnerRequester(this);
	};

	render() {
		const mode = this.props.mode != null ? this.props.mode : "Rise";
		const color = this.color();
		if (mode === "Bar") return (<BarLoader color={color} loading={true}/>);
		else if (mode === "Circle") return (<CircleLoader color={color} loading={true}/>);
		else if (mode === "Hash") return (<HashLoader color={color} loading={true}/>);
		else if (mode === "Ring") return (<RingLoader color={color} loading={true}/>);
		else if (mode === "Rise") return (<RiseLoader color={color} loading={true}/>);
	};

	refreshLoading = (value) => {
		this.setState({ loading : value });
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