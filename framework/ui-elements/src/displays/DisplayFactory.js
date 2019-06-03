const DisplayFactory = (function () {
    var elements = [];
    var factory = {};

    factory.get = (name) => {
        return elements[name];
    };
    
    factory.register = (name, component) => {
        elements[name] = component;
    };

    return factory;
})();

export default DisplayFactory;