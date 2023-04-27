import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractReel from "../../../gen/displays/components/AbstractReel";
import ReelNotifier from "../../../gen/displays/notifiers/ReelNotifier";
import ReelRequester from "../../../gen/displays/requesters/ReelRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { DndProvider } from 'react-dnd'
import { HTML5Backend } from 'react-dnd-html5-backend'
import ReelToolbar from './reel/toolbar'
import ReelSignal from './reel/signal'
import Theme from "app-elements/gen/Theme";

const styles = theme => ({
    signal : { position:'relative', padding:'5px 0', marginBottom: '1px', minWidth:'180px' },
    signalLabel : { width:'200px',fontWeight:'bold',textOverflow:'ellipsis',overflow:'hidden',whiteSpace:'nowrap' },
    signalStep : { height:'14px' },
    signalStepBlock: { padding: theme.spacing(1) },
    signalStepBlockLink: { width: '100%', display: 'block', height: '100%', cursor: 'pointer' },
    emptySignal : { width:'100%', height:'20px' },
    scale : { cursor:'pointer',padding:'0 4px' },
    selectedScale : { backgroundColor: theme.palette.primary.main, color: 'white' },
    icon : { color: "white" },
});

class Reel extends AbstractReel {

	constructor(props) {
		super(props);
		this.notifier = new ReelNotifier(this);
		this.requester = new ReelRequester(this);
		this.state = {
		    ...this.state,
		    scales: [],
		    inside : false,
		    toolbar: { label: '', scale: null, canNext: false, canPrevious: false },
		    signals: [],
		    signalsSorting : {},
		}
	};

    render() {
        if (!this.state.visible) return (<React.Fragment/>);
        return (
            <DndProvider backend={HTML5Backend}>
                <div className="layout vertical wrap" onMouseEnter={this.handleMouseEnter.bind(this)} onMouseLeave={this.handleMouseLeave.bind(this)} style={this.style()}>
                    {this.renderToolbar({fontSize:'14pt',fontWeight:'300'})}
                    {this.renderSignals()}
                    {this.renderCookieConsent()}
                </div>
            </DndProvider>
        );
    };

    renderSignals = () => {
        const signals = this.sortedSignals().map((s, idx) => this.renderSignal(s, idx));
        return (
            <React.Fragment>
                <div style={{marginBottom:'2px',borderBottom:'1px solid #ddd'}}></div>
                {signals}
            </React.Fragment>
        );
    };

    setup = (info) => {
        const signalsSorting = this.getCookie(info.name + "_sorting") != null ? this.getCookie(info.name + "_sorting") : this.state.signalsSorting;
        this.setState({ scales: info.scales, toolbar: info.toolbar, signals: info.signals, signalsSorting: signalsSorting, sourceName: info.name });
    };

    refreshSignalsSorting = (sorting) => {
        const newSorting = {};
        for (let i=0; i<sorting.length; i++) newSorting[sorting[i].name] = sorting[i].position;
        this.setState({signalsSorting: newSorting});
    };

    refreshSignals = (signals) => {
        this.setState({signals});
    };

	refreshToolbar = (toolbar) => {
	    this.setState({toolbar});
	};

    sortedSignals = () => {
        const signals = this.state.signals;
        const sorting = this.state.signalsSorting;
        if (sorting != null) signals.sort(this.sortingComparator);
        return signals;
    };

    sortingComparator = (m1, m2) => {
        const m1Position = this.state.signalsSorting[m1.name] != null ? this.state.signalsSorting[m1.name] : m1.position;
        const m2Position = this.state.signalsSorting[m2.name] != null ? this.state.signalsSorting[m2.name] : m2.position;
        if (m1Position < m2Position ) return -1;
        if (m1Position > m2Position ) return 1;
        return 0;
    };

    renderToolbar = (style) => {
        return (
            <ReelToolbar
                label={{title:this.state.name, style:style}}
                scales={this.state.scales}
                toolbar={this.state.toolbar}
                onFirst={this.first.bind(this)}
                onPrevious={this.previous.bind(this)}
                onNext={this.next.bind(this)}
                onLast={this.last.bind(this)}
                onChangeScale={this.changeScale.bind(this)}
                translate={this.translate.bind(this)}
                classes={this.props.classes}
            />
        );
    };

    renderSignal = (signal, idx) => {
        return (
            <ReelSignal style={{paddingBottom:'2px',borderBottom:'1px solid #ddd'}}
                 signal={signal}
                 key={this.props.id + signal.name}
                 index={idx}
                 id={signal.name}
                 classes={this.props.classes}
                 translate={this.translate.bind(this)}
                 moveSignal={this.moveSignal.bind(this)}
            />
        );
    };

	moveSignal = (dragIndex, hoverIndex) => {
        const signals = this.state.signals;
        var signal = signals[dragIndex];
        signals.splice(dragIndex, 1);
        signals.splice(hoverIndex, 0, signal);
        this.setState({signals: signals, signalsSorting: this.saveSignalsSorting()});
    };

    saveSignalsSorting = () => {
        const signals = this.state.signals;
        const signalsSorting = {};
        for (var i=0; i<signals.length; i++) signalsSorting[signals[i].name] = i;
        this.updateCookie(signalsSorting, this.state.sourceName + "_sorting");
        const list = [];
        for (var i=0; i<signals.length; i++) list.push({ name: signals[i].name, position: i });
        this.requester.signalsSorting(list);
        return signalsSorting;
    };

	changeScale = (scale) => { this.requester.changeScale(scale); };
	first = () => { this.requester.first(); };
	previous = () => { this.requester.previous(); };
	next = () => { this.requester.next(); };
	last = () => { this.requester.last(); };

    handleMouseEnter = () => {
        this.setState({inside: true});
    };

    handleMouseLeave = () => {
        this.setState({inside: false});
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Reel));
DisplayFactory.register("Reel", withStyles(styles, { withTheme: true })(withSnackbar(Reel)));