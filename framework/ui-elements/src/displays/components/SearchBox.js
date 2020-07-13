import React from "react";
import { Paper, InputBase } from '@material-ui/core';
import SearchIcon from '@material-ui/icons/Search';
import { withStyles } from '@material-ui/core/styles';
import { fade } from '@material-ui/core/styles/colorManipulator';
import AbstractSearchBox from "../../../gen/displays/components/AbstractSearchBox";
import SearchBoxNotifier from "../../../gen/displays/notifiers/SearchBoxNotifier";
import SearchBoxRequester from "../../../gen/displays/requesters/SearchBoxRequester";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import NumberUtil from "../../util/NumberUtil";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	grow: {
		flexGrow: 1,
	},
	search: {
		position: 'relative',
		borderRadius: theme.shape.borderRadius,
		backgroundColor: fade(theme.palette.common.white, 0.75),
		'&:hover': {
			backgroundColor: theme.palette.common.white,
		},
		marginLeft: 0,
		width: '100%',
		[theme.breakpoints.up('sm')]: {
			marginLeft: theme.spacing(1),
			width: 'auto',
		},
	},
	searchIcon: {
		width: theme.spacing(9),
		height: '100%',
		position: 'absolute',
		pointerEvents: 'none',
		display: 'flex',
		alignItems: 'center',
		justifyContent: 'center',
	},
	inputRoot: {
		color: 'inherit',
		width: '100%',
	},
	inputInput: {
		paddingTop: theme.spacing(1),
		paddingRight: theme.spacing(1),
		paddingBottom: theme.spacing(1),
		paddingLeft: theme.spacing(10),
		transition: theme.transitions.create('width'),
		width: '100%',
		[theme.breakpoints.up('sm')]: {
			width: 120,
			'&:focus': {
				width: 200,
			},
		},
	},
	root: {
		width: "100%",
	},
	count: {
		height: "9px",
		right: "0",
	}
});

class SearchBox extends AbstractSearchBox {
	state = {
		count: null
	};

	constructor(props) {
		super(props);
		this.notifier = new SearchBoxNotifier(this);
		this.requester = new SearchBoxRequester(this);
		this.input = React.createRef();
	};

	render() {
		const {classes} = this.props;
		const placeholder = this.translate(this.props.placeholder != null && this.props.placeholder !== "" ? this.props.placeholder : "Search...");
		return (
			<div className={classNames(classes.root, "layout horizontal")} style={this.style()}>
				<div className={classes.grow}/>
				<div style={{position:'relative'}}>
					<Paper className={classes.search} elevation={1}>
						<div className={classes.searchIcon}>
							<SearchIcon/>
						</div>
                        <InputBase
                            placeholder={placeholder}
                            onChange={this.handleSearch.bind(this)}
                            ref={this.input}
                            classes={{
                                root: classes.inputRoot,
                                input: classes.inputInput,
                            }}
                        />
					</Paper>
					<div className={classNames(classes.count, "layout horizontal end-justified")}><div title={this.countHint()}>{this.countMessage()}</div></div>
				</div>
			</div>
		);
	};

	refreshCount = (count) => {
		this.setState({count: count});
	};

	handleSearch = (e) => {
		this.search(e.target.value);
	};

	search = (condition) => {
		if (this.timeout != null) {
			window.clearTimeout(this.timeout);
			this.timeout = null;
		}
		this.timeout = window.setTimeout(() => this.requester.search(condition), 300);
	};

	countMessage = () => {
		if (!this.existResults()) return undefined;
		let message = "";
		if (this.state.count === 0) message = this.translate("no results");
		else if (this.state.count === 1) message = this.translate("one result");
		else if (this.state.count >= 1000) message = "1k+ " + this.translate("results");
		else message = this.state.count + " " + this.translate("results");
		return this.translate(message);
	};

	countHint = () => {
		if (!this.existResults()) return undefined;
		if (this.state.count < 1000) return;
		return this.translate(NumberUtil.format(this.state.count, this.translate("0,0")) + " " + this.translate("results"));
	};

	existResults = () => {
		return this.input.current != null && this.input.current.childNodes[0].value !== "" && this.state.count != null;
	}

}

export default withStyles(styles, { withTheme: true })(SearchBox);
DisplayFactory.register("SearchBox", withStyles(styles, { withTheme: true })(SearchBox));