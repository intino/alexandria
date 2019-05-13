import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Select, MenuItem, FormControl } from "@material-ui/core";
import I18nComponent from "../../I18nComponent";

const styles = theme => ({
	options : {
		marginBottom: "20px",
		width: "100%"
	}
});

class OptionSelector extends I18nComponent {

	state = {
		option: this.props.options != null && this.props.options.length > 0 ? this.props.options[0] : ""
	};

	constructor(props) {
		super(props);
	};

	render = () => {
		const options = this._options();
		if (options.length <= 1) return;
		return (<FormControl className={this.props.classes.options}>
				<Select value={this.state.option} style={{width:"100%"}} displayEmpty onChange={this.handleOptionChange.bind(this)}>
					<MenuItem value="" disabled>{this.translate("Select an option")}</MenuItem>
					{options.map((option, i) => <MenuItem key={i} value={option}>{option}</MenuItem>)}
				</Select>
			</FormControl>
		);
	};

	handleOptionChange(e) {
		if (this.props.onSelect) this.props.onSelect(e.target.value);
		this.setState({ option: e.target.value });
	};

	_options = () => {
		return this.props.options != null ? this.props.options : [];
	};

}

export default withStyles(styles, { withTheme: true })(OptionSelector);