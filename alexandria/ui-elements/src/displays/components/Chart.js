import React, { Suspense } from "react";
import {CircularProgress, Typography, withStyles} from "@material-ui/core";
import AbstractChart from "../../../gen/displays/components/AbstractChart";
import ChartNotifier from "../../../gen/displays/notifiers/ChartNotifier";
import ChartRequester from "../../../gen/displays/requesters/ChartRequester";
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
	error: {
		color: theme.palette.error.main,
		margin: "10px 0"
	}
});

const ChartPlotly = React.lazy(() => import("./chart/ChartPlotly"));

class Chart extends AbstractChart {
	state = {
		value : undefined,
		mode : "Html",
		loading : true,
		error : undefined
	};

	constructor(props) {
		super(props);
		this.notifier = new ChartNotifier(this);
		this.requester = new ChartRequester(this);
		this.container = React.createRef();
	};

	render() {
		const { classes } = this.props;
		const error = this.state.error;
		const value = this.state.value != undefined && this.state.value !== "" ? this.state.value : undefined;

		if (value != undefined && this.container.current != null)
			this.height = this.container.current.offsetHeight;

		if (error != undefined)
			return (<Typography style={this.style()} className={classes.error}>{error}</Typography>);

		return (
			<Suspense fallback={<div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }><CircularProgress/></div>}>
				<div style={this.style()} ref={this.container}>
					{this.state.loading ? <div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }><CircularProgress/></div> : undefined}
					{this.state.mode === "Image" && !this.state.loading && value != undefined ? <img style={ { width: this._width() } } src={"data:image/png;base64, " + value}></img> : undefined }
					{this.state.mode === "Html" && !this.state.loading && value != undefined ? <ChartPlotly data={value} width={this._width()}/> : undefined}
				</div>
			</Suspense>
		);
	};

	style() {
		var result = super.style();
		if (this.state.loading && this.height) result.height = this.height;
		else if (this._heightDefined()) result.height = this.props.height;
		return result;
	};

	showLoading = () => {
		this.setState({ "loading" : true, "error" : undefined });
	};

	refresh = (info) => {
		let data = null;

		if (info.mode === "Html") {
			let config = null;
			eval("config = " + info.config);
			data = config.data;
		}
		else data = info.config;

		const mode = info.mode;
		this.setState({ "mode": mode, "value": data, "loading": false, "error": undefined });
	};

	_width = () => {
		if (this._widthDefined()) return this.props.width;
		return this.container.current != null ? this.container.current.offsetWidth : "100%";
	};

	refreshError = (error) => {
		this.setState({ "error": error });
	};
}

export default withStyles(styles, { withTheme: true })(Chart);