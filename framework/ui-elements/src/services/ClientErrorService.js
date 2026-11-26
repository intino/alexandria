// Deliberately omit error messages, arbitrary rejection values and page URLs: they can contain user data.
export function createClientErrorService(browser) {
    const pending = new Map();
    let endpoint, flight, lastSend = 0, sending = false;
    const types = new Set(['Error', 'TypeError', 'ReferenceError', 'SyntaxError', 'RangeError', 'URIError', 'EvalError', 'AggregateError']);
    function capture(error) {
        try {
            const type = error && types.has(error.name) ? error.name : 'Error';
            const frames = String(error && error.stack || '').split('\n').slice(1).map(line => {
                const match = line.match(/\/([A-Za-z0-9_.-]{1,64}\.m?js)(?:\?[^\s)]*)?:(\d{1,7}):(\d{1,7})/);
                return match ? match[1] + ':' + match[2] + ':' + match[3] : null;
            }).filter(Boolean).slice(0, 6).join(',');
            const key = type + '\t' + frames;
            if (pending.has(key)) pending.get(key).count = Math.min(1000000, pending.get(key).count + 1);
            else if (pending.size < 20) pending.set(key, { type, frames, count: 1, time: Date.now() });
        } catch (_) { /* Reporting must never produce another error. */ }
    }
    async function flush() {
        if (!endpoint || sending || Date.now() - lastSend < 30000) return;
        if (!flight && pending.size) {
            const records = Array.from(pending.values());
            pending.clear();
            flight = browser.crypto.randomUUID() + '\n' + records.map(r => [r.type, r.count, r.time, r.frames].join('\t')).join('\n');
        }
        if (!flight) return;
        lastSend = Date.now();
        const batch = flight;
        sending = true;
        const controller = new AbortController();
        const timeout = browser.setTimeout(() => controller.abort(), 10000);
        try {
            const response = await browser.fetch(endpoint, { method: 'POST', credentials: 'same-origin',
                headers: { 'Content-Type': 'text/plain', 'X-Alexandria-Client-Errors': '1' }, body: batch, signal: controller.signal });
            if (response.ok && flight === batch) flight = null;
        } catch (_) { /* Keep the same batch id for a bounded retry. */ }
        finally { browser.clearTimeout(timeout); sending = false; }
    }
    browser.addEventListener('error', event => capture(event.error));
    browser.addEventListener('unhandledrejection', event => capture(event.reason));
    browser.setInterval(flush, 30000);
    return {
        configure(pushUrl) {
            const url = new URL(pushUrl, browser.location.href);
            url.protocol = url.protocol === 'wss:' ? 'https:' : url.protocol === 'ws:' ? 'http:' : url.protocol;
            if (url.origin !== browser.location.origin || !url.pathname.endsWith('/_alexandria/push')) return;
            url.pathname = url.pathname.replace(/\/push$/, '/client-errors');
            url.search = ''; url.hash = '';
            endpoint = url.href;
        },
        capture, flush
    };
}

export default typeof window !== 'undefined' ? createClientErrorService(window) : null;
