import React from "react";
import { Slide, Fade, Grow, Zoom } from '@material-ui/core';

const BlockBehavior = (function () {

    function center(component) {
        const center = component.state.center != null ? component.state.center : component.props.center;
        return center != null ? center : undefined;
    }

    function zoom(component) {
        const zoom = component.state.zoom != null ? component.state.zoom : component.props.zoom;
        return zoom != null ? zoom : { defaultZoom : 12, min: 0, max: 18 };
    }

    return {
        center : (component) => { return center(component); },
        zoom : (component) => { return zoom(component); },
    }
})();

export default BlockBehavior;