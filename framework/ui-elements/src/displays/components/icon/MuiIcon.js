import React from "react";
import * as Icons from "@mui/icons-material";

export default function MuiIcon(props) {
    const IconComponent = props != null ? Icons[props.icon] : undefined;
    if (IconComponent == null) return null;
    return React.createElement(IconComponent, props);
}
