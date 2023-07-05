import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { ButtonGroup, Button, Popper, Grow, Paper, ClickAwayListener, MenuItem, MenuList } from '@material-ui/core';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import AbstractActionSplit from "../../../gen/displays/components/AbstractActionSplit";
import ActionSplitNotifier from "../../../gen/displays/notifiers/ActionSplitNotifier";
import ActionSplitRequester from "../../../gen/displays/requesters/ActionSplitRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class ActionSplit extends AbstractActionSplit {

	constructor(props) {
		super(props);
		this.notifier = new ActionSplitNotifier(this);
		this.requester = new ActionSplitRequester(this);
		this.state = {
			...this.state,
			options: this.props.options != null ? this.props.options : [],
			option : this.traceValue() != null ? this.traceValue() : this.props.option,
			readonly : this.props.readonly,
			selectedIndex : this.props.defaultOption != null ? this._indexOf(this.props.defaultOption) : 0,
			open: false,
		};
		this.anchorRef = React.createRef(null);
	};

	renderTrigger = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		const mode = this.props.mode.toLowerCase();
		let trigger = (<React.Fragment/>);
		if (mode === "splitbutton") trigger = this.renderSplitButton();
		else if (mode === "iconsplitbutton") trigger = this.renderIconButton(this.anchorRef);
		else if (mode === "materialiconsplitbutton") trigger = this.renderMaterialIconButton(this.anchorRef);
		return (
		    <React.Fragment>
		        {trigger}
                {this.renderDialog()}
            </React.Fragment>
		);
	};

	renderSplitButton = () => {
	    return (
            <ButtonGroup variant={this._highlightVariant()} style={this.style()} size={this._size()}
                         color="primary" ref={this.anchorRef} aria-label={this.props.label}>
              <Button onClick={this.handleClick.bind(this)}><div style={{overflow:'hidden',textOverflow:'ellipsis',whiteSpace:'nowrap'}}>{this.state.options[this.state.selectedIndex]}</div></Button>
              <Button color="primary" size="small" aria-controls={this.state.open ? 'split-button-menu' : undefined}
                      aria-expanded={this.state.open ? 'true' : undefined} aria-label="select merge strategy" aria-haspopup="menu"
                      onClick={this.handleToggle.bind(this)}>
                <ArrowDropDownIcon />
              </Button>
            </ButtonGroup>
        );
	};

	clickEvent = () => {
	    return this.handleOpen.bind(this);
	};

	renderDialog = () => {
	    return (
            <Popper open={this.state.open} anchorEl={this.anchorRef.current} role={undefined} transition disablePortal style={{zIndex:1,marginRight:'20px'}}>
              {({ TransitionProps, placement }) => (
                <Grow {...TransitionProps} style={{transformOrigin: placement === 'bottom' ? 'center top' : 'center bottom'}}>
                  <Paper>
                    <ClickAwayListener onClickAway={this.handleClose.bind(this)}>
                      <MenuList id="split-button-menu">
                        {this.state.options.map((option, index) => (
                          <MenuItem key={option} selected={index === this.state.selectedIndex}
                                    onClick={this.handleMenuItemClick.bind(this, index)}>
                            <div style={{whiteSpace:'wrap'}}>{option}</div>
                          </MenuItem>
                        ))}
                      </MenuList>
                    </ClickAwayListener>
                  </Paper>
                </Grow>
              )}
            </Popper>
        );
	};

	refreshOptions = (options) => {
	    this.setState({options});
	};

	refreshOption = (value) => {
		this.setState({ selectedIndex: this._indexOf(value) });
	};

    handleClick = () => {
        this.executeOption(this.state.selectedIndex);
    };

    handleMenuItemClick = (index) => {
        this.setState({selectedIndex : index, open: false});
        this.executeOption(index);
    };

    handleToggle = () => {
        this.setState({ open: !this.state.open});
    };

    handleOpen = (event) => {
        this.setState({open: true});
    };

    handleClose = (event) => {
        if (this.anchorRef.current && this.anchorRef.current.contains(event.target)) return;
        this.setState({open: false});
    };

    executeOption = (index) => {
        if (!this.canExecute()) return;
        const option = this.state.options[index];
        this.requester.execute(option);
        this.trace(option);
    };

    _indexOf = (option) => {
        const options = this.state.options != null ? this.state.options : this.props.options;
        for (var i=0; i<options.length; i++) {
            if (options[i] === option) return i;
        }
        return 0;
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(ActionSplit));
DisplayFactory.register("ActionSplit", withStyles(styles, { withTheme: true })(withSnackbar(ActionSplit)));