import moment from "moment/moment"

const DateUtil = (function () {
    return {
        format : (date) => {
            return moment(date).format(format);
        }
    }
})();

export default DateUtil;