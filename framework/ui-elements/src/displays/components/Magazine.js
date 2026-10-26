import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AutoSizer from 'react-virtualized-auto-sizer';
import AbstractMagazine from "../../../gen/displays/components/AbstractMagazine";
import MagazineNotifier from "../../../gen/displays/notifiers/MagazineNotifier";
import MagazineRequester from "../../../gen/displays/requesters/MagazineRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import {CollectionStyles} from "./Collection";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export const MagazineStyles = theme => ({
	...CollectionStyles(theme),
	itemView : {
		height: "100%",
		padding: "0 10px",
		boxSizing: "border-box",
		position: "relative",
		transition: "transform 160ms ease",
		willChange: "transform",
		'&:hover $selector' : {
			opacity: 1,
			pointerEvents: "auto"
		},
		'&:hover': {
			transform: "translateY(-3px)",
			zIndex: 1
		}
	}
});

export class EmbeddedMagazine extends AbstractMagazine {

	constructor(props) {
		super(props);
		this.notifier = new MagazineNotifier(this);
	this.requester = new MagazineRequester(this);
	};

	selectedStyleRules = () => ({
		border: "0",
		borderRadius: "0",
		background: "transparent",
		boxShadow: "none"
	});

	render() {
		const { classes } = this.props;
		if (this.usesPageInfiniteScroll()) return (
			<div ref={this.container} className={classes.collectionViewport} style={{width:"100%",position:"relative"}}>
				<AutoSizer disableHeight>{({ width }) => (this.behavior.renderCollection(undefined, width, "Column"))}</AutoSizer>
			</div>
		);
		return (<div ref={this.container} className={classes.collectionViewport + " flex"} style={{width:"100%",position:'relative'}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width, "Column"))}</AutoSizer></div>);
	};

}

class Magazine extends EmbeddedMagazine {
    constructor(props) {
        super(props);
    }
}

export default withStyles(MagazineStyles, { withTheme: true })(Magazine);
DisplayFactory.register("Magazine", withStyles(MagazineStyles, { withTheme: true })(Magazine));
