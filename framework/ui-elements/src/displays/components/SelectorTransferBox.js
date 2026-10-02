import React from "react";
import {withStyles} from "alexandria-ui-elements/src/util/muiStylesCompat";
import AbstractSelectorTransferBox from "../../../gen/displays/components/AbstractSelectorTransferBox";
import SelectorTransferBoxNotifier from "../../../gen/displays/notifiers/SelectorTransferBoxNotifier";
import SelectorTransferBoxRequester from "../../../gen/displays/requesters/SelectorTransferBoxRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import Theme from "app-elements/gen/Theme";
import {Button, List, ListItemButton, ListItemText, Paper} from "@mui/material";
import {fieldPalette} from "./FieldStyles";

const styles = theme => ({
	container: {
		width: "100%",
	},
	label: {
		fontSize: "10pt",
		color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.84)" : "#0000008a",
		marginBottom: "5px",
	},
	layout: {
		display: "grid",
		gridTemplateColumns: "minmax(0, 1fr) auto minmax(0, 1fr)",
		gap: "16px",
		alignItems: "stretch",
		"@media (max-width: 720px)": {
			gridTemplateColumns: "1fr",
		},
	},
	panel: {
		height: "240px",
		display: "flex",
		flexDirection: "column",
		borderRadius: "16px",
		overflow: "hidden",
	},
	panelHeader: {
		padding: "12px 16px 8px 16px",
		fontSize: "10pt",
		fontWeight: 600,
	},
	list: {
		flex: 1,
		minHeight: 0,
		overflow: "auto",
		paddingTop: 0,
		paddingBottom: 0,
	},
	actions: {
		display: "flex",
		flexDirection: "column",
		gap: "8px",
		alignSelf: "center",
		"@media (max-width: 720px)": {
			flexDirection: "row",
			justifyContent: "center",
		},
	},
	actionButton: {
		minWidth: "44px",
		borderRadius: "12px",
	},
	item: {
		minHeight: "32px",
		margin: "2px 6px",
		padding: "4px 10px",
		borderRadius: "10px",
	},
	itemText: {
		"& .MuiListItemText-primary": {
			fontSize: "0.875rem",
			lineHeight: 1.3,
		},
	},
	empty: {
		padding: "16px",
		fontSize: "0.95rem",
		opacity: 0.75,
	},
});

class SelectorTransferBox extends AbstractSelectorTransferBox {

	constructor(props) {
		super(props);
		this.notifier = new SelectorTransferBoxNotifier(this);
		this.requester = new SelectorTransferBoxRequester(this);
		this.state = {
			multipleSelection: this.props.multipleSelection != null ? this.props.multipleSelection : false,
			selection: this.traceValue() ? this.traceValue() : (this.props.selection != null ? this.props.selection : []),
			activeAvailable: [],
			activeSelected: [],
			...this.state
		};
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const { classes, theme } = this.props;
		const runtimeTheme = Theme.get();
		const effectiveTheme = runtimeTheme != null ? runtimeTheme : theme;
		const palette = fieldPalette(effectiveTheme);
		const label = this.props.label;
		const available = this.availableItems();
		const selected = this.selectedItems();
		const textColor = this.state.readonly ? effectiveTheme.palette.grey.A700 : palette.textColor;
		const panelStyle = {
			background: palette.background,
			border: "1px solid " + palette.borderColor,
			...this.highlightBackgroundStyle(),
			...this.highlightStyle(),
		};

		return (
			<div className={classes.container} style={this.style()}>
				{this.renderTraceConsent()}
				{label != null && label !== "" ? <div className={classes.label} style={{ color: textColor }}>{this.translate(label)}</div> : undefined}
				<div className={classes.layout}>
					{this.renderPanel(this.translate("Available"), available, this.state.activeAvailable, "available", panelStyle)}
					<div className={classes.actions}>
						<Button className={classes.actionButton} variant="outlined" disabled={this.state.readonly || available.length === 0 || this.state.activeAvailable.length === 0} onClick={this.moveToSelected}>
							&gt;
						</Button>
						{this.state.multipleSelection &&
							<Button className={classes.actionButton} variant="outlined" disabled={this.state.readonly || available.length === 0} onClick={this.moveAllToSelected}>
								&gt;&gt;
							</Button>
						}
						<Button className={classes.actionButton} variant="outlined" disabled={this.state.readonly || selected.length === 0 || this.state.activeSelected.length === 0} onClick={this.moveToAvailable}>
							&lt;
						</Button>
						{this.state.multipleSelection &&
							<Button className={classes.actionButton} variant="outlined" disabled={this.state.readonly || selected.length === 0} onClick={this.moveAllToAvailable}>
								&lt;&lt;
							</Button>
						}
					</div>
					{this.renderPanel(this.translate("Selected"), selected, this.state.activeSelected, "selected", panelStyle)}
				</div>
			</div>
		);
	};

	renderPanel = (title, items, active, side, style) => {
		const { classes } = this.props;
		return (
			<Paper elevation={0} className={classes.panel} style={style}>
				<div className={classes.panelHeader}>{title}</div>
				<List className={classes.list}>
					{items.length === 0 && <div className={classes.empty}>{this.translate("No options")}</div>}
					{items.map(item => this.renderItem(item, active, side))}
				</List>
			</Paper>
		);
	};

	renderItem = (item, active, side) => {
		const { classes } = this.props;
		const selected = active.indexOf(item.value) >= 0;
		return (
			<ListItemButton key={item.value} selected={selected} className={classes.item} onClick={() => this.handleItemClick(side, item.value)} onDoubleClick={() => this.handleItemDoubleClick(side, item.value)}>
				<ListItemText className={classes.itemText} primary={item.label} />
			</ListItemButton>
		);
	};

	handleItemClick = (side, value) => {
		if (this.state.readonly) return;
		const stateKey = side === "available" ? "activeAvailable" : "activeSelected";
		const current = this.state[stateKey];
		const active = this.state.multipleSelection ? this.toggleValue(current, value) : [value];
		const update = { [stateKey]: active };
		if (side === "available" && !this.state.multipleSelection) update.activeSelected = [];
		if (side === "selected" && !this.state.multipleSelection) update.activeAvailable = [];
		this.setState(update);
	};

	handleItemDoubleClick = (side, value) => {
		if (this.state.readonly) return;
		const selection = this.state.selection || [];
		const next = side === "available"
			? (this.state.multipleSelection ? this.mergeSelection(selection, [value]) : [value])
			: selection.filter(item => item !== value);
		this.applySelection(next);
	};

	moveToSelected = () => {
		const next = this.state.multipleSelection ? this.mergeSelection(this.state.selection, this.state.activeAvailable) : this.state.activeAvailable.slice(0, 1);
		this.applySelection(next);
	};

	moveAllToSelected = () => {
		this.applySelection(this.items().map(item => item.value));
	};

	moveToAvailable = () => {
		const next = this.state.selection.filter(value => this.state.activeSelected.indexOf(value) === -1);
		this.applySelection(next);
	};

	moveAllToAvailable = () => {
		this.applySelection([]);
	};

	applySelection = (selection) => {
		this.trace(selection);
		this.requester.updateSelection(selection);
		this.setState({
			selection,
			activeAvailable: [],
			activeSelected: []
		});
	};

	items = () => {
		const children = this.children();
		const result = React.Children.map(children, option => ({ value: this._name(option), label: this._label(option), item: option })) || [];
		const selection = this.state.selection || [];
		for (let i = 0; i < selection.length; i++) {
			if (this.option(result, selection[i]) == null) result.unshift({ value: selection[i], label: selection[i], item: selection[i], disabled: true });
		}
		return result;
	};

	availableItems = () => {
		const selected = this.state.selection || [];
		return this.items().filter(item => selected.indexOf(item.value) === -1);
	};

	selectedItems = () => {
		const items = this.items();
		return (this.state.selection || []).map(value => this.option(items, value)).filter(item => item != null);
	};

	toggleValue = (values, value) => {
		return values.indexOf(value) >= 0 ? values.filter(item => item !== value) : values.concat(value);
	};

	mergeSelection = (selection, added) => {
		const result = selection.slice();
		for (let i = 0; i < added.length; i++) {
			if (result.indexOf(added[i]) === -1) result.push(added[i]);
		}
		return result;
	};

	_name = (option) => {
		return option.props.name;
	};

	_label = (option) => {
		const label = option.props.label != null && option.props.label !== "" ? option.props.label : option.props.value;
		return this.translate(label != null ? label : "no label");
	};

	option = (options, key) => {
		for (let i = 0; i < options.length; i++) {
			if (options[i].value === key || options[i].label === key) return options[i];
		}
		return null;
	};

	refreshSelection = (selection) => {
		this.setState({ selection: selection || [], activeAvailable: [], activeSelected: [] });
	};

	refreshMultipleSelection = (multipleSelection) => {
		this.setState({
			multipleSelection,
			activeAvailable: [],
			activeSelected: [],
			selection: multipleSelection ? this.state.selection : this.state.selection.slice(0, 1)
		});
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorTransferBox));
DisplayFactory.register("SelectorTransferBox", withStyles(styles, { withTheme: true })(withSnackbar(SelectorTransferBox)));
