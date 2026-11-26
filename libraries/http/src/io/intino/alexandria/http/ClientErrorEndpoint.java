package io.intino.alexandria.http;

import io.intino.alexandria.logger.Logger;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

final class ClientErrorEndpoint {
    private final String salt = UUID.randomUUID().toString();
    private final Map<String, Long> senders = new LinkedHashMap<>();
    private final Map<String, Boolean> batches = new LinkedHashMap<>();
    private long window;
    private int received;

    static void register(Javalin service, Function<Context, String> identity) {
        ClientErrorEndpoint endpoint = new ClientErrorEndpoint();
        service.unsafe.routes.post("/_alexandria/client-errors", ctx -> endpoint.receive(ctx, identity));
    }

    private void receive(Context ctx, Function<Context, String> identity) throws Exception {
        // The custom header prevents cross-origin form submissions; never enable CORS for this route.
        if (!"1".equals(ctx.header("X-Alexandria-Client-Errors")) ||
                !"text/plain".equals(ctx.contentType())) {
            ctx.status(400);
            return;
        }
        if (ctx.req().getContentLengthLong() > 16384) { ctx.status(413); return; }
        byte[] bytes = ctx.req().getInputStream().readNBytes(16385);
        if (bytes.length > 16384) { ctx.status(413); return; }
        String[] lines = new String(bytes, StandardCharsets.UTF_8).split("\n", -1);
        if (lines.length < 2 || lines.length > 21 || !lines[0].matches("[a-f0-9-]{36}")) {
            ctx.status(400); return;
        }
        for (int i = 1; i < lines.length; i++) {
            if (!validRecord(lines[i])) { ctx.status(400); return; }
        }
        String sender = reference(identity.apply(ctx));
        int status = accept(sender, lines[0]);
        if (status == 200) {
            for (int i = 1; i < lines.length; i++)
                Logger.error("[client-error] actor=" + sender + " batch=" + lines[0] + " record=" + lines[i].replace('\t', ' '));
        }
        ctx.status(status == 200 ? 204 : status);
    }

    static boolean validRecord(String record) {
        return record.matches("(Error|TypeError|ReferenceError|SyntaxError|RangeError|URIError|EvalError|AggregateError)\\t[1-9][0-9]{0,6}\\t[0-9]{13}\\t([A-Za-z0-9_.-]{1,100}\\.m?js:[0-9]{1,7}:[0-9]{1,7}(,[A-Za-z0-9_.-]{1,100}\\.m?js:[0-9]{1,7}:[0-9]{1,7}){0,7})?");
    }

    private synchronized int accept(String sender, String batch) {
        String key = sender + ":" + batch;
        if (batches.containsKey(key)) return 204;
        long now = System.currentTimeMillis();
        if (now - window >= 60000) { window = now; received = 0; }
        if (received >= 120 || now - senders.getOrDefault(sender, 0L) < 10000) return 429;
        received++;
        senders.put(sender, now);
        batches.put(key, true);
        if (senders.size() > 1024) senders.remove(senders.keySet().iterator().next());
        if (batches.size() > 4096) batches.remove(batches.keySet().iterator().next());
        return 200;
    }

    private String reference(String identity) {
        if (identity == null) return "anonymous";
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest((salt + identity).getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 12; i++) result.append(String.format("%02x", digest[i]));
            return result.toString();
        } catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }
}
