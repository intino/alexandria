import React from "react";
import Typography from '@material-ui/core/Typography';

const ComponentBehavior = (function () {
    const Variants = ["h1","h2","h3","h4","h5","h6","subtitle1","subtitle2","body1","body2","caption","button","overline","srOnly","inherit"];

    return {
        label : (props) => {
            const service = Application.services.translatorService;
            const label = props.label;
            return label != null && label !== undefined ? service.translate(label) : undefined;
        },

        labelBlock : (props, defaultVariant, style) => {
            const { classes } = props;
            const label = ComponentBehavior.label(props);
            const variant = ComponentBehavior.variant(props, defaultVariant != null ? defaultVariant : "body1");
            const labelClass = classes != null ? classes.label : undefined;
            return (label !== undefined) ? <Typography style={style} variant={variant} className={labelClass}>{label}</Typography> : undefined;
        },

        variant : (props, defaultVariant) => {
            if (props.format == null || props.format === "default") return defaultVariant;
            const variant = props.format.split(" ")[0];
            return Variants.indexOf(variant) !== -1 ? variant : defaultVariant;
        }
    }
})();

export default ComponentBehavior;
