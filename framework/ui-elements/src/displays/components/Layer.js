import React, {Suspense} from "react";
import {
	AppBar,
	Dialog as MuiDialog,
	DialogContent,
	Fade,
	Grow,
	IconButton,
	Slide,
	Typography,
	Zoom
} from "@mui/material"
import {Close, NavigateBefore, NavigateNext} from "@mui/icons-material";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractLayer from "../../../gen/displays/components/AbstractLayer";
import LayerNotifier from "../../../gen/displays/notifiers/LayerNotifier";
import LayerRequester from "../../../gen/displays/requesters/LayerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import history from "alexandria-ui-elements/src/util/History";
import {containerPalette, dialogPaperStyles} from "./ContainerStyles";
import {linkPalette} from "./ThemeTokens";
import Theme from "app-elements/gen/Theme";

const LayerIcon = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./icon/MuiIcon"), 300));
	});
});

const styles = theme => ({
    header : {
        padding: "10px 18px",
        background: containerPalette(theme).surfaceMuted,
        color: containerPalette(theme).title,
        boxShadow: "none",
        borderBottom: `1px solid ${containerPalette(theme).headerBorder}`,
        borderTopLeftRadius: "0",
        borderTopRightRadius: "0",
    },
    contentWithHeader : {
        overflow: "hidden",
        marginTop: "74px",
    },
    content : {
        padding: "0 !important",
        background: theme.palette.mode === "dark" ? `${containerPalette(theme).surface} !important` : "#f6f8fb",
    },
    link : {
        cursor: "pointer",
        color: linkPalette(theme).color,
        textDecoration: "none",
        "&:hover": {
            color: linkPalette(theme).hoverColor,
        },
    },
    icon : {
        cursor: "pointer",
        color: containerPalette(theme).text,
    },
    title: {
        fontWeight: 600,
        letterSpacing: "-0.01em",
        color: containerPalette(theme).title,
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
		const runtimeTheme = Theme.get() || this.props.theme;
		const isDark = runtimeTheme != null && runtimeTheme.palette != null && runtimeTheme.palette.mode === "dark";
		const palette = containerPalette(runtimeTheme);
		const slotProps = isDark ? {
			paper: {
				style: {
					...dialogPaperStyles(runtimeTheme),
					borderTopLeftRadius: "0",
					borderTopRightRadius: "0",
				},
			},
			backdrop: {
				style: {
					backgroundColor: palette.backdrop,
					backdropFilter: "blur(8px) saturate(120%)",
				},
			},
		} : undefined;
		return (
			<MuiDialog fullScreen={true} open={this.state.opened}
					   onClose={this.handleClose.bind(this)}
					   slots={{ transition: this._transition() }}
					   slotProps={slotProps}
                       aria-labelledby={this.props.id + "_draggable"}>
				{this.renderHeader()}
				{this.renderContent()}
			</MuiDialog>
		);
	};

	renderHeader = () => {
	    if (!this._showHeader()) return (<React.Fragment/>);
		const { classes } = this.props;
		const theme = Theme.get() || this.props.theme;
		const palette = containerPalette(theme);
		const style = this.props.color != null ? { backgroundColor: this.props.color } : { background: palette.surfaceMuted, color: palette.title };
		const showHome = this.state.toolbar.home.visible;
		const showPrevious = this.state.toolbar.previous.visible;
		const previousDisabled = !this.state.toolbar.previous.enabled;
		const showNext = this.state.toolbar.next.visible;
		const nextDisabled = !this.state.toolbar.next.enabled;
		return (
			<AppBar style={style} className={classes.header}>
				<div className="layout horizontal flex center">
					{showHome && <a onClick={this.handleShowHome.bind(this)} className={classes.link}><Typography variant="h5" className={classes.title}>{this.translate(this.state.title)}</Typography></a>}
					{!showHome && <Typography variant="h5" className={classes.title}>{this.translate(this.state.title)}</Typography>}
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
		const classList = this._showHeader() ? classes.contentWithHeader : classes.content;
		return (
		    <DialogContent className={classList} style={this.style()}>
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

    _showHeader = () => {
        return this.props.showHeader != null && this.props.showHeader === "true";
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Layer));
DisplayFactory.register("Layer", withStyles(styles, { withTheme: true })(withSnackbar(Layer)));
