import React, { Suspense } from "react";
import AbstractActionable from "../../../gen/displays/components/AbstractActionable";
import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button, IconButton, Typography } from "@material-ui/core";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import TextField from '@material-ui/core/TextField';
import Tooltip from '@material-ui/core/Tooltip';
import Delayer from 'alexandria-ui-elements/src/util/Delayer';

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
			color: theme.palette.grey.primary,
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
		return this.renderLink();
	};

	renderLink = () => {
		const {classes} = this.props;
		const className = this._readonly() ? classes.readonly : classes.link;
		return (
		    <a onClick={this.handleClick.bind(this)} disabled={this._readonly()}>
				<Typography style={this.style()} variant={this.variant("body1")} className={className}>{this._title()}</Typography>
			</a>
		);
	};

	renderButton = () => {
		const {classes} = this.props;
		return (
		    <Button style={this.style()} size={this._size()} color="primary" variant={this._highlightVariant()}
						disabled={this._readonly()} onClick={this.handleClick.bind(this)}
						className={classes.button}>
				{this._title()}
			</Button>
		);
	};

	_highlightVariant = () => {
		const highlighted = this.props.highlighted;
		if (highlighted == null) return undefined;
		else if (highlighted.toLowerCase() === "outline") return "outlined";
		return "contained";
	};

	renderIconButton = () => {
		const {classes} = this.props;
		return (
            <Tooltip title={this._title()}>
                <IconButton color="primary" disabled={this._readonly()}
                                onClick={this.handleClick.bind(this)}
                                className={classes.iconButton} size={this._size()}>
                    <img src={this._icon()} style={{width:"24px",height:"24px"}}/>
                </IconButton>
            </Tooltip>
		);
	};

	renderMaterialIconButton = () => {
		const {classes} = this.props;
		const style = this.style();
		if (this.state.color != null) style.color = this.state.color;
		return (
            <Tooltip title={this._title()}>
                <IconButton color="primary" disabled={this._readonly()}
                                onClick={this.handleClick.bind(this)} className={classes.materialIconButton}
                                style={style} size={this._size()}>
                    <ActionableMui icon={this._icon()}/>
                </IconButton>
            </Tooltip>
		);
	};

	renderAffirm = () => {
		if (!this.requireAffirm()) return;
		const openAffirm = this.state.openAffirm != null ? this.state.openAffirm : false;
		return (
		    <Dialog onClose={this.handleAffirmClose} open={openAffirm}>
				<DialogTitle onClose={this.handleAffirmClose}>{this.translate("Affirm")}</DialogTitle>
				<DialogContent><DialogContentText>{this.props.affirmed}</DialogContentText></DialogContent>
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
				    <TextField autoFocus={true} style={{width:'100%'}} type="password" value={this.state.signInfo.sign}
				               onChange={this.handleSignTextChange.bind(this)} onKeyPress={this.handleSignKeypress.bind(this)}/>
				    {this.requireSignReason() &&
				        <div style={{marginTop:"25px"}}>
                            <DialogContentText style={{marginBottom:'5px'}}>{this.props.signed.reason}</DialogContentText>
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

	handleClick(e) {
		if (this._readonly()) return;
	    Delayer.execute(this, () => this.execute(), Actionable.Delay);
	};

	execute = () => {
	    if (this._readonly()) return;

		if (this.requireAffirm()) {
			this.setState({ openAffirm : true });
			return;
		}
		if (this.requireSign()) {
			this.setState({ openSign : true });
			return;
		}
		this.requester.execute();
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

	requireAffirm = () => {
		return this.props.affirmed != null && this.props.affirmed !== "";
	};

	requireSign = () => {
		return this.props.signed != null;
	};

	requireSignReason = () => {
		if (!this.requireSign()) return false;
		const reason = this.props.signed.reason;
		return reason != null && reason !== "";
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
	}

};

DisplayFactory.register("Actionable", Actionable);