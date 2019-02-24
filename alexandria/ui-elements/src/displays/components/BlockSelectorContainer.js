import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockSelectorContainer from "../../../gen/displays/components/AbstractBlockSelectorContainer";
import BlockSelectorContainerNotifier from "../../../gen/displays/notifiers/BlockSelectorContainerNotifier";
import BlockSelectorContainerRequester from "../../../gen/displays/requesters/BlockSelectorContainerRequester";
import Block from "./Block";

const styles = theme => ({
	hidden : {
		display: "none"
	}
});

class BlockSelectorContainer extends AbstractBlockSelectorContainer {
	state = {
		selected : 0
	};

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
				   width={this.props.width}>
				{React.Children.map(this.props.children, (child, i) => {
					return (<div className={i !== this.state.selected ? classes.hidden : undefined}>{child}</div>);
				})}
			</Block>
		);
	};

	refreshSelected = (selected) => {
		this.setState({ selected });
	};
}

export default withStyles(styles, { withTheme: true })(BlockSelectorContainer);