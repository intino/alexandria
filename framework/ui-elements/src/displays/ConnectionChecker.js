import React from "react";
import ConnectionLost from "./ConnectionLost";
import {withSnackbar} from "notistack";

class ConnectionChecker extends React.Component {

    constructor(props) {
        super(props);
        this.listenOnClose();
    };

    listenOnClose = () => {
        Application.services.pushService.onClose(() => {
            const options = { persist: true, variant: "error", anchorOrigin: { vertical: 'top', horizontal: 'center' }, children: (key) => {
                return <ConnectionLost id={key}></ConnectionLost>;
            }};
            this.props.enqueueSnackbar('', options);
        });
    };

    render() {
        return (<React.Fragment/>);
    }
}

export default withSnackbar(ConnectionChecker);