import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Badge from '@material-ui/core/Badge';
import AbstractBlockBadge from "../../../gen/displays/components/AbstractBlockBadge";
import BlockBadgeNotifier from "../../../gen/displays/notifiers/BlockBadgeNotifier";
import BlockBadgeRequester from "../../../gen/displays/requesters/BlockBadgeRequester";
import Block from "./Block";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({});

class BlockBadge extends AbstractBlockBadge {

	constructor(props) {
		super(props);
		this.notifier = new BlockBadgeNotifier(this);
		this.requester = new BlockBadgeRequester(this);
        this.state = {
            ...this.state,
            value : this.props.value
        };
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const { showZero, max, mode } = this.props;
		return (
			<Badge color="primary" style={this.style()} badgeContent={this.state.value}
				   showZero={showZero} max={max} variant={mode === "Dot" ? "dot" : undefined}>
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
DisplayFactory.register("BlockBadge", withStyles(styles, { withTheme: true })(BlockBadge));