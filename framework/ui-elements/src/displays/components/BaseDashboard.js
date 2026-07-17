import AbstractBaseDashboard from "../../../gen/displays/components/AbstractBaseDashboard";
import BaseDashboardNotifier from "../../../gen/displays/notifiers/BaseDashboardNotifier";
import BaseDashboardRequester from "../../../gen/displays/requesters/BaseDashboardRequester";

const styles = theme => ({});

export default class BaseDashboard extends AbstractBaseDashboard {

	constructor(props) {
		super(props);
		this.notifier = new BaseDashboardNotifier(this);
		this.requester = new BaseDashboardRequester(this);
	};

	showLoading = () => {
	};
}
