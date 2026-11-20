const assert = require('node:assert/strict');
const {test} = require('node:test');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const sourcePath = process.env.DISPLAY_SOURCE || path.join(__dirname, '../src/displays/Display.js');
const source = fs.readFileSync(sourcePath, 'utf8');
const load = source.match(/_loadAppMode = \(\) => \{([\s\S]*?)\n    \};/)[1];
const save = source.match(/_saveAppModeInCookies = \(mode\) => \{([\s\S]*?)\n    \};/)[1];

function application({storage = new Map(), system = 'dark', consent = false, blocked = false, baseUrl = 'https://quassar.test', cookie = null} = {}) {
    const Display = {AppModes: new Map()};
    const cookies = new Map(cookie == null ? [] : [[encodeURI(baseUrl + '/appmode'), cookie]]);
    const context = {
        Display, Application: {configuration: {baseUrl}},
        Theme: {isAutoMode: () => true, defaultMode: () => 'auto'},
        window: {
            sessionStorage: {
                getItem(key) {if (blocked) throw new Error('Storage disabled'); return storage.get(key);},
                setItem(key, value) {if (blocked) throw new Error('Storage disabled'); storage.set(key, value);},
            },
            matchMedia: () => ({matches: system === 'dark'}),
        },
    };
    const read = vm.runInNewContext('(function() {' + load + '})', context);
    const write = vm.runInNewContext('(function(mode) {' + save + '})', context);
    const instance = {
        _initializeCookieConsent() {},
        getCookie: key => consent ? cookies.get(key) : null,
        updateCookie: (value, key) => {if (consent) cookies.set(key, value);},
    };
    return {storage, cookies, read: () => read.call(instance), save: mode => write.call(instance, mode)};
}

test('manual light survives a full OpenPage navigation and back while the OS is dark', () => {
    const storage = new Map();
    const studio = application({storage});
    assert.equal(studio.read(), 'dark');
    studio.save('light');
    assert.equal(application({storage}).read(), 'light');
    assert.equal(application({storage}).read(), 'light');
    assert.equal(studio.cookies.size, 0);
});

test('manual dark also takes precedence over an automatic light system', () => {
    const storage = new Map();
    application({storage, system: 'light'}).save('dark');
    assert.equal(application({storage, system: 'light'}).read(), 'dark');
});

test('startup does not depend on the asynchronous preferences consent initialization', () => {
    const storage = new Map();
    application({storage, consent: true}).save('light');
    assert.equal(application({storage, consent: false}).read(), 'light');
});

test('automatic mode is preserved until a user explicitly changes it', () => {
    const storage = new Map();
    assert.equal(application({storage, system: 'dark'}).read(), 'dark');
    assert.equal(application({storage, system: 'light'}).read(), 'light');
    assert.equal(storage.size, 0);
});

test('existing preference cookies still work and are written only with consent', () => {
    assert.equal(application({consent: true, cookie: 'light'}).read(), 'light');
    const allowed = application({consent: true});
    allowed.save('light');
    assert.equal([...allowed.cookies.values()][0], 'light');
    const denied = application();
    denied.save('light');
    assert.equal(denied.cookies.size, 0);
});

test('blocked session storage retains the choice in memory during SPA navigation', () => {
    const app = application({blocked: true});
    app.save('light');
    assert.equal(app.read(), 'light');
});

test('theme choices remain isolated by application base URL', () => {
    const storage = new Map();
    application({storage, baseUrl: 'https://quassar.test/site'}).save('light');
    assert.equal(application({storage, baseUrl: 'https://quassar.test/editor'}).read(), 'dark');
});

test('invalid stored modes are ignored', () => {
    const storage = new Map([[encodeURI('https://quassar.test/appmode'), 'invalid']]);
    const app = application({storage});
    assert.equal(app.read(), 'dark');
    app.save('invalid');
    assert.equal(app.read(), 'dark');
});
