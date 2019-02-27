import I18n from "../../gen/I18n";

const TranslatorService = {
    create : function (configuration) {
        let language = configuration.language;

        return {
            translate: (word) => {
                let translator = I18n.load(language);
                return translator != null && translator[word] != null ? translator[word] : word;
            }
        };
    }
};

export default TranslatorService;