import I18n from "app-elements/gen/I18n";

const TranslatorService = {
    create : function (configuration) {
        let language = configuration.language;
        let translator = {};

        translator.translate = (word, params) => {
            const i18n = I18n.load(language);
            let wordTranslation = i18n != null && i18n[word] != null ? i18n[word] : word;
            return translator.translateParams(wordTranslation, params);
        };

        translator.translateParams = (text, params) => {
            if (params == null) return text;
            for (var i=0; i<params.length; i++) text = text.replace("{" + (i+1) + "}", params[i]);
            return text;
        };

        return {
            language: () => {
                return language;
            },
            translate: (word, params) => {
                return translator.translate(word, params);
            }
        };
    }
};

export default TranslatorService;