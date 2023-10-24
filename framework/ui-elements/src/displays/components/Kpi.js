import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Tooltip, Typography } from "@material-ui/core";
import AbstractKpi from "../../../gen/displays/components/AbstractKpi";
import KpiNotifier from "../../../gen/displays/notifiers/KpiNotifier";
import KpiRequester from "../../../gen/displays/requesters/KpiRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import 'alexandria-ui-elements/res/styles/layout.css';
import classnames from "classnames";

const styles = theme => ({
    link : { cursor: 'pointer' },
    label : { whiteSpace:'nowrap',textOverflow:'ellipsis',overflow:'hidden',marginTop:'2px',textAlign:'center',width:'100%',fontSize:'8pt' },
    labelMedium : { whiteSpace:'nowrap',textOverflow:'ellipsis',overflow:'hidden',marginTop:'2px',textAlign:'center',width:'100%',fontSize:'10pt' },
    labelLarge : { whiteSpace:'nowrap',textOverflow:'ellipsis',overflow:'hidden',marginTop:'2px',textAlign:'center',width:'100%',fontSize:'10pt' },
    value : { fontSize: '14pt' },
    valueMedium : { fontSize: '18pt' },
    valueLarge : { fontSize: '22pt' },
    unit : { border:'1px solid',marginLeft:'2px',padding:'0 2px',marginTop:'2px',fontSize:'7pt' },
    unitMedium : { border:'1px solid',marginLeft:'2px',padding:'0 2px',marginTop:'2px',fontSize:'8pt' },
    unitLarge : { border:'1px solid',marginLeft:'2px',padding:'0 2px',marginTop:'2px',fontSize:'8pt' },
    container : { border:'3px solid transparent', margin: 0 },
    highlighted : { border:'3px solid black' },
});

class Kpi extends AbstractKpi {

	constructor(props) {
		super(props);
		this.notifier = new KpiNotifier(this);
		this.requester = new KpiRequester(this);
		this.state = {
            ...this.state,
		    mode: this.props.mode,
		    label: this.props.label,
		    unit: this.props.unit,
		    colors: { background: this.props.backgroundColor, text: this.props.textColor },
		    value: this.props.value,
		    highlighted: this.props.highlighted,
		}
	};

	render() {
	    const { classes } = this.props;
	    const labelDefined = this.state.label != null && this.state.label !== "";
	    const colors = this.state.colors;
	    const style = { ...this.style(), padding: '10px', backgroundColor: colors.background, color: colors.text, minWidth:'50px', ...this._borderStyle(this.state.highlighted) };
	    const inside = this._insideTextPosition();
	    this._addModeStyle(style);
	    return (
	        <div>
                <div className={classnames(classes.container,"layout vertical center-center")} style={style}>
                    <a className={classes.link} onClick={this.handleSelect.bind(this)}>
                        <div className={classnames("layout horizontal center-center", classes.value)}>
                            <Typography variant="h5" className={this._valueClass()}>{this.state.value}</Typography>
                            {this.state.unit !== "" && <Typography variant="body2" className={this._unitClass()}>{this.state.unit}</Typography>}
                        </div>
                    {(labelDefined && inside) &&
                        <Tooltip title={this.state.label} placement="top" style={{marginTop:"-8px",marginBottom:'8px'}}>
                            <Typography variant="body2" className={this._labelClass()}>{this.state.label}</Typography>
                        </Tooltip>
                    }
                    </a>
                </div>
	            {(labelDefined && !inside) &&
	                <Tooltip title={this.state.label} placement="top">
                        <Typography variant="body2" className={this._labelClass()}>{this.state.label}</Typography>
                    </Tooltip>
                }
            </div>
        );
	};

	refresh = (value) => {
	    this.setState({value});
	};

	refreshLabel = (label) => {
	    this.setState({label});
	};

	refreshColors = (colors) => {
        this.setState({colors});
    };

	refreshHighlighted = (highlighted) => {
        this.setState({highlighted});
    };

    handleSelect = () => {
        this.requester.select();
    };

	_addModeStyle = (style) => {
	    const mode = this.state.mode;
	    style.padding = mode === "Circle" ? "13px" : "5px";
	    style.borderRadius = mode === "Circle" ? "100px" : "0";
	};

	_unitClass = () => {
	    const { classes } = this.props;
	    const size = this.props.size;
	    if (size == "Large") return classes.unitLarge;
	    if (size == "Medium") return classes.unitMedium;
	    return classes.unit;
	};

	_labelClass = () => {
	    const { classes } = this.props;
	    const size = this.props.size;
	    if (size == "Large") return classes.labelLarge;
	    if (size == "Medium") return classes.labelMedium;
	    return classes.label;
	};

	_valueClass = () => {
	    const { classes } = this.props;
	    const size = this.props.size;
	    if (size == "Large") return classes.valueLarge;
	    if (size == "Medium") return classes.valueMedium;
	    return classes.value;
	};

	_insideTextPosition = () => {
	    const position = this.props.textPosition;
	    return position == "Inside";
	};

	_borderStyle = (highlighted) => {
	    const { classes } = this.props;
	    const size = this.props.size;
	    const borderSize = size == "Medium" ? "5px" : (size == "Large" ? "8px" : "3px");
	    return { border: borderSize + " solid " + (highlighted ? this.props.highlightedColor : "transparent") };
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Kpi));
DisplayFactory.register("Kpi", withStyles(styles, { withTheme: true })(withSnackbar(Kpi)));