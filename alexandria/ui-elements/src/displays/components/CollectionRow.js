import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCollectionRow from "../../../gen/displays/components/AbstractCollectionRow";
import CollectionRowNotifier from "../../../gen/displays/notifiers/CollectionRowNotifier";
import CollectionRowRequester from "../../../gen/displays/requesters/CollectionRowRequester";
import classNames from "classnames";

const styles = theme => ({});

class CollectionRow extends AbstractCollectionRow {

	constructor(props) {
		super(props);
		this.notifier = new CollectionRowNotifier(this);
		this.requester = new CollectionRowRequester(this);
	};

	render() {
		const instances = this.instances();
		const { classes } = this.props;
		return (
			<div className={classNames(classes.row, "layout horizontal")}>
				{instances.map((instance, index) => {
					return (<div key={index} style={style}>{React.createElement(Elements[instance.tp], instance.pl)}</div>);
				})}

			</div>
		);



		return (<React.Fragment>{this.renderInstances()}</React.Fragment>);
	};

}

export default withStyles(styles, { withTheme: true })(CollectionRow);