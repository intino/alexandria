import React from "react";
import {Typography, DialogTitle, AppBar, Slide, DialogContent, Paper} from "@material-ui/core"
import AbstractBaseDialog from "../../../gen/displays/components/AbstractBaseDialog";
import { IconButton } from "@material-ui/core";
import { Close } from "@material-ui/icons";
import 'alexandria-ui-elements/res/styles/layout.css';
import Draggable from 'react-draggable';

export const makeDraggable = (id, style, props) => {
  return (
    <Draggable handle={"#" + id + "_draggable"} cancel={'[class*="MuiDialogContent-root"]'}>
      <Paper {...props} style={style}/>
    </Draggable>
  );
}

export default class BaseDialog extends AbstractBaseDialog {
	state = {
		title: this.props.title,
		size: { height: this.props.height, width: this.props.width },
		opened: false,
		modal: this.props.modal,
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

	handleClose = () => {
		this.requester.close();
	};

	renderTitle = () => {
		const { classes } = this.props;
		const style = this.props.color != null ? { backgroundColor: this.props.color } : undefined;
		if (this.props.fullscreen) return (
			<AppBar style={style} className={classes.header}>
				<div className="layout horizontal flex center">
					<Typography variant="h5">{this.translate(this.state.title)}</Typography>
					<div className="layout horizontal end-justified flex"><IconButton onClick={this.handleClose.bind(this)} className={classes.icon}><Close fontSize="large"/></IconButton></div>
				</div>
			</AppBar>
		);
		return (<DialogTitle style={{cursor:'move'}} id={this.props.id + "_draggable"}>{this.translate(this.state.title)}</DialogTitle>);
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

	refreshSize = (size) => {
		this.setState({size});
	};

	sizeStyle = () => {
	    const result = {};
	    if (this._widthDefined()) {
	        result.width = this.state.size.width;
	        result.margin = "0 auto";
	    }
	    if (this._heightDefined()) result.height = this.state.size.height;
	    return result;
	}

	_widthDefined = () => {
	    return this.state.size.width != null && this.state.size.width.indexOf("-1") === -1;
	}

	_heightDefined = () => {
	    return this.state.size.height != null && this.state.size.height.indexOf("-1") === -1;
	}
}