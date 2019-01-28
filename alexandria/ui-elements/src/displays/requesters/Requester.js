import { PushService, FileService } from "../../../src/services/Services";

export default class Requester {

    constructor(element) {
        this.element = element;
        this.pushService = PushService;
        this.fileService = FileService;
    };

}