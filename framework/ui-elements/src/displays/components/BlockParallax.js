import React from "react";
import AbstractBlockParallax from "../../../gen/displays/components/AbstractBlockParallax";
import BlockParallaxNotifier from "../../../gen/displays/notifiers/BlockParallaxNotifier";
import BlockParallaxRequester from "../../../gen/displays/requesters/BlockParallaxRequester";
import {withStyles} from "alexandria-ui-elements/src/util/muiStylesCompat";
import 'alexandria-ui-elements/res/styles/parallax.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import classNames from "classnames";

const styles = theme => ({
	container : {
		position: "relative",
		height: "100%"
	},
	children: {
		position: "absolute",
		top: "0",
		height: "100%",
		width: "100%"
	}
});

class BlockParallax extends AbstractBlockParallax {
	state = {
		background: null
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockParallaxNotifier(this);
		this.requester = new BlockParallaxRequester(this);
		this.containerRef = React.createRef();
		this.backgroundRef = React.createRef();
		this.parallaxFrame = null;
	};

	componentDidMount() {
		super.componentDidMount != null && super.componentDidMount();
		window.addEventListener("scroll", this.handleParallax, { passive: true });
		window.addEventListener("resize", this.handleParallax);
		this.handleParallax();
	}

	componentDidUpdate() {
		this.handleParallax();
	}

	componentWillUnmount() {
		window.removeEventListener("scroll", this.handleParallax);
		window.removeEventListener("resize", this.handleParallax);
		super.componentWillUnmount != null && super.componentWillUnmount();
	}

	handleParallax = () => {
		if (this.parallaxFrame != null) return;
		this.parallaxFrame = window.requestAnimationFrame(() => {
			this.parallaxFrame = null;
			const container = this.containerRef.current;
			const background = this.backgroundRef.current;
			if (container == null || background == null) return;
			const offset = Math.max(Math.min((window.scrollY - container.offsetTop) * 0.25, 160), -160);
			background.style.backgroundPosition = `center calc(50% + ${offset}px)`;
		});
	};

	render() {
		const {classes} = this.props;
		const hasBackground = this.state.background != null;
		const height = this.props.height != -1 ? this.props.height : "100%";

		return (
			<div ref={this.containerRef} className={classNames("parallax-container", classes.container)} style={{height:height}}>
				{ hasBackground ? (
                    <div
                        ref={this.backgroundRef}
                        className="parallax"
                        style={{
                            position:"absolute",
                            top:0,
                            left:0,
                            right:0,
                            bottom:0,
                            overflow:"hidden",
                            zIndex:0,
                            backgroundImage:`url(${this.state.background})`,
                            backgroundRepeat:"no-repeat",
                            backgroundSize:"cover",
                            backgroundPosition:"center center",
                            willChange:"background-position"
                        }}
                    />
                ) : undefined}
				<div className={classes.children} style={{...this.style(), position:"relative", zIndex:1}}>{this.props.children}</div>
			</div>
		);
	};

	refresh = (background) => {
		this.setState({background});
	};

}

export default withStyles(styles, { withTheme: true })(BlockParallax);
DisplayFactory.register("BlockParallax", withStyles(styles, { withTheme: true })(BlockParallax));
