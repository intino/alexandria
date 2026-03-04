import React from "react";
import {withStyles} from '@material-ui/core/styles';
import AbstractImageEditable from "../../../gen/displays/components/AbstractImageEditable";
import ImageEditableNotifier from "../../../gen/displays/notifiers/ImageEditableNotifier";
import ImageEditableRequester from "../../../gen/displays/requesters/ImageEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from 'notistack';
import ComponentBehavior from "./behaviors/ComponentBehavior";
import ImageGallery from 'react-image-gallery';
import 'react-image-gallery/styles/css/image-gallery.css';
import Theme from "app-elements/gen/Theme";
import classnames from 'classnames';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import {Fullscreen} from '@material-ui/icons';

const styles = theme => ({
	input: {
		display: "none"
	},
	image: {
		border: theme.isDark() ? "1px solid #0a0a0a" : "1px solid #555",
		display: "block",
		objectFit: 'contain',
		position: "absolute",
		borderRadius: "14px",
		background: "#f5ffff",
		"&:hover": {
			background: "#d2f1fe",
			border: "1px solid #555"
		},
		"&:focus": {
			background: "#d2f1fe",
			border: "1px solid #555"
		},
	},
	disabledImage: {
		border: theme.isDark() ? "1px solid #0a0a0a" : "1px solid #efefef",
		background: "none !important",
		"&:hover": {
			border: "0 !important",
			background: "none !important",
		},
		"&:focus": {
			border: "0 !important",
			background: "none !important",
		}
	},
	overlay: {
		borderRadius: "14px",
		"justify-content": "center",
		"align-items": "center",
		top: "0",
		background: "#f5ffff",
		"&:hover": {
			border: theme.isDark() ? "1px solid #ffffff00" : "1px solid #555",
			background: "#d2f1fe",
		},
		"&:focus": {
			border: theme.isDark() ? "1px solid #ffffffde" : "1px solid #00000023",
		}
	},
	borderedOverlay: {
		border: theme.isDark() ? "1px solid #0a0a0a" : "1px solid #555",
	},
	disabledOverlay: {
		border: theme.isDark() ? "1px solid #0a0a0a" : "1px solid #efefef",
		background: "none !important",
		"&:hover": {
			border: theme.isDark() ? "1px solid #0a0a0a" : "1px solid #efefef",
			background: "none !important",
		},
		"&:focus": {
			border: theme.isDark() ? "1px solid #0a0a0a" : "1px solid #efefef",
			background: "none !important",
		},
	},
	bordered: {
		position: "absolute",
		border: "1px solid #efefef",
		width: "100%",
		height: "calc(100% - 20px)",
		"justify-content": "center",
		"align-items": "center",
		top: '0',
	},
	icon: {
		color: "#005ba4"
	},
	empty : {
		fontSize: '12pt',
	},
	link : {
        cursor: 'pointer',
        width: '70px',
        textAlign: 'center',
		marginRight: '10px',
		fontSize: '11pt',
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
            focused: this.props.focused,
			showPreview: this.props.showPreview != null ? this.props.showPreview : true,
        }
	};

	refresh = (value) => {
		this.setState({ "value": this.forceReload(value) });
	};

	forceReload = (value) => {
		if (value == null) return null;
		const prefix = value.indexOf("?") !== -1 ? "&" : "?";
		return value + prefix + "_r=" + Math.random();
	};

	handleChange(e) {
		let fileInput = false;
		if (e.target.files[0]) fileInput = true;
		if (!fileInput) return;
		this.requester.notifyUploading();
		this.requester.notifyChange(e.target.files[0], progress => {});
	};

	handleRemove(e) {
		this.requester.notifyChange(null, progress => {});
	};

	renderFullScreen = () => {
		if (this.state.value == null || this.state.value === "") return (<React.Fragment/>);
		return (
			<a className="image-gallery-icon image-gallery-fullscreen-button" color="primary" onClick={this.handlePreview.bind(this)}>
				<Fullscreen fontSize="large"/>
			</a>
		);
	}

	handlePreview(e) {
		const overlay = document.createElement("div");
		overlay.style.position = "fixed";
		overlay.style.top = "0";
		overlay.style.left = "0";
		overlay.style.width = "100vw";
		overlay.style.height = "100vh";
		overlay.style.background = "rgba(0, 0, 0, 0.9)";
		overlay.style.display = "flex";
		overlay.style.alignItems = "center";
		overlay.style.justifyContent = "center";
		overlay.style.zIndex = "9999";
		overlay.style.cursor = "zoom-out";

		// Crear imagen
		const fullImg = document.createElement("img");
		fullImg.src = this.state.value;
		fullImg.style.maxWidth = "90%";
		fullImg.style.maxHeight = "90%";
		fullImg.style.objectFit = "contain";

		overlay.appendChild(fullImg);

		overlay.addEventListener("click", function () {
			document.body.removeChild(overlay);
		});

		document.addEventListener("keydown", function escClose(e) {
			if (e.key === "Escape") {
				document.body.removeChild(overlay);
				document.removeEventListener("keydown", escClose);
			}
		});

		document.body.appendChild(overlay);
	};

	handleDownload(e) {
	    this.requester.download();
	};

	handleEdit(e) {
	    const input = document.getElementById(this._inputId());
		if (input != null) input.click();
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
		const inputId = this._inputId();
		const showImageGallery = this._showImageGallery();
		const theme = Theme.get();
		const url = this.state.value != null ? this.state.value + (this.state.value.indexOf("?") === -1 ? "?" : "&") + Math.random() : null;
        const removeStyle = this.state.readonly ? { display: 'none', pointerEvents: 'none' } : {};

		return (
			<div style={{position:'relative',...this.style()}} className={classnames("image-editable layout ", this.state.showPreview ? "vertical" : "horizontal center")}>
			    { ComponentBehavior.labelBlock(this.props, "body1", { marginRight: '15px', color: theme.palette.grey.primary, fontSize: this.state.showPreview ? "9pt" : "14pt" }) }
				{this.state.showPreview &&
					<label htmlFor={inputId} className={classnames(classes.overlay, this.state.readonly ? classes.disabledOverlay : undefined, !showImageGallery || this.state.value == null ? classes.borderedOverlay : undefined)} style={{display:'flex',cursor: this.state.readonly ? 'default' : 'pointer',...this.sizeStyle()}} >
						{this.state.showPreview && !showImageGallery && this.state.value && <img className={classnames(classes.image, this.state.readonly ? classes.disabledImage : undefined)} alt={this.props.label} title={this.props.label} src={url} style={this.sizeStyle()}/>}
					</label>
				}
			    {(this.state.showPreview && showImageGallery && this.state.value != null) &&
					<div className={classnames(classes.image, this.state.readonly ? classes.disabledImage : undefined)} style={{top: this.props.label != null && this.props.label !== "" ? '19px' : '0',...this.sizeStyle()}} >
						<ImageGallery items={[this._galleryItems()]} showThumbnails={false} showBullets={false} showPlayButton={false} renderFullscreenButton={this.renderFullScreen.bind(this)}/>
					</div>
				}
				<React.Fragment>
					<input accept="image/*" id={inputId} type="file"
					   className={classes.input} onChange={this.handleChange.bind(this)}
					   disabled={this.state.readonly} value="" />
				</React.Fragment>
				<div style={{marginTop:'4px',zIndex:'6',position:'relative'}}>
					{(this.state.readonly && !this.state.value && !this.state.showPreview) && <div className={classes.empty}>{this.translate("No image")}</div>}
					{(!this.state.readonly && !this.state.value) && <a className={classes.link} onClick={this.handleEdit.bind(this)}>{this.translate("Select")}</a>}
					{!this.state.showPreview && this.state.value && <a className={classes.link} onClick={this.handlePreview.bind(this)}>{this.translate("Preview")}</a>}
					{this.state.value && <a className={classes.link} onClick={this.handleDownload.bind(this)}>{this.translate("Download")}</a>}
					{(!this.state.readonly && this.state.value) && <a className={classes.link} onClick={this.handleRemove.bind(this)} style={removeStyle}>{this.translate("Remove")}</a>}
				</div>
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
        return this.props.id + "-image-input";
    }

	sizeStyle() {
		const result = {};
		if (!this.state.showPreview) return result;
		if (this.props.width != null) {
			result.width = this.props.width;
			result.minWidth = this.props.width;
		}
		if (this.props.height != null) {
			result.height = this.props.height;
			result.minHeight = this.props.height;
		}
		return result;
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	refreshFocused = (focused) => {
		this.setState({ focused });
	};

    _showImageGallery = () => {
        return this.props.allowFullscreen;
    }

}

export default withStyles(styles, { withTheme: true })(withSnackbar(ImageEditable));
DisplayFactory.register("ImageEditable", withStyles(styles, { withTheme: true })(withSnackbar(ImageEditable)));