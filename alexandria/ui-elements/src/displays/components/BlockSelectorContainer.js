import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockSelectorContainer from "../../../gen/displays/components/AbstractBlockSelectorContainer";
import BlockSelectorContainerNotifier from "../../../gen/displays/notifiers/BlockSelectorContainerNotifier";
import BlockSelectorContainerRequester from "../../../gen/displays/requesters/BlockSelectorContainerRequester";
import Block from "./Block";

const styles = theme => ({});

class BlockSelectorContainer extends AbstractBlockSelectorContainer {

	constructor(props) {
		super(props);
		this.notifier = new BlockSelectorContainerNotifier(this);
		this.requester = new BlockSelectorContainerRequester(this);
	};

	render() {
        const { classes } = this.props;
		return (
			<Block styleName={this.props.styleName}
				   layout={this.props.layout}
				   width={this.props.width}>{this.props.children}</Block>
		);
	};

	refreshSelected = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BlockSelectorContainer);