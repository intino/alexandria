import I18n from "app-elements/gen/I18n";

const TranslatorService = {
    FamilyLanguages: { "mx": "es" },

    create : function (configuration) {
        let language = configuration.language;
        let translator = {};

        translator.translate = (word, params) => {
            const i18n = translator.load(language);
            let wordTranslation = i18n != null && i18n[word] != null ? i18n[word] : word;
            return translator.translateParams(wordTranslation, params);
        };

        translator.translateParams = (text, params) => {
            if (params == null) return text;
            for (var i=0; i<params.length; i++) text = text.replace("{" + (i+1) + "}", params[i]);
            return text;
        };

        translator.load = (language) => {
            let i18n = I18n.load(language);
            if (Object.keys(i18n).length === 0 && TranslatorService.FamilyLanguages[language] != null) i18n = I18n.load(TranslatorService.FamilyLanguages[language]);
            return Object.keys(i18n).length !== 0 ? i18n : I18n.load("en");
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