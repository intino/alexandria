import React from "react";
import {withStyles} from '@material-ui/core/styles';
import AbstractImageEditable from "../../../gen/displays/components/AbstractImageEditable";
import ImageEditableNotifier from "../../../gen/displays/notifiers/ImageEditableNotifier";
import ImageEditableRequester from "../../../gen/displays/requesters/ImageEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from 'notistack';
import ComponentBehavior from "./behaviors/ComponentBehavior";
import Theme from "app-elements/gen/Theme";
import ImageGallery from 'react-image-gallery';
import Resizer from "react-image-file-resizer";

const styles = theme => ({
	input: {
		display: "none"
	},
	image: {
		display: "block",
		height: "calc(100% - 20px)",
		width: "100%",
		objectFit: 'contain',
		position: "absolute",
	},
	overlay: {
		background: "rgba(0, 0, 0, 0.1)",
		border: "1px solid #efefef",
		width: "100%",
		height: "calc(100% - 20px)",
		"justify-content": "center",
		"align-items": "center",
		cursor: "pointer",
		top: "0"
	},
	bordered: {
		position: "absolute",
		border: "1px solid #efefef",
		width: "100%",
		height: "calc(100% - 20px)",
		"justify-content": "center",
		"align-items": "center",
		top: '0'
	},
	icon: {
		color: "#005ba4"
	},
	download : {
        cursor: 'pointer',
        width: '70px',
        marginLeft: '10px',
        textAlign: 'center',
        color: theme.palette.primary.main,
	},
	remove : {
        cursor: 'pointer',
        width: '70px',
		marginLeft: '10px',
        textAlign: 'center',
        color: theme.palette.primary.main,
	},
	edit : {
		cursor: 'pointer',
		width: '70px',
		textAlign: 'center',
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
        }
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	handleChange(e) {
		let fileInput = false;
		if (e.target.files[0]) fileInput = true;
		if (!fileInput) return;
		this.requester.notifyUploading();
		if (this.props.width == null || this.props.height == null) {
			this.requester.notifyChange(e.target.files[0], progress => {});
			return;
		}
		const width = parseInt(this.props.width.replace("px", ""));
		const height = parseInt(this.props.height.replace("px", ""));
		Resizer.imageFileResizer(e.target.files[0], width, height, "PNG",
			100, 0, (file) => { this.requester.notifyChange(file, progress => {}); },
			"file", width, null
		);
	};

	handleRemove(e) {
		this.requester.notifyChange(null, progress => {});
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
		const theme = Theme.get();
		const labelDisplay = this.state.readonly ? "none" : "flex";
		const borderDisplay = this.state.readonly ? "flex" : "none";
		const removeButtonDisplay = this.state.readonly || this.state.value == null ? "none" : "flex";
		const showImageGallery = this._showImageGallery();
		const url = this.state.value != null ? this.state.value + (this.state.value.indexOf("?") == -1 ? "?" : "&") + Math.random() : null;
        const removeStyle = this.state.readonly ? { display: 'none', pointerEvents: 'none' } : {};

		return (
			<div style={{...this.style(),position:'relative'}}>
			    { ComponentBehavior.labelBlock(this.props, "body1", { color: theme.palette.grey.A700, marginRight: '5px', fontSize: "9pt", color: "#777777" }) }
                {!showImageGallery && this.state.value && <img className={classes.image} alt={this.props.label} title={this.props.label} src={url} />}
                <label htmlFor={inputId} className={classes.overlay} style={{display:labelDisplay}}></label>
                <div className={classes.bordered} style={{display:borderDisplay}}></div>
			    {(showImageGallery && this.state.value != null) &&
					<div className={classes.image} style={{top:'-20px'}} >
						<ImageGallery items={[this._galleryItems()]} showThumbnails={false} showBullets={false} showPlayButton={false} />
					</div>
				}
			    {!this.state.readonly &&
			        <React.Fragment>
                        <input accept="image/*" id={inputId} type="file"
                           className={classes.input} onChange={this.handleChange.bind(this)}
                           disabled={this.state.readonly} value="" />
                    </React.Fragment>
                }
				<div style={{marginTop:'4px'}}>
					{<a className={classes.edit} onClick={this.handleEdit.bind(this)}>{this.translate(this.translate("Edit..."))}</a>}
					{this.state.value && <a className={classes.remove} onClick={this.handleRemove.bind(this)} style={removeStyle}>{this.translate("Remove")}</a>}
					{this.state.value && <a className={classes.download} onClick={this.handleDownload.bind(this)}>{this.translate("Download")}</a>}
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

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this.props.width != null) {
			result.width = this.props.width;
			result.minWidth = this.props.width;
		}
		if (this.props.height != null) {
			result.height = this.props.height;
			result.minHeight = this.props.height;
		}
		result.position = "relative";
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