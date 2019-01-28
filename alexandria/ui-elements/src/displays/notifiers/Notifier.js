import { PushService } from "../../../src/services/Services";

export default class Notifier {

    constructor(element) {
        this.element = element;
        this.pushService = PushService;
    };

}