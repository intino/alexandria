import React from "react";
import I18nComponent from "./I18nComponent";
import classnames from 'classnames';
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import Collapse from '@mui/material/Collapse';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import RefreshIcon from '@mui/icons-material/Refresh';

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
        const unit = this.props.unit != null ? this.props.unit : "server";

        return (
            <Card ref={this.props.snackbarRef} className={classes.card} style={this.props.style}>
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
                        <Typography gutterBottom>{this.translate("Connection with " + (unit == "Default" ? "server" : unit) + " is broken")}</Typography>
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

const StyledConnectionLost = withStyles(styles, { withTheme: true })(ConnectionLost);

export default React.forwardRef((props, ref) => (
    <StyledConnectionLost {...props} snackbarRef={ref} />
));
