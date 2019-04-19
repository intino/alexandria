import React from "react";
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
    const readonly = props.onChange == null;
    return (<AceEditor mode={aceMode} theme={aceTheme} fontSize="10pt" readOnly={readonly}
                       width={props.width} height={props.height} className={props.className}
                       value={props.value} showPrintMargin={!readonly} showGutter={!readonly}
                       onChange={props.onChange} editorProps={{$blockScrolling: Infinity}}/>);
}