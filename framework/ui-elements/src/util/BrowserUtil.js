const BrowserUtil = (function () {
    return {
        isMobile : () => {
            return window.outerWidth <= 736;
        }
    }
})();

export default BrowserUtil;