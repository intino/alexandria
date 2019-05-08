import I18nComponent from "./I18nComponent";

export default class PassiveView extends I18nComponent {

    constructor(props) {
        super(props);
        this.notifier = null;
        this.requester = null;
    };

    name = () => {
        return this.constructor.name;
    }

}