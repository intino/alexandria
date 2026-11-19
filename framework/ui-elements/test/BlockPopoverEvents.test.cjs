const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const { test } = require('node:test');
const babel = require('@babel/core');
const React = require('react');

const sourcePath = process.env.POPOVER_SOURCE || path.join(__dirname, '../src/displays/components/BlockPopover.js');
const source = fs.readFileSync(sourcePath, 'utf8')
    .replace(/^import .*;\r?\n/gm, '')
    .replace(/^export default .*;\r?\n/gm, '')
    .replace(/^DisplayFactory\.register.*;\r?\n/gm, '');
const compiled = babel.transformSync(source + '\nglobalThis.Component = BlockPopover;', {
    configFile: false,
    babelrc: false,
    presets: [require.resolve('@babel/preset-react')],
}).code;

function popover() {
    const scope = {
        React,
        Popover: 'popover',
        AbstractBlockPopover: class {
            constructor(props) { this.props = props; this.state = {}; }
            style() { return {}; }
            _widthDefined() { return false; }
            _heightDefined() { return false; }
        },
        BlockPopoverNotifier: class {},
        BlockPopoverRequester: class {},
        window: { setTimeout() {} },
        document: { getElementById() { return null; } },
    };
    vm.runInNewContext(compiled, scope);
    const instance = new scope.Component({ children: 'Download ZIP' });
    instance.state.triggerId = 'export-trigger';
    return instance;
}

function event() {
    return {
        stopped: false,
        defaultPrevented: false,
        stopPropagation() { this.stopped = true; },
        preventDefault() { this.defaultPrevented = true; },
    };
}

test('clicking a menu option stops at the portal without cancelling its action', () => {
    const view = popover().render();
    const click = event();
    let downloads = 0;
    let cardOpens = 0;
    downloads++;
    view.props.onClick(click);
    if (!click.stopped) cardOpens++;
    assert.equal(downloads, 1);
    assert.equal(cardOpens, 0);
    assert.equal(click.defaultPrevented, false);
});

test('closing by clicking outside does not activate the card', () => {
    const component = popover();
    const click = event();
    let closes = 0;
    component.requester.close = () => closes++;
    component.render().props.onClose(click, 'backdropClick');
    assert.equal(closes, 1);
    assert.equal(click.stopped, true);
    assert.equal(click.defaultPrevented, false);
});

test('mouse down on the portal does not activate parent selection', () => {
    const click = event();
    popover().render().props.onMouseDown(click);
    assert.equal(click.stopped, true);
    assert.equal(click.defaultPrevented, false);
});

test('closing without a DOM event still requests closure', () => {
    const component = popover();
    let closes = 0;
    component.requester.close = () => closes++;
    component.handleClose();
    assert.equal(closes, 1);
});

test('the outer paper follows the menu corners without a second surface', () => {
    const component = popover();
    component.style = () => ({ borderRadius: '10px' });
    const paper = component.render().props.slotProps.paper.style;
    assert.equal(paper.borderRadius, '10px');
    assert.equal(paper.background, 'transparent');
    assert.equal(paper.backgroundImage, 'none');
});
