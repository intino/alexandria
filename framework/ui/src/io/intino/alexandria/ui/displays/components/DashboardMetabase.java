package io.intino.alexandria.ui.displays.components;

import com.auth0.jwt.algorithms.Algorithm;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.AbstractDashboardMetabase;
import io.intino.alexandria.ui.displays.notifiers.DashboardMetabaseNotifier;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class DashboardMetabase<DN extends DashboardMetabaseNotifier, B extends Box> extends AbstractDashboardMetabase<DN, B> {
    private String url;
    private String secretKey;
    private int dashboard = -1;
    private boolean bordered;
    private boolean titled;
    private Theme theme = Theme.Light;

    private static final String Header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String Payload = "{\"resource\":{\"dashboard\":%d},\"params\":{%s},\"iat\":%d}";

    public enum Theme { Dark, Light }

    public DashboardMetabase(B box) {
        super(box);
    }

    public DashboardMetabase<DN, B> url(String url) {
        _url(url);
        return this;
    }

    public DashboardMetabase<DN, B> secretKey(String key) {
        _secretKey(key);
        return this;
    }

    public DashboardMetabase<DN, B> dashboard(int id) {
        _dashboard(id);
        return this;
    }

    public DashboardMetabase<DN, B> bordered(boolean bordered) {
        _bordered(bordered);
        return this;
    }

    public DashboardMetabase<DN, B> titled(boolean titled) {
        _titled(bordered);
        return this;
    }

    public DashboardMetabase<DN, B> theme(Theme theme) {
        _theme(theme);
        return this;
    }

    protected DashboardMetabase<DN, B> _url(String url) {
        this.url = url;
        return this;
    }

    protected DashboardMetabase<DN, B> _secretKey(String key) {
        this.secretKey = key;
        return this;
    }

    protected DashboardMetabase<DN, B> _dashboard(int id) {
        this.dashboard = id;
        return this;
    }

    protected DashboardMetabase<DN, B> _bordered(boolean bordered) {
        this.bordered = bordered;
        return this;
    }

    protected DashboardMetabase<DN, B> _titled(boolean titled) {
        this.titled = titled;
        return this;
    }

    protected DashboardMetabase<DN, B> _theme(Theme theme) {
        this.theme = theme;
        return this;
    }

    @Override
    public void refresh() {
        if (!check()) return;
        notifier.refresh(new DashboardMetabaseInfo().location(location()));
    }

    private String location() {
        String header = base64(Header);
        String payload = base64(payload());
        String signature = base64(sign(header, payload));
        String token = header + "." + payload + "." + signature;
        return url + "/embed/dashboard/" + token + "#bordered=" + bordered + "&titled=" + titled + (theme == Theme.Dark ? "&theme=night" : "");
    }

    private String payload() {
        java.util.Date expireDate = Date.from(Instant.now().plus(120, ChronoUnit.MINUTES));
        return String.format(Payload, dashboard, params(), expireDate.toInstant().toEpochMilli());
    }

    private String params() {
        return parameters().entrySet().stream().map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"").collect(Collectors.joining(","));
    }

    private byte[] sign(String header, String payload) {
        return Algorithm.HMAC256(secretKey.getBytes()).sign(header.getBytes(), payload.getBytes());
    }

    private boolean check() {
        if (dashboard == -1) {
            notifier.refreshError(translate("You must set dashboard id provided by Metabase"));
            return false;
        }
        if (url == null || url.isEmpty()) {
            notifier.refreshError(translate("You must set the url where Metabase instance is running"));
            return false;
        }
        if (secretKey == null || secretKey.isEmpty()) {
            notifier.refreshError(translate("You must set the secret key provided by Metabase"));
            return false;
        }
        return true;
    }

    private String base64(String text) {
        return base64(text.getBytes());
    }

    public static String base64(byte[] data) {
        byte[] encode = new org.apache.commons.codec.binary.Base64().encode(data);
        for (int i = 0; i < encode.length; i++) {
            if (encode[i] == '+') {
                encode[i] = '-';
            } else if (encode[i] == '/') {
                encode[i] = '_';
            }
        }
        return new String(encode).replace("=","");
    }
}