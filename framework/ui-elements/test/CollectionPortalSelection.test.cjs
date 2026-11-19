const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const { test } = require('node:test');
const babel = require('@babel/core');

const sourcePath = process.env.COLLECTION_SOURCE || path.join(__dirname, '../src/displays/components/behaviors/CollectionBehavior.js');
const source = fs.readFileSync(sourcePath, 'utf8')
    .replace(/^import .*;\r?\n/gm, '')
    .replace('const self = { collection, selection: [] };', 'const self = globalThis.selection = { collection, selection: [] };')
    .replace('export default CollectionBehavior;', 'globalThis.createBehavior = CollectionBehavior;');
const code = babel.transformSync(source, {
    configFile: false, babelrc: false,
    presets: [require.resolve('@babel/preset-react')],
}).code;

function fixture(inside) {
    const selections = [];
    let updates = 0;
    const scope = { window: {
        addEventListener() {}, clearTimeout() {},
        setTimeout(callback) { callback(); return 1; },
    } };
    vm.runInNewContext(code, scope);
    scope.createBehavior({
        props: { selection: [] },
        allowMultiSelection: () => false,
        forceUpdate() { updates++; },
        requester: { selection(value) { selections.push(Array.from(value)); } },
    });
    const event = {
        target: {},
        currentTarget: { contains: () => inside },
        prevented: false,
        preventDefault() { this.prevented = true; },
        stopPropagation() {},
    };
    return { scope, event, selections, updates: () => updates };
}

for (const target of ['export option in a portal', 'popover backdrop']) {
    test(`ignores a click from the ${target}`, () => {
        const { scope, event, selections, updates } = fixture(false);
        scope.selection.handleSelect('model', event);
        assert.deepEqual(selections, []);
        assert.equal(updates(), 0);
        assert.equal(event.prevented, false);
    });
}

test('a click inside the card still selects its model', () => {
    const { scope, event, selections, updates } = fixture(true);
    scope.selection.handleSelect('model', event);
    assert.deepEqual(selections, [['model']]);
    assert.equal(updates(), 1);
});
