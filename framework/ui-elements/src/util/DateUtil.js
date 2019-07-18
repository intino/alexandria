import moment from "moment/moment"

const DateUtil = (function () {
    return {
        format : (date, format) => {
            return moment(date).format(format);
        }
    }
})();

export default DateUtil;