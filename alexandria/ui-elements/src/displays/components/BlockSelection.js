import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockSelection from "../../../gen/displays/components/AbstractBlockSelection";
import BlockSelectionNotifier from "../../../gen/displays/notifiers/BlockSelectionNotifier";
import BlockSelectionRequester from "../../../gen/displays/requesters/BlockSelectionRequester";
import Block from "./Block";

const styles = theme => ({
	visible : {
		display: "block"
	},
	hidden : {
		display: "none"
	}
});

class BlockSelection extends AbstractBlockSelection {
	state = {
		visible : false
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockSelectionNotifier(this);
		this.requester = new BlockSelectionRequester(this);
	};

	render() {
        const { classes } = this.props;
		return (
			<div className={this.state.visible ? classes.visible : classes.hidden}>
				<Block styleName={this.props.styleName}
					   layout={this.props.layout}
				   	   width={this.props.width}
				   	   height={this.props.height}
				   	   spacing={this.props.spacing}>
					{this.props.children}
				</Block>
			</div>
		);
	};

	refreshVisibility = (visible) => {
		this.setState({ visible });
	};
}

export default withStyles(styles, { withTheme: true })(BlockSelection);