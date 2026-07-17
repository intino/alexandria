import AbstractBaseSorting from "../../../gen/displays/components/AbstractBaseSorting";
import BaseSortingNotifier from "../../../gen/displays/notifiers/BaseSortingNotifier";
import BaseSortingRequester from "../../../gen/displays/requesters/BaseSortingRequester";
import {linkPalette} from "./ThemeTokens";

export const BaseSortingStyles = theme => ({
	link : {
		color: linkPalette(theme).color,
		cursor: "pointer",
		"&:hover": {
			color: linkPalette(theme).hoverColor,
		},
	},
});

export default class BaseSorting extends AbstractBaseSorting {

	constructor(props) {
		super(props);
		this.notifier = new BaseSortingNotifier(this);
		this.requester = new BaseSortingRequester(this);
	};

	handleToggle = () => {
		this.requester.toggle();
	};

	refreshSelection = (selection) => {
	};

}
