import Numeral from "numeral";
import "numeral/locales/es.js";
import "numeral/locales/de.js";

const NumberUtil = (function () {
    return {
        format : (number, format) => {
            const language = window.Application.configuration.language;
            Numeral.locale(language);
            return Numeral(number).format(format);
        }
    }
})();

export default NumberUtil;