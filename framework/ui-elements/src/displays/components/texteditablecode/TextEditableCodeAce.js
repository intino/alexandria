import React from "react";
import AceEditor from 'react-ace';

import "ace-builds/src-noconflict/mode-html";
import "ace-builds/src-noconflict/mode-java";
import "ace-builds/src-noconflict/mode-javascript";
import "ace-builds/src-noconflict/mode-r";
import "./mode/inl";

import "ace-builds/src-noconflict/theme-eclipse";
import "ace-builds/src-noconflict/theme-monokai";

export default function TextEditableCodeAce(props) {
    const aceMode = props.language.toLowerCase();
    const aceTheme = props.theme != null && props.theme.palette.type === "dark" ? "monokai" : "eclipse";
    const readonly = props.onChange == null;
    return (<AceEditor mode={aceMode} theme={aceTheme} fontSize="10pt" readOnly={readonly}
                       width={props.width} height={props.height} className={props.className}
                       value={props.value} showPrintMargin={!readonly} showGutter={!readonly}
                       onChange={props.onChange} editorProps={{$blockScrolling: Infinity}}/>);
}