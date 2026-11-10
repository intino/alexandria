import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractHeading from "../../../gen/displays/components/AbstractHeading";
import HeadingNotifier from "../../../gen/displays/notifiers/HeadingNotifier";
import HeadingRequester from "../../../gen/displays/requesters/HeadingRequester";
import 'alexandria-ui-elements/res/styles/layout.css';
import 'alexandria-ui-elements/res/styles/hidden.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {columnWidthStyle, TableColumnContext} from "./TableColumnLayout";

const styles = theme => ({});

class Heading extends AbstractHeading {
	static Height = 40;
    static contextType = TableColumnContext;

	constructor(props) {
		super(props);
		this.notifier = new HeadingNotifier(this);
		this.requester = new HeadingRequester(this);
	};

	render() {
	    const className = "layout vertical center-justified" + (this.hiddenClass() !== "" ? " " + this.hiddenClass() : "");
        const layout = this.context;
        const index = this.props.tableColumnIndex;
        const resizable = layout != null && index != null;
        const width = resizable && layout.widths != null ? layout.widths[index] : null;
		return (
            <div className={className} style={{height:Heading.Height,...this.props.style,...this.style(),...columnWidthStyle(width),position:"relative"}}>
                {this.props.children}
                {resizable && <span
                    className={layout.handleClassName}
                    role="separator"
                    aria-orientation="vertical"
                    aria-label={this.translate("Resize column") + " " + (index + 1)}
                    aria-valuemin={48}
                    aria-valuenow={width == null ? undefined : Math.round(width)}
                    tabIndex={0}
                    onPointerDown={event => layout.start(index, event)}
                    onPointerMove={layout.move}
                    onPointerUp={layout.end}
                    onPointerCancel={layout.end}
                    onLostPointerCapture={layout.end}
                    onDoubleClick={event => { event.stopPropagation(); layout.reset(index); }}
                    onClick={event => event.stopPropagation()}
                    onKeyDown={event => layout.keyDown(index, event)}
                />}
            </div>
        );
	};

}

export default withStyles(styles, { withTheme: true })(Heading);
DisplayFactory.register("Heading", withStyles(styles, { withTheme: true })(Heading));
