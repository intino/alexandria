import React, { Component } from "react";
import Theme from "app-elements/gen/Theme";
import { Box, Typography, Paper, LinearProgress } from "@material-ui/core";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import { withStyles } from '@material-ui/core/styles';

const styles = (theme) => ({
    progressItem: {
        marginBottom: theme.spacing(1),
        padding: theme.spacing(1.5),
    },
    progressContainer: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginBottom: theme.spacing(1),
    },
    progressInfo: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'start',
    },
    label: {
        fontWeight: 500,
    }
});

class ProgressBar extends Component {
  render() {
      const { classes, label, info, progress } = this.props;
      return (
        <Paper className={classes.progressItem} elevation={1}>
        <Box className={classes.progressContainer}>
            <Box className={classes.progressInfo}>
                <Typography variant="body2" className={classes.label}>{label}</Typography>
                <Typography variant="body2">{info}</Typography>
            </Box>
            <Typography variant="body2" color="textSecondary">{`${Math.round(progress)}%`}</Typography>
        </Box>
        <LinearProgress variant="determinate" value={progress}/>
        </Paper>
      );
  }
}

export default withStyles(styles, { withTheme: true })(ProgressBar);
DisplayFactory.register("ProgressBar", withStyles(styles, { withTheme: true })(ProgressBar));