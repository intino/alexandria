import React from "react";
import AbstractMold from "../../../gen/displays/components/AbstractMold";
import MoldNotifier from "../../../gen/displays/notifiers/MoldNotifier";
import MoldRequester from "../../../gen/displays/requesters/MoldRequester";
import Theme from "../../../gen/Theme";
import {Spinner} from "../../../gen/Displays";

export default class Mold extends AbstractMold {
	state = {
		loading: true
	};

	constructor(props) {
		super(props);
		this.notifier = new MoldNotifier(this);
		this.requester = new MoldRequester(this);
	};

	renderTimeConsuming(components) {
		const theme = Theme.get();;
		return (
			<React.Fragment>
				{this.state.loading ? <div style={{position:'absolute',top:'50%',left:'50%'}}><Spinner mode="Rise" color={theme.palette.secondary.main} loading={this.state.loading}/></div> : undefined }
				<div style={ { display: this.state.loading ? "none" : "block" } }>{components}</div>
			</React.Fragment>
		);
	}

	refreshLoading = (loading) => {
		this.setState({ loading: loading });
	}

}