import Notifier from "../../../gen/displays/notifiers/Notifier";

export default class ComponentNotifier extends Notifier {

	constructor(element) {
		super(element);
		this.setup();
	};

	setup() {
		super.setup();
		if (this.element == null || this.pushLinked) return;
		this.when("refreshLoading").toSelf().execute((parameters) => this.element.refreshLoading(parameters.v));
		this.when("refreshVisibility").toSelf().execute((parameters) => this.element.refreshVisibility(parameters.v));
		this.when("refreshColor").toSelf().execute((parameters) => this.element.refreshColor(parameters.v));
		this.when("userMessage").toSelf().execute((parameters) => this.element.userMessage(parameters.v));
		this.pushLinked = true;
	};

}