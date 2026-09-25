import AbstractTemplate from "../../../gen/displays/components/AbstractTemplate";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Template extends AbstractTemplate {

	constructor(props) {
		super(props);
		this.state = {
		    ...this.state,
		    canClose : true
		};
	};

	enableCloseManager = () => {
	    const template = this;
	    window.addEventListener("beforeunload", function(event) {
	        if (template.state.canClose) return;
            event.returnValue = true;
            return true;
        });
	};

	canClose = (value) => {
	    this.setState({ canClose : value });
	};

	fixHeight = (height) => {
	    // A template may be rendered inside a dialog. Keep percentage heights relative
	    // to its immediate container instead of forcing the viewport height.
	    return height;
	};

}

DisplayFactory.register("Template", Template);
