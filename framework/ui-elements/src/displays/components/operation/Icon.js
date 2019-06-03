import React from "react";
import * as Icons from "@material-ui/icons";

export default function MuiIcon(props) {
    return React.createElement(Icons[props.icon]);
}