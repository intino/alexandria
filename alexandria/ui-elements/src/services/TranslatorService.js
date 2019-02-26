import Dictionary from "../../gen/Dictionary";

const TranslatorService = {
    create : function (configuration) {
        let language = configuration.language;

        return {
            translate: (word) => {
                let dictionary = Dictionary.load(language);
                return dictionary != null && dictionary[word] != null ? dictionary[word] : word;
            }
        };
    }
};

export default TranslatorService;