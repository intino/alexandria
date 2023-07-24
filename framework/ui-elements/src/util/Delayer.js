const Delayer = (function () {
    return {
        execute : (component, callback, delay) => {
            if (delay == null) delay = 100;
            if (component.delayer != null) {
                window.clearTimeout(component.delayer);
                component.delayer = null;
            }
            component.delayer = window.setTimeout(() => callback(), delay);
        },
        stop : (component) => {
            if (component.delayer == null) return;
            window.clearTimeout(component.delayer);
            component.delayer = null;
        }
    }
})();

export default Delayer;