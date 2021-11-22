import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractImageEditable from "../../../gen/displays/components/AbstractImageEditable";
import ImageEditableNotifier from "../../../gen/displays/notifiers/ImageEditableNotifier";
import ImageEditableRequester from "../../../gen/displays/requesters/ImageEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { IconButton } from '@material-ui/core';
import { AddAPhoto, Clear } from '@material-ui/icons';
import ComponentBehavior from "./behaviors/ComponentBehavior";
import Theme from "app-elements/gen/Theme";
import ImageGallery from 'react-image-gallery';
import BrowserUtil from "alexandria-ui-elements/src/util/BrowserUtil";

const styles = theme => ({
	input: {
		display: "none"
	},
	image: {
		display: "block",
		position: "absolute",
		height: "calc(100% - 20px)",
		width: "100%"
	},
	overlay: {
		position: "absolute",
		background: "rgba(0, 0, 0, 0.4)",
		border: "1px solid #efefef",
		width: "100%",
		height: "calc(100% - 20px)",
		"justify-content": "center",
		"align-items": "center",
		cursor: "pointer"
	},
	bordered: {
		position: "absolute",
		border: "1px solid #efefef",
		width: "100%",
		height: "calc(100% - 20px)",
		"justify-content": "center",
		"align-items": "center",
	},
	icon: {
		color: "#e8e8e8"
	},
	download : {
        background: 'white',
        padding: '2px 7px',
        borderRadius: '0 3px 3px 0',
	    position: 'absolute',
        bottom: '0',
        left: '0',
        cursor: 'pointer',
        fontSize: '8pt',
        marginLeft: '1px',
        marginBottom: '3pt',
        color: theme.palette.primary.main,
	},
});

class ImageEditable extends AbstractImageEditable {

	constructor(props) {
		super(props);
		this.notifier = new ImageEditableNotifier(this);
		this.requester = new ImageEditableRequester(this);
		this.state = {
            ...this.state,
            value: this.props.value,
            readonly: this.props.readonly,
        }
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	handleChange(e) {
		this.requester.notifyChange(e.target.files[0]);
	};

	handleRemove(e) {
		this.requester.notifyChange(null);
	};

	handleDownload(e) {
	    this.requester.download();
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
		const inputId = this._inputId();
		const theme = Theme.get();
		const labelDisplay = this.state.readonly ? "none" : "flex";
		const borderDisplay = this.state.readonly ? "flex" : "none";
		const removeButtonDisplay = this.state.readonly || this.state.value == null ? "none" : "flex";
		const showImageGallery = this._showImageGallery();

		return (
			<div style={{...this.style(),position:'relative'}}>
			    { ComponentBehavior.labelBlock(this.props, "body1", { color: theme.palette.grey.A700, marginRight: '5px', fontSize: "9pt", color: "#777777" }) }
                {this.state.value && <img className={classes.image} title={this.props.label} src={this.state.value} />}
                <label htmlFor={inputId} className={classes.overlay} style={{display:labelDisplay}}>
                    <AddAPhoto className={classes.icon} />
                </label>
                <div className={classes.bordered} style={{display:borderDisplay}}></div>
			    {showImageGallery && <ImageGallery items={[this._galleryItems()]} showThumbnails={false} showBullets={false} showPlayButton={false} /> }
			    {!showImageGallery &&
			        <React.Fragment>
                        <input accept="image/*" id={inputId} type="file"
                           className={classes.input} onChange={this.handleChange.bind(this)}
                           disabled={this.state.readonly} />
                    </React.Fragment>
                }
                <IconButton color="primary" aria-label="upload picture" size="small" component="span" onClick={this.handleRemove.bind(this)}
                            style={{position:'absolute',right:"0",zIndex:"0",background:'white',marginTop:'-12px',marginRight:'-12px',border:'1px solid #efefef',display:removeButtonDisplay}}>
                    <Clear />
                </IconButton>
                {this.state.value && <a className={classes.download} onClick={this.handleDownload.bind(this)}>{this.translate("Download")}</a>}
			</div>
		);
	};

	_galleryItems = () => {
	    return {
	        original: this.state.value,
	        thumbnail: this.state.value
        };
	};

	_inputId = () => {
        var dt = new Date().getTime();
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = (dt + Math.random()*16)%16 | 0;
            dt = Math.floor(dt/16);
            return (c=='x' ? r :(r&0x3|0x8)).toString(16);
        });
        return uuid + "-image-input";
    }

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this.props.width != null) {
			result.width = this.props.width;
			result.minWidth = this.props.width;
		}
		if (!this._showImageGallery() && this.props.height != null) {
			result.height = this.props.height;
			result.minHeight = this.props.height;
		}
		result.position = "relative";
		return result;
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

    _showImageGallery = () => {
        return this.props.allowFullscreen && this.state.readonly;
    }

}

export default withStyles(styles, { withTheme: true })(withSnackbar(ImageEditable));
DisplayFactory.register("ImageEditable", withStyles(styles, { withTheme: true })(withSnackbar(ImageEditable)));