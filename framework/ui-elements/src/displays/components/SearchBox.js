import React from "react";
import {OutlinedInput} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractSearchBox from "../../../gen/displays/components/AbstractSearchBox";
import SearchBoxNotifier from "../../../gen/displays/notifiers/SearchBoxNotifier";
import SearchBoxRequester from "../../../gen/displays/requesters/SearchBoxRequester";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import NumberUtil from "../../util/NumberUtil";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {fieldPalette} from "./FieldStyles";
import Theme from "app-elements/gen/Theme";

const styles = theme => ({
	grow: {
		flexGrow: 1,
	},
	search: {
		position: 'relative',
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
		zIndex: 1,
		pointerEvents: 'none',
		display: 'flex',
		alignItems: 'center',
		justifyContent: 'center',
		color: theme.palette.mode === "dark" ? "rgba(255,255,255,0.72)" : "rgba(15,23,42,0.62)",
	},
	searchIconSmall: {
		width: theme.spacing(7),
	},
	clearIcon: {
		top: '50%',
		right: '0',
		cursor: 'pointer',
		position: 'absolute',
		transform: 'translateY(-50%)',
		marginRight: '16px',
		color: theme.palette.mode === "dark" ? "rgba(255,255,255,0.72)" : "rgba(15,23,42,0.62)",
	},
	clearIconSmall: {
		height: '26px',
	},
	inputRoot: {
		color: fieldPalette(theme).textColor,
		backgroundColor: fieldPalette(theme).background,
		borderRadius: "16px",
		boxShadow: "none",
		transition: "background-color 160ms ease, box-shadow 160ms ease, border-color 160ms ease",
		"& .MuiOutlinedInput-notchedOutline": {
			borderColor: fieldPalette(theme).borderColor,
			borderWidth: "1px",
		},
		"&:hover": {
			backgroundColor: fieldPalette(theme).hoverBackground,
		},
		"&:hover .MuiOutlinedInput-notchedOutline": {
			borderColor: fieldPalette(theme).hoverBorderColor,
		},
		"&.Mui-focused": {
			backgroundColor: fieldPalette(theme).focusBackground,
			boxShadow: `0 0 0 3px ${fieldPalette(theme).focusRing}`,
		},
		"&.Mui-focused .MuiOutlinedInput-notchedOutline": {
			borderColor: fieldPalette(theme).focusColor,
			borderWidth: "1px",
		},
		width: '100%',
	},
	inputRootSmall: {
		color: fieldPalette(theme).textColor,
		backgroundColor: fieldPalette(theme).background,
		borderRadius: "16px",
		boxShadow: "none",
		transition: "background-color 160ms ease, box-shadow 160ms ease, border-color 160ms ease",
		"& .MuiOutlinedInput-notchedOutline": {
			borderColor: fieldPalette(theme).borderColor,
			borderWidth: "1px",
		},
		"&:hover": {
			backgroundColor: fieldPalette(theme).hoverBackground,
		},
		"&:hover .MuiOutlinedInput-notchedOutline": {
			borderColor: fieldPalette(theme).hoverBorderColor,
		},
		"&.Mui-focused": {
			backgroundColor: fieldPalette(theme).focusBackground,
			boxShadow: `0 0 0 3px ${fieldPalette(theme).focusRing}`,
		},
		"&.Mui-focused .MuiOutlinedInput-notchedOutline": {
			borderColor: fieldPalette(theme).focusColor,
			borderWidth: "1px",
		},
		width: '100%',
		height: '38px',
		minHeight: '38px',
	},
	inputInputSmall: {
		color: fieldPalette(theme).textColor,
		paddingTop: "4px !important",
		paddingBottom: "4px !important",
		paddingLeft: theme.spacing(7),
		transition: theme.transitions.create('width'),
		width: '100%',
		paddingRight: '40px',
		"&::placeholder": {
			color: fieldPalette(theme).placeholderColor,
			opacity: 1,
		},
		[theme.breakpoints.up('sm')]: {
			width: 120,
			'&:focus': {
				width: 200,
			},
		},
	},
	inputInput: {
		color: fieldPalette(theme).textColor,
		paddingTop: "11px !important",
		paddingBottom: "11px !important",
		paddingLeft: theme.spacing(10),
		transition: theme.transitions.create('width'),
		width: '100%',
		paddingRight: '46px',
		"&::placeholder": {
			color: fieldPalette(theme).placeholderColor,
			opacity: 1,
		},
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

	constructor(props) {
		super(props);
		this.notifier = new SearchBoxNotifier(this);
		this.requester = new SearchBoxRequester(this);
		this.input = React.createRef();
		this.state = {
			count: null,
			value: '',
			...this.state,
		}
	};

	componentDidMount() {
		if (super.componentDidMount) super.componentDidMount();
		const theme = Theme.get();
		if (theme == null || typeof theme.onChangeMode !== "function") return;
		this.previousChangeModeListener = theme.changeModeListener;
		theme.onChangeMode((mode) => {
			if (typeof this.previousChangeModeListener === "function") this.previousChangeModeListener(mode);
			this.setState({ __searchBoxThemeMode: mode });
		});
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const {classes, theme: providedTheme} = this.props;
		const runtimeTheme = Theme.get();
		const activeTheme = runtimeTheme != null ? runtimeTheme : providedTheme;
		const palette = fieldPalette(activeTheme);
		const isDark = activeTheme != null && activeTheme.palette != null && activeTheme.palette.mode === "dark";
		const darkInputSx = isDark ? {
			backgroundColor: `${palette.background} !important`,
			"& .MuiOutlinedInput-notchedOutline": {
				borderColor: `${palette.borderColor} !important`,
			},
			"&:hover": {
				backgroundColor: `${palette.hoverBackground} !important`,
			},
			"&:hover .MuiOutlinedInput-notchedOutline": {
				borderColor: `${palette.hoverBorderColor} !important`,
			},
			"&.Mui-focused": {
				backgroundColor: `${palette.focusBackground} !important`,
				boxShadow: `0 0 0 3px ${palette.focusRing}`,
			},
			"&.Mui-focused .MuiOutlinedInput-notchedOutline": {
				borderColor: `${palette.focusColor} !important`,
			},
		} : undefined;
		const placeholder = this.translate(this.props.placeholder != null && this.props.placeholder !== "" ? this.props.placeholder : "Search...");
		return (
			<div className={classNames(classes.root, "layout horizontal", "alexandria-searchbox", isDark ? "dark" : undefined)} style={{...this.style(),position:'relative'}}>
				<div className={classes.grow}/>
				<div style={{position:'relative'}}>
					<div className={classes.search} elevation={1}>
						<div className={classNames(classes.searchIcon, this.props.size === "Small" ? classes.searchIconSmall : undefined)} style={isDark ? { color: palette.placeholderColor } : undefined}>
							<SearchIcon/>
						</div>
						<OutlinedInput
							key={isDark ? "dark" : "light"}
							placeholder={placeholder}
							value={this.state.value}
							onChange={this.handleSearch.bind(this)}
							ref={this.input}
							fullWidth
							sx={darkInputSx}
							classes={{
								root: this.props.size === "Small" ? classes.inputRootSmall : classes.inputRoot,
								input: this.props.size === "Small" ? classes.inputInputSmall : classes.inputInput,
							}}
						/>
						{this.state.value != null && this.state.value !== "" && <a onClick={this.handleClear.bind(this)} className={classNames(classes.clearIcon, this.props.size === "Small" ? classes.clearIconSmall : undefined)} style={isDark ? { color: palette.placeholderColor } : undefined}><ClearIcon/></a>}
					</div>
					{this.props.showCountMessage && <div className={classNames(classes.count, "layout horizontal end-justified")}><div title={this.countHint()}>{this.countMessage()}</div></div>}
				</div>
			</div>
		);
	};

	refreshCount = (count) => {
		this.setState({count: count});
	};

	refreshCondition = (condition) => {
		this.setState({value: condition != null ? condition : ""});
	};

	handleClear = (e) => {
		this.setState({value: ''});
		this.requester.search('');
	};

	handleSearch = (e) => {
		this.setState({value: e.target.value});
		this.search(e.target.value);
	};

	search = (condition) => {
		if (this.timeout != null) {
			window.clearTimeout(this.timeout);
			this.timeout = null;
		}
		this.timeout = window.setTimeout(() => this.requester.search(condition), 1000);
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
