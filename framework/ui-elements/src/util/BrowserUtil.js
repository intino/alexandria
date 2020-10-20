const BrowserUtil = (function () {
    return {
        isMobile : () => {
            return window.outerWidth <= 736;
        },
        isFirefox : () => {
            return typeof InstallTrigger !== 'undefined';
        }
    }
})();

export default BrowserUtil;