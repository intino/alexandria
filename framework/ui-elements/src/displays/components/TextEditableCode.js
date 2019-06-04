import React, { Suspense } from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTextEditableCode from "../../../gen/displays/components/AbstractTextEditableCode";
import TextEditableCodeNotifier from "../../../gen/displays/notifiers/TextEditableCodeNotifier";
import TextEditableCodeRequester from "../../../gen/displays/requesters/TextEditableCodeRequester";
import CodeBehavior from "./behaviors/CodeBehavior";
import Spinner from "../../../src/displays/components/Spinner";
import { Theme } from "../../../gen/Theme";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import { withSnackbar } from 'notistack';
import Delayer from '../../util/Delayer';

const TextEditableCodeAce = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./texteditablecode/TextEditableCodeAce"), 300));
	});
});

const styles = theme => ({
	editor: {
		width: "100%",
		minHeight: "100px",
		fontSize: "12pt"
	}
});

class TextEditableCode extends AbstractTextEditableCode {
	state = {
		value : this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new TextEditableCodeNotifier(this);
		this.requester = new TextEditableCodeRequester(this);
	};

	handleChange(value) {
		Delayer.execute(this, () => this.requester.notifyChange(value.replace(/\+/g, "&plus;")), 500);
	};

	render() {
		const { classes, theme } = this.props;
		const value = CodeBehavior.clean(this.state.value);
		return (
			<Suspense fallback={<div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }><Spinner/></div>}>
				<div style={this.style()}><TextEditableCodeAce language={this.props.language} theme={theme} className={classes.editor}
															   width="100%" height="100%"
															   value={value} onChange={this.handleChange.bind(this)}/></div>
			</Suspense>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(TextEditableCode));
DisplayFactory.register("TextEditableCode", withStyles(styles, { withTheme: true })(withSnackbar(TextEditableCode)));