import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractImageEditable from "../../../gen/displays/components/AbstractImageEditable";
import ImageEditableNotifier from "../../../gen/displays/notifiers/ImageEditableNotifier";
import ImageEditableRequester from "../../../gen/displays/requesters/ImageEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import ImageGallery from 'react-image-gallery';
import 'react-image-gallery/styles/css/image-gallery.css';
import Theme from "app-elements/gen/Theme";
import classnames from 'classnames';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import {Fullscreen} from '@mui/icons-material';
import {fieldPalette, outlinedSurfaceStyles} from "./FieldStyles";
import {linkPalette} from "./ThemeTokens";

const styles = theme => ({
	input: {
		display: "none"
	},
	surface: {
		...outlinedSurfaceStyles(theme),
		background: fieldPalette(theme).background,
		position: "relative",
		overflow: "hidden",
		minHeight: "52px",
	},
	emptySurface: {
		border: `1px solid ${fieldPalette(theme).borderColor}`,
		boxSizing: "border-box",
		background: fieldPalette(theme).background,
		boxShadow: fieldPalette(theme).shadow,
	},
	image: {
		display: "block",
		objectFit: 'contain',
		position: "absolute",
		inset: "0",
		borderRadius: "15px",
		background: "transparent",
	},
	disabledImage: {
		background: "transparent !important",
	},
	overlay: {
		borderRadius: "16px",
		"justify-content": "center",
		"align-items": "center",
		top: "0",
		background: fieldPalette(theme).background,
	},
	borderedOverlay: {
		border: `1px solid ${fieldPalette(theme).borderColor}`,
		boxSizing: "border-box",
		background: fieldPalette(theme).background,
		boxShadow: fieldPalette(theme).shadow,
	},
	disabledOverlay: {
		background: `${fieldPalette(theme).background} !important`,
	},
	readonlySurface: {
		background: `${theme.palette.mode === "dark" ? "rgba(15,23,42,0.52)" : fieldPalette(theme).background} !important`,
		borderColor: `${theme.palette.mode === "dark" ? "rgba(255, 255, 255, 0.1)" : fieldPalette(theme).borderColor} !important`,
		boxShadow: `${theme.palette.mode === "dark" ? "none" : fieldPalette(theme).shadow} !important`,
		"&:hover": {
			background: `${theme.palette.mode === "dark" ? "rgba(15,23,42,0.52)" : fieldPalette(theme).background} !important`,
			borderColor: `${theme.palette.mode === "dark" ? "rgba(255, 255, 255, 0.1)" : fieldPalette(theme).borderColor} !important`,
			boxShadow: `${theme.palette.mode === "dark" ? "none" : fieldPalette(theme).shadow} !important`,
		},
	},
	bordered: {
		position: "absolute",
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
		fontSize: '0.95rem',
		color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.72)" : theme.palette.text.secondary,
	},
	link : {
        cursor: 'pointer',
        minWidth: '82px',
        textAlign: 'center',
		marginRight: '10px',
		fontSize: '0.9rem',
        color: linkPalette(theme).color,
        textDecoration: "none",
		"&:hover": {
			color: linkPalette(theme).hoverColor,
		},
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

	refresh = (info) => {
		this.setState({ "value": this.forceReload(info.value) });
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
		const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
		const url = this.state.value != null ? this.state.value + (this.state.value.indexOf("?") === -1 ? "?" : "&") + Math.random() : null;
	        const removeStyle = this.state.readonly ? { display: 'none', pointerEvents: 'none' } : {};
		const darkSurfaceStyle = isDark ? { boxShadow: "none" } : undefined;
		const dropzoneClass = this.state.readonly
			? (isDark ? "fileeditable-dropzone-readonly-dark" : "fileeditable-dropzone-readonly")
			: (isDark ? "fileeditable-dropzone-dark" : "fileeditable-dropzone");

			return (
				<div style={{position:'relative',...this.style()}} className={classnames("image-editable layout ", this.state.showPreview ? "vertical" : "horizontal center", isDark ? "dark" : undefined, this.state.readonly ? "readonly" : undefined)}>
			    { ComponentBehavior.labelBlock(this.props, "body1", { marginRight: '15px', color: theme.palette.grey.primary, fontSize: this.state.showPreview ? "9pt" : "14pt" }) }
				{this.state.showPreview &&
					<label htmlFor={inputId} className={classnames(classes.surface, classes.overlay, dropzoneClass, !this.state.value ? classes.emptySurface : undefined, this.state.readonly ? classes.disabledOverlay : undefined, this.state.readonly ? classes.readonlySurface : undefined, !this.state.value ? classes.borderedOverlay : undefined)} style={{display:'flex',cursor: this.state.readonly ? 'default' : 'pointer',...darkSurfaceStyle,...this.sizeStyle()}} >
						{this.state.showPreview && !showImageGallery && this.state.value && <img className={classnames(classes.image, this.state.readonly ? classes.disabledImage : undefined)} alt={this.props.label} title={this.props.label} src={url} style={this.sizeStyle()}/>}
					</label>
				}
			    {(this.state.showPreview && showImageGallery && this.state.value != null) &&
					<div className={classnames(classes.surface, classes.image, dropzoneClass, this.state.readonly ? classes.disabledImage : undefined, this.state.readonly ? classes.readonlySurface : undefined)} style={{top: this.props.label != null && this.props.label !== "" ? '19px' : '0',...darkSurfaceStyle,...this.sizeStyle()}} >
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
