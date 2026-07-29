import React from "react";
import PassiveView from "./PassiveView";
import Typography from "@mui/material/Typography";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import Cookies from "js-cookie";
import * as CookieConsent from "vanilla-cookieconsent";
import "vanilla-cookieconsent/dist/cookieconsent.css";
import Theme from 'app-elements/gen/Theme';
import history from "alexandria-ui-elements/src/util/History";

export const enrichDisplayProperties = (instance) => {
    instance.pl.context = () => { return instance.pl.o };
    instance.pl.owner = () => {
        const ownerPath = instance.pl.o;
        if (ownerPath == null || ownerPath === "") return instance.i != null ? instance.i : instance.pl.id;
        return ownerPath.substring(ownerPath.lastIndexOf(".") + 1);
    };
};

export default class Display extends PassiveView {
    static CookieConsentRendered;
    static CookieConsentInitialized = false;
    address = null;

    constructor(props) {
        super(props);
        this.onClearContainer = null;
        this.state = {
            traceable: this.props.traceable,
            appMode : this._loadAppMode(),
        }
    };

    componentDidMount() {
        if (this.props.id == null) return;
        if (this.props.owner == null) return;
        if (this.props.context == null) return;
        this.requester.didMount();
    };

    addInstance = (instance) => {
        this._updateInstances(instance.c, (instances) => [...instances, instance]);
    };

    addInstances = (params) => {
        this._updateInstances(params.c, (instances) => [...instances, ...params.value]);
    };

    insertInstance = (instance) => {
        this._updateInstances(instance.c, (instances) => {
            const nextInstances = [...instances];
            nextInstances[instance.idx] = instance;
            return nextInstances;
        });
    };

    insertInstances = (params) => {
        this._updateInstances(params.c, (instances) => {
            const nextInstances = [...instances];
            params.value.forEach(instance => nextInstances[instance.idx] = instance);
            return nextInstances;
        });
    };

    hiddenClass = () => {
        const hidden = this.props.hidden;
        if (hidden == null || hidden === "Never") return "";
        return "hidden-" + hidden.toLowerCase();
    };

    removeInstance = (params) => {
        this._updateInstances(params.c, (instances) => {
            const id = params.id;
            return instances.filter(instance => instance != null && instance.pl.id !== id);
        });
    };

    clearContainer = (params) => {
        this._registerInstances(params.c, []);
        if (this.onClearContainer) this.onClearContainer();
    };

    redirect = (params) => {
        let url = params.url;
        if (url == null || url === "" || url === "null" || url === undefined) window.location.reload();
        else window.location.href = url;
    };

    dispatch = (params) => {
        history.push(params.path);
        return true;
    };

    addressed = (params) => {
        this.address = params.address != null ? Application.configuration.basePath + params.address : null;
    };

    closeClient = (params) => {
        window.close();
    };

    historyAddress = () => {
        return this.address;
    };

    instances = (container) => {
        if (container == null) container = "__elements";
        return this.state[container] != null ? this.state[container] : [];
    };

    renderInstances = (container, props, style) => {
        let instances = this.instances(container);
        if (instances == null || instances.length <= 0) return this.renderEmptyInstances(props);
        return instances.map((instance, index) => {
            enrichDisplayProperties(instance);
            this.copyProps(props, instance.pl);
            return this.renderInstance(instance, props, style, index)
        });
    };

    renderEmptyInstances = (props) => {
        return props != null && props.noItemsMessage != null ? (<Typography style={{margin:'5px 0'}} variant="body1">{this.translate(props.noItemsMessage)}</Typography>) : undefined;
    };

    renderInstance = (instance, props, style, index) => {
        const key = instance != null && instance.pl != null && instance.pl.id != null ? instance.pl.id : index;
        const Component = DisplayFactory.get(instance.tp);
        if (Component == null) {
            console.error("DisplayFactory component not registered", instance != null ? instance.tp : instance, instance);
            return null;
        }
        return (<div key={key} style={style}>{React.createElement(Component, instance.pl)}</div>);
    };

    buildApplicationUrl = (path) => {
        return Application.configuration.baseUrl + path;
    };

    copyProps = (from, to, excludedList) => {
        excludedList = excludedList != null ? excludedList : "";
        excludedList += "id,context,owner";
        for (var index in from) {
            if (excludedList != null && excludedList.indexOf(index) !== -1) continue;
            to[index] = from[index];
        }
    };

    showError = (error) => {
        this.showMessage(error, 'error', 5000);
    };

    showMessage = (message, type, autoHideDuration) => {
        const loading = type.toLowerCase() === "loading";
        const persist = loading || autoHideDuration == -1;
        const messageType = loading ? "info" : type.toLowerCase();
        const options = { variant: messageType, persist: persist, autoHideDuration: autoHideDuration, anchorOrigin: { vertical: 'top', horizontal: 'center' }};
        if (this.snack != null) this.props.closeSnackbar(this.snack);
        if (this.messageTimeout != null) window.clearTimeout(this.messageTimeout);
        this.messageTimeout = window.setTimeout(() => {
            const snack = this.props.enqueueSnackbar(message, options);
            if (loading) this.snack = snack;
        }, 100);
    };

    hideMessage = () => {
        if (this.messageTimeout != null) window.clearTimeout(this.messageTimeout);
        this.props.closeSnackbar();
    };

    componentWillUnmount() {
        if (this.notifier != null) this.notifier.detached();
    };

    attribute = (name) => {
        return this.state[name] != null ? this.state[name] : this.props[name];
    };

    trace = (value, name) => {
        if (!this.state.traceable) return;
        this.updateCookie(value, name);
    };

    traceValue = (name) => {
        if (!this.state.traceable) return null;
        return this.getCookie(name);
    };

    updateCookie = (value, name) => {
        if (!this._cookieConsentAccepted()) return;
        Cookies.set(name != null ? name : this.props.id, JSON.stringify(value));
    };

    getCookie = (name) => {
        if (!this._cookieConsentAccepted()) return null;
        const value = Cookies.get(name != null ? name : this.props.id);
        return value != null ? JSON.parse(value) : null;
    };

    renderTraceConsent = () => {
        if (!this.state.traceable) return (<React.Fragment/>);
        this.renderCookieConsent();
    };

    renderCookieConsent = () => {
        if (Display.CookieConsentRendered != undefined && Display.CookieConsentRendered != this.props.id) return (<React.Fragment/>);
        Display.CookieConsentRendered = this.props.id;
        this._initializeCookieConsent();
        return (<React.Fragment/>);
    };

    static showCookiePreferences = () => {
        if (Display.CookieConsentInitialized) CookieConsent.showPreferences();
    };

    _context() {
        const id = this.props.id;
        if (id != null) {
            const bareUuid = id.indexOf(".") === -1 && /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(id);
            if (bareUuid && this.props.rootContextPath != null) return this.props.rootContextPath;
            const hasPathSegments = id.indexOf(".") !== -1;
            const root = hasPathSegments ? id.substring(0, id.indexOf(".")) : id;
            const absolutePath = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(root);
            if (absolutePath) return id;
        }
        const context = this.props.context != null ? this.props.context() + "." : "";
        return context + id;
    };

    _owner() {
        return this.shortId();
    };

    _registerInstances = (container, instances) => {
        let object = {};
        object[container] = instances;
        this.setState(object);
    };

    _updateInstances = (container, updater) => {
        const key = container == null ? "__elements" : container;
        this.setState((prevState) => {
            const currentInstances = prevState[key] != null ? prevState[key] : [];
            const nextInstances = updater(currentInstances);
            return { [key]: nextInstances };
        });
    };

    _cookieConsentAccepted = () => {
        return Display.CookieConsentInitialized && CookieConsent.acceptedCategory("preferences");
    };

    _traceConsentVariable = () => {
        return Application.configuration.url.replace(/[^\w\s]/gi, '');
    };

    _initializeCookieConsent = () => {
        if (Display.CookieConsentInitialized || typeof document === "undefined") return;
        Display.CookieConsentInitialized = true;
        CookieConsent.run({
            cookie: {
                name: this._traceConsentVariable() + "_consent",
                expiresAfterDays: 182,
                sameSite: "Lax",
            },
            guiOptions: {
                consentModal: {
                    layout: "cloud inline",
                    position: "bottom center",
                    equalWeightButtons: true,
                    flipButtons: false,
                },
                preferencesModal: {
                    layout: "box",
                    equalWeightButtons: true,
                    flipButtons: false,
                },
            },
            categories: {
                necessary: { enabled: true, readOnly: true },
                preferences: {},
                analytics: {},
                marketing: {},
            },
            language: {
                default: "es",
                translations: {
                    es: {
                        consentModal: {
                            title: "Tu privacidad",
                            description: "Utilizamos cookies necesarias para el funcionamiento del sitio y, con tu consentimiento, cookies de preferencias, analítica y marketing.",
                            acceptAllBtn: "Aceptar todas",
                            acceptNecessaryBtn: "Rechazar",
                            showPreferencesBtn: "Configurar",
                        },
                        preferencesModal: {
                            title: "Preferencias de cookies",
                            acceptAllBtn: "Aceptar todas",
                            acceptNecessaryBtn: "Rechazar",
                            savePreferencesBtn: "Guardar selección",
                            closeIconLabel: "Cerrar",
                            sections: [
                                {
                                    title: "Gestiona tus preferencias",
                                    description: "Puedes cambiar o retirar tu consentimiento en cualquier momento desde la opción de configuración de cookies.",
                                },
                                {
                                    title: "Cookies necesarias",
                                    description: "Permiten el funcionamiento básico y seguro del sitio. No se pueden desactivar.",
                                    linkedCategory: "necessary",
                                },
                                {
                                    title: "Preferencias",
                                    description: "Permiten recordar opciones como el idioma o el modo de visualización.",
                                    linkedCategory: "preferences",
                                },
                                {
                                    title: "Analítica",
                                    description: "Nos ayudan a comprender el uso del sitio para mejorarlo.",
                                    linkedCategory: "analytics",
                                },
                                {
                                    title: "Marketing",
                                    description: "Permiten mostrar contenidos o comunicaciones más relevantes.",
                                    linkedCategory: "marketing",
                                },
                            ],
                        },
                    },
                },
            },
        });
    };

    _loadAppMode = () => {
        var mode = this.getCookie(encodeURI(Application.configuration.baseUrl + "/appmode"));
        if (mode != null) return mode;
        if (!Theme.isAutoMode()) return Theme.defaultMode();
        return window.matchMedia('(prefers-color-scheme: dark)') ? 'dark' : 'light';
    };

    _saveAppModeInCookies = (mode) => {
        this.updateCookie(mode, encodeURI(Application.configuration.baseUrl + "/appmode"));
    };
}
