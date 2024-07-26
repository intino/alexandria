import React from "react";
import { withStyles } from '@material-ui/core/styles';
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
		'&:hover' : {
			background: '#ddd'
		},
		'&:hover $selector' : {
			display: 'block'
		}
	},
});

export class EmbeddedMagazine extends AbstractMagazine {

	constructor(props) {
		super(props);
		this.notifier = new MagazineNotifier(this);
		this.requester = new MagazineRequester(this);
	};

	render() {
		return (<div ref={this.container} className="flex" style={{width:"100%"}}><AutoSizer>{({ height, width }) => (this.behavior.renderCollection(height, width, "Column"))}</AutoSizer></div>);
	};

}

class Magazine extends EmbeddedMagazine {
    constructor(props) {
        super(props);
    }
}

export default withStyles(MagazineStyles, { withTheme: true })(Magazine);
DisplayFactory.register("Magazine", withStyles(MagazineStyles, { withTheme: true })(Magazine));