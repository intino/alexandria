import Numeral from "numeral";
import "numeral/locales/es.js";
import "numeral/locales/de.js";
import "numeral/locales/pt-pt.js";

const NumberUtil = (function () {
    return {
        format : (number, format) => {
            let language = window.Application.configuration.language;
            if (language !== "es" && language !== "de" && language !== "pt") language = "en";
            if (language === "mx") language = "en"; // Mexican not defined, used compatible language
            if (language === "pt") language = "pt-pt";
            if (Numeral.locales[language] == null) language = "es";
            Numeral.locale(language);
            return Numeral(number).format(format);
        }
    }
})();

export default NumberUtil;