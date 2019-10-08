import React from "react";

export default class I18nComponent extends React.Component {

    constructor(props) {
        super(props);
        this.translator = Application.services.translatorService;
    };

    language = () => {
        return this.translator.language();
    };

    translate = (word, params) => {
        return this.translator.translate(word, params);
    };

}