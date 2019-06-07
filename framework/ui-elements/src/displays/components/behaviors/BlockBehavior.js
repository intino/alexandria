import React from "react";
import { Slide, Fade, Grow, Zoom } from '@material-ui/core';

const BlockBehavior = (function () {
    return {
        renderAnimation : (animation, visible, content) => {
            const duration = parseInt(animation.duration);
            if (animation.mode === "Slide") return (<Slide in={visible} timeout={duration} direction={animation.direction.toLowerCase()}>{content}</Slide>);
            else if (animation.mode === "Fade") return (<Fade in={visible} timeout={duration}>{content}</Fade>);
            else if (animation.mode === "Grow") return (<Grow in={visible} timeout={duration}>{content}</Grow>);
            else if (animation.mode === "Zoom") return (<Zoom in={visible} timeout={duration}>{content}</Zoom>);
            return content;
        }
    }
})();

export default BlockBehavior;
