import React from "react";
import { Slide, Fade, Grow, Zoom } from '@material-ui/core';

const BlockBehavior = (function () {

    function center(component) {
        return component.props.center != null ? component.props.center : undefined;
    }

    return {
        center : (component) => {
            return center(component);
        },
    }
})();

export default BlockBehavior;