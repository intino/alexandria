import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMultipleImage from "../../../gen/displays/components/AbstractMultipleImage";
import MultipleImageNotifier from "../../../gen/displays/notifiers/MultipleImageNotifier";
import MultipleImageRequester from "../../../gen/displays/requesters/MultipleImageRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import ComponentBehavior from "./behaviors/ComponentBehavior";
import { IconButton, Button } from '@material-ui/core';
import { Clear } from '@material-ui/icons';
import ImageGallery from 'react-image-gallery';
import 'react-image-gallery/styles/css/image-gallery.css';
import 'alexandria-ui-elements/res/styles/components/multipleimage/styles.css';

const styles = theme => ({
    borderRed : {
        border: "1px solid red"
    },
    borderBlue : {
        border: "1px solid blue"
    }
});

class MultipleImage extends AbstractMultipleImage {

	constructor(props) {
		super(props);
		this.notifier = new MultipleImageNotifier(this);
		this.requester = new MultipleImageRequester(this);
		this.selectedIndex = 0;
		this.inputFiles = React.createRef();
		this.container = React.createRef();
		this.state = {
		    ...this.state,
		    readonly: false,
		    expandedItem : false,
		    images: [],
		};
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		this.selectedIndex = 0;
		const multiple = this.props.multiple;
		const layout = multiple.arrangement.toLowerCase();
		const wrap = multiple.wrap;
		const style = this._multipleStyle(multiple);
		const height = layout === "horizontal" ? '100%' : 'auto';
		const items = this._items();
		return (
		    <div style={{height:height,...this.style()}} ref={this.container}>
                { ComponentBehavior.labelBlock(this.props, "body1", { fontSize:"10pt",color:"#0000008a",marginBottom: "5px" }) }
                <div className={"layout flex " + (wrap ? "wrap " : "") + layout} style={{height:height,marginBottom:'0',position:'relative'}}>
                    { multiple.editable && this._renderAdd() }
                    { multiple.editable && this._renderRemove() }
                    { items.length > 0 &&
                        <ImageGallery items={items}
                                      showPlayButton={false}
                                      showFullscreenButton={this.props.allowFullscreen != undefined && this.props.allowFullscreen}
                                      showThumbnails={false}
                                      showBullets={true}
                                      onSlide={this.handleSlide.bind(this)} />
                    }
                </div>
            </div>
        );
	};

	refreshReadonly = (readonly) => {
	    this.setState({ readonly });
	};

	refreshImages = (images) => {
	    this.setState({ images });
	};

	_renderAdd = () => {
	    return (
	        <input accept="image/*" type="file" ref={this.inputFiles} multiple={true} style={{marginBottom:'5px'}}
	               onChange={this.handleChange.bind(this)}
	               disabled={this.state.readonly} />
	    );
	};

	_renderRemove = () => {
	    const removeButtonDisplay = this.state.readonly || this.state.images.length <= 0 ? "none" : "flex";
	    return (
            <IconButton color="primary" aria-label="upload picture" size="small" component="span" onClick={this.handleRemove.bind(this)}
                        style={{position:'absolute',right:"0",zIndex:"1",background:'white',marginTop:'10px',marginRight:'-12px',border:'1px solid #efefef',display:removeButtonDisplay}}>
                <Clear />
            </IconButton>
	    );
	};

    reset = () => {
        const input = this.inputFiles.current;
        if (input != null) input.value = null;
    };

    _addAllowed = () => {
		const multiple = this.props.multiple;
		return multiple.count == null || multiple.count.max == -1 || this._countItems() < multiple.count.max;
    };

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

	_multipleStyle = (multiple) => {
		let spacingStyle = this._spacingStyle(multiple);
		if (spacingStyle === undefined) spacingStyle = (multiple.arrangement.toLowerCase() === "horizontal") ? { right: 5, top: 2, bottom: 2 } : { right: 0, top: 0, bottom: 0 };
		return { marginRight: spacingStyle.right + "px", marginTop: spacingStyle.top + "px", marginBottom: spacingStyle.bottom + "px"/*, height: "calc(100% - " + spacingStyle.bottom + "px)"*/ };
	};

	_spacingStyle = (multiple) => {
		let spacingSize = multiple.spacing;
		if (spacingSize === 0) return undefined;
		return { right: spacingSize, bottom: spacingSize };
	};

	_items = () => {
	    const result = [];
	    const { classes } = this.props;
	    let height = this.container.current != null ? this.container.current.offsetHeight : "100%";
	    if (height == 0) height = "100%";
	    for (let i=0; i<this.state.images.length; i++) {
	        result.push({original: this.state.images[i], thumbnail: this.state.images[i], originalHeight: height});
	    }
	    return result;
	};

    handleSlide = (index) => {
        this.selectedIndex = index;
    };

    handleAdd = () => {
        this.requester.add();
    };

	handleChange = (e) => {
	    const files = e.target.files;
	    for (let i=0; i<files.length; i++) this.requester.add(files[i]);
	};

	handleRemove = (e) => {
	    this.requester.remove(this.selectedIndex);
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(MultipleImage));
DisplayFactory.register("MultipleImage", withStyles(styles, { withTheme: true })(withSnackbar(MultipleImage)));