import moment from "moment/moment"

const StringUtil = (function () {
    return {
        toBase64 : (str) => {
            return window.btoa(str);
        },
        initials : (str, length) => {
            var result = "";
            for (var i=0; i<str.length; i++) {
                if (str[i] !== " " && str[i].toUpperCase() == str[i]) result += str[i];
                if (str[i] === " " && (i+1 < str.length)) result += str[i+1];
                if (result.length == length) break;
            }
            return result.toUpperCase();
        }
    }
})();

export default StringUtil;