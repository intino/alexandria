import moment from "moment/moment"

const StringUtil = (function () {
    return {
        toBase64 : (str) => {
            return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, (match, p1) => {
                return String.fromCharCode('0x' + p1);
            }));
        }

    }
})();

export default StringUtil;