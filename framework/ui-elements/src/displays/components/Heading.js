import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractHeading from "../../../gen/displays/components/AbstractHeading";
import HeadingNotifier from "../../../gen/displays/notifiers/HeadingNotifier";
import HeadingRequester from "../../../gen/displays/requesters/HeadingRequester";
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({});

class Heading extends AbstractHeading {
	static Height = 40;

	constructor(props) {
		super(props);
		this.notifier = new HeadingNotifier(this);
		this.requester = new HeadingRequester(this);
	};

	render() {
		return (<div className="layout vertical center-justified" style={{height:Heading.Height,...this.props.style}}>{this.props.children}</div>);
	}

}

export default withStyles(styles, { withTheme: true })(Heading);