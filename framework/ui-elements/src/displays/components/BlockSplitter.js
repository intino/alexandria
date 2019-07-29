import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import AbstractBlockSplitter from "../../../gen/displays/components/AbstractBlockSplitter";
import BlockSplitterNotifier from "../../../gen/displays/notifiers/BlockSplitterNotifier";
import BlockSplitterRequester from "../../../gen/displays/requesters/BlockSplitterRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Block from "./Block";
import BlockBehavior from "./behaviors/BlockBehavior";
import BrowserUtil from "../../util/BrowserUtil";
import 'alexandria-ui-elements/res/styles/mobile.css';

const styles = theme => ({});

class BlockSplitter extends AbstractBlockSplitter {
	state = {
		active: 0
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockSplitterNotifier(this);
		this.requester = new BlockSplitterRequester(this);
	};

	render() {
		return (
			<Block
				layout={this.props.layout}
				width={this.props.width}
				height={this.props.height}
				spacing={this.props.spacing}>
				{this.renderChildren()}
			</Block>
		);
	};

	renderChildren = () => {
		const isMobile = BrowserUtil.isMobile();

		return React.Children.map(this.props.children, (child, i) => {
			const isActive = (this.state.active !== -1 && i === this.state.active) || (this.state.active === -1 && i === 0);
			const className = isActive ? "visible" : (isMobile ? "hidden" : "hidden-ifmobile");
			const classNames = BlockBehavior.classNames(child) + " " + className;

			let style = this.style();
			if (isMobile) style.width = "100%";

			return (
				<div className={classNames} style={style}>
					<Button variant="outlined" color="primary" style={{width:'150px'}} className={i!==0 ? "hidden-ifnotmobile" : "hidden"} onClick={this.handleBack.bind(this)}>{this._backLabel()}</Button>
					{child}
				</div>
			);
		});
	};

	show = (index) => {
		this.setState({ active: index });
	};

	_backLabel = () => {
		return this.translate(this.props.splitMobileLabel != null ? this.props.splitMobileLabel : "Back");
	};

	handleBack = () => {
		this.requester.back();
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(BlockSplitter));
DisplayFactory.register("BlockSplitter", withStyles(styles, { withTheme: true })(withSnackbar(BlockSplitter)));