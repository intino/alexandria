import React, { Suspense } from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMaterialIcon from "../../../gen/displays/components/AbstractMaterialIcon";
import MaterialIconNotifier from "../../../gen/displays/notifiers/MaterialIconNotifier";
import MaterialIconRequester from "../../../gen/displays/requesters/MaterialIconRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

const MaterialIconMui = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./icon/MuiIcon"), 300));
	});
});

class MaterialIcon extends AbstractMaterialIcon {
	constructor(props) {
		super(props);
		this.notifier = new MaterialIconNotifier(this);
		this.requester = new MaterialIconRequester(this);
	};

	render() {
		return (
			<Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
				{this.renderLayer(<div style={{color:this._color(),...this.style()}}><MaterialIconMui aria-label={this._title()} icon={this._icon()}/></div>)}
			</Suspense>
		);
	}

	_color = () => {
		if (this.state.color != null) return this.state.color;
		if (this.props.color != null) return this.props.color;
		return "primary";
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(MaterialIcon));
DisplayFactory.register("MaterialIcon", withStyles(styles, { withTheme: true })(withSnackbar(MaterialIcon)));