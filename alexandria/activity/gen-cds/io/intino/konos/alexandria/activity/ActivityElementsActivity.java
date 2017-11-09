package io.intino.konos.alexandria.activity;

import io.intino.konos.alexandria.activity.AlexandriaActivityBox;
import io.intino.konos.alexandria.activity.AssetResourceLoader;
import io.intino.konos.alexandria.activity.displays.*;
import io.intino.konos.alexandria.activity.displays.notifiers.*;
import io.intino.konos.alexandria.activity.displays.requesters.*;
import io.intino.konos.alexandria.activity.resources.*;

import io.intino.konos.alexandria.activity.ActivityAlexandriaSpark;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.services.push.PushService;
import io.intino.konos.alexandria.activity.spark.resources.AfterDisplayRequest;
import io.intino.konos.alexandria.activity.spark.resources.AssetResource;
import io.intino.konos.alexandria.activity.spark.resources.AuthenticateCallbackResource;
import io.intino.konos.alexandria.activity.spark.resources.BeforeDisplayRequest;

public class ActivityElementsActivity extends io.intino.konos.alexandria.activity.Activity {

	public static ActivityAlexandriaSpark init(ActivityAlexandriaSpark spark, ActivityBox box) {
		//AlexandriaActivityConfiguration.ActivityElementsActivityConfiguration configuration = box.configuration().activityElementsConfiguration;
		spark.route("/push").push(new PushService());
		spark.route("/authenticate-callback").get(manager -> new AuthenticateCallbackResource(manager, notifierProvider()).execute());
		spark.route("/asset/:name").get(manager -> new AssetResource(name -> new AssetResourceLoader(box).load(name), manager, notifierProvider()).execute());

		spark.route("").get(manager -> new HomePageResource(box, manager, notifierProvider()).execute());

		spark.route("/alexandriapanelcatalogview/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriapanelcatalogview/:displayId").post(manager -> new AlexandriaPanelCatalogViewDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriapanelcatalogview/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriapagecontainer/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriapagecontainer/:displayId").post(manager -> new AlexandriaPageContainerDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriapagecontainer/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriadialogcontainer/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriadialogcontainer/:displayId").post(manager -> new AlexandriaDialogContainerDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriadialogcontainer/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriadesktop/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriadesktop/:displayId").post(manager -> new AlexandriaDesktopDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriadesktop/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriapaneldisplayview/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriapaneldisplayview/:displayId").post(manager -> new AlexandriaPanelDisplayViewDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriapaneldisplayview/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriatablayout/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriatablayout/:displayId").post(manager -> new AlexandriaTabLayoutDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriatablayout/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriacataloglistview/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriacataloglistview/:displayId").post(manager -> new AlexandriaCatalogListViewDisplayRequester(manager, notifierProvider()).execute());
		spark.route("/alexandriacataloglistview/:displayId").get(manager -> new AlexandriaCatalogListViewDisplayRequester(manager, notifierProvider()).execute());
		spark.route("/alexandriacataloglistview/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriatimerangenavigator/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriatimerangenavigator/:displayId").post(manager -> new AlexandriaTimeRangeNavigatorDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriatimerangenavigator/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriatimenavigator/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriatimenavigator/:displayId").post(manager -> new AlexandriaTimeNavigatorDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriatimenavigator/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriacatalogmapview/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriacatalogmapview/:displayId").post(manager -> new AlexandriaCatalogMapViewDisplayRequester(manager, notifierProvider()).execute());
		spark.route("/alexandriacatalogmapview/:displayId").get(manager -> new AlexandriaCatalogMapViewDisplayRequester(manager, notifierProvider()).execute());
		spark.route("/alexandriacatalogmapview/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriacatalogmagazineview/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriacatalogmagazineview/:displayId").post(manager -> new AlexandriaCatalogMagazineViewDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriacatalogmagazineview/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriamenulayout/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriamenulayout/:displayId").post(manager -> new AlexandriaMenuLayoutDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriamenulayout/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriamold/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriamold/:displayId").post(manager -> new AlexandriaMoldDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriamold/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriapanelcustomview/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriapanelcustomview/:displayId").post(manager -> new AlexandriaPanelCustomViewDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriapanelcustomview/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriacatalogviewlist/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriacatalogviewlist/:displayId").post(manager -> new AlexandriaCatalogViewListDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriacatalogviewlist/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriapanel/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriapanel/:displayId").post(manager -> new AlexandriaPanelDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriapanel/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriacatalog/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriacatalog/:displayId").post(manager -> new AlexandriaCatalogDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriacatalog/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriatemporalrangecatalog/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriatemporalrangecatalog/:displayId").post(manager -> new AlexandriaTemporalRangeCatalogDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriatemporalrangecatalog/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriacatalogdisplayview/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriacatalogdisplayview/:displayId").post(manager -> new AlexandriaCatalogDisplayViewDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriacatalogdisplayview/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriatemporaltimecatalog/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriatemporaltimecatalog/:displayId").post(manager -> new AlexandriaTemporalTimeCatalogDisplayRequester(manager, notifierProvider()).execute());

		spark.route("/alexandriatemporaltimecatalog/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());
		spark.route("/alexandriaitem/:displayId").before(manager -> new BeforeDisplayRequest(manager).execute());
		spark.route("/alexandriaitem/:displayId").post(manager -> new AlexandriaItemDisplayRequester(manager, notifierProvider()).execute());
		spark.route("/alexandriaitem/:displayId").get(manager -> new AlexandriaItemDisplayRequester(manager, notifierProvider()).execute());
		spark.route("/alexandriaitem/:displayId").after(manager -> new AfterDisplayRequest(manager).execute());


		registerNotifiers();
		return spark;
	}

	private static void registerNotifiers() {
		register(AlexandriaPanelCatalogViewDisplayNotifier.class).forDisplay(AlexandriaPanelCatalogViewDisplay.class);
		register(AlexandriaPageContainerDisplayNotifier.class).forDisplay(AlexandriaPageContainerDisplay.class);
		register(AlexandriaDialogContainerDisplayNotifier.class).forDisplay(AlexandriaDialogContainerDisplay.class);
		register(AlexandriaDesktopDisplayNotifier.class).forDisplay(AlexandriaDesktopDisplay.class);
		register(AlexandriaPanelDisplayViewDisplayNotifier.class).forDisplay(AlexandriaPanelDisplayViewDisplay.class);
		register(AlexandriaTabLayoutDisplayNotifier.class).forDisplay(AlexandriaTabLayoutDisplay.class);
		register(AlexandriaCatalogListViewDisplayNotifier.class).forDisplay(AlexandriaCatalogListViewDisplay.class);
		register(AlexandriaTimeRangeNavigatorDisplayNotifier.class).forDisplay(AlexandriaTimeRangeNavigatorDisplay.class);
		register(AlexandriaTimeNavigatorDisplayNotifier.class).forDisplay(AlexandriaTimeNavigatorDisplay.class);
		register(AlexandriaCatalogMapViewDisplayNotifier.class).forDisplay(AlexandriaCatalogMapViewDisplay.class);
		register(AlexandriaCatalogMagazineViewDisplayNotifier.class).forDisplay(AlexandriaCatalogMagazineViewDisplay.class);
		register(AlexandriaMenuLayoutDisplayNotifier.class).forDisplay(AlexandriaMenuLayoutDisplay.class);
		register(AlexandriaMoldDisplayNotifier.class).forDisplay(AlexandriaMoldDisplay.class);
		register(AlexandriaPanelCustomViewDisplayNotifier.class).forDisplay(AlexandriaPanelCustomViewDisplay.class);
		register(AlexandriaCatalogViewListDisplayNotifier.class).forDisplay(AlexandriaCatalogViewListDisplay.class);
		register(AlexandriaPanelDisplayNotifier.class).forDisplay(AlexandriaPanelDisplay.class);
		register(AlexandriaCatalogDisplayNotifier.class).forDisplay(AlexandriaCatalogDisplay.class);
		register(AlexandriaTemporalRangeCatalogDisplayNotifier.class).forDisplay(AlexandriaTemporalRangeCatalogDisplay.class);
		register(AlexandriaCatalogDisplayViewDisplayNotifier.class).forDisplay(AlexandriaCatalogDisplayViewDisplay.class);
		register(AlexandriaTemporalTimeCatalogDisplayNotifier.class).forDisplay(AlexandriaTemporalTimeCatalogDisplay.class);
		register(AlexandriaItemDisplayNotifier.class).forDisplay(AlexandriaItemDisplay.class);

	}
}