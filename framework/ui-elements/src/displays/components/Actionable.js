import React, { Suspense } from "react";
import AbstractActionable from "../../../gen/displays/components/AbstractActionable";
import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button, IconButton, Typography } from "@material-ui/core";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import TextField from '@material-ui/core/TextField';
import Delayer from 'alexandria-ui-elements/src/util/Delayer';
import StringUtil from 'alexandria-ui-elements/src/util/StringUtil';
import Theme from "app-elements/gen/Theme";

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
			cursor: "pointer",
		},
		button : {
			cursor: "pointer",
            whiteSpace: "nowrap",
		},
		iconButton : {
			cursor: "pointer",
		},
		materialIconButton : {
			cursor: "pointer",
		},
		readonly : {
			color: theme.palette.grey.A700,
			cursor: "default"
		},
		shortcut : {
		    color: theme.palette.grey.primary,
		    padding: '2px 4px',
		    marginRight: '2px',
		    fontSize: '7pt',
		    marginLeft: '10px',
		},
		shortcutPart : {
		    margin: '0 1px'
		}
	});

	constructor(props) {
		super(props);
		this.state = {
			...this.state,
			icon : this.props.icon,
			darkIcon : this.props.darkIcon,
			title: this.props.title,
			readonly: this.props.readonly,
			openAffirm : false,
			openSign : false,
			openSignConfig : false,
			affirmed : this.props.affirmed != null ? this.props.affirmed : null,
			affirmedRequired : true,
			highlighted : this.props.highlighted != null ? this.props.highlighted : null,
			signInfo : {
			    sign: "",
			    reason: "",
			    secret: null,
			    secretImage: null,
			    setupRequired: false,
			}
		}
	};

	componentDidMount() {
        this.registerShortcut();
    };

	render = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		return (
			<Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
				{this.renderActionable()}
			</Suspense>
		);
	};

	renderActionable() {
		if (!this.state.visible) return (<React.Fragment/>);
		const hasShortcut = this.props.shortcut != null;
		return hasShortcut ? this.renderActionableWithShortcut() : this.renderActionableWithoutShortcut();
	};

	renderActionableWithoutShortcut = () => {
	    return (
	        <React.Fragment>
                {this.renderTraceConsent()}
                {this.renderAffirmed()}
                {this.renderSignConfiguration()}
                {this.renderSign()}
                {this.renderTrigger()}
	        </React.Fragment>
	    );
	};

	renderActionableWithShortcut = () => {
        return (
            <div className="layout horizontal center flex" style={{width:'100%'}}>
                <div className="layout vertical start">
                    {this.renderTraceConsent()}
                    {this.renderAffirmed()}
                    {this.renderSignConfiguration()}
                    {this.renderSign()}
                    {this.renderTrigger()}
                </div>
                <div className="layout horizontal flex end-justified">{this.renderShortcut()}</div>
            </div>
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

	renderShortcut = () => {
	    const { classes } = this.props;
	    const shortcut = this.props.shortcut;
	    if (shortcut == null || !shortcut.visible) return (<React.Fragment/>);
	    return (
    	    <div>
                <div className={classes.shortcut} style={this.style()}>
                    {shortcut.shiftKey && <span className={classes.shortcutPart}>Shift +</span>}
                    {shortcut.ctrlKey && <span className={classes.shortcutPart}>Ctrl +</span>}
                    {shortcut.altKey && <span className={classes.shortcutPart}>Alt +</span>}
                    {<span className={classes.shortcutPart}>{shortcut.key}</span>}
                </div>
	        </div>
        );
	};

	renderLink = () => {
		const {classes} = this.props;
		const className = this._readonly() ? classes.readonly : classes.link;
		const color = this.state.color != null ? this.state.color : (this.props.color != null ? this.props.color : undefined);
		const backgroundColor = this.state.backgroundColor != null ? this.state.backgroundColor : (this.props.backgroundColor != null ? this.props.backgroundColor : undefined);
		return (
		    <a id={this.triggerId()}
		        onClick={this.clickEvent()} onMouseEnter={this.mouseEnterEvent()} onMouseLeave={this.mouseLeaveEvent()}
		        disabled={this._readonly()}>
				<Typography style={{...this.style(),color:color,backgroundColor:backgroundColor}} variant={this.variant("body1")} className={className}>{this._title()}</Typography>
			</a>
		);
	};

	renderButton = () => {
		const {classes} = this.props;
		return (
		    <Button id={this.triggerId()} style={this.style()} size={this._size()} color="primary" variant={this._highlightVariant()}
						onClick={this.clickEvent()} onMouseEnter={this.mouseEnterEvent()} onMouseLeave={this.mouseLeaveEvent()}
						disabled={this._readonly()} className={classes.button}>
				{this.renderContent()}
			</Button>
		);
	};

	renderIconButton = (ref) => {
		const {classes} = this.props;
		const style = this._readonly() ? { opacity: "0.3", ...this.style() } : this.style();
		const applyStyles = this.props.titlePosition == null || this.props.titlePosition == "None";
		const button = (
            <IconButton id={this.triggerId()} color="primary" disabled={this._readonly()} ref={ref != null ? ref : undefined}
                            onClick={this.clickEvent()} onMouseEnter={this.mouseEnterEvent()} onMouseLeave={this.mouseLeaveEvent()}
                            style={applyStyles ? style : null} className={classes.iconButton} size={this._size()}>
                {this.renderContent()}
            </IconButton>
        );
		return this.renderWithText(button, style);
	};

	renderMaterialIconButton = (ref) => {
		const {classes} = this.props;
		const style = this.style();
		if (this.state.color != null) style.color = this.state.color;
		const applyStyles = this.props.titlePosition == null || this.props.titlePosition == "None";
		const colorStyle = this.props.color != null ? { color: this.props.color } : undefined;
		const button = (
            <IconButton id={this.triggerId()} color="primary" disabled={this._readonly()} ref={ref != null ? ref : undefined}
                            onClick={this.clickEvent()} onMouseEnter={this.mouseEnterEvent()} onMouseLeave={this.mouseLeaveEvent()}
                            className={classes.materialIconButton} style={applyStyles ? { ...style, ...colorStyle} : colorStyle} size={this._size()}>
                {this.renderContent()}
            </IconButton>
		);
		return this.renderWithText(button, style);
	};

	renderWithText = (button, style) => {
        if (this.props.titlePosition == null || this.props.titlePosition == "None") return button;
		const {classes} = this.props;
		const className = this._readonly() ? classes.readonly : classes.link;
        const position = this.props.titlePosition;
        const isVertical = position == "Top" || position == "Bottom";
		const applyStyles = this.props.titlePosition != null && this.props.titlePosition != "None";
		const colorStyle = this.props.color != null ? { color: this.props.color } : undefined;
        const link = (
            <a id={this.triggerId()}
             onClick={this.clickEvent()} onMouseEnter={this.mouseEnterEvent()} onMouseLeave={this.mouseLeaveEvent()}
             disabled={this._readonly()}>
             <Typography variant={this.variant("body1")} className={className} style={colorStyle}>{this._title()}</Typography>
            </a>
        );
        return (
            <div className={"layout center-center " + (isVertical ? "vertical" : "horizontal")} style={style}>
                {(position == "Left" || position == "Top") && link}
                {button}
                {(position == "Right" || position == "Bottom") && link}
            </div>
        );
	};

	renderAvatarIconButton = () => {
		const {classes} = this.props;
		const highlighted = this.state.highlighted != null && this.state.highlighted.toLowerCase() === "fill";
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
                            onClick={this.clickEvent()} onMouseEnter={this.mouseEnterEvent()} onMouseLeave={this.mouseLeaveEvent()}
                            className={classes.materialIconButton} style={style} size={this._size()}>
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
		else if (mode === "iconbutton" || mode === "icontoggle" || mode === "iconsplitbutton") return (<img title={this._title()} src={this._icon()} style={{...this._addFilter(),...this._addDimensions({})}}/>);
		else if (mode === "materialiconbutton" || mode === "materialicontoggle" || mode === "materialiconsplitbutton") return (<ActionableMui titleAccess={this._title()} icon={this._icon()} style={this._addDimensions({})}/>);
	};

	renderAffirmed = () => {
		if (!this.requireAffirmed()) return;
		const openAffirm = this.state.openAffirm != null ? this.state.openAffirm : false;
		return (
		    <Dialog onClose={this.handleAffirmClose} open={openAffirm}>
				<DialogTitle onClose={this.handleAffirmClose}>{this.translate("Confirm")}</DialogTitle>
				<DialogContent><DialogContentText>{this.translate(this.state.affirmed)}</DialogContentText></DialogContent>
				<DialogActions>
					<Button onClick={this.handleAffirmClose} color="primary" style={{marginRight:'10px'}}>{this.translate("Cancel")}</Button>
					<Button variant="contained" onClick={this.handleAffirmAccept} color="primary">{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	renderSignConfiguration = () => {
		if (!this.requireSign()) return;
		const openSignConfig = this.state.openSignConfig != null ? this.state.openSignConfig : false;
		return (
		    <Dialog onClose={this.handleSignConfigurationClose} open={openSignConfig}>
				<DialogTitle onClose={this.handleSignConfigurationClose}>{this.translate("Enable two factor authentication")}</DialogTitle>
				<DialogContent>
				    <DialogContentText style={{marginBottom:'5px'}}>{this.translate("Download one time password app like Google Authenticator App in your device, then follow steps below.")}</DialogContentText>
				    <br/>
                    <DialogContentText style={{marginBottom:'5px'}}>{this.translate("Step 1. Scan QR code or enter the secret key in your one time password app")}</DialogContentText>
                    <div className="layout vertical center-center">
				        <img src={"data:image/png;base64, " + this.state.signInfo.secretImage} alt={this.translate("QR code")} />
                        <DialogContentText style={{marginBottom:'5px'}}>{this.state.signInfo.secret}</DialogContentText>
                    </div>
				    <br/>
				    <DialogContentText style={{marginBottom:'5px'}}>{this.translate("Step 2. Enter 6-digit passcode it gives you to finish configuration")}</DialogContentText>
				    <div style={{marginTop:'10px'}}>{this.renderPasscode()}</div>
                </DialogContent>
				<DialogActions>
					<Button onClick={this.handleSignConfigurationClose} color="primary" style={{marginRight:'10px'}}>{this.translate("Cancel")}</Button>
					<Button variant="contained" onClick={this.handleSignConfigurationAccept} color="primary">{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	renderPasscode = () => {
	    const setupDialog = this.state.openSignConfig;
	    const disabled = !setupDialog && this.state.signInfo.setupRequired;
	    const theme = Theme.get();
	    return (
	        <div style={{marginTop:'10px'}}>
                <div className="layout horizontal center-center">
                    <TextField disabled={disabled} id={this._fieldKeycode(0)} style={{marginRight:'10px'}} autoFocus={true} type="tel" InputProps={{disableUnderline:true}} inputProps={{style: { padding:'0 10px',border:'1px solid #ddd',borderRadius:'5px',width:'20px',fontSize:'23pt' }, maxlength: 1 }} value={this._signDigit(0)} onClick={this.handleSignKeycodeClick.bind(this)} onKeyUp={this.handleSignKeycodeUp.bind(this, 0)} onChange={this.handleSignKeycodeChange.bind(this, 0)} />
                    <TextField disabled={disabled} id={this._fieldKeycode(1)} style={{marginRight:'10px'}} type="tel" InputProps={{disableUnderline:true}} inputProps={{style: { padding:'0 10px',border:'1px solid #ddd',borderRadius:'5px',width:'20px',fontSize:'23pt' }, maxlength: 1 }} value={this._signDigit(1)} onClick={this.handleSignKeycodeClick.bind(this)} onKeyUp={this.handleSignKeycodeUp.bind(this, 1)} onChange={this.handleSignKeycodeChange.bind(this, 1)} />
                    <TextField disabled={disabled} id={this._fieldKeycode(2)} style={{marginRight:'10px'}} type="tel" InputProps={{disableUnderline:true}} inputProps={{style: { padding:'0 10px',border:'1px solid #ddd',borderRadius:'5px',width:'20px',fontSize:'23pt' }, maxlength: 1 }} value={this._signDigit(2)} onClick={this.handleSignKeycodeClick.bind(this)} onKeyUp={this.handleSignKeycodeUp.bind(this, 2)} onChange={this.handleSignKeycodeChange.bind(this, 2)} />
                    <TextField disabled={disabled} id={this._fieldKeycode(3)} style={{marginRight:'10px'}} type="tel" InputProps={{disableUnderline:true}} inputProps={{style: { padding:'0 10px',border:'1px solid #ddd',borderRadius:'5px',width:'20px',fontSize:'23pt' }, maxlength: 1 }} value={this._signDigit(3)} onClick={this.handleSignKeycodeClick.bind(this)} onKeyUp={this.handleSignKeycodeUp.bind(this, 3)} onChange={this.handleSignKeycodeChange.bind(this, 3)} />
                    <TextField disabled={disabled} id={this._fieldKeycode(4)} style={{marginRight:'10px'}} type="tel" InputProps={{disableUnderline:true}} inputProps={{style: { padding:'0 10px',border:'1px solid #ddd',borderRadius:'5px',width:'20px',fontSize:'23pt' }, maxlength: 1 }} value={this._signDigit(4)} onClick={this.handleSignKeycodeClick.bind(this)} onKeyUp={this.handleSignKeycodeUp.bind(this, 4)} onChange={this.handleSignKeycodeChange.bind(this, 4)} />
                    <TextField disabled={disabled} id={this._fieldKeycode(5)} style={{marginRight:'10px'}} type="tel" InputProps={{disableUnderline:true}} inputProps={{style: { padding:'0 10px',border:'1px solid #ddd',borderRadius:'5px',width:'20px',fontSize:'23pt' }, maxlength: 1 }} value={this._signDigit(5)} onClick={this.handleSignKeycodeClick.bind(this)} onKeyUp={this.handleSignKeycodeUp.bind(this, 5)} onChange={this.handleSignKeycodeChange.bind(this, 5)} />
                </div>
                {disabled && <div style={{marginTop:'10px', color: theme.palette.secondary.main}}>{this.translate("Two factor authentication not enabled. Before signing, you must enable it by clicking in enable button.")}</div>}
            </div>
        );
	};

	registerShortcut = () => {
	    const shortcut = this.props.shortcut;
	    if (shortcut == null) return;
	    document.addEventListener("keyup", () => {
            var e = e || window.event; // for IE to cover IEs window event-object
            if ((!shortcut.ctrlKey || (e.ctrlKey && shortcut.ctrlKey)) &&
                (!shortcut.shiftKey || (e.shiftKey && shortcut.shiftKey)) &&
                (!shortcut.altKey || (e.altKey && shortcut.altKey)) &&
                (e.which == shortcut.key.charCodeAt(0))) {
                this.execute();
                return false;
            }
        });
	}

	_fieldKeycode = (pos) => {
	    return this.props.id + "_keycodefield_" + pos + "_" + (this.state.openSignConfig ? 1 : 0);
	};

	_signDigit = (pos) => {
	    const sign = this.state.signInfo.sign;
	    return sign != null ? sign[pos] : "";
	};

	renderSign = () => {
		if (!this.requireSign()) return;
		const openSign = this.state.openSign != null ? this.state.openSign : false;
		const isOneTimePasswordSignMode = this.props.signed.mode === "OneTimePassword";
		return (
		    <Dialog onClose={this.handleSignClose} open={openSign}>
				<DialogTitle onClose={this.handleSignClose}>{this.translate("Sign")}</DialogTitle>
				<DialogContent>
				    <DialogContentText style={{marginBottom:'5px'}}>{this.translate(this.props.signed.text)}</DialogContentText>
				    {this.renderSignField()}
				    {this.requireSignReason() &&
				        <div style={{marginTop:"25px"}}>
                            <DialogContentText style={{marginBottom:'5px'}}>{this.translate(this.props.signed.reason)}</DialogContentText>
                            <TextField style={{width:'100%'}} multiline={true} rows={5} value={this.state.signInfo.reason}
                                       onChange={this.handleSignReasonChange.bind(this)} onKeyPress={this.handleSignKeypress.bind(this)}/>
                       </div>
                    }
                </DialogContent>
				<DialogActions>
				    { this.state.signInfo.canSetup &&
                        <div className="layout horizontal flexible" style={{width:'100%',display:isOneTimePasswordSignMode?'block':'none'}}>
                            <Button onClick={this.handleSignSetup} color="primary">{this.translate(this.state.signInfo.setupRequired ? "Enable" : "Setup")}</Button>
                        </div>
                    }
				    <div className="layout horizontal end-justified">
					    <Button onClick={this.handleSignClose} color="primary" style={{marginRight:'10px'}}>{this.translate("Cancel")}</Button>
					    <Button variant="contained" onClick={this.handleSignAccept} color="primary">{this.translate("OK")}</Button>
                    </div>
				</DialogActions>
			</Dialog>
		);
	};

	renderSignField = () => {
	    if (this.props.signed.mode === "OneTimePassword") return this.renderPasscode();
        return (<TextField autoFocus={true} style={{width:'100%'}} type="password" value={this.translate(this.state.signInfo.sign)}
                           onChange={this.handleSignTextChange.bind(this)} onKeyPress={this.handleSignKeypress.bind(this)}/>);
	};

	triggerId = () => {
	    return this.props.id;
	};

	clickEvent = () => {
	    return this.handleClick.bind(this);
	};

	mouseEnterEvent = () => {
	    return null;
	};

	mouseLeaveEvent = () => {
	    return null;
	};

	handleClick(e) {
		if (this._readonly()) return;
	    e.stopPropagation();
	    Delayer.execute(this, () => this.execute(), Actionable.Delay);
	};

    launch = () => {
        const element = document.getElementById(this.triggerId());
        if (element == null) return;
        element.click();
    };

    execute = () => {
	    if (this._readonly()) return false;
        if (this.requireAffirmed()) this.requester.checkAffirmed();
        else this.doExecute();
    };

    refreshAffirmedRequired = (value) => {
        this.setState({affirmedRequired:value});
        this.doExecute();
    };

    refreshHighlight = (value) => {
        this.setState({highlighted:value});
    };

	doExecute = () => {
	    if (!this.canExecute()) return;
		this.requester.execute();
	};

	canExecute = () => {
	    if (this._readonly()) return false;

		if (this.requireAffirmed() && this.state.affirmedRequired) {
			this.setState({ openAffirm : true });
			return false;
		}

		if (this.requireSign()) {
		    this.requester.beforeSigned();
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

	continueSigned = (e) => {
        this.setState({ openSignConfig: false, openSign : true });
	};

	handleAffirmClose = () => {
	    Delayer.execute(this, () => {
            this.setState({ openAffirm : false });
            this.requester.cancelAffirm();
	    }, Actionable.Delay);
	};

    handleSignTextChange = (e) => {
        const signInfo = this.state.signInfo;
        signInfo.sign = e.target.value;
        this.setState({signInfo});
    };

	handleSignKeycodeClick = (e) => {
	    e.target.setSelectionRange(0, e.target.value.length);
	};

	handleSignKeycodeUp = (pos, e) => {
	    if (e.keyCode == 13) this.handleSignAccept();
	    else if (e.keyCode == 8) {
	        if (pos > 0) {
                const previousField = document.getElementById(this._fieldKeycode(pos-1));
                previousField.focus();
                previousField.setSelectionRange(0, 1);
	        }
            this.setState({signInfo: this.state.signInfo});
	    }
	};

	handleSignKeycodeChange = (pos, e) => {
        const signInfo = this.state.signInfo;
        signInfo.sign = this.signKeycode();
        signInfo.r = Math.random();
	    if (pos < 5 && e.target.value !== "") {
	        let nextPos = pos+1;
	        const nextField = document.getElementById(this._fieldKeycode(nextPos));
	        nextField.focus();
	        nextField.setSelectionRange(0, 1);
	    }
        this.setState({signInfo});
	};

	signKeycode = () => {
	    return this.signFieldKeycode(0) + this.signFieldKeycode(1) +
	           this.signFieldKeycode(2) + this.signFieldKeycode(3) +
	           this.signFieldKeycode(4) + this.signFieldKeycode(5);
	};

	signFieldKeycode = (pos) => {
	    return document.getElementById(this._fieldKeycode(pos)).value;
	}

    handleSignReasonChange = (e) => {
        const signInfo = this.state.signInfo;
        signInfo.reason = e.target.value;
        this.setState({signInfo});
    };

	handleSignKeypress = (e) => {
	    if (e.key === "Enter") this.handleSignAccept();
	};

	handleSignConfigurationKeypress = (e) => {
	    if (e.key === "Enter") this.handleSignConfigurationAccept();
	};

	handleSignSetup = (e) => {
	    this.setState({ openSignConfig: true, openSign: false });
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

	handleSignConfigurationAccept = (e) => {
	    Delayer.execute(this, () => {
            this.requester.setupSign(this.state.signInfo);
	    }, Actionable.Delay);
	};

	checkSignResult = (value) => {
	    if (value) {
    		this.setState({ openSign : false });
	        this.requester.execute();
	    }
	    else {
	        this.showError(this.translate("User not granted to execute operation"));
			const signInfo = this.state.signInfo;
			signInfo.sign = null;
	        this.setState({signInfo});
	        const field = document.getElementById(this._fieldKeycode(0));
	        field.focus();
	    }
	}

	setupSignResult = (value) => {
	    if (value) {
            const signInfo = this.state.signInfo;
            signInfo.setupRequired = false;
	        this.setState({ signInfo: signInfo, openSignConfig: false, openSign: true });
	    }
	    else this.showError(this.translate("Could not validate 6-digit passcode against one time password app"));
	}

	handleSignClose = () => {
		this.setState({ openSign : false });
	};

	handleSignConfigurationClose = () => {
		this.setState({ openSignConfig : false, openSign: true });
	};

	refresh = ({ title, readonly }) => {
		this.setState({ title, readonly });
	};

	refreshSignInfo = ({ setupRequired, secret, secretImage, canSetup }) => {
        const signInfo = this.state.signInfo;
        signInfo.secret = secret;
        signInfo.secretImage = secretImage;
        signInfo.canSetup = canSetup;
        signInfo.setupRequired = setupRequired;
		this.setState({ signInfo });
	};

	refreshReadonly = (value) => {
		this.setState({ readonly: value });
	};

	refreshIcon = (value) => {
		this.setState({ icon: value });
	};

	refreshDarkIcon = (value) => {
		this.setState({ darkIcon: value });
	};

	refreshAffirmed = (value) => {
		this.setState({ affirmed: value });
	};

	requireAffirmed = () => {
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
		const highlighted = this.state.highlighted;
		if (highlighted == null) return undefined;
		else if (highlighted.toLowerCase() === "outline") return "outlined";
		return "contained";
	};

	_title = () => {
		return this.translate(this.state.title != null ? this.state.title : this.props.title);
	};

	_icon = () => {
	    const isDark = Theme.get().isDark();
	    if (isDark) {
	        if (this.state.darkIcon != null) return this.state.darkIcon;
	        if (this.props.darkIcon != null) return this.props.darkIcon;
	    }
		return this.state.icon != null ? this.state.icon : this.props.icon;
	};

	_size = () => {
		const size = this.state.size != null ? this.state.size : this.props.size;
		return size != null ? size.toLowerCase() : "small";
	};

	_readonly = () => {
		return this.state.readonly != null ? this.state.readonly : false;
	};

	_addFilter = (props) => {
	    const theme = Theme.get();
	    return theme.isDark() ? { filter: "invert(1)" } : {};
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