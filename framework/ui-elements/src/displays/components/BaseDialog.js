import React from "react";
import {Typography, DialogTitle, AppBar, Slide, DialogContent} from "@material-ui/core"
import AbstractBaseDialog from "../../../gen/displays/components/AbstractBaseDialog";
import { IconButton } from "@material-ui/core";
import { Close } from "@material-ui/icons";
import 'alexandria-ui-elements/res/styles/layout.css';

export default class BaseDialog extends AbstractBaseDialog {
	state = {
		title: this.props.title,
		opened: false,
		modal: false,
	};

	static Styles = theme => ({
		header : {
			padding: "2px 15px",
		},
		fullscreen : {
			marginTop: "61px"
		},
		icon : {
			color: "white"
		},
	});

	static Transition = React.forwardRef(function Transition(props, ref) {
		return <Slide direction="up" ref={ref} {...props} />;
	});

	constructor(props) {
		super(props);
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this._widthDefined() && !this.props.fullscreen) result.width = this.props.width;
		if (this._heightDefined() && !this.props.fullscreen) result.height = this.props.height;
		return result;
	};

	handleClose = () => {
		this.requester.close();
	};

	renderTitle = () => {
		const { classes } = this.props;
		const style = this.props.color != null ? { backgroundColor: this.props.color } : undefined;
		if (this.props.fullscreen) return (
			<AppBar style={style} className={classes.header}>
				<div className="layout horizontal flex center">
					<Typography variant="h5">{this.state.title}</Typography>
					<div className="layout horizontal end-justified flex"><IconButton onClick={this.handleClose.bind(this)} className={classes.icon}><Close fontSize="large"/></IconButton></div>
				</div>
			</AppBar>
		);
		return (<DialogTitle>{this.state.title}</DialogTitle>);
	};

	renderContent = (content) => {
		const { classes } = this.props;
		return (<DialogContent className={this.props.fullscreen && classes.fullscreen} style={this.style()}>{content != null && content()}</DialogContent>);
	};

	open = () => {
		this.setState({ opened: true });
	};

	close = () => {
		this.setState({ opened: false });
	};

	refreshTitle = (title) => {
		this.setState({title});
	};

}