import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractReel from "../../../gen/displays/components/AbstractReel";
import ReelNotifier from "../../../gen/displays/notifiers/ReelNotifier";
import ReelRequester from "../../../gen/displays/requesters/ReelRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import { DndProvider } from 'react-dnd'
import { HTML5Backend } from 'react-dnd-html5-backend'
import ReelSignal from './reel/signal'
import Theme from "app-elements/gen/Theme";

const ReelStepWidth = 20;
const ReelStepLabelWidth = 200;

const styles = theme => ({
    signal : { position:'relative', padding:'5px 0', marginBottom: '1px', minWidth:'180px' },
    signalLabel : { width: ReelStepLabelWidth + 'px',fontWeight:'bold',textOverflow:'ellipsis',overflow:'hidden',whiteSpace:'nowrap' },
    signalStep : { height:'11px', opacity: '0.7' },
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
		    navigation: { steps: 0 },
		    signals: [],
		    signalsSorting : {}
		}
	};

    render() {
        if (!this.state.visible) return (<React.Fragment/>);
        return (
            <DndProvider backend={HTML5Backend}>
                <div className="layout vertical wrap" onMouseEnter={this.handleMouseEnter.bind(this)} onMouseLeave={this.handleMouseLeave.bind(this)} style={this.style()}>
                    {this.renderSignals()}
                    {this.renderNavigation()}
                    {this.renderRange()}
                    {this.renderCookieConsent()}
                </div>
            </DndProvider>
        );
    };

    renderSignals = () => {
        const signals = this.sortedSignals().map((s, idx) => this.renderSignal(s, idx));
        return (<div style={{marginTop:'10px'}}>{signals}</div>);
    };

    renderNavigation = () => {
        return (<React.Fragment/>);
//        if (this.state.navigation.steps == 0) return (<React.Fragment/>);
//        const steps = [];
//        for (let i=0; i<this.state.navigation.steps; i++) steps.push(this.navigationStep());
//        return (
//            <div style={{position:'relative'}}>
//                <div className="layout horizontal flex" style={{position:'absolute',width:'100%',overflow:'auto'}} onScroll={this.handleMove.bind(this)}>
//                    {steps}
//                </div>
//            </div>
//        );
    };

    renderRange = () => {
        const signals = this.sortedSignals();
        if (signals.length <= 0) return (<React.Fragment/>);
        const signal = signals[0];
        const steps = signal.steps;
        const width = ReelStepWidth*steps.length;
        return (
            <div className="layout horizontal" style={{marginLeft:'200px',width:width+"px"}}>
                <div className="layout vertical flex" style={{fontSize:'8pt',color:'#777'}}>{steps[0].scaledDate}</div>
                <div className="layout vertical end-justified" style={{fontSize:'8pt',color:'#777'}}>{steps[steps.length-1].scaledDate}</div>
            </div>
        );
    };

    handleMove = (e) => {
        console.log(Math.round(e.target.scrollLeft/ReelStepWidth));
    };

    navigationStep = () => {
        return (<div><div style={{width:ReelStepWidth+"px",height:'20px',border:'1px solid blue',display:'block'}}></div></div>);
    };

    setup = (info) => {
        const signalsSorting = this.getCookie(info.name + "_sorting") != null ? this.getCookie(info.name + "_sorting") : this.state.signalsSorting;
        this.setState({ scales: info.scales, signals: info.signals, navigation: info.navigation, signalsSorting: signalsSorting, sourceName: info.name });
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

	refreshNavigation = (navigation) => {
	    this.setState({navigation});
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

    renderSignal = (signal, idx) => {
        const width = (signal.steps.length*ReelStepWidth)+ReelStepLabelWidth;
        return (
            <ReelSignal style={{paddingBottom:'2px',borderBottom:'1px solid #ddd',width:width+"px"}}
                 signal={signal}
                 key={this.props.id + signal.name}
                 index={idx}
                 id={signal.name}
                 classes={this.props.classes}
                 translate={this.translate.bind(this)}
                 moveSignal={this.moveSignal.bind(this)}
                 stepWidth={ReelStepWidth}
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

    handleMouseEnter = () => {
        this.setState({inside: true});
    };

    handleMouseLeave = () => {
        this.setState({inside: false});
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Reel));
DisplayFactory.register("Reel", withStyles(styles, { withTheme: true })(withSnackbar(Reel)));