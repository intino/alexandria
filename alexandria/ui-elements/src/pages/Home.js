import React from "react";
import { withStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Theme from '../../gen/Theme';
import Page from "alexandria-ui-elements/src/displays/Page";
import Main from "../../src/displays/roots/Main";

let theme = Theme.create();
const styles = theme => ({});

class Home extends Page {
render() {
    const { classes } = this.props;
    return (
    	<MuiThemeProvider theme={theme}>
        	<CssBaseline />
    		<Main id="3304f5c3-8133-49e3-a510-7f957c8675eb"></Main>
    	</MuiThemeProvider>
    );
}
}

export default withStyles(styles)(Home);