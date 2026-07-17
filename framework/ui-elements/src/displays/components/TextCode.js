import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractTextCode from "../../../gen/displays/components/AbstractTextCode";
import TextCodeNotifier from "../../../gen/displays/notifiers/TextCodeNotifier";
import TextCodeRequester from "../../../gen/displays/requesters/TextCodeRequester";
import CodeBehavior from "./behaviors/CodeBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {marked} from "marked";
import Theme from "app-elements/gen/Theme";

const styles = theme => ({
	value : {
		padding: "5px 10px",
		display: "block",
		overflow: "auto"
	}
});

class TextCode extends AbstractTextCode {

	constructor(props) {
		super(props);
		this.notifier = new TextCodeNotifier(this);
		this.requester = new TextCodeRequester(this);
		this.state = {
			...this.state,
    		value: this.props.value
		}
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const {classes} = this.props;
		const value = CodeBehavior.clean(this.state.value);
			const theme = Theme.get();
			const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
		const style = {
			...this.style(),
			background: isDark ? "rgba(15,23,42,0.72)" : "#f4f4f4",
			border: isDark ? "1px solid rgba(148,163,184,0.22)" : "1px solid #ddd",
			color: isDark ? "rgba(226,232,240,0.92)" : "inherit"
		};

		return (
			<React.Fragment>
				<code style={style} className={classes.value}
					  dangerouslySetInnerHTML={{__html: this.format(value)}}></code>
			</React.Fragment>
		);
	};

	format = (value) => {
	    if (this.props.language !== "Markdown") return value;
	    return marked.parse(value);
	}

	refresh = (value) => {
		this.setState({"value": value});
	};
}

export default withStyles(styles, { withTheme: true })(TextCode);
DisplayFactory.register("TextCode", withStyles(styles, { withTheme: true })(TextCode));
