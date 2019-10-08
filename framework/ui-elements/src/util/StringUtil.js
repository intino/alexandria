import moment from "moment/moment"

const StringUtil = (function () {
    return {
        toBase64 : (str) => {
            return window.btoa(str);
        }
    }
})();

export default StringUtil;