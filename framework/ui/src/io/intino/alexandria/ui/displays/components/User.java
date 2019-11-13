package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.I18n;
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

    public User(B box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        refresh();
    }

    public User onRefresh(Listener listener) {
        refreshListener = listener;
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
        session().logout();
        notifier.redirect(session().browser().baseUrl());
    }

    private UserInfo info(io.intino.alexandria.ui.services.push.User user) {
        String fullName = user != null ? user.fullName() : I18n.translate("anonymous", language());
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