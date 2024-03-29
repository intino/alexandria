import React from "react";
import ConnectionLost from "./ConnectionLost";
import {withSnackbar} from "notistack";

class ConnectionChecker extends React.Component {

    constructor(props) {
        super(props);
        this.listenOnClose();
    };

    listenOnClose = () => {
        Application.services.pushService.onClose((unit) => {
            const options = { persist: true, variant: "error", anchorOrigin: { vertical: 'top', horizontal: 'center' }, content: (key) => {
                return (<ConnectionLost id={key} unit={unit}></ConnectionLost>);
            }};
            this.props.enqueueSnackbar('', options);
        });
    };

    render() {
        return (<React.Fragment/>);
    }
}

export default withSnackbar(ConnectionChecker);