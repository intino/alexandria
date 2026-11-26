import assert from 'node:assert/strict';
import {readFile} from 'node:fs/promises';

const source = await readFile(new URL('../src/services/ClientErrorService.js', import.meta.url), 'utf8');
const { createClientErrorService } = await import('data:text/javascript;base64,' + Buffer.from(source).toString('base64'));
const listeners = {}, requests = [];
let ok = false, now = 1800000000000;
const originalNow = Date.now;
Date.now = () => now;
const browser = {
    location: { href: 'https://example.com/app/private?token=secret', origin: 'https://example.com' },
    crypto: { randomUUID: () => '12345678-1234-1234-1234-123456789012' },
    addEventListener: (name, listener) => { listeners[name] = listener; },
    setInterval() {}, setTimeout() {}, clearTimeout() {},
    fetch: async (url, options) => { requests.push({ url, ...options }); return { ok }; }
};
try {
    const service = createClientErrorService(browser);
    service.configure('wss://example.com/app/_alexandria/push?currentSession=secret');
    const error = { name: 'TypeError', message: 'password secret@example.com',
        stack: 'TypeError: password\n at account (https://example.com/app/main.js?token=secret:12:34)' };
    listeners.error({ error }); listeners.unhandledrejection({ reason: error });
    await service.flush();
    assert.equal(requests.length, 1);
    assert.equal(requests[0].url, 'https://example.com/app/_alexandria/client-errors');
    assert.match(requests[0].body, /TypeError\t2\t\d+\tmain.js:12:34/);
    assert.doesNotMatch(requests[0].body, /secret|password|account|example|private/);
    await service.flush(); assert.equal(requests.length, 1);
    now += 30000; ok = true; await service.flush();
    assert.equal(requests[0].body, requests[1].body);
    now += 30000; await service.flush(); assert.equal(requests.length, 2);
    for (let i = 0; i < 30; i++) service.capture({ name: 'Error', stack: 'Error\n at https://example.com/f' + i + '.js:1:1' });
    await service.flush();
    assert.equal(requests[2].body.split('\n').length, 21);
    assert.ok(Buffer.byteLength(requests[2].body) < 16384);
    console.log('Client errors: privacy, batching, deduplication, retry and buffer checks passed');
} finally { Date.now = originalNow; }
