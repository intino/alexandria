import React from "react";
import { MuiThemeProvider, withStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Theme from '../../gen/Theme';
import Page from "alexandria-ui-elements/src/displays/Page";
import Main from "../../src/displays/templates/Main";

let theme = Theme.create();

export default class Home extends Page {
	render() {
		return (
			<MuiThemeProvider theme={theme}>
				<CssBaseline/>
				<Main id="d15566ed-1090-4560-a56c-2b2845b5e0aa"></Main>
			</MuiThemeProvider>
		);
	}
}