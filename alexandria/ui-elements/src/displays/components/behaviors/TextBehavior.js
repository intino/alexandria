import ComponentBehavior from "./ComponentBehavior";

const TextBehavior = (function () {
    return {
        label : (props) => {
            ComponentBehavior.label(props);
        },

        mode: (value, props) => {
            if (value == null) return value;
            if (props.mode === "capitalize") return this.capitalize(value);
            else if (props.mode === "uppercase") return value.toUpperCase();
            else if (props.mode === "lowercase") return value.toLowerCase();
            return value;
        },

        capitalize: (label) => {
            return label.charAt(0).toUpperCase() + label.slice(1).toLowerCase();
        }
    }
})();

export default TextBehavior;
