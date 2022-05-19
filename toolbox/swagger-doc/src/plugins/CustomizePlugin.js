import Topbar from '../components/TopBar'

const CustomizePlugin = () => {
    return {
        wrapComponents: {
            Topbar: () => Topbar, // Override original Topbar plugin
            InfoUrl: () => () => null, // Disable info URL
            authorizeBtn: () => () => null, // Disable authorize button
            // InfoBasePath: () => () => null, //Disable base path link
        },
        statePlugins: {
          spec: {
              wrapSelectors: { allowTryItOutFor: () => () => false }, // Disable try it out
              wrapActions: {
                updateJsonSpec: function(oriAction, system) {
                    return (spec) => {
                        spec.host = window.location.host;
                        return oriAction(spec);
                    }
                }
              } 
            }
        }
    }
}

export default CustomizePlugin