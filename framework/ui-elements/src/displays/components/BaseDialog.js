import React from "react";
import {AppBar, DialogContent, DialogTitle, Fade, Grow, IconButton, Paper, Slide, Typography, Zoom} from "@mui/material"
import AbstractBaseDialog from "../../../gen/displays/components/AbstractBaseDialog";
import {Close} from "@mui/icons-material";
import 'alexandria-ui-elements/res/styles/layout.css';
import {containerPalette, dialogPaperStyles} from "./ContainerStyles";
import Theme from "app-elements/gen/Theme";

const createDraggablePaper = (id, sizeStyle, owner) => React.forwardRef(function DraggablePaper(props, ref) {
  const paperRef = React.useRef(null);
  const dragStateRef = React.useRef({ dragging: false, startX: 0, startY: 0, baseX: 0, baseY: 0 });
  const [position, setPosition] = React.useState({ x: 0, y: 0 });
  React.useImperativeHandle(ref, () => paperRef.current);

  React.useEffect(() => {
    const stopDragging = () => {
      dragStateRef.current.dragging = false;
    };
    const handleMove = (event) => {
      if (!dragStateRef.current.dragging) return;
      event.preventDefault();
      const dx = event.clientX - dragStateRef.current.startX;
      const dy = event.clientY - dragStateRef.current.startY;
      setPosition({
        x: dragStateRef.current.baseX + dx,
        y: dragStateRef.current.baseY + dy,
      });
    };
    window.addEventListener("mousemove", handleMove);
    window.addEventListener("mouseup", stopDragging);
    window.addEventListener("mouseleave", stopDragging);
    return () => {
      window.removeEventListener("mousemove", handleMove);
      window.removeEventListener("mouseup", stopDragging);
      window.removeEventListener("mouseleave", stopDragging);
    };
  }, []);

  const handleMouseDown = (event) => {
    const target = event.target;
    if (!(target instanceof Element)) return;
    if (target.closest(".dialog-draggable-handle") == null) return;
    dragStateRef.current.dragging = true;
    dragStateRef.current.startX = event.clientX;
    dragStateRef.current.startY = event.clientY;
    dragStateRef.current.baseX = position.x;
    dragStateRef.current.baseY = position.y;
    event.preventDefault();
  };

  return (
    <Paper
      ref={paperRef}
      {...props}
      onMouseDown={handleMouseDown}
      style={{
        ...(props.style != null ? props.style : {}),
        ...sizeStyle.call(owner),
        ...dialogPaperStyles(Theme.get() || owner.props.theme),
        transform: `translate3d(${position.x}px, ${position.y}px, 0)`,
        transition: dragStateRef.current.dragging ? "none" : "transform 0.05s linear",
        willChange: "transform",
      }}
    />
  );
});

export const makeDraggable = (sizeStyle, owner) => createDraggablePaper(null, sizeStyle, owner);

export default class BaseDialog extends AbstractBaseDialog {
	state = {
		title: this.props.title,
		size: { height: this.props.height, width: this.props.width },
		opened: false,
		modal: this.props.modal,
	};

	static Styles = theme => ({
		dialogPaper: {
			...dialogPaperStyles(theme),
			backgroundColor: `${containerPalette(theme).surface} !important`,
			backgroundImage: `${dialogPaperStyles(theme).backgroundImage} !important`,
			color: containerPalette(theme).text,
		},
		header : {
			padding: "10px 18px",
			background: containerPalette(theme).surfaceMuted,
			color: containerPalette(theme).title,
			boxShadow: "none",
			borderBottom: `1px solid ${containerPalette(theme).headerBorder}`,
		},
		fullscreen : {
		    overflow: "hidden",
			marginTop: "74px"
		},
		content: {
			padding: "18px 20px 20px !important",
			background: `${containerPalette(theme).surface} !important`,
			color: containerPalette(theme).text,
		},
		title: {
			fontWeight: 600,
			letterSpacing: "-0.01em",
			color: containerPalette(theme).title,
		},
		icon : {
			color: containerPalette(theme).text
		},
	});

	static FadeTransition = React.forwardRef(function Transition(props, ref) {
		return <Fade ref={ref} {...props} />;
	});

	static GrowTransition = React.forwardRef(function Transition(props, ref) {
		return <Grow ref={ref} {...props} />;
	});

	static SlideTransition = React.forwardRef(function Transition(props, ref) {
		return <Slide direction="up" ref={ref} {...props} />;
	});

	static ZoomTransition = React.forwardRef(function Transition(props, ref) {
		return <Zoom ref={ref} {...props} />;
	});

	constructor(props) {
		super(props);
		this.DraggablePaper = createDraggablePaper(this.props.id, this.sizeStyle, this);
	};

	handleClose = () => {
		this.requester.close();
	};

	renderTitle = () => {
		const { classes } = this.props;
		const theme = Theme.get() || this.props.theme;
		const palette = containerPalette(theme);
		const style = this.props.color != null ? { backgroundColor: this.props.color } : { background: palette.surfaceMuted, color: palette.title };
		if (this.props.fullscreen) return (
			<AppBar style={style} className={classes.header}>
				<div className="layout horizontal flex center">
					<Typography component="div" variant="h5" className={classes.title}>{this.translate(this.state.title)}</Typography>
					<div className="layout horizontal end-justified flex"><IconButton onClick={this.handleClose.bind(this)} className={classes.icon}><Close fontSize="large"/></IconButton></div>
				</div>
			</AppBar>
		);
		return (<DialogTitle className="dialog-draggable-handle" style={{cursor:'move', padding:"18px 20px 12px", ...style}} id={this.props.id + "_draggable"}><Typography component="span" variant="h5" className={classes.title}>{this.translate(this.state.title)}</Typography></DialogTitle>);
	};

	renderContent = (content) => {
		const { classes } = this.props;
		const theme = Theme.get() || this.props.theme;
		const palette = containerPalette(theme);
		const className = this.props.fullscreen ? classes.fullscreen : classes.content;
		const contentStyle = { ...(this.style() || {}) };
		delete contentStyle.background;
		delete contentStyle.backgroundColor;
		delete contentStyle.backgroundImage;
		delete contentStyle.color;
		return (<DialogContent className={className} style={{ ...contentStyle, background: palette.surface, color: palette.text }}>{content != null && content()}</DialogContent>);
	};

	open = () => {
		this.setState({ opened: true });
	};

	close = () => {
		this.setState({ opened: false });
	};

	refreshTitle = (title) => {
		this.setState({title});
	};

	refreshSize = (size) => {
		this.setState({size});
	};

	sizeStyle = () => {
	    const result = {};
	    if (this._widthDefined()) {
	        result.width = this.state.size.width;
	        result.margin = "0 auto";
	    }
	    if (this._heightDefined()) result.height = this.state.size.height;
	    return result;
	}

	_widthDefined = () => {
	    return this.state.size.width != null && this.state.size.width.indexOf("-1") === -1;
	}

	_heightDefined = () => {
	    return this.state.size.height != null && this.state.size.height.indexOf("-1") === -1;
	}

    _transition = () => {
        const animation = this.props.animation;
        if (animation == null) return BaseDialog.SlideTransition;
        else if (animation.mode === "Grow") return BaseDialog.GrowTransition;
        else if (animation.mode === "Fade") return BaseDialog.FadeTransition;
        else if (animation.mode === "Zoom") return BaseDialog.ZoomTransition;
        return BaseDialog.SlideTransition;
    };
}
