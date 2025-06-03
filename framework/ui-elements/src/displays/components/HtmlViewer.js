import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractHtmlViewer from "../../../gen/displays/components/AbstractHtmlViewer";
import HtmlViewerNotifier from "../../../gen/displays/notifiers/HtmlViewerNotifier";
import HtmlViewerRequester from "../../../gen/displays/requesters/HtmlViewerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Theme from 'app-elements/gen/Theme';

const styles = theme => ({});

class HtmlViewer extends AbstractHtmlViewer {

	constructor(props) {
		super(props);
		this.notifier = new HtmlViewerNotifier(this);
		this.requester = new HtmlViewerRequester(this);
		this.state = {
		    ...this.state,
		    content: this.props.content != null ? this.props.content : '',
		    operations: [],
		}
	};

	render() {
	    this.refreshEvents();
	    if (this.state.content === "") return (<React.Fragment/>);
	    return (<div id={this.props.id + "-html"} dangerouslySetInnerHTML={{__html: this.content()}}></div>);
	};

	refresh = (content) => {
	    this.setState({content});
	};

	refreshOperations = (operations) => {
	    this.setState({operations});
	};

	refreshEvents = () => {
	    const operations = this.state.operations;
	    for (var i=0; i<operations.length; i++) window[operations[i]] = this.execute.bind(this, operations[i]);
    };

    execute = (operation, parameters) => {
        const params = parameters != null ? (Array.isArray(parameters) ? parameters : [ parameters ]) : [];
        this.requester.execute({ name: operation, params: params });
    };

    print = (title) => {
        var w = window.open();
        var html = '<html><head><title>' + document.title + '</title>';
        html += '</head><body class="print-mode">';
        html += '<h1>' + (title != null ? title : document.title) + '</h1>';
        html += document.getElementById(this.props.id + "-html").innerHTML;
        html += '</body></html>';
        w.document.write(html);
        w.document.close(); // necessary for IE >= 10
        w.focus(); // necessary for IE >= 10*/
        w.print();
        w.close();
        return true;
    };

    content = () => {
        const result = this.state.content;
        const isDark = Theme.get().isDark();
        return isDark ? this.addDark(result) : result;
    };

    addDark = (content) => {
        return "<div class='dark'>" + content + "</div>";
    }

}

export default withStyles(styles, { withTheme: true })(withSnackbar(HtmlViewer));
DisplayFactory.register("HtmlViewer", withStyles(styles, { withTheme: true })(withSnackbar(HtmlViewer)));