import React from "react";

export default class I18nComponent extends React.Component {

    constructor(props) {
        super(props);
        this.translator = Application.services.translatorService;
    };

    translate = (word, params) => {
        return this.translator.translate(word, params);
    };

}