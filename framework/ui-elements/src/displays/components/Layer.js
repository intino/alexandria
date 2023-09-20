import React, { Suspense } from "react";
import { Dialog as MuiDialog, DialogContent, Fade, Grow, Slide, Zoom, Typography, AppBar, IconButton } from "@material-ui/core"
import { Home, Close, NavigateBefore, NavigateNext } from "@material-ui/icons";
import { withStyles } from '@material-ui/core/styles';
import AbstractLayer from "../../../gen/displays/components/AbstractLayer";
import LayerNotifier from "../../../gen/displays/notifiers/LayerNotifier";
import LayerRequester from "../../../gen/displays/requesters/LayerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import history from "alexandria-ui-elements/src/util/History";

const LayerIcon = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./icon/MuiIcon"), 300));
	});
});

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
            toolbar: { home: { visible: false, enabled: false}, previous : { visible: false, enabled: false }, next : { visible: false, enabled: false }, customOperations: []},
            opened: false,
            closeAddress: Application.configuration.basePath,
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
		const showHome = this.state.toolbar.home.visible;
		const showPrevious = this.state.toolbar.previous.visible;
		const previousDisabled = !this.state.toolbar.previous.enabled;
		const showNext = this.state.toolbar.next.visible;
		const nextDisabled = !this.state.toolbar.next.enabled;
		return (
			<AppBar style={style} className={classes.header}>
				<div className="layout horizontal flex center">
					{showHome && <a onClick={this.handleShowHome.bind(this)} className={classes.link}><Typography variant="h5">{this.translate(this.state.title)}</Typography></a>}
					{!showHome && <Typography variant="h5">{this.translate(this.state.title)}</Typography>}
					{showPrevious && <IconButton onClick={this.handlePrevious.bind(this)} disabled={previousDisabled} className={classes.icon} style={{marginLeft:'10px'}}><NavigateBefore fontSize="large"/></IconButton>}
					{showNext && <IconButton onClick={this.handleNext.bind(this)} disabled={nextDisabled} className={classes.icon}><NavigateNext fontSize="large"/></IconButton>}
					<div className="layout horizontal end-justified flex">
					    {this.renderCustomOperations()}
					    <IconButton onClick={this.handleClose.bind(this)} className={classes.icon}><Close fontSize="large"/></IconButton>
                    </div>
				</div>
			</AppBar>
		);
	};

	renderCustomOperations = () => {
	    const operations = this.state.toolbar.customOperations;
	    const result = [];
	    for (let i=0; i<operations.length; i++) result.push(this.renderCustomOperation(operations[i]));
	    return result;
	};

	renderCustomOperation = (operation) => {
        const { classes } = this.props;
	    return (
            <Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
	            <IconButton onClick={this.handleExecuteOperation.bind(this, operation.name)} className={classes.icon}><LayerIcon aria-label={operation.name} icon={operation.icon} fontSize="large"/></IconButton>
	        </Suspense>
        );
	};

	handleExecuteOperation = (name) => {
	    this.requester.execute(name);
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
		this.setState({ opened: true, closeAddress: document.location.pathname });
	};

	close = () => {
		this.setState({ opened: false });
	};

	handleClose = () => {
        history.stopListening();
	    if (this.state.closeAddress !== document.location.pathname) history.push(this.state.closeAddress, {});
	    else history.push(Application.configuration.basePath, {});
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