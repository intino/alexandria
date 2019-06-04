import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTextCode from "../../../gen/displays/components/AbstractTextCode";
import TextCodeNotifier from "../../../gen/displays/notifiers/TextCodeNotifier";
import TextCodeRequester from "../../../gen/displays/requesters/TextCodeRequester";
import CodeBehavior from "./behaviors/CodeBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	value : {
		background: "#f4f4f4",
		border: "1px solid #ddd",
		padding: "5px 10px",
		display: "block",
		overflow: "auto"
	}
});

class TextCode extends AbstractTextCode {
	state = {
		value: this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new TextCodeNotifier(this);
		this.requester = new TextCodeRequester(this);
	};

	render() {
		const {classes} = this.props;
		const value = CodeBehavior.clean(this.state.value);
		return (
			<React.Fragment>
				<code style={this.style()} className={classes.value}
					  dangerouslySetInnerHTML={{__html: value}}></code>
			</React.Fragment>
		);
	};

	refresh = (value) => {
		this.setState({"value": value});
	};
}

export default withStyles(styles, { withTheme: true })(TextCode);
DisplayFactory.register("TextCode", withStyles(styles, { withTheme: true })(TextCode));