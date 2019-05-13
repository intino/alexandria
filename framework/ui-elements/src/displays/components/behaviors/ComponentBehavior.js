const ComponentBehavior = (function () {
    return {
        label : (props) => {
            const service = Application.services.translatorService;
            const label = props.label;
            return label !== undefined ? service.translate(label) : undefined;
        }
    }
})();

export default ComponentBehavior;
