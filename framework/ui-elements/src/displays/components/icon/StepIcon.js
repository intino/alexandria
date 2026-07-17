import React, {Suspense} from "react";
import CheckCircle from '@mui/icons-material/CheckCircle';
import Warning from '@mui/icons-material/Warning';
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';

const styles = theme => ({
    root: {
        display: 'block',
        color: theme.palette.text.disabled
    },
    active: {
        color: theme.palette.primary.main,
    },
    completed: {
        color: theme.palette.primary.main,
    },
    error: {
        color: theme.palette.error.main,
    }
});

const MaterialIconMui = React.lazy(() => {
    return new Promise(resolve => {
        setTimeout(() => resolve(import("./MuiIcon"), 300));
    });
});

function clsx(props, classes){
    let classNames = [];
    classNames.push(classes.root);
    if (props.active) classNames.push(classes.active);
    if (props.completed) classNames.push(classes.completed);
    if (props.error) classNames.push(classes.error);
    return classNames.join(" ");
}

function StepIcon(props) {
    const { completed, error, icon, classes } = props;
    const classNames = clsx(props, classes);
    if (error) {
        return <Warning className={classNames}/>;
    }
    if (completed) {
        return <CheckCircle className={classNames}/>;
    }
    return (
        <Suspense fallback={<div style={{width: "24px"}}/>}>
            <div className={classNames}><MaterialIconMui icon={String(icon)}/></div>
        </Suspense>
    );
}

export default withStyles(styles)(StepIcon);