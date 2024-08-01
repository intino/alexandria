import React from "react";
import AbstractImage from "../../../gen/displays/components/AbstractImage";
import ImageNotifier from "../../../gen/displays/notifiers/ImageNotifier";
import ImageRequester from "../../../gen/displays/requesters/ImageRequester";
import {withStyles} from "@material-ui/core";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import BrowserUtil from "alexandria-ui-elements/src/util/BrowserUtil";
import ImageGallery from 'react-image-gallery';
import Theme from "app-elements/gen/Theme";

const styles = theme => ({
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
		if (this.state.value == null) return (<React.Fragment/>);
		return (
			<React.Fragment>
			    {this.props.allowFullscreen && <ImageGallery items={[this._galleryItems()]} showThumbnails={false} showBullets={false} showPlayButton={false} /> }
                {!this.props.allowFullscreen && <img style={this.style()} title={this.props.label} src={this.state.value}/> }
			</React.Fragment>
		);
	};

	resize = () => {
		this.setState({ width: this._width(), height: this._height() });
	};

	_galleryItems = () => {
	    return {
	        original: this.state.value,
	        originalHeight: this._height(),
            originalWidth: this._width(),
	        thumbnail: this.state.value
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
		if (this.props.colorInvertedWithDarkMode && Theme.get().isDark()) result.filter = "invert(1)";
		return result;
	};

}

export default withStyles(styles, { withTheme: true })(Image);
DisplayFactory.register("Image", withStyles(styles, { withTheme: true })(Image));