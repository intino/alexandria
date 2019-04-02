import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockBadge from "../../../gen/displays/components/AbstractBlockBadge";
import BlockBadgeNotifier from "../../../gen/displays/notifiers/BlockBadgeNotifier";
import BlockBadgeRequester from "../../../gen/displays/requesters/BlockBadgeRequester";
import Block from "./Block";

const styles = theme => ({});

class BlockBadge extends AbstractBlockBadge {
	state = {
		value : this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockBadgeNotifier(this);
		this.requester = new BlockBadgeRequester(this);
	};

	render() {
		dddd
		return (
			<Badge style={this.style()} dddddd>
				<Block style={this.style()}
					   layout={this.props.layout}
					   width={this.props.width}
					   height={this.props.height}
					   spacing={this.props.spacing}>
					{this.props.children}
				</Block>
			</Badge>
		);
	};

	refresh = (value) => {
		this.setState({ value });
	};

}

export default withStyles(styles, { withTheme: true })(BlockBadge);