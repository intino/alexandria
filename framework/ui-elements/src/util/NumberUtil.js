import Numeral from "numeral";
import "numeral/locales/es.js";
import "numeral/locales/de.js";

const NumberUtil = (function () {
    return {
        format : (number, format) => {
            let language = window.Application.configuration.language;
            if (language === "mx") language = "en"; // Mexican not defined, used compatible language
            Numeral.locale(language);
            return Numeral(number).format(format);
        }
    }
})();

export default NumberUtil;