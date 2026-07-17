import React from "react";
import AbstractImage from "../../../gen/displays/components/AbstractImage";
import ImageNotifier from "../../../gen/displays/notifiers/ImageNotifier";
import ImageRequester from "../../../gen/displays/requesters/ImageRequester";
import {withStyles} from "alexandria-ui-elements/src/util/muiStylesCompat";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import BrowserUtil from "alexandria-ui-elements/src/util/BrowserUtil";
import ImageGallery from 'react-image-gallery';
import Theme from "app-elements/gen/Theme";
import 'react-image-gallery/styles/css/image-gallery.css';

const styles = theme => ({
	surface: {
		position: "relative",
		overflow: "hidden",
	},
	image: {
		display: "block",
		height: "100%",
		width: "100%",
		objectFit: 'contain',
	},
});

class Image extends AbstractImage {

	constructor(props) {
		super(props);
		this.notifier = new ImageNotifier(this);
		this.requester = new ImageRequester(this);
		this.state = {
			...this.state,
			value : this.props.value,
			width : this.props.width,
			height : this.props.height
		};
	};

	componentDidMount() {
		window.addEventListener("resize", this.resize);
		this.resize();
	};

	componentWillUnmount() {
		window.removeEventListener("resize", this.resize);
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const value = this.value();
		if (value == null) return (<div style={{...this.style(),position:'relative'}}></div>);
		const source = value + this.forceParameter();
		const { classes } = this.props;
		const imageClassName = [this.cssRuleSelectors(), classes.image].filter(Boolean).join(" ");
		return (
			<div style={{...this.style(),position:'relative'}} className={classes.surface}>
				{this.props.allowFullscreen && <ImageGallery className={imageClassName} items={[this._galleryItems()]} showThumbnails={false} showBullets={false} showPlayButton={false} /> }
				{!this.props.allowFullscreen && <img className={imageClassName} style={{height: "100%", width: "100%"}} alt={this.props.label} title={this.props.label} src={source}/> }
			</div>
		);
	};

	forceParameter = () => {
		const value = this.value();
		if (value == null) return "";
		return (value.indexOf("?") !== -1 ? "&" : "?") + "r=" + Math.random();
	};

	value = () => {
		const theme = Theme.get();
		const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
		return isDark && this.state.darkValue != null ? this.state.darkValue : this.state.value;
	};

	resize = () => {
		this.setState({ width: this._width(), height: this._height() });
	};

	_galleryItems = () => {
		return {
			original: this.value(),
			thumbnail: this.value()
		};
	};

	_width = () => {
		if (this.props.width == null) return null;
		if (this.props.mobileReduceFactor == null || this.props.mobileReduceFactor === 0) return this.props.width;
		let width = this._number(this.props.width);
		if (BrowserUtil.isMobile()) width = width*(this.props.mobileReduceFactor/100);
		return width + (this._isAbsolute(this.props.width) ? "px" : "%");
	};

	_height = () => {
		if (this.props.height == null) return null;
		if (this.props.mobileReduceFactor == null || this.props.mobileReduceFactor === 0) return this.props.height;
		let height = this._number(this.props.height);
		if (BrowserUtil.isMobile()) height = height*(this.props.mobileReduceFactor/100);
		return height + (this._isAbsolute(this.props.height) ? "px" : "%");
	};

	_number = (value) => {
		return value.toLowerCase().replace("px", "").replace("%", "");
	};

	_isAbsolute = (value) => {
		return value != null && value.toLowerCase().indexOf("px") !== -1;
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		const width = this.state.width;
		const height = this.state.height;
		if (width != null) {
			result.width = width;
			result.minWidth = width;
		}
		if (height != null) {
			result.height = height;
			result.minHeight = height;
		}
		result.imageOrientation = "none";
		const theme = Theme.get();
		const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
		if (this.props.colorInvertedWithDarkMode && isDark) result.filter = "invert(1)";
		return result;
	};

}

export default withStyles(styles, { withTheme: true })(Image);
DisplayFactory.register("Image", withStyles(styles, { withTheme: true })(Image));
