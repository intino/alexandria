import React from "react";
import {
    closeSnackbar,
    enqueueSnackbar,
    MaterialDesignContent,
    SnackbarContent,
    SnackbarProvider,
    Transition,
    useSnackbar
} from "notistack/notistack.esm.js";

const withSnackbar = (Component) => {
	const WithSnackbar = React.forwardRef((props, ref) => {
		const snackbar = useSnackbar();
		return <Component {...props} {...snackbar} ref={ref}/>;
	});
	WithSnackbar.displayName = `WithSnackbar(${Component.displayName || Component.name || "Component"})`;
	return WithSnackbar;
};

export {
	closeSnackbar,
	enqueueSnackbar,
	MaterialDesignContent,
	SnackbarContent,
	SnackbarProvider,
	Transition,
	useSnackbar,
	withSnackbar
};
