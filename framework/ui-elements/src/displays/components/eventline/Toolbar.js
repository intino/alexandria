import React from "react";
import classnames from "classnames";
import { Typography, IconButton } from '@material-ui/core';
import { FirstPage, NavigateBefore, NavigateNext, LastPage } from "@material-ui/icons";
import Theme from "app-elements/gen/Theme";

const EventlineToolbarStyles = {
    label : { fontSize:'12pt',marginRight:'5px' },
    icon : { height:'20px',width:'20px' },
};

const EventlineToolbar = ({ label, arrangement, toolbar, onPrevious, onNext, onFirst, onLast, translate, classes }) => {
    const theme = Theme.get();
    const handleFirst = (e) => { e.stopPropagation(); onFirst();};
    const handlePrevious = (e) => { e.stopPropagation(); onPrevious();};
    const handleNext = (e) => { e.stopPropagation(); onNext();};
    const handleLast = (e) => { e.stopPropagation(); onLast();};
    const previousColor = toolbar.canPrevious ? theme.palette.primary.main : theme.palette.grey.A900;
    const nextColor = toolbar.canNext ? theme.palette.primary.main : theme.palette.grey.A900;
    const title = translate(label.title != null ? label.title : null);
    const toolbarLayout = "layout horizontal center " + (arrangement === "Left" ? "start-justified" : "end-justified");
    return (
        <div className={toolbarLayout}>
            {title && <div className="layout vertical flex" style={{marginRight:'15px',fontSize:'18pt',display: title === "" ? "none" : "block",...label.style}}>{title}</div>}
            <div style={{marginBottom:'1px'}} className="layout horizontal">
                <div className="layout vertical"><Typography style={EventlineToolbarStyles.label}>{toolbar.label}</Typography></div>
                <div className="layout horizontal">
                    <IconButton disabled={!toolbar.canPrevious} onClick={handleFirst} size="small" style={{color:previousColor}}><FirstPage style={EventlineToolbarStyles.icon}/></IconButton>
                    <IconButton disabled={!toolbar.canPrevious} onClick={handlePrevious} size="small" style={{color:previousColor}}><NavigateBefore style={EventlineToolbarStyles.icon}/></IconButton>
                    <IconButton disabled={!toolbar.canNext} onClick={handleNext} size="small" style={{color:nextColor}}><NavigateNext style={EventlineToolbarStyles.icon}/></IconButton>
                    <IconButton disabled={!toolbar.canNext} onClick={handleLast} size="small" style={{color:nextColor}}><LastPage style={EventlineToolbarStyles.icon}/></IconButton>
                </div>
            </div>
        </div>
    );
};
export default EventlineToolbar;