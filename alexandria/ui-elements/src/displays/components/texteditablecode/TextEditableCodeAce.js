import React from "react";
import brace from 'brace';
import AceEditor from 'react-ace';

import "brace/mode/html";
import "brace/mode/java";
import "brace/mode/javascript";
import "brace/mode/r";

import "brace/theme/eclipse";
import "brace/theme/monokai";

export default function TextEditableCodeAce(props) {
    const aceMode = props.language.toLowerCase();
    const aceTheme = props.theme != null && props.theme.palette.type === "dark" ? "monokai" : "eclipse";
    return (<AceEditor mode={aceMode} theme={aceTheme} fontSize="10pt"
                       width={props.width} height={props.height} className={props.className}
                       value={props.value}
                       onChange={props.onChange}/>);
}