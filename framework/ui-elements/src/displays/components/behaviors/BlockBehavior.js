import React from "react";
import { Slide, Fade, Grow, Zoom } from '@material-ui/core';

const BlockBehavior = (function () {

    function layoutClasses(block) {
        let layout = block.state != null && block.state.layout != null ? block.state.layout : block.props.layout;
        if (layout == null) return "";
        layout = layout.toLowerCase();
        layout = layout.replace("flexible", "flex");
        layout = layout.replace("centercenter", "center-center");
        layout = layout.replace("preverse", "p-reverse");
        layout = layout.replace("lreverse", "l-reverse");
        layout = layout.replace("rjustified", "r-justified");("tjustified", "t-justified");
        layout = layout.replace("djustified", "d-justified");
        layout = layout.replace("nowrap", "no-wrap");
        return "layout " + layout;
    }

    function hiddenClass(block) {
        const hidden = block.props.hidden;
        if (hidden == null || hidden === "Never") return "";
        return "hidden-" + hidden.toLowerCase();
    }

    return {
        classNames : (block) => {
            let result = layoutClasses(block);
            result += result !== "" ? " " + hiddenClass(block) : "";
            return result.trim();
        },
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