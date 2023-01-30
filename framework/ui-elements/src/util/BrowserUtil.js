const BrowserUtil = (function () {
    return {
        isMobile : () => {
            let hasTouchScreen = false;
            if ("maxTouchPoints" in navigator) {
                hasTouchScreen = navigator.maxTouchPoints > 0;
            } else if ("msMaxTouchPoints" in navigator) {
                hasTouchScreen = navigator['msMaxTouchPoints'] > 0;
            } else {
                let mQ = window.matchMedia && matchMedia("(pointer:coarse)");
                if (mQ && mQ.media === "(pointer:coarse)") {
                    hasTouchScreen = !!mQ.matches;
                } else if ('orientation' in window) {
                    hasTouchScreen = true; // deprecated, but good fallback
                }
            }
            return hasTouchScreen;
        },
        isFirefox : () => {
            return typeof InstallTrigger !== 'undefined';
        }
    }
})();

export default BrowserUtil;