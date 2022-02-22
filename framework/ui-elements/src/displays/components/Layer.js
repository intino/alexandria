import React from "react";
import { Dialog as MuiDialog, DialogContent, Fade, Grow, Slide, Zoom, Typography, AppBar, IconButton } from "@material-ui/core"
import { Home, Close, NavigateBefore, NavigateNext } from "@material-ui/icons";
import { withStyles } from '@material-ui/core/styles';
import AbstractLayer from "../../../gen/displays/components/AbstractLayer";
import LayerNotifier from "../../../gen/displays/notifiers/LayerNotifier";
import LayerRequester from "../../../gen/displays/requesters/LayerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import history from "alexandria-ui-elements/src/util/History";

const styles = theme => ({
    header : {
        padding: "2px 15px",
    },
    content : {
        marginTop: "61px",
    },
    link : {
        cursor: "pointer",
    },
    icon : {
        cursor: "pointer",
        color: "white",
    },
});

class Layer extends AbstractLayer {

	constructor(props) {
		super(props);
		this.notifier = new LayerNotifier(this);
		this.requester = new LayerRequester(this);
        this.state = {
            ...this.state,
            title: this.props.title,
            toolbar: { homeButton: { visible: false, enabled: false}, previousButton : { visible: false, enabled: false }, nextButton : { visible: false, enabled: false }},
            opened: false,
            closeAddress: document.location.pathname,
        };
	};

    static FadeTransition = React.forwardRef(function Transition(props, ref) {
        return <Fade ref={ref} {...props} />;
    });

    static GrowTransition = React.forwardRef(function Transition(props, ref) {
        return <Grow ref={ref} {...props} />;
    });

    static SlideTransition = React.forwardRef(function Transition(props, ref) {
        return <Slide direction="up" ref={ref} {...props} />;
    });

    static ZoomTransition = React.forwardRef(function Transition(props, ref) {
        return <Zoom ref={ref} {...props} />;
    });

	render() {
		return this.renderLayer();
	};

	renderLayer = () => {
		return (
			<MuiDialog fullScreen={true} open={this.state.opened}
					   onClose={this.handleClose.bind(this)}
					   disableBackdropClick={false}
					   disableEscapeKeyDown={false}
					   TransitionComponent={this._transition()}
                       aria-labelledby={this.props.id + "_draggable"}>
				{this.renderTitle()}
				{this.renderContent()}
			</MuiDialog>
		);
	};

	renderTitle = () => {
		const { classes } = this.props;
		const style = this.props.color != null ? { backgroundColor: this.props.color } : undefined;
		const showHome = this.state.toolbar.homeButton.visible;
		const showPrevious = this.state.toolbar.previousButton.visible;
		const previousDisabled = !this.state.toolbar.previousButton.enabled;
		const showNext = this.state.toolbar.nextButton.visible;
		const nextDisabled = !this.state.toolbar.nextButton.enabled;
		return (
			<AppBar style={style} className={classes.header}>
				<div className="layout horizontal flex center">
					{showHome && <a onClick={this.handleShowHome.bind(this)} className={classes.link}><Typography variant="h5">{this.translate(this.state.title)}</Typography></a>}
					{!showHome && <Typography variant="h5">{this.translate(this.state.title)}</Typography>}
					{showPrevious && <IconButton onClick={this.handlePrevious.bind(this)} disabled={previousDisabled} className={classes.icon} style={{marginLeft:'10px'}}><NavigateBefore fontSize="large"/></IconButton>}
					{showNext && <IconButton onClick={this.handleNext.bind(this)} disabled={nextDisabled} className={classes.icon}><NavigateNext fontSize="large"/></IconButton>}
					<div className="layout horizontal end-justified flex"><IconButton onClick={this.handleClose.bind(this)} className={classes.icon}><Close fontSize="large"/></IconButton></div>
				</div>
			</AppBar>
		);
	};

	renderContent = () => {
		const { classes } = this.props;
		return (
		    <DialogContent className={classes.content} style={this.style()}>
                {this.renderInstances(null, null, {height:'100%',...this.style()})}
            </DialogContent>
        );
	};

	open = () => {
		this.setState({ opened: true });
	};

	close = () => {
		this.setState({ opened: false });
	};

	handleClose = () => {
        history.stopListening();
	    if (this.state.closeAddress !== document.location.pathname) history.push(this.state.closeAddress, {});
	    history.continueListening();
		this.requester.close();
	};

	handleShowHome = () => {
		this.requester.home();
	};

	handlePrevious = () => {
		this.requester.previous();
	};

	handleNext = () => {
		this.requester.next();
	};

	refreshTitle = (title) => {
		this.setState({title});
	};

	refreshToolbar = (value) => {
		this.setState({toolbar:value});
	};

    _transition = () => {
        if (this.props.transition === "Grow") return Layer.GrowTransition;
        else if (this.props.transition === "Fade") return Layer.FadeTransition;
        else if (this.props.transition === "Zoom") return Layer.ZoomTransition;
        return Layer.SlideTransition;
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Layer));
DisplayFactory.register("Layer", withStyles(styles, { withTheme: true })(withSnackbar(Layer)));