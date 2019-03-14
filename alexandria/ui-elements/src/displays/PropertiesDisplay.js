import React from "react";
import Typography from "@material-ui/core/Typography";
import { withStyles } from '@material-ui/core/styles';
import AbstractPropertiesDisplay from "../../gen/displays/AbstractPropertiesDisplay";
import PropertiesDisplayNotifier from "../../gen/displays/notifiers/PropertiesDisplayNotifier";
import PropertiesDisplayRequester from "../../gen/displays/requesters/PropertiesDisplayRequester";
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
	},
	itemName : {
		fontWeight: "bold",
		marginRight: "10px"
	},
    itemNameProps : {
	    color: "#888",
        textTransform: "lowercase"
    }
});

class PropertiesDisplay extends AbstractPropertiesDisplay {
	state = {
		propertyList : []
	};

	constructor(props) {
		super(props);
		this.notifier = new PropertiesDisplayNotifier(this);
		this.requester = new PropertiesDisplayRequester(this);
	};

	render() {
        const { classes } = this.props;
        const propertyList = this.state.propertyList;
		return (
			<ul className={classes.list}>{propertyList != null && propertyList.length > 0 ? this.renderProperties() : this.emptyProperties() }</ul>
		);
	};

	renderProperties = () => {
		return this.state.propertyList.map((property, index) => this.renderProperty(property, index));
	};

	renderProperty = (property, index) => {
        const { classes } = this.props;
        return (<li className={classnames(classes.item, "layout vertical")} key={index}>
                    <div className="layout horizontal">
                        <Typography className={classes.itemName} variant="h6">{property.name}:</Typography>
                        <Typography className={classes.itemNameProps} variant="h6">{property.type}</Typography>
                    </div>
                    <div>{property.description}</div>
                    {this.renderPropertyValues(property)}
                </li>);
    };

	renderPropertyValues = (property) => {
		return (
			<div className="layout horizontal">
				<Typography>{property.type === "Word" ? this.translate("allowed values") : this.translate("default value") }</Typography>
				<Typography>{property.values.join(", ")}</Typography>
			</div>
		);
    };

	emptyProperties = () => {
		return (<li>{this.translate("no properties")}</li>);
	};

	refresh = (propertyList) => {
		this.setState({ propertyList });
	};
}

export default withStyles(styles, { withTheme: true })(PropertiesDisplay);