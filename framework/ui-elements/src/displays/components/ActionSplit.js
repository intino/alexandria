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
			option : this.traceValue() != null ? this.traceValue() : this.props.option,
			readonly : this.props.readonly,
			selectedIndex : this.props.defaultOption != null ? this._indexOf(this.props.defaultOption) : 0,
			open: false,
		};
		this.anchorRef = React.createRef(null);
	};

	renderTrigger = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		return (
		    <React.Fragment>
                <ButtonGroup variant={this._highlightVariant()} style={this.style()} size={this._size()}
                             color="primary" ref={this.anchorRef} aria-label={this.props.label}>
                  <Button onClick={this.handleClick.bind(this)}>{this.props.options[this.state.selectedIndex]}</Button>
                  <Button color="primary" size="small" aria-controls={this.state.open ? 'split-button-menu' : undefined}
                          aria-expanded={this.state.open ? 'true' : undefined} aria-label="select merge strategy" aria-haspopup="menu"
                          onClick={this.handleToggle.bind(this)}>
                    <ArrowDropDownIcon />
                  </Button>
                </ButtonGroup>
                {this.renderDialog()}
            </React.Fragment>
		);
	};

	renderDialog = () => {
	    return (
            <Popper open={this.state.open} anchorEl={this.anchorRef.current} role={undefined} transition disablePortal>
              {({ TransitionProps, placement }) => (
                <Grow {...TransitionProps} style={{transformOrigin: placement === 'bottom' ? 'center top' : 'center bottom'}}>
                  <Paper>
                    <ClickAwayListener onClickAway={this.handleClose.bind(this)}>
                      <MenuList id="split-button-menu">
                        {this.props.options.map((option, index) => (
                          <MenuItem key={option} selected={index === this.state.selectedIndex}
                                    onClick={this.handleMenuItemClick.bind(this, index)}>
                            {option}
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

    handleClose = (event) => {
        if (this.anchorRef.current && this.anchorRef.current.contains(event.target)) return;
        this.setState({open: false});
    };

    executeOption = (index) => {
        if (!this.canExecute()) return;
        const option = this.props.options[index];
        this.requester.execute(option);
        this.trace(option);
    };

    _indexOf = (option) => {
        for (var i=0; i<this.props.options.length; i++) {
            if (this.props.options[i] === option) return i;
        }
        return 0;
    };
}

export default withStyles(styles, { withTheme: true })(withSnackbar(ActionSplit));
DisplayFactory.register("ActionSplit", withStyles(styles, { withTheme: true })(withSnackbar(ActionSplit)));