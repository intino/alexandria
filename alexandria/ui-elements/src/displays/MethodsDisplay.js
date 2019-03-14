import React from "react";
import Typography from "@material-ui/core/Typography";
import { withStyles } from '@material-ui/core/styles';
import AbstractMethodsDisplay from "../../gen/displays/AbstractMethodsDisplay";
import MethodsDisplayNotifier from "../../gen/displays/notifiers/MethodsDisplayNotifier";
import MethodsDisplayRequester from "../../gen/displays/requesters/MethodsDisplayRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import classnames from "classnames";

const styles = theme => ({
	list : {
		listStyle: "none",
		margin: 0,
		padding: 0
	},
	item : {
		marginBottom: "5px"
	}
});

class MethodsDisplay extends AbstractMethodsDisplay {
	state = {
		methodList: []
	};

	constructor(props) {
		super(props);
		this.notifier = new MethodsDisplayNotifier(this);
		this.requester = new MethodsDisplayRequester(this);
	};

	render() {
		const {classes} = this.props;
		const methodList = this.state.methodList;
		return (
			<ul className={classes.list}>{methodList != null && methodList.length > 0 ? this.renderMethods() : this.emptyMethods()}</ul>
		);
	};

	renderMethods = () => {
		return this.state.methodList.map((property, index) => this.renderMethod(property, index));
	};

	renderMethod = (method, index) => {
		const {classes} = this.props;
		return (<li className={classnames(classes.item, "layout vertical")} key={index}>
			<div className="layout horizontal">
				<Typography variant="h6">{method.name}</Typography>
				<Typography variant="h6">(</Typography>
				<Typography variant="h6">{this.renderParameters(method)}</Typography>
				<Typography variant="h6">) : {method.returnType}</Typography>
			</div>
			<div>{method.description}</div>
		</li>);
	};

	renderParameters = (method) => {
		return method.params.map((param, index) => this.renderParameter(param, index)).join(", ");
	};

	renderParameter = (parameter) => {
		return parameter.name + ": " + parameter.type;
	};

	emptyMethods = () => {
		return (<li>{this.translate("no methods")}</li>);
	};

	refresh = (methodList) => {
		this.setState({methodList});
	};
}

export default withStyles(styles, { withTheme: true })(MethodsDisplay);