import React, {Suspense} from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractTextEditableCode from "../../../gen/displays/components/AbstractTextEditableCode";
import TextEditableCodeNotifier from "../../../gen/displays/notifiers/TextEditableCodeNotifier";
import TextEditableCodeRequester from "../../../gen/displays/requesters/TextEditableCodeRequester";
import CodeBehavior from "./behaviors/CodeBehavior";
import Spinner from "../../../src/displays/components/Spinner";
import Theme from "app-elements/gen/Theme";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import Delayer from '../../util/Delayer';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import {fieldPalette, outlinedSurfaceStyles} from "./FieldStyles";
import classnames from "classnames";

const TextEditableCodeAce = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./texteditablecode/TextEditableCodeAce"), 300));
	});
});

const styles = theme => ({
	container: {
		...outlinedSurfaceStyles(theme),
		background: fieldPalette(theme).background,
		boxShadow: fieldPalette(theme).shadow,
		width: "100%",
		height: "100%",
		minHeight: "100px",
		overflow: "hidden",
	},
	editor: {
		width: "100%",
		minHeight: "100px",
		height: "100%",
		fontSize: "12pt",
		background: fieldPalette(theme).background,
		boxShadow: fieldPalette(theme).shadow,
		color: fieldPalette(theme).textColor,
		"& .ace_editor": {
			borderRadius: "16px",
			backgroundColor: fieldPalette(theme).background,
			color: fieldPalette(theme).textColor,
		},
		"& .ace_editor .ace_scroller, & .ace_editor .ace_layer, & .ace_editor .ace_gutter-layer, & .ace_editor .ace_text-layer, & .ace_editor .ace_marker-layer": {
			backgroundColor: fieldPalette(theme).background,
			color: fieldPalette(theme).textColor,
		},
		"& .ace_editor .ace_content": {
			background: `${fieldPalette(theme).background} !important`,
			backgroundColor: `${fieldPalette(theme).background} !important`,
			color: fieldPalette(theme).textColor,
		},
		"& .ace_editor .ace_gutter": {
			background: `${fieldPalette(theme).background} !important`,
			backgroundColor: `${fieldPalette(theme).background} !important`,
			color: fieldPalette(theme).textColor,
		},
	}
});

class TextEditableCode extends AbstractTextEditableCode {

	constructor(props) {
		super(props);
		this.notifier = new TextEditableCodeNotifier(this);
		this.requester = new TextEditableCodeRequester(this);
		this.state = {
    		...this.state,
    		value : this.props.value,
			readonly: this.props.readonly
		}
	};

	handleChange(value) {
		Delayer.execute(this, () => this.requester.notifyChange(value.replace(/\+/g, "&plus;")), 500);
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
		const runtimeTheme = Theme.get();
		const theme = runtimeTheme != null ? runtimeTheme : this.props.theme;
		const value = CodeBehavior.clean(this.state.value);
		const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
		const editorTheme = isDark ? "monokai" : "eclipse";
		const containerClass = isDark ? "fileeditable-dropzone-dark" : "fileeditable-dropzone";

		return (
			<Suspense fallback={<div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }><Spinner/></div>}>
				<div style={this.style()} className={classnames("texteditable-code", isDark ? "dark" : undefined, this.state.readonly ? "readonly" : undefined, classes.container, containerClass)}><TextEditableCodeAce language={this.props.language} theme={editorTheme} className={classes.editor}
															   width="100%" height="100%" readonly={this.state.readonly}
															   value={value} onChange={this.handleChange.bind(this)}/></div>
			</Suspense>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value != null ? value : "" });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	refreshFocused = (focused) => {
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(TextEditableCode));
DisplayFactory.register("TextEditableCode", withStyles(styles, { withTheme: true })(withSnackbar(TextEditableCode)));
