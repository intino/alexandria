package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.UserInfo;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.UserNotifier;
import io.intino.alexandria.ui.utils.AvatarUtil;

import java.net.URL;

public class User<DN extends UserNotifier, B extends Box> extends AbstractUser<B> {
    private Listener refreshListener;
    private Listener beforeLogoutListener;
    private Listener logoutListener;
    private String logoutUrl;

    public User(B box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        refresh();
    }

    @Override
    public void unregister() {
    }

    public User onRefresh(Listener listener) {
        refreshListener = listener;
        return this;
    }

    public User onBeforeLogout(Listener listener) {
        beforeLogoutListener = listener;
        return this;
    }

    public User onLogout(Listener listener) {
        logoutListener = listener;
        return this;
    }

    public User logoutUrl(String url) {
        this.logoutUrl = url;
        return this;
    }

    @Override
    public void refresh() {
        super.refresh();
        io.intino.alexandria.ui.services.push.User user = session().user();
        notifier.refresh(info(user));
    }

    public void refreshChildren() {
        notifyRefresh();
        children().forEach(Display::refresh);
    }

    public void logout() {
        if (beforeLogoutListener != null) beforeLogoutListener.accept(new Event(this));
        session().logout();
        if (logoutListener != null) logoutListener.accept(new Event(this));
        notifier.redirect(logoutUrl != null ? logoutUrl : session().browser().baseUrl());
    }

    private UserInfo info(io.intino.alexandria.ui.services.push.User user) {
        String fullName = user != null ? user.fullName() : translate("anonymous");
        URL photo = user != null ? user.photo() : null;
        String color = color() != null ? color() : "#2096F3";
        String photoLink = photo != null ? photoLink(photo) : AvatarUtil.generateAvatar(fullName, color);
        return new UserInfo().fullName(fullName).photo(photoLink);
    }

    private String photoLink(URL photo) {
        return Asset.toResource(baseAssetUrl(), photo).toUrl().toString();
    }

    private void notifyRefresh() {
        if (refreshListener != null) refreshListener.accept(new Event(this));
    }
}