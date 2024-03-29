import React, { createRef, useRef, useState } from "react";
import { useDrag, useDrop } from "react-dnd";
import ItemTypes from "./ItemTypes";
import classnames from "classnames";
import { ArrowDropDown, ArrowDropUp, DragIndicator } from "@material-ui/icons";
import { Tooltip, Typography, Icon, Popover } from '@material-ui/core';

const DefaultStyle = {
  margin: "0",
  cursor: "move"
};

const ReelSignal = ({ signal, index, id, moveSignal, classes, translate, style, stepWidth }) => {
    const ref = useRef(null);
    const [fullView, setFullView] = useState(false);
    const [selection, setSelection] = useState(null);
    const chart = createRef();
    let previousValue = null;
    const [{ handlerId }, drop] = useDrop({
        accept: ItemTypes.REEL_SIGNAL,
        collect(monitor) {
            return {
                handlerId: monitor.getHandlerId(),
            }
        },
        hover(item, monitor) {
            if (!ref.current) return;
            const dragIndex = item.index;
            const hoverIndex = index;
            if (dragIndex === hoverIndex) return;
            const hoverBoundingRect = ref.current.getBoundingClientRect();
            const hoverMiddleY = (hoverBoundingRect.bottom - hoverBoundingRect.top) / 2;
            const hoverMiddleX = (hoverBoundingRect.right - hoverBoundingRect.left) / 2;
            const clientOffset = monitor.getClientOffset();
            const hoverClientY = clientOffset.y - hoverBoundingRect.top;
            const hoverClientX = clientOffset.x - hoverBoundingRect.left;
            const upwards = dragIndex > hoverIndex && hoverClientY > hoverMiddleY;
            const downwards = dragIndex < hoverIndex && hoverClientY < hoverMiddleY;
            const leftwards = dragIndex > hoverIndex && hoverClientX > hoverMiddleX;
            const rightwards = dragIndex < hoverIndex && hoverClientX < hoverMiddleX;
            if (upwards && (leftwards || rightwards)) return;
            if (downwards && (leftwards || rightwards)) return;
            moveSignal(dragIndex, hoverIndex);
            item.index = hoverIndex;
        }
    });
    const [{ isDragging }, drag] = useDrag({
        item: () => { return { id, index } },
        type: ItemTypes.REEL_SIGNAL,
        collect: monitor => ({
          isDragging: monitor.isDragging()
        })
    });
	const handlePopoverOpen = (block, endDate, annotation, event) => {
	    setSelection({block: block, endDate: endDate, annotation: annotation, anchorEl: event.currentTarget});
	};
	const handlePopoverClose = (event) => {
	    setSelection(null);
	};
	const annotationLabel = (annotation) => {
        let result = "";
        annotation.entries.forEach(a => result += (a + "\n"));
        return result;
	};
	const renderAnnotation = (annotation, color, paddingTop) => {
	    return (<div style={{color:color,fontWeight:'bold',textAlign:'center',marginTop:'-2px',paddingTop:paddingTop}} title={annotationLabel(annotation)}>&#8226;&#8226;&#8226;</div>);
	};
    const renderBlock = (block, endDate, color, annotation) => {
        if (block.length == 0) return (<React.Fragment/>);
        const isEmpty = block[0].value === "" || block[0].value === " ";
        const background = isEmpty ? "transparent" : color;
        const style = { position: 'relative', width: (stepWidth*block.length) + "px", borderRadius: isEmpty ? '0' : '3px', backgroundColor: background, position: 'relative' };
        return (
            <div className={classes.signalStep} style={style} onClick={e => handlePopoverOpen(block, endDate, annotation, e)}>
                {annotation != null && renderAnnotation(annotation, isEmpty ? "#ed6c03" : "white", isEmpty ? '3px' : '2px')}
            </div>
        );
    };
    const findAnnotation = (signal, date) => {
        const annotations = signal.annotations;
        for (let i=0; i<annotations.length; i++) {
            annotations[i].index = i;
            if (annotations[i].date == date) return annotations[i];
        }
        return null;
    };
    const renderSteps = (signal) => {
        let i=0;
        const result = [];
        const steps = signal.steps;
        let block = [];
        let current = null;
        for (i=0; i<steps.length; i++) {
            const value = steps[i].value;
            const date = steps[i].date;
            if (current != null && value != current) {
                result.push(renderBlock(block, i < steps.length-1 ? steps[i+1].date : date, signal.color, findAnnotation(signal, date)));
                block = [];
            }
            block.push({ value: value, date: steps[i].date });
            current = value;
        }
        if (block.length > 0) result.push(renderBlock(block, steps[steps.length-1].date, signal.color, findAnnotation(signal, steps[steps.length-1].date)));
        return result;

    };
    const renderValue = (signal) => {
        if (signal.type === "Empty") return (<div className={classes.emptySignal}></div>);
        return (
            <div className="layout horizontal center">
                <Tooltip title={signal.label} placement="top">
                    <Typography variant="body2" className={classes.signalLabel}>{signal.label}</Typography>
                </Tooltip>
                <div style={{position:'relative'}} className="layout horizontal center">
                    <div style={{borderTop:"1px solid",position:'absolute',width:'100%'}}></div>
                    {renderSteps(signal)}
                </div>
            </div>
        );
    };
	const renderBlockPopup = () => {
	    return (
            <Popover className={classes.popover} classes={{paper:classes.signalStepBlock}} open={selection != null}
                    anchorEl={selection != null ? selection.anchorEl : null}
                    anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
                    transformOrigin={{ vertical: 'bottom', horizontal: 'center' }}
                    onClose={e => handlePopoverClose(e)}
                    disableRestoreFocus>
                { (selection != null && selection.annotation != null) &&
                    <div style={{color:selection.annotation.color}} dangerouslySetInnerHTML={{__html: annotationLabel(selection.annotation).replaceAll("\n", "<br/>")}}></div>
                }
                { (selection != null && selection.block.length > 1) &&
                    <React.Fragment>
                        <div className="layout horizontal center">
                            <Typography variant="body2" style={{marginRight:'5px',color:'#777',width:'60px'}}>{translate("since")}</Typography>
                            {selection.block[0].date}
                        </div>
                        <div className="layout horizontal center">
                            <Typography variant="body2" style={{marginRight:'5px',color:'#777',width:'60px'}}>{translate("until")}</Typography>
                            {selection.block[selection.block.length-1].date}
                        </div>
                    </React.Fragment>
                }
                { (selection != null && selection.block.length == 1) &&
                    <React.Fragment>
                        <div className="layout horizontal center">
                            <Typography variant="body2" style={{marginRight:'5px',color:'#777',width:'60px'}}>{translate("at")}</Typography>
                            {selection.block[0].date}
                        </div>
                    </React.Fragment>
                }
            </Popover>
        );
	};
    const opacity = isDragging ? 0 : 1;
    drag(drop(ref));
    return (
        <div ref={ref} style={{ ...DefaultStyle, ...style, opacity, position: 'relative' }}
             data-handler-id={handlerId} onClick={() => setFullView(!fullView)}
             onMouseLeave={() => setFullView(false)}>
            <div className="layout horizontal">
                {renderValue(signal)}
                {renderBlockPopup()}
            </div>
        </div>
    );
};
export default ReelSignal;