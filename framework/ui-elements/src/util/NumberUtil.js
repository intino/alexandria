import Numeral from "numeral";

const NumberUtil = (function () {
    return {
        format : (number, format) => {
            return Numeral(number).format(format);
        }
    }
})();

export default NumberUtil;