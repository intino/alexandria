import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTextCode from "../../../gen/displays/components/AbstractTextCode";
import TextCodeNotifier from "../../../gen/displays/notifiers/TextCodeNotifier";
import TextCodeRequester from "../../../gen/displays/requesters/TextCodeRequester";

const styles = theme => ({
	value : {
		background: "#f4f4f4",
		border: "1px solid #ddd",
		padding: "5px 10px"
	}
});

class TextCode extends AbstractTextCode {
	state = {
		value: this.props.children
	};

	constructor(props) {
		super(props);
		this.notifier = new TextCodeNotifier(this);
		this.requester = new TextCodeRequester(this);
	};

	render() {
		const {classes} = this.props;

		return (
			<React.Fragment>
				<code className={classes.value}>{this.state.value}</code>
			</React.Fragment>
		);
	};

	refresh = (value) => {
		this.setState({"value": value});
	};
}

export default withStyles(styles, { withTheme: true })(TextCode);