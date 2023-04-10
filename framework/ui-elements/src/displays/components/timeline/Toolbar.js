import React from "react";
import classnames from "classnames";
import { Typography, IconButton } from '@material-ui/core';
import { FirstPage, NavigateBefore, NavigateNext, LastPage } from "@material-ui/icons";
import Theme from "app-elements/gen/Theme";

const TimelineToolbarStyles = {
    label : { fontSize:'12pt',marginRight:'5px' },
    icon : { height:'20px',width:'20px' },
};

const TimelineToolbar = ({ label, scales, toolbar, onChangeScale, onPrevious, onNext, onFirst, onLast, translate, classes }) => {
    const theme = Theme.get();
    const handleFirst = (e) => { e.stopPropagation(); onFirst();};
    const handlePrevious = (e) => { e.stopPropagation(); onPrevious();};
    const handleNext = (e) => { e.stopPropagation(); onNext();};
    const handleLast = (e) => { e.stopPropagation(); onLast();};
    const previousColor = toolbar.canPrevious ? theme.palette.primary.main : theme.palette.grey.A900;
    const nextColor = toolbar.canNext ? theme.palette.primary.main : theme.palette.grey.A900;
    const renderScales = () => {
        return (<div>{scales.map((s, idx) => renderScale(s, idx==scales.length-1))}</div>);
    };
    const renderScale = (scale, lastScale) => {
        const style = lastScale ? { border:'1px solid #888' } : { border:'1px solid #888',borderRight:'0' };
        const classNames = scale === toolbar.scale ? classnames(classes.scale, classes.selectedScale) : classes.scale;
        return (<a onClick={(e) => { e.stopPropagation(); onChangeScale(scale)} } style={style} className={classNames}>{scale.substring(0,1)}</a>);
    };
    const title = translate(label.title != null ? label.title : "History");
    return (
        <div className="layout horizontal start center flex">
            <div style={{marginRight:'15px',fontSize:'18pt',display: title === "" ? "none" : "block",...label.style}}>{title}</div>
            <div style={{marginRight:'15px',marginTop:'2px'}}>{renderScales()}</div>
            <div className="layout vertical"><Typography style={TimelineToolbarStyles.label}>{toolbar.label}</Typography></div>
            <div className="layout horizontal">
                <IconButton disabled={!toolbar.canPrevious} onClick={handleFirst} size="small" style={{color:previousColor}}><FirstPage style={TimelineToolbarStyles.icon}/></IconButton>
                <IconButton disabled={!toolbar.canPrevious} onClick={handlePrevious} size="small" style={{color:previousColor}}><NavigateBefore style={TimelineToolbarStyles.icon}/></IconButton>
                <IconButton disabled={!toolbar.canNext} onClick={handleNext} size="small" style={{color:nextColor}}><NavigateNext style={TimelineToolbarStyles.icon}/></IconButton>
                <IconButton disabled={!toolbar.canNext} onClick={handleLast} size="small" style={{color:nextColor}}><LastPage style={TimelineToolbarStyles.icon}/></IconButton>
            </div>
        </div>
    );
};
export default TimelineToolbar;