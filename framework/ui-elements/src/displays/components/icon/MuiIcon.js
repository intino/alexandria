import React from "react";
import * as Icons from "@mui/icons-material";

export default function MuiIcon(props) {
    return React.createElement(Icons[props.icon], props);
}