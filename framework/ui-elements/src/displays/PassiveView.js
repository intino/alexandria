import I18nComponent from "./I18nComponent";

export default class PassiveView extends I18nComponent {
    state = {
        socket: null
    };

    constructor(props) {
        super(props);
        this.notifier = null;
        this.requester = null;
    };

    name = () => {
        return this.constructor.name;
    };

    socket = () => {
        return this.state.socket;
    };

}