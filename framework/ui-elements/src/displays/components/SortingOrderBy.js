import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSortingOrderBy from "../../../gen/displays/components/AbstractSortingOrderBy";
import SortingOrderByNotifier from "../../../gen/displays/notifiers/SortingOrderByNotifier";
import SortingOrderByRequester from "../../../gen/displays/requesters/SortingOrderByRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { Typography, TableCell, TableSortLabel } from "@material-ui/core";
import { BaseSortingStyles } from "./BaseSorting";

const styles = theme => ({
    ...BaseSortingStyles(theme),
    visuallyHidden: {
        border: 0,
        clip: 'rect(0 0 0 0)',
        height: 1,
        margin: -1,
        overflow: 'hidden',
        padding: 0,
        position: 'absolute',
        top: 20,
        width: 1,
    },
});

class SortingOrderBy extends AbstractSortingOrderBy {

	constructor(props) {
		super(props);
		this.notifier = new SortingOrderByNotifier(this);
		this.requester = new SortingOrderByRequester(this);
		this.state = {
		    ...this.state,
		    active: false,
		}
	};

	render() {
		const {classes} = this.props;
		const label = this.attribute("label");
        const active = this.state.active;
        const activeMode = this.props.mode === "asc" ? "desc" : "asc";
        const inactiveMode = this.props.mode;
        const align = this.props.align !== null ? this.props.align.toLowerCase() : 'left';
		return (
		    <TableCell align={this.props.align} style={{padding:'0',margin:'0',border:'0'}}>
                <TableSortLabel active={active} direction={active ? activeMode : inactiveMode} onClick={this.handleToggle.bind(this)} style={this.style()}>
                    <Typography style={{whiteSpace:'nowrap'}} variant={this.variant("body1")} className={classes.link}><span style={this.style()}>{this.translate(label !== "" && label != null ? label : "no label")}</span></Typography>
                </TableSortLabel>
            </TableCell>
		);
	};

	handleToggle = () => {
	    this.setState({active: !this.state.active});
		this.requester.toggle();
	};

	refreshSelection = (selection) => {
	    this.setState({active: selection});
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SortingOrderBy));
DisplayFactory.register("SortingOrderBy", withStyles(styles, { withTheme: true })(withSnackbar(SortingOrderBy)));