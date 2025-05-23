import React from "react";
import AbstractBaseIcon from "../../../gen/displays/components/AbstractBaseIcon";
import BaseIconNotifier from "../../../gen/displays/notifiers/BaseIconNotifier";
import BaseIconRequester from "../../../gen/displays/requesters/BaseIconRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Theme from "app-elements/gen/Theme";

export default class BaseIcon extends AbstractBaseIcon {
	constructor(props) {
		super(props);
		this.notifier = new BaseIconNotifier(this);
		this.requester = new BaseIconRequester(this);
		this.state = {
			...this.state,
			title: this.props.title,
			icon: this.props.icon,
			darkIcon: this.props.darkIcon
		}
	};

	renderLayer = (iconLayer) => {
		if (!this.state.visible) return (<React.Fragment/>);
		if (this._icon() == null) return (<div></div>);
		return iconLayer;
	};

	refreshTitle = (title) => {
		this.setState({title});
	};

	refreshIcon = (icon) => {
		this.setState({icon});
	};

	refreshDarkIcon = (darkIcon) => {
		this.setState({darkIcon});
	};

	_icon = () => {
	    const theme = Theme.get();
	    if (theme.isDark() && this.state.darkIcon != null) return this.state.darkIcon;
	    if (theme.isDark() && this.props.darkIcon != null) return this.props.darkIcon;
		return this.state.icon != null ? this.state.icon : this.props.icon;
	};

	_color = () => {
		return this.state.color != null ? this.state.color : this.props.color;
	};

	_title = () => {
		return this.translate(this.state.title != null ? this.state.title : this.props.title);
	};

}

DisplayFactory.register("BaseIcon", BaseIcon);