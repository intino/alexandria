import React from "react";
import I18nComponent from "./I18nComponent";
import classnames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import Collapse from '@material-ui/core/Collapse';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import RefreshIcon from '@material-ui/icons/Refresh';

const styles = theme => ({
    card: {
        maxWidth: 400,
        minWidth: 344,
    },
    typography: {
        fontWeight: 'bold',
        color: "white",
    },
    actionRoot: {
        padding: '8px 8px 8px 16px',
        backgroundColor: '#d32f2f',
    },
    icons: {
        marginLeft: 'auto',
    },
    expand: {
        color: 'white',
        padding: '8px 8px',
        transform: 'rotate(0deg)',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
    collapse: {
        padding: 16,
    },
    checkIcon: {
        fontSize: 20,
        color: '#b3b3b3',
        paddingRight: 4,
    },
    button: {
        padding: 0,
        textTransform: 'none',
    },
});

class ConnectionLost extends I18nComponent {
    state = {
        expanded: false
    };

    constructor(props) {
        super(props);
    };

    handleReload = () => {
        window.location.reload(true);
    };

    handleExpandClick = () => {
        this.setState({expanded: !this.state.expanded});
    };

    render() {
        const { classes } = this.props;
        const expanded = this.state.expanded;

        return (
            <Card className={classes.card}>
                <CardActions classes={{ root: classes.actionRoot }}>
                    <Typography variant="subtitle2" className={classes.typography}>{this.translate("Connection lost")}</Typography>
                    <div className={classes.icons}>
                        <IconButton
                            aria-label={this.translate("Show more")}
                            className={classnames(classes.expand, { [classes.expandOpen]: expanded })}
                            onClick={this.handleExpandClick.bind(this)}
                        >
                            <ExpandMoreIcon />
                        </IconButton>
                    </div>
                </CardActions>
                <Collapse in={expanded} timeout="auto" unmountOnExit>
                    <Paper className={classes.collapse}>
                        <Typography gutterBottom>{this.translate("Connection with server is broken")}</Typography>
                        <Button size="small" className={classes.button} onClick={this.handleReload.bind(this)}>
                            <RefreshIcon className={classes.checkIcon} />
                            {this.translate("Reload page")}
                        </Button>
                    </Paper>
                </Collapse>
            </Card>
        );
    };
}

export default withStyles(styles, { withTheme: true })(ConnectionLost);