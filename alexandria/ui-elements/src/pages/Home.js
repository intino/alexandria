import React from "react";
import { MuiThemeProvider, createMuiTheme, withStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Page from "alexandria-ui-elements/src/displays/Page";
import Main from "../../src/displays/roots/Main";

let theme = createMuiTheme({});
const styles = {};

class Home extends Page {
    render() {
        const { classes } = this.props;
        return (
            <MuiThemeProvider theme={theme}>
                <CssBaseline />
                <Main id="63595d67-1eb8-42a9-8e73-d5a29f318eea"></Main>
            </MuiThemeProvider>
        );
    }
}

export default withStyles(styles)(Home);