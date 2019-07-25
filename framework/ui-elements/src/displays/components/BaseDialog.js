import React from "react";
import {Typography, DialogTitle, AppBar, Slide, DialogContent} from "@material-ui/core"
import AbstractBaseDialog from "../../../gen/displays/components/AbstractBaseDialog";

export default class BaseDialog extends AbstractBaseDialog {
	state = {
		opened: false
	};

	static Styles = theme => ({
		header : {
			padding: "20px 10px",
		},
		fullscreen : {
			marginTop: "70px"
		}
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
		this.close();
	};

	renderTitle = () => {
		const { classes } = this.props;
		const style = this.props.color != null ? { backgroundColor: this.props.color } : undefined;
		if (this.props.fullscreen) return (<AppBar style={style} className={classes.header}><Typography variant="h5">{this.props.title}</Typography></AppBar>);
		return (<DialogTitle>{this.props.title}</DialogTitle>);
	};

	renderContent = (content) => {
		const { classes } = this.props;
		return (<DialogContent className={this.props.fullscreen && classes.fullscreen} style={this.style()}>{content != null && content()}</DialogContent>);
	}

	open = () => {
		this.setState({ opened: true });
	};

	close = () => {
		this.setState({ opened: false });
	};

}