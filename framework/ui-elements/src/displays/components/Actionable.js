import React, { Suspense } from "react";
import AbstractActionable from "../../../gen/displays/components/AbstractActionable";
import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button, IconButton, Typography } from "@material-ui/core";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import TextField from '@material-ui/core/TextField';
import Delayer from 'alexandria-ui-elements/src/util/Delayer';
import StringUtil from 'alexandria-ui-elements/src/util/StringUtil';

const ActionableMui = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./icon/MuiIcon"), 300));
	});
});

export default class Actionable extends AbstractActionable {
    static Delay = 350;

	static Styles = theme => ({
		link : {
			color: theme.palette.primary.main,
			cursor: "pointer"
		},
		button : {
			cursor: "pointer"
		},
		iconButton : {
			cursor: "pointer"
		},
		materialIconButton : {
			cursor: "pointer"
		},
		readonly : {
			color: theme.palette.grey.A700,
			cursor: "default"
		},
	});

	constructor(props) {
		super(props);
		this.state = {
			...this.state,
			icon : this.props.icon,
			title: this.props.title,
			readonly: this.props.readonly,
			openAffirm : false,
			openSign : false,
			affirmed : this.props.affirmed != null ? this.props.affirmed : null,
			signInfo : {
			    sign: "",
			    reason: ""
			}
		}
	};

	render = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		return (
			<Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
				{this.renderActionable()}
			</Suspense>
		);
	};

	renderActionable = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		return (
			<React.Fragment>
                {this.renderTraceConsent()}
				{this.renderAffirm()}
				{this.renderSign()}
				{this.renderTrigger()}
			</React.Fragment>
		);
	};

	renderTrigger = () => {
		const mode = this.props.mode.toLowerCase();
		if (mode === "button") return this.renderButton();
		else if (mode === "iconbutton") return this.renderIconButton();
		else if (mode === "materialiconbutton") return this.renderMaterialIconButton();
		else if (mode === "avatariconbutton") return this.renderAvatarIconButton();
		return this.renderLink();
	};

	renderLink = () => {
		const {classes} = this.props;
		const className = this._readonly() ? classes.readonly : classes.link;
		return (
		    <a id={this.triggerId()} onClick={this.handleClick.bind(this)} disabled={this._readonly()}>
				<Typography style={this.style()} variant={this.variant("body1")} className={className}>{this._title()}</Typography>
			</a>
		);
	};

	renderButton = () => {
		const {classes} = this.props;
		return (
		    <Button id={this.triggerId()} style={this.style()} size={this._size()} color="primary" variant={this._highlightVariant()}
						disabled={this._readonly()} onClick={this.handleClick.bind(this)}
						className={classes.button}>
				{this.renderContent()}
			</Button>
		);
	};

	renderIconButton = () => {
		const {classes} = this.props;
		const style = this._readonly() ? { filter: "grayscale(100%)", ...this.style() } : this.style();
		const button = (
            <IconButton id={this.triggerId()} color="primary" disabled={this._readonly()}
                            onClick={this.handleClick.bind(this)} style={style}
                            className={classes.iconButton} size={this._size()}>
                {this.renderContent()}
            </IconButton>
        );
		return button;
	};

	renderMaterialIconButton = () => {
		const {classes} = this.props;
		const style = this.style();
		if (this.state.color != null) style.color = this.state.color;
		const button = (
            <IconButton id={this.triggerId()} color="primary" disabled={this._readonly()}
                            onClick={this.handleClick.bind(this)} className={classes.materialIconButton}
                            style={style} size={this._size()}>
                {this.renderContent()}
            </IconButton>
		);
		return button;
	};

	renderAvatarIconButton = () => {
		const {classes} = this.props;
		const highlighted = this.props.highlighted != null && this.props.highlighted.toLowerCase() === "fill";
		const style = this.style();
        const large = this._size() === "large";
        const width = style.width != null ? style.width : (large ? "48px" : "24px");
        const height = style.height != null ? style.height : (large ? "48px" : "24px");
        const color = highlighted ? "white" : "inherit";
        const background = highlighted ? (this.props.color != null ? this.props.color : "#3f51b5") : "transparent";
        const border = highlighted ? "0" : "1px solid " + (this.props.color != null ? this.props.color : "#3f51b5");
        const fontSize = large ? "18pt" : "12pt";
        const paddingLeft = large ? "1px" : "0";
        const paddingTop = large ? "10px" : "3px";
        const title = StringUtil.initials(this.state.title, 1);
		if (this.state.color != null) style.color = this.state.color;
		const button = (
            <IconButton id={this.triggerId()} color="primary" disabled={this._readonly()}
                            onClick={this.handleClick.bind(this)} className={classes.materialIconButton}
                            style={style} size={this._size()}>
                <div style={{width:width,height:height,background:background,border:border,
                             borderRadius:'40px',color:color,fontSize:fontSize,
                             paddingTop:paddingTop,paddingLeft:paddingLeft}}>{title}</div>
            </IconButton>
		);
		return button;
	};

	renderContent = () => {
		const mode = this.props.mode.toLowerCase();
		if (mode === "button" || mode === "toggle") return (<div>{this._title()}</div>);
		else if (mode === "iconbutton" || mode === "icontoggle") return (<img title={this._title()} src={this._icon()} style={this._addDimensions({})}/>);
		else if (mode === "materialiconbutton" || mode === "materialicontoggle") return (<ActionableMui titleAccess={this._title()} icon={this._icon()} style={this._addDimensions({})}/>);
	};

	renderAffirm = () => {
		if (!this.requireAffirm()) return;
		const openAffirm = this.state.openAffirm != null ? this.state.openAffirm : false;
		return (
		    <Dialog onClose={this.handleAffirmClose} open={openAffirm}>
				<DialogTitle onClose={this.handleAffirmClose}>{this.translate("Affirm")}</DialogTitle>
				<DialogContent><DialogContentText>{this.translate(this.state.affirmed)}</DialogContentText></DialogContent>
				<DialogActions>
					<Button onClick={this.handleAffirmClose} color="primary">{this.translate("Cancel")}</Button>
					<Button variant="contained" onClick={this.handleAffirmAccept} color="primary">{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	renderSign = () => {
		if (!this.requireSign()) return;
		const openSign = this.state.openSign != null ? this.state.openSign : false;
		return (
		    <Dialog onClose={this.handleSignClose} open={openSign}>
				<DialogTitle onClose={this.handleSignClose}>{this.translate("Sign")}</DialogTitle>
				<DialogContent>
				    <DialogContentText style={{marginBottom:'5px'}}>{this.props.signed.text}</DialogContentText>
				    <TextField autoFocus={true} style={{width:'100%'}} type="password" value={this.translate(this.state.signInfo.sign)}
				               onChange={this.handleSignTextChange.bind(this)} onKeyPress={this.handleSignKeypress.bind(this)}/>
				    {this.requireSignReason() &&
				        <div style={{marginTop:"25px"}}>
                            <DialogContentText style={{marginBottom:'5px'}}>{this.translate(this.props.signed.reason)}</DialogContentText>
                            <TextField autoFocus={true} style={{width:'100%'}} multiline={true} rows={5} value={this.state.signInfo.reason}
                                       onChange={this.handleSignReasonChange.bind(this)} onKeyPress={this.handleSignKeypress.bind(this)}/>
                       </div>
                    }
                </DialogContent>
				<DialogActions>
					<Button onClick={this.handleSignClose} color="primary">{this.translate("Cancel")}</Button>
					<Button variant="contained" onClick={this.handleSignAccept} color="primary">{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	triggerId = () => {
	    return this.props.id;
	};

	handleClick(e) {
		if (this._readonly()) return;
	    Delayer.execute(this, () => this.execute(), Actionable.Delay);
	};

	execute = () => {
	    if (!this.canExecute()) return;
		this.requester.execute();
	};

	canExecute = () => {
	    if (this._readonly()) return false;

		if (this.requireAffirm()) {
			this.setState({ openAffirm : true });
			return false;
		}
		if (this.requireSign()) {
			this.setState({ openSign : true });
			return false;
		}

		return true;
	};

	handleAffirmAccept = (e) => {
	    Delayer.execute(this, () => {
            this.setState({ openAffirm : false });
            this.requester.execute();
	    }, Actionable.Delay);
	};

	handleAffirmClose = () => {
		this.setState({ openAffirm : false });
	};

    handleSignTextChange = (e) => {
        const signInfo = this.state.signInfo;
        signInfo.sign = e.target.value;
        this.setState({signInfo});
    };

    handleSignReasonChange = (e) => {
        const signInfo = this.state.signInfo;
        signInfo.reason = e.target.value;
        this.setState({signInfo});
    };

	handleSignKeypress = (e) => {
	    if (e.key === "Enter") this.handleSignAccept();
	};

	handleSignAccept = (e) => {
	    Delayer.execute(this, () => {
            if (this.requireSignReason() && this.state.signInfo.reason === "") {
                this.showError(this.translate("Reason must be filled"));
                return;
            }
            this.requester.checkSign(this.state.signInfo);
	    }, Actionable.Delay);
	};

	checkSignResult = (value) => {
	    if (value) {
    		this.setState({ openSign : false });
	        this.requester.execute();
	    }
	    else this.showError(this.translate("User not granted to execute operation"));
	}

	handleSignClose = () => {
		this.setState({ openSign : false });
	};

	refresh = ({ title, readonly }) => {
		this.setState({ title, readonly });
	};

	refreshReadonly = (value) => {
		this.setState({ readonly: value });
	};

	refreshIcon = (value) => {
		this.setState({ icon: value });
	};

	refreshAffirmed = (value) => {
		this.setState({ affirmed: value });
	};

	requireAffirm = () => {
		return this.state.affirmed != null && this.state.affirmed !== "";
	};

	requireSign = () => {
		return this.props.signed != null;
	};

	requireSignReason = () => {
		if (!this.requireSign()) return false;
		const reason = this.props.signed.reason;
		return reason != null && reason !== "";
	};

	_highlightVariant = () => {
		const highlighted = this.props.highlighted;
		if (highlighted == null) return undefined;
		else if (highlighted.toLowerCase() === "outline") return "outlined";
		return "contained";
	};

	_title = () => {
		return this.translate(this.state.title != null ? this.state.title : this.props.title);
	};

	_icon = () => {
		return this.state.icon != null ? this.state.icon : this.props.icon;
	};

	_size = () => {
		const size = this.state.size != null ? this.state.size : this.props.size;
		return size != null ? size.toLowerCase() : "small";
	};

	_readonly = () => {
		return this.state.readonly != null ? this.state.readonly : false;
	};

	_addDimensions = (props) => {
        const large = this._size() === "large";
        const style = this.style();
        props.width = style.width != null ? style.width : (large ? "48px" : "24px");
        props.height = style.height != null ? style.height : (large ? "48px" : "24px");
        return props;
    };

};

DisplayFactory.register("Actionable", Actionable);