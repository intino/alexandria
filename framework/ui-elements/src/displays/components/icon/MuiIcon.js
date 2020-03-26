import React from "react";
import * as Icons from "@material-ui/icons";

export default function MuiIcon(props) {
console.log(props);
    return React.createElement(Icons[props.icon], props);
}