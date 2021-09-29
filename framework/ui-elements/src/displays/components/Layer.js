import React from "react";
import { Dialog as MuiDialog, DialogContent, Slide, Typography, AppBar, IconButton } from "@material-ui/core"
import { Home, Close } from "@material-ui/icons";
import { withStyles } from '@material-ui/core/styles';
import AbstractLayer from "../../../gen/displays/components/AbstractLayer";
import LayerNotifier from "../../../gen/displays/notifiers/LayerNotifier";
import LayerRequester from "../../../gen/displays/requesters/LayerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({
    header : {
        padding: "2px 15px",
    },
    content : {
        marginTop: "61px"
    },
    icon : {
        color: "white"
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
            homeAvailable: false,
            opened: false,
        };
	};

	static Transition = React.forwardRef(function Transition(props, ref) {
		return <Slide direction="up" ref={ref} {...props} />;
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
					   TransitionComponent={Layer.Transition}
                       aria-labelledby={this.props.id + "_draggable"}>
				{this.renderTitle()}
				{this.renderContent()}
			</MuiDialog>
		);
	};

	renderTitle = () => {
		const { classes } = this.props;
		const style = this.props.color != null ? { backgroundColor: this.props.color } : undefined;
		const homeAvailable = this.state.homeAvailable;
		return (
			<AppBar style={style} className={classes.header}>
				<div className="layout horizontal flex center">
					{homeAvailable &&
					    <React.Fragment>
					        <IconButton onClick={this.handleShowHome.bind(this)} className={classes.icon} style={{marginRight:'10px'}}><Home fontSize="large"/></IconButton>
					        <Typography variant="h5">{this.translate(this.state.title)}</Typography>
					    </React.Fragment>
                    }
					{!homeAvailable && <Typography variant="h5">{this.translate(this.state.title)}</Typography>}
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
		this.requester.close();
	};

	handleShowHome = () => {
		this.requester.home();
	};

	refreshTitle = (title) => {
		this.setState({title});
	};

	refreshHome = (value) => {
		this.setState({homeAvailable:value});
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Layer));
DisplayFactory.register("Layer", withStyles(styles, { withTheme: true })(withSnackbar(Layer)));