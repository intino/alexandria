package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.ui.documentation.Item;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.*;
import io.intino.alexandria.ui.model.datasource.locations.Location;
import io.intino.alexandria.ui.model.datasource.locations.Point;
import io.intino.alexandria.ui.model.datasource.locations.Polygon;
import io.intino.alexandria.ui.model.datasource.locations.Polyline;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.intino.alexandria.ui.displays.components.Map.Type.Cluster;
import static io.intino.alexandria.ui.displays.components.Map.Type.Heatmap;
import static io.intino.alexandria.ui.documentation.Person.Gender.Female;
import static io.intino.alexandria.ui.documentation.Person.Gender.Male;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class Datasources {
	public static final int ItemCount = 1000;
	private static List<Item> itemPopulation = null;
	private static List<Person> personPopulation = null;
	private static Map<String, List<Group>> groupsMap = new HashMap<>();

	public static PageDatasource<Item> itemDatasource() {
		return itemDatasource(itemPopulation());
	}

	public static PageDatasource<Item> itemDatasource(List<Item> population) {
		return new PageDatasource<Item>() {
			@Override
			public long itemCount(String condition, List<Filter> filters) {
				return ItemCount;
			}

			@Override
			public List<Item> items(int start, int count, String condition, List<Filter> filters, List<String> sortings) {
				List<Item> items = population;
				if (sortings.size() > 0) Collections.sort(items, sortingComparator(sortings));
				return page(items, start, count);
			}

			@Override
			public List<Group> groups(String name) {
				if (!name.equalsIgnoreCase("alphabeticOrder")) return emptyList();
				return Datasources.groups("itemalphabeticorder", population, item -> ((Item)item).label().substring(0, 1));
			}

			public Comparator<Item> sortingComparator(List<String> sortings) {
				return Comparator.comparing(Item::label);
			}
		};
	}

	public static MapDatasource<Item> mapDatasource() {
		return locatedItemDatasource(itemPopulation().subList(0, 4), null);
	}

	public static MapDatasource<Item> clusterDatasource() {
		return locatedItemDatasource(itemPopulation(), Cluster);
	}

	public static MapDatasource<Item> heatDatasource() {
		return locatedItemDatasource(itemPopulation(), Heatmap);
	}

	public static PageDatasource<Person> personDatasource() {
		return personDatasource(personPopulation());
	}

	public static PageDatasource<Person> personDatasource(List<Person> population) {

		return new PageDatasource<Person>() {

			@Override
			public long itemCount(String condition, List<Filter> filters) {
				return filterPersonPopulation(population, condition, filters).size();
			}

			@Override
			public List<Person> items(int start, int count, String condition, List<Filter> filters, List<String> sortings) {
				List<Person> persons = filterPersonPopulation(population, condition, filters);
				if (sortings.size() > 0) persons.sort(sortingComparator(sortings));
				return page(persons, start, count);
			}

			@Override
			public List<Group> groups(String key) {
				if (key.toLowerCase().contains("gender")) return Datasources.groups("persongender", personPopulation(), item -> ((Person)item).gender().name());
				if (key.toLowerCase().contains("age group")) return Datasources.groups("personagegroup", personPopulation(), item -> ageGroupLabel(((Person)item).age()));
				return emptyList();
			}

			public Comparator<Person> sortingComparator(List<String> sortings) {
				if (sortings.size() <= 0) return Comparator.comparing(Person::lastName);
				Comparator<Person> comparator = null;

				for (String sorting : sortings) {
					if (comparator == null) comparator = comparator(sorting);
					else comparator.thenComparing(comparator(sorting));
				}

				return comparator;
			}

			private Comparator<Person> comparator(String sorting) {
				if (sorting.contains("oldest")) return (o1, o2) -> Integer.compare(o2.age(), o1.age());
				else if (sorting.contains("youthest")) return Comparator.comparingInt(Person::age);
				else if (sorting.contains("female")) return genderComparator(Female);
				else if (sorting.contains("male")) return genderComparator(Male);
				else return Comparator.comparing(Person::lastName);
			}

			private Comparator<Person> genderComparator(Person.Gender flag) {
				return (o1, o2) -> {
					if (o1.gender() == Female && o2.gender() == Female && flag == Female) return 1;
					if (o1.gender() == Female && o2.gender() == Female && flag == Male) return -1;
					if (o1.gender() == Male && o2.gender() == Male && flag == Male) return 1;
					if (o1.gender() == Male && o2.gender() == Male && flag == Female) return -1;
					if (o1.gender() == Male && o2.gender() == Female && flag == Female) return 1;
					if (o1.gender() == Female && o2.gender() == Male && flag == Female) return -1;
					if (o1.gender() == Male && o2.gender() == Female && flag == Male) return -1;
					if (o1.gender() == Female && o2.gender() == Male && flag == Male) return 1;
					return 1;
				};
			}

		};
	}

	private static List<Group> groups(String id, List<? extends Object> items, Function<Object, String> labelFunction) {
		if (!groupsMap.containsKey(id)) {
			Map<String, Group> groups = new HashMap<>();

			items.forEach(item -> {
				String label = labelFunction.apply(item);
				if (!groups.containsKey(label)) groups.put(label, new Group().label(label).count(0));
				Group group = groups.get(label);
				group.count(group.count() + 1);
			});

			groupsMap.put(id, new ArrayList<>(groups.values()));
		}

		return groupsMap.get(id);
	}

	private static <T> List<T> page(List<T> items, int start, int count) {
		List<T> result = new ArrayList<>();
		int end = start+count;
		if (items.size() < (start+count)) end = items.size();
		for (int i = start; i < end; i++) result.add(items.get(i));
		return result;
	}

	private static List<Person> filterPersonPopulation(List<Person> population, String condition, List<Filter> filters) {
		List<Person> result = population;
		result = filterCondition(result, condition);
		result = filter(result, getFilter("gender", filters), item -> ((Person)item).gender().name());
		result = filter(result, getFilter("age group", filters), item -> ageGroupLabel(((Person)item).age()));
		return result;
	}

	private static List<Person> filterCondition(List<Person> population, String condition) {
		if (condition == null || condition.isEmpty()) return population;
		return population.stream().filter(person -> person.firstName().toLowerCase().contains(condition.toLowerCase()) ||
				person.lastName().toLowerCase().contains(condition.toLowerCase()) ||
				person.gender().name().toLowerCase().equalsIgnoreCase(condition) ||
				String.valueOf(person.age()).contains(condition.toLowerCase())
		).collect(Collectors.toList());
	}

	private static List<Person> filter(List<Person> population, Filter filter, Function<Object, String> valueFunction) {
		if (filter == null || filter.groups().isEmpty()) return population;
		return population.stream().filter(person -> filter.groups().contains(valueFunction.apply(person))).collect(toList());
	}

	private static Filter getFilter(String name, List<Filter> filters) {
		return filters.stream().filter(f -> f.grouping().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	private static String ageGroupLabel(int age) {
		if (age <= 14) return "Child";
		if (age <= 24) return "Youth";
		if (age <= 64) return "Adult";
		return "Senior";
	}

	private static List<Item> itemPopulation() {
		if (itemPopulation != null) return new ArrayList<>(itemPopulation);
		itemPopulation = new ArrayList<>();
		for (int i = 0; i < ItemCount; i++)
			itemPopulation.add(new Item().label("item " + (i + 1)));
		return new ArrayList<>(itemPopulation);
	}

	private static List<Person> personPopulation() {
		if (personPopulation != null) return new ArrayList<>(personPopulation);
		personPopulation = new ArrayList<>();
		for (int i = 0; i < ItemCount; i++)
			personPopulation.add(new Person().firstName("first name " + (i + 1)).lastName("last name" + (i + 1)).gender(Math.random() < 0.5 ? Male : Female).age(randomAge()));
		return new ArrayList<>(personPopulation);
	}

	private static int randomAge() {
		Random r = new Random();
		return r.nextInt(100);
	}

	private static MapDatasource<Item> locatedItemDatasource(List<Item> population, io.intino.alexandria.ui.displays.components.Map.Type type) {
		Datasource datasource = itemDatasource(population);

		return new MapDatasource<Item>() {
			@Override
			public List<PlaceMark<Item>> placeMarks(String condition, List<Filter> filters, BoundingBox boundingBox) {
				List<PlaceMark<Item>> placeMarks = new ArrayList<>();
				List<Item> items = population;
				for (int i=0; i<items.size(); i++) placeMarks.add(placeMarkOf(items.get(i), i));
				return placeMarks;
			}

			@Override
			public long itemCount(String condition, List<Filter> filters) {
				return datasource.itemCount(condition, filters);
			}

			@Override
			public List<Group> groups(String key) {
				return datasource.groups(key);
			}

			private PlaceMark<Item> placeMarkOf(Item item, int pos) {
				return Datasources.placeMarkOf(item, pos, type);
			}
		};
	}

	private static <O> PlaceMark<O> placeMarkOf(Object item, int pos, io.intino.alexandria.ui.displays.components.Map.Type type) {
		Location location = locations[pos] != null ? locations[pos] : point(31.76672014, -19.48827855);
		Point point = location.points().get(0);
		if (type != null) location = point(point.latitude(), point.longitude());
		return new PlaceMark<>().item(item).location(location).label(String.valueOf(pos+1));
	}

	private static MapDatasource<Person> locatedPersonDatasource(List<Person> population, io.intino.alexandria.ui.displays.components.Map.Type type) {
		Datasource datasource = personDatasource(population);

		return new MapDatasource<Person>() {
			@Override
			public List<PlaceMark<Person>> placeMarks(String condition, List<Filter> filters, BoundingBox boundingBox) {
				List<PlaceMark<Person>> placeMarks = new ArrayList<>();
				List<Person> persons = filterPersonPopulation(population, condition, filters);
				for (int i=0; i<persons.size(); i++) placeMarks.add(placeMarkOf(persons.get(i), i));
				return placeMarks;
			}

			@Override
			public long itemCount(String condition, List<Filter> filters) {
				return datasource.itemCount(condition, filters);
			}

			@Override
			public List<Group> groups(String key) {
				return datasource.groups(key);
			}

			private PlaceMark<Person> placeMarkOf(Person person, int pos) {
				return Datasources.placeMarkOf(person, pos, type);
			}
		};
	}

	private static final Location[] locations = { point(28.39873982,-19.22047279),polygon(29.68417207,-15.12159456),polyline(27.15733156,-17.46439945),point(27.12464497,-16.43193532),point(27.97980838,-8.75748754),point(30.31037542,-17.65777327),polygon(26.57877013,-20.27289984),point(24.90553049,-11.14465802),point(30.04667921,-19.9549154),point(32.68565118,-16.36700389),polygon(26.05648419,-12.09302567),point(31.232192,-8.01642989),point(25.71967882,-6.96602706),point(23.138863,-18.55085127),point(34.9821108,-19.60844274),point(31.92551758,-16.18650004),point(28.87867862,-8.86357618),point(23.75078334,-11.96183814),point(36.59353564,-18.03742487),point(35.40458623,-19.47351813),point(29.34857718,-16.88999476),point(20.65711758,-13.14239962),point(19.59264658,-12.74525988),point(30.78947022,-18.11328464),point(30.70742607,-18.62017427),point(29.42187419,-23.67705339),point(28.17955721,-8.99414317),polyline(33.05811518,-14.14156879),point(36.73569935,-17.69732852),point(22.00779184,-20.08536148),point(35.55432437,-19.18274943),point(21.11253504,-14.68826361),point(23.48476948,-10.26510565),point(23.26584631,-20.64653521),point(25.72827153,-21.31910192),point(36.02225872,-18.89620394),point(25.45444719,-17.81136879),point(23.79179707,-16.46550773),point(24.4151836,-20.03872456),point(25.34608678,-12.01342524),point(31.18053799,-7.07654284),point(31.22958989,-12.34251736),point(21.65305731,-15.47296139),point(29.99768941,-18.84683127),polyline(33.50273333,-15.872497),polyline(33.18115496,-8.26061127),point(31.51127123,-5.93214219),polygon(26.46623389,-19.85422179),polygon(26.10858911,-8.01737326),polygon(26.18646196,-14.34720341),point(35.51779863,-13.25115706),point(35.72397608,-12.08654589),point(28.87159073,-19.80493153),point(21.72266345,-17.77702202),point(30.62164959,-11.01390842),point(19.69350518,-12.30194484),point(23.26198048,-9.0886273),point(24.11194987,-9.85223509),point(28.5709116,-23.54090823),point(22.74333163,-22.32417122),polyline(33.54351915,-22.44769276),point(36.30269114,-12.75712813),point(19.69330428,-15.37810873),point(30.62176978,-6.14301936),point(31.19689401,-19.53557228),point(30.78212709,-6.66060872),point(24.45738035,-13.18565279),point(24.8087408,-23.87756478),point(30.60848262,-16.35385683),point(29.03235423,-6.86505688),point(32.33992784,-9.44670677),point(32.26843419,-12.83942711),point(31.28914187,-16.68859699),point(25.15033223,-15.71080621),point(34.48060547,-10.62857816),polygon(26.24683014,-6.18396652),point(21.80165307,-16.27254064),point(27.96640667,-24.17674688),point(27.58180117,-9.64019095),point(28.29509772,-20.45062548),point(31.63409879,-16.07713515),point(30.53316417,-16.29018491),point(31.04117493,-11.97790777),point(32.20296529,-15.01254047),point(27.53283061,-23.96583709),point(32.87324964,-14.54617884),point(30.47297021,-11.07336167),polyline(33.02082131,-8.00060856),point(24.35945895,-17.04926149),point(29.14062152,-14.94297755),polygon(26.97800107,-25.11169645),point(27.79334001,-24.22719397),point(21.49349425,-15.03338429),point(24.7647981,-6.89774448),polyline(33.12649443,-19.50231647),point(32.97270503,-15.11234326),point(35.71637191,-15.75211903),point(34.45502093,-22.04714183),point(31.2019025,-8.24367262),point(25.48433209,-8.99299487),point(29.23577366,-16.66768716),point(30.44606929,-11.84099924),point(30.94592332,-22.95096767),point(31.7937682,-11.54531967),point(34.34253197,-11.21476583),point(34.96911465,-9.05465241),point(21.30163826,-18.4639924),point(31.77971852,-10.21440541),point(28.21233611,-25.38946403),point(28.2809361,-8.34327592),point(28.61607032,-11.08223491),point(31.07488907,-15.07793267),point(25.93837841,-6.02903224),point(23.54232292,-11.26680804),point(32.30900594,-12.92279521),polygon(26.25108937,-23.4444707),point(25.88429932,-7.78738872),point(23.16158836,-16.21599583),point(21.33066352,-10.70889758),point(32.30675509,-18.06943838),point(23.45257443,-20.03313924),point(35.5904776,-10.28713651),point(34.72198011,-19.05798411),point(30.63835688,-12.94315323),point(32.98789346,-12.71624363),point(23.73414539,-14.28126212),point(25.25813048,-22.06163981),point(28.17102209,-22.722107),point(20.95225656,-15.59059981),point(36.75149999,-17.69717432),point(32.48377201,-9.52855207),point(28.9268447,-20.26589757),point(27.5868812,-14.77722404),polygon(26.18496606,-24.80666163),point(21.26413349,-13.87516387),point(24.06265028,-21.0968703),polygon(26.59287648,-24.48142726),point(23.41210654,-20.46795452),point(21.65412887,-9.754503),point(25.64423238,-12.36252808),point(34.51492101,-17.63999726),point(22.85206017,-7.80316798),point(34.98377819,-18.10896915),point(28.44730027,-23.62379868),point(32.97485167,-10.34230968),point(21.98221737,-22.61450624),point(27.43793957,-21.00326332),point(20.54541382,-14.29540444),polygon(26.81055056,-14.82794747),point(19.42974771,-16.47567731),polygon(26.90551889,-15.00715512),point(23.88573352,-18.73367083),point(29.92812192,-16.14170424),point(36.20605748,-11.6523249),point(29.03489598,-9.29590194),point(29.46562084,-16.77425677),point(23.54529625,-12.22888397),point(23.36442729,-16.39645649),point(31.71431275,-7.96355208),point(24.52918551,-11.01702379),point(30.1578345,-15.64671677),polygon(26.21656665,-18.58880777),point(24.88925796,-20.04836118),point(22.87766948,-22.23150831),point(24.98377754,-9.58995639),point(31.31162042,-7.04413222),point(19.7245605,-16.09660865),point(20.96142873,-16.1363218),point(22.10286407,-16.56407414),point(34.63006828,-9.80996488),point(21.01816068,-14.45010521),polyline(33.02576542,-23.04089139),point(22.03070964,-9.69900779),point(21.62749455,-22.17173293),point(34.27166679,-18.89075027),point(24.42620284,-14.91066746),point(32.17477699,-19.16031894),point(20.85797742,-14.05018467),point(29.95188311,-9.32322674),point(29.65598102,-16.62988367),polygon(26.93462563,-6.60332089),point(29.46305692,-22.18399487),point(29.61141311,-15.7818352),point(34.99224051,-9.67962213),point(29.94719862,-14.04546077),polygon(26.38360475,-20.65793434),point(24.70481727,-23.43289151),point(27.61886726,-15.01913146),polygon(26.09800881,-12.64896532),point(20.64981013,-12.50152858),point(34.43997282,-18.69387997),point(28.40500515,-25.44228106),point(35.16918583,-17.25418546),point(21.4817851,-12.06852329),point(23.81779303,-15.58772311),point(35.13292128,-9.48762443),point(35.73576292,-10.36384238),point(21.07378288,-19.0651387),point(22.48728591,-10.88351131),point(23.70366312,-16.27486134),polyline(33.03709563,-16.93055611),point(27.14326532,-19.53514677),point(30.61579097,-24.21258939),polyline(33.39742652,-7.88644935),point(36.70280599,-17.477392),point(22.24113214,-17.09162148),point(24.18598722,-21.89053809),point(25.62888217,-17.7172251),point(27.84866403,-7.54085406),point(28.34023053,-13.5526463),point(30.48786935,-13.73965751),point(21.49634666,-12.6673201),point(25.18925417,-23.06829684),polygon(26.77310869,-7.26559495),point(27.50517506,-25.17205132),point(29.56996286,-23.63217256),polygon(26.65514094,-13.81126135),polygon(26.90961182,-18.44736508),polygon(26.79706151,-12.328588),polyline(33.07720747,-17.26439839),point(23.07371043,-12.57875564),point(35.31881696,-18.63774246),point(29.3313631,-22.12472593),polyline(33.10049227,-16.85015117),point(25.82565173,-22.48992597),point(29.2795236,-24.16412623),point(21.1120239,-15.55110498),polygon(26.43204493,-14.41311163),point(34.5416377,-8.27245),point(30.42753262,-23.94714049),point(35.13728237,-20.89582957),point(32.62168149,-11.08480487),polygon(26.17475482,-23.27592637),point(25.97917969,-14.57232036),point(25.38780269,-22.48002867),point(20.85161521,-15.92384024),point(30.86957587,-17.03350951),point(24.7972016,-8.24584251),point(34.37866627,-8.78221062),point(24.63651366,-14.68797761),point(36.99654807,-15.43068864),point(20.89893159,-16.96067288),point(25.45191714,-9.95583103),point(25.39957049,-7.04882679),point(22.24725621,-18.51584887),polyline(33.3203487,-21.89103363),point(31.10908438,-11.18618351),point(23.55100027,-9.59070377),polyline(33.62122927,-14.6545434),point(21.37080104,-12.66095625),point(23.71721793,-10.23331162),point(34.55945085,-12.79757782),point(30.58032877,-11.87532065),point(24.73985492,-21.09119397),point(30.06071914,-17.92479982),point(34.80321767,-14.23988605),point(25.4039002,-24.65985356),point(32.8366975,-18.71950107),point(23.32765962,-12.53147972),point(31.9336199,-23.98590912),point(25.18797383,-20.47549001),point(30.15490446,-14.64719566),point(27.50253309,-10.63808502),point(30.58148856,-22.66564802),point(28.51636934,-11.95536693),point(34.91167054,-20.21878658),point(30.21927513,-16.71303247),point(27.22568448,-10.2553326),polygon(26.96414934,-12.93254578),point(27.33390961,-8.40699259),point(31.94644156,-13.84090902),point(27.73000874,-16.41109677),point(35.79332648,-16.48923181),point(29.59362196,-14.61275113),point(24.47843192,-12.93919279),point(27.33035276,-12.03654034),point(27.17278249,-5.398902),point(19.97474001,-18.05239841),point(20.80626849,-18.5079853),point(29.97082986,-18.29113898),point(30.47462269,-22.5515792),polygon(26.50640279,-15.28065051),point(25.62565968,-14.15739711),polygon(26.0848404,-6.21251785),point(22.99626023,-20.88673111),point(36.18688227,-13.55667822),polygon(26.80609953,-9.92945114),point(20.59582543,-19.8678397),point(28.32071121,-6.11741524),point(30.93189448,-14.58980995),point(23.91871544,-19.55445904),point(28.60236141,-14.47015381),point(32.08889263,-16.27479084),point(30.83654843,-23.19124368),point(28.21648008,-13.93172813),point(25.17401252,-13.79551165),point(31.70573824,-11.14552671),point(31.76214037,-15.51442535),point(24.9509419,-9.77712636),point(30.72951682,-13.6348796),point(21.49758397,-10.82807747),point(35.44474395,-13.26894429),point(35.98956161,-12.84438929),point(25.70154656,-20.33573372),polygon(26.54768196,-16.84743226),point(30.81599773,-15.29085795),point(22.23115924,-10.76843939),polygon(26.51121918,-20.10901613),polyline(33.84347392,-21.87546125),point(31.94907396,-20.66597289),point(23.98511029,-11.90811177),point(27.96111034,-9.35834306),point(23.16006525,-23.17955037),point(28.51527873,-22.40965781),point(28.13308628,-18.12426417),point(23.75722361,-16.58928151),point(23.83428743,-13.86134995),point(23.89205703,-17.82796406),point(24.00766192,-16.16946442),point(25.13518369,-24.73975395),point(36.37504084,-16.04890918),polyline(33.19468425,-13.14350613),polygon(26.38768068,-20.81444073),point(23.56557012,-19.24166209),point(24.68687721,-15.23680806),point(32.70826222,-9.47853458),point(32.93711178,-11.63215367),point(24.8653755,-23.13544978),point(28.6322565,-22.19558571),point(29.07566764,-16.37149299),point(27.58355122,-10.64348294),point(32.5965335,-17.78280427),point(23.97489346,-19.86716887),point(27.92073895,-15.25202605),point(28.02618497,-7.82500908),point(29.67831625,-25.08673034),point(30.3046738,-13.85118398),point(28.31252876,-20.44052937),point(30.25852058,-6.1177149),point(34.70112492,-18.36125053),point(24.95533128,-14.24082422),point(28.25048835,-22.42625754),point(30.142654,-17.21516776),point(25.3250607,-21.46748905),polygon(26.941704,-17.6419203),point(19.98900839,-14.3406384),point(23.17305152,-22.39191837),point(22.74445922,-13.60500606),point(29.82956784,-17.79749313),point(31.23777843,-17.26476929),polygon(26.9673418,-6.82015219),point(36.42720968,-13.86355221),point(19.51669042,-13.21246942),point(28.83126764,-11.81927701),point(25.51854962,-20.05200743),point(29.67397493,-24.34641611),point(25.93854297,-23.41980318),point(22.195302,-9.67535399),polygon(26.36516773,-19.02895363),polyline(33.45094044,-9.946442),point(29.1259662,-10.29450817),point(20.6249626,-12.96821539),point(23.26123633,-7.32821883),point(34.59104564,-21.80511167),point(24.70177131,-21.59596453),point(29.61358504,-12.01257725),point(21.30255795,-17.30324159),point(28.6617113,-18.58203544),point(19.45852026,-15.1211319),point(35.11728024,-12.40471556),point(24.38124795,-17.28214066),point(22.09939264,-11.32955633),point(32.40242764,-13.6571973),point(24.61936742,-13.22026156),point(28.63917604,-24.55096229),point(34.88988853,-10.8459783),point(34.95452701,-21.29429234),polyline(33.56918158,-11.35934295),point(34.69809549,-16.49102918),point(22.40743129,-22.80593539),point(25.01178983,-16.69322949),point(28.99623459,-5.75270936),point(21.71105378,-15.31348478),point(32.14553004,-13.61532394),point(36.67472935,-14.35947112),point(29.42039672,-24.01721423),point(28.49037314,-14.38559398),point(22.76431917,-18.43169333),polygon(26.12792879,-11.1284024),point(31.18962619,-24.18710242),point(35.15869202,-15.92706055),polygon(26.09981254,-23.13508542),point(34.82831627,-19.08703752),point(21.07194048,-14.5280522),point(22.96984541,-8.93430985),polygon(26.68142639,-10.88357083),point(27.99280105,-19.28629746),point(31.0142022,-24.6831913),point(25.41647439,-8.59260918),point(28.60285913,-9.1646478),point(31.33878077,-17.62733568),point(30.1666891,-18.61821981),point(23.87840094,-15.76333667),point(35.25623992,-12.56902031),polyline(33.0395566,-15.10039664),point(27.35994691,-13.14261314),point(29.31706548,-24.19664043),point(30.94445607,-15.74372141),polygon(26.94765286,-20.12726368),point(28.85596033,-12.35748799),point(22.87958034,-12.06970679),polyline(33.44310343,-21.60008165),point(21.56368338,-12.53141476),point(36.15598081,-12.5019102),point(23.94023391,-11.24105735),point(31.13197849,-23.04398601),point(24.97889445,-16.52418726),point(34.0281311,-16.35945453),point(24.81471042,-18.17847838),point(29.0258703,-13.24009465),polyline(33.34663599,-15.00912029),polygon(26.35495726,-16.20011765),polygon(26.60022276,-22.54665545),point(29.60525912,-13.06284262),point(22.67438317,-20.38806302),point(28.44867267,-23.81668903),point(24.53178354,-7.00388727),point(35.81783381,-16.02725126),point(20.71009021,-19.83175849),point(34.79721545,-21.10082421),point(35.59259209,-13.34173469),point(25.56979016,-22.19806908),point(27.22324047,-23.97690534),point(30.92863073,-6.37119666),point(24.61579309,-14.43296454),point(30.76348547,-11.54877709),point(22.44813016,-15.58572789),polyline(33.17890233,-10.59950366),polygon(26.47060328,-6.32835283),point(31.02313804,-13.22886673),point(28.38849056,-16.64529446),point(31.07645684,-10.28673589),point(30.01521492,-14.51184251),polygon(26.49903571,-12.45000903),point(22.3461229,-21.81339007),polygon(26.97765511,-16.35396461),point(30.95315694,-23.02097096),point(32.48650992,-22.57840373),point(31.66630015,-18.75780788),point(35.0320916,-11.44485521),point(25.98843119,-10.59404203),polyline(33.19750896,-15.56606565),point(32.4902457,-8.03041496),point(21.44446278,-19.16838715),point(29.80194089,-23.62846437),point(21.17689356,-14.45786834),point(35.91407749,-19.05352477),point(30.37807488,-19.9671605),point(23.5731061,-24.01850892),point(25.4882737,-12.24345299),point(29.94974639,-24.94163145),point(28.57525301,-5.38975842),point(22.28200111,-14.68298072),point(30.25007233,-14.45794763),point(32.96298326,-20.54729167),point(25.94155848,-23.10820963),point(28.25511172,-23.14854816),point(24.16975249,-11.2209156),polygon(26.09265547,-18.00861494),point(23.48964913,-16.52109084),point(32.85874506,-16.47857659),point(32.71352636,-12.06053155),point(35.75437938,-11.70196916),point(20.9160618,-18.71154882),point(22.65406058,-14.7883863),point(32.18457699,-14.05341048),point(24.88988917,-15.51761494),point(29.89665494,-18.02306186),point(29.0813144,-22.42912167),point(21.82692457,-11.78699043),point(28.23076501,-5.72314695),point(28.76952322,-20.70495758),polyline(33.53032231,-10.41848623),point(27.35803702,-11.27216315),point(36.21453301,-11.95877017),point(32.08470205,-14.94581005),point(27.42996954,-5.77212979),point(24.14112584,-9.06486202),point(35.56746727,-13.14693858),point(22.49089274,-8.54814388),point(29.07050912,-18.75247418),point(25.61635071,-14.16713806),point(32.69985829,-12.78915827),point(34.71438373,-21.67576836),point(34.55470264,-11.8109127),point(28.0529271,-20.35478139),point(22.76856812,-16.49536557),point(29.98133998,-20.86752682),point(34.42886375,-15.83547093),point(22.43016798,-15.47387398),point(29.9260171,-16.36034119),polygon(26.00882598,-7.83239267),point(20.63236798,-11.10590506),point(31.85409705,-7.04093969),point(30.45259732,-16.19373801),point(25.0439359,-8.7545078),point(22.25470329,-8.86987793),point(24.15497303,-19.78770815),point(25.01255644,-17.22250893),point(28.67112166,-5.36706727),point(32.55609818,-6.54254452),point(31.13936179,-7.5360122),point(23.30303007,-22.78386365),point(34.95485269,-14.87267242),point(21.65929995,-21.15256658),point(36.02590928,-19.99660144),point(24.91333411,-22.0219223),point(24.69266701,-13.26497302),polygon(26.3295759,-13.47399641),point(34.13128439,-16.41439066),point(35.82638524,-15.14445202),point(28.78794697,-17.61361779),point(36.05396538,-17.09921126),point(21.87416827,-19.84691773),point(32.81153941,-19.4611532),point(35.00323025,-20.37634391),point(25.79342051,-14.97645175),point(23.50390437,-21.84166898),point(24.9824761,-13.33292603),point(35.18125476,-20.65395164),point(22.43553976,-15.6321335),point(22.29191215,-18.91157294),point(36.26473166,-15.10708129),point(24.40357056,-14.43803586),point(29.56695865,-5.66326424),point(27.68785443,-16.6993488),point(29.43595354,-7.0710525),point(34.43805167,-20.04487468),point(29.96108169,-13.54582457),point(32.21887608,-10.53524272),polyline(33.59281084,-21.19877566),point(22.95679022,-10.81737842),point(28.85572601,-14.92112609),point(20.80926776,-14.53585502),point(25.1860787,-12.06304929),point(21.24525431,-20.75177589),point(20.66415832,-20.11810975),point(32.24580206,-6.26380617),point(34.5146948,-8.37336569),point(32.18181638,-20.7869774),point(21.56357098,-12.89657502),point(30.68774068,-22.43473474),point(27.51728895,-7.09381824),point(25.18933686,-15.63549795),point(21.99501061,-10.29066697),point(34.15713311,-22.47032067),point(24.12733947,-13.52813962),point(29.76178891,-17.74368284),point(27.24680594,-17.8671197),point(29.52049308,-11.78610828),point(24.93460938,-7.46980147),point(25.17058561,-19.22070528),point(30.49343901,-21.75612815),point(30.30278389,-22.30465854),polygon(26.09683427,-17.33391742),polyline(33.91525175,-19.08879015),point(23.66390972,-10.75843506),point(32.97646136,-21.15043573),polygon(26.70419068,-12.0555183),point(22.00371674,-13.9974765),point(25.27577371,-15.57465607),point(35.95453746,-10.50803136),point(34.22799558,-21.11901914),point(30.06238667,-24.91227213),point(30.90332905,-10.21415836),point(23.92789889,-10.31596715),point(23.84372933,-11.63574879),point(31.89588241,-24.48221883),point(29.33482067,-18.5316565),point(23.79878711,-6.98508844),point(21.5478492,-18.35282988),polygon(26.41808437,-25.26825213),polyline(33.94226517,-11.93698706),point(35.23485292,-14.12084997),point(34.7785471,-16.62622217),point(28.44263993,-9.16324112),point(32.59692747,-21.59021462),point(25.43078946,-17.26718454),point(28.70960551,-12.47970924),point(19.9764041,-14.98006847),point(23.56556153,-21.78645227),point(36.33830506,-12.97116116),point(27.63431983,-6.77413349),point(25.0078067,-6.19162313),point(23.11474322,-7.29307645),polyline(33.30644046,-10.83470831),point(27.86554884,-17.45596595),point(31.96339894,-14.905998),point(27.83061126,-15.30715165),point(29.64928808,-11.94781904),point(34.83302522,-20.70499105),point(20.57886088,-14.85435005),point(31.46073647,-6.63077202),point(23.47324027,-21.15521793),point(34.70980696,-16.08979378),point(25.72462294,-16.03566765),point(31.04012644,-19.79965851),point(29.21304651,-21.75089002),point(21.16587173,-12.2682465),point(30.3615155,-15.27757697),point(19.93415374,-16.91445621),point(36.86740104,-17.92382721),point(30.91226254,-25.15839549),point(28.92121321,-22.89326566),point(25.33341384,-7.08283526),point(24.40549487,-16.7309659),point(23.26130777,-12.80491254),point(27.11333088,-7.79009727),point(27.3708997,-21.81913262),polygon(26.98644779,-24.0675816),point(21.40810103,-17.90919049),point(24.90891486,-21.92666319),point(23.33324989,-23.82457591),point(25.54653423,-14.89869613),point(27.03569053,-22.83586489),point(28.34740783,-22.24858475),point(30.35632618,-8.74181642),point(23.50604296,-22.2937543),point(19.99573155,-15.2132654),point(29.54915611,-24.34605583),point(34.27722415,-16.34692993),point(32.03438618,-22.85158177),point(25.84951185,-19.80680009),polygon(26.65315616,-7.42543133),point(29.42538858,-13.43290535),point(20.92191776,-21.24031497),point(32.20523716,-13.63674934),point(21.58630276,-17.59769508),polygon(26.9338101,-23.42697635),point(21.16294876,-18.09000812),point(21.92697685,-12.40140735),point(27.38057893,-12.09623691),point(22.48563415,-13.09388252),point(24.15650847,-23.11546678),point(19.46289333,-17.33192447),point(24.98342956,-7.8344216),point(30.79898389,-19.24444606),polygon(26.87385385,-9.55490858),polygon(26.4054322,-14.22778169),point(35.94624719,-19.13725098),point(31.78547975,-11.93864647),point(20.7870985,-11.31124782),point(23.61407477,-12.0089682),point(25.91053786,-9.68761881),point(28.53575419,-23.62626965),point(29.42798703,-20.10158648),point(21.47156066,-18.55849273),point(28.99703254,-5.51382989),point(21.28449876,-9.30703494),point(24.73712647,-8.03705239),point(28.8458178,-6.57581033),point(24.99716268,-10.77877095),point(28.80430231,-24.56357708),point(24.04883456,-17.62350348),point(24.32584532,-11.38178816),point(34.56546569,-10.23591763),polygon(26.38806693,-8.78603035),polygon(26.74571572,-11.99095469),point(21.73276519,-19.49014315),point(28.74624481,-16.85817661),point(24.7247588,-24.33493002),point(32.83700476,-23.62251919),polygon(26.08428734,-12.51534704),point(25.20374832,-22.58079076),point(20.04102398,-15.26441953),point(31.34630692,-8.03542567),point(36.54635198,-18.91969479),point(27.96457276,-23.68359807),point(30.64735871,-18.82087884),polyline(33.55500482,-20.60597814),point(27.5196692,-13.05728786),point(35.59532949,-16.66194177),point(20.523073,-20.36901827),point(25.02659503,-22.4378132),point(35.50033528,-20.37624706),polygon(26.39936821,-19.52349266),point(24.29395856,-15.50645047),point(32.45457044,-12.42020244),point(32.44452826,-6.85537124),point(29.43669494,-11.90670699),polygon(26.83068637,-24.64858653),point(23.27473323,-8.83894734),point(31.96330502,-17.69883668),point(22.03565557,-18.3881519),point(19.93544795,-18.23610516),point(34.47719779,-9.13474263),polygon(26.68508445,-21.36676349),point(29.40642934,-24.24513315),point(22.5953051,-15.36385181),point(30.74742899,-12.66888191),point(23.70730999,-20.09209574),point(22.10420566,-9.50967032),polygon(26.94951061,-21.20520906),point(23.59932159,-20.43082556),point(27.93549178,-24.79892457),point(21.97883953,-8.99072483),point(20.50430978,-13.49883605),polyline(33.18779452,-11.69570004),point(20.97549576,-9.57231761),point(29.36514688,-17.9970872),point(30.79713025,-6.55806401),point(31.44492436,-10.13252468),point(34.05605609,-17.20580299),point(28.64134865,-15.76446827),polyline(33.00338309,-7.72066889),point(28.3864282,-16.07022716),point(32.64936026,-19.3856356),point(34.45636605,-16.37043627),point(24.73374962,-16.55289264),point(24.77781165,-9.47866847),point(35.37544217,-13.62615986),point(31.80687785,-23.28128376),point(31.51105904,-22.16215617),point(20.40301401,-15.4022332),polyline(33.98759826,-11.71207708),polygon(26.89668059,-16.25456568),point(29.47445627,-10.44835731),polygon(26.99545909,-13.18373562),point(30.35778242,-17.76554218),point(21.73654683,-16.38684095),point(28.66600272,-21.45715223),polygon(26.17111707,-8.00588792),point(28.5529783,-15.1916682),point(30.73092966,-12.13866405),point(28.45317136,-10.28698876),point(21.78849031,-15.3430376),point(30.1465655,-11.55178062),point(25.96321615,-14.58130171),point(32.62301366,-12.75207936),point(29.84555485,-5.39906352),point(21.17837556,-18.96377361),point(29.18175476,-15.68389878),point(31.60054879,-22.96609379),point(23.99036734,-18.50045472),point(28.77184418,-15.85942416),point(32.837132,-14.11313537),point(23.69310115,-20.90571316),point(34.06022842,-19.32208657),point(36.67043063,-13.02308324),point(28.46571733,-15.25157888),point(24.45218646,-17.11896794),point(29.17157297,-5.91766842),point(20.49452587,-11.99841572),point(27.70514985,-5.84408115),point(28.8254049,-14.36411664),point(29.36636337,-8.25424206),point(35.33916443,-11.37113867),point(25.36507326,-13.42763932),point(29.14905707,-15.73109727),point(29.09265158,-21.01599847),point(22.89176659,-18.24634885),point(23.37441369,-10.17300533),point(29.83589848,-24.48372434),point(35.37419425,-15.02713623),point(35.10499324,-19.86836252),polyline(33.56733658,-13.03168416),point(32.74309011,-16.34832336),point(35.69208616,-19.58535501),point(23.70050543,-21.16271573),point(24.71490122,-13.27610141),point(35.64259996,-13.14349384),point(23.44936479,-8.24853322),point(27.01137761,-14.45479741),polyline(33.22024611,-15.02086169),point(23.33252865,-19.21409802),point(29.99732397,-22.56595591),point(28.18902029,-7.13473734),point(35.07495548,-20.65938896),point(34.71245262,-20.34915556),point(28.29548845,-19.39510827),point(24.29243793,-21.72863931),point(31.69531371,-7.13989453),point(27.68485639,-23.09777102),point(27.18827837,-17.4130594),polyline(33.54975685,-12.69545734),polygon(26.91664471,-18.90946642),point(30.72243969,-20.42309572),point(20.79543406,-11.51314289),point(19.91597877,-11.81693028),point(36.4722393,-14.80172951),polyline(33.57954564,-18.00903089),point(31.24169151,-8.27294455),polygon(26.27076712,-13.55423616),point(30.331719,-21.82736014),point(29.20550468,-21.84489127),point(24.77525271,-8.95696976),point(21.18837567,-18.39478792),polygon(26.63436523,-15.93763352),point(30.57721488,-7.78847898),polygon(26.66111458,-21.38227909),point(25.24053784,-13.63205409),point(24.69788777,-20.97666547),point(34.82373633,-22.46610131),polygon(26.79709616,-21.4854982),point(24.21225837,-11.95067922),point(34.90817833,-13.43880621),point(23.92140472,-16.65006525),point(22.45373994,-22.96765793),point(31.34608033,-12.47191826),point(34.85451068,-19.90208854),point(29.13928801,-23.31160276),point(28.27618181,-15.71864437),point(23.0199207,-21.26232744),point(29.2544011,-12.7004885),point(20.66736661,-13.64382428),polyline(33.57956919,-16.87267953),point(21.20480514,-17.47914768),point(30.18343899,-23.06035382),point(25.11362253,-17.20205296),point(22.84216599,-21.54811176),point(27.48242043,-21.85450837),point(20.07945804,-17.21987996),point(28.0816918,-25.30107809),point(19.48890282,-16.23935316),point(29.22289469,-24.48209537),point(31.76672014,-19.48827855),point(27.28184338,-22.01743311),polygon(26.76514344,-20.51637686),point(24.60862657,-16.42399433),point(25.91929349,-8.56977991),point(30.64833378,-14.21876703),point(28.95382709,-7.91804442),polygon(26.57797377,-14.9127972),point(23.60868659,-16.43120379),point(30.60973769,-24.03342294),polyline(33.39133731,-10.20745288),point(27.21637756,-9.29081043),polygon(26.53214767,-17.8318326),point(28.95718037,-13.37608406),point(20.60489261,-17.19113884),point(28.82689191,-10.45502554),point(24.39398714,-8.37973343),point(29.78413042,-23.99666552),point(21.2226811,-11.45162094),point(22.43458654,-13.08235884),point(20.92775931,-20.66291859),point(30.03909019,-14.96773649),point(32.04686982,-8.8382559),point(21.91076878,-11.88197429),point(23.6328072,-10.6414591),point(24.67014685,-19.63908517),point(23.48683919,-20.93721626),point(31.57951766,-19.27868213),point(21.32746746,-12.92865597),polygon(26.25656547,-10.87824937),polygon(26.65437469,-9.43738299),point(21.88364858,-18.79080783),point(22.67955266,-13.58229613),point(30.24810919,-5.44398),point(27.21558127,-18.32789267),point(28.6905598,-15.63572876),point(32.64410325,-20.38859375),point(20.0459419,-14.09178924),point(27.04915638,-13.13922929),point(31.50633529,-9.90722517),point(21.70166528,-17.00209781),point(31.88892437,-21.18434384),polygon(26.74207999,-24.36760067),point(30.20499993,-9.13123096),point(36.30276113,-13.50587409),point(22.00773664,-22.55870328),point(32.13702393,-11.85260832),point(28.22429404,-15.27225178),point(28.44050827,-14.61279254),point(31.2246552,-6.75768124),point(34.04823122,-9.41305859),point(24.94039367,-12.77238565),point(30.11444424,-9.41863445),point(24.00985706,-14.0183172),point(29.68860858,-14.36976153),point(32.51823286,-14.65826899),point(21.67332196,-21.37137272),point(28.93091566,-24.41688717),point(23.68838934,-11.81950344),point(25.49444619,-15.20640676),point(19.58643797,-17.93294104),point(20.07488684,-13.37068372),point(31.23514989,-15.60786348),point(30.19752237,-11.1438555),point(28.56470641,-14.86066186),point(30.44622515,-23.42569717),point(25.19772897,-8.67035038),point(27.74636925,-7.22386443),point(22.67877225,-8.10386409),point(34.11465655,-11.42632056),point(22.70606528,-20.92867096),point(32.60982981,-22.94550033),point(29.88184346,-9.72372221),point(31.68344349,-23.35484882),point(30.83764378,-11.79139592),point(23.6740137,-7.98989968),point(22.92450627,-12.24996075),point(21.85442094,-19.76815419),point(30.71514329,-14.47793359),point(30.25400216,-23.94617539),point(19.93539405,-18.50411458),point(30.03413722,-16.58438543),point(30.10814406,-25.29371214),point(21.25838829,-20.63241151),point(35.20563886,-11.08965386),point(29.69489701,-23.87024349),point(19.89397159,-16.8566618),point(23.66380784,-21.73956389),polygon(26.54200397,-14.94034652),polygon(26.35618103,-9.30239172),point(24.40180983,-10.00970288),point(24.84171353,-16.3279402),point(34.0031748,-11.12598871),polyline(33.70626814,-13.39528335),point(25.78051816,-11.06676966),polyline(33.53283422,-19.41514205),point(31.49176249,-7.42904184),point(29.29180429,-10.2724845),point(22.59064424,-18.36703987),point(31.07060332,-7.5989478),point(28.80722165,-12.03119547),point(35.70429775,-11.01095603),point(20.39905148,-17.12953271),point(27.7873156,-9.32516442),point(29.0208355,-13.76924766),point(29.10240732,-11.2510271),point(34.49285852,-13.75574435),point(36.76002423,-17.43640783),point(30.92684926,-11.00049725),point(35.25530534,-15.2526578),point(36.93115135,-16.50572316),point(23.67576314,-16.35071949),point(19.32335395,-16.12545978),polygon(26.97757799,-9.51258544),point(28.93545328,-8.30007905),point(23.00963199,-22.70072924),point(32.61395407,-10.27705755),point(34.08459125,-12.04229921),point(24.02640309,-23.23844379),polyline(33.67738745,-17.61644574),point(25.2723838,-13.2338553),point(25.24228844,-22.33965374),point(22.14259013,-12.21382588),point(23.37072695,-10.22438511),point(23.58947073,-14.26619534),point(22.40456354,-22.16146454),point(30.71494934,-12.98922128),polygon(26.31095209,-15.61680071),point(22.03915175,-17.585741),point(21.61484786,-11.62356612),point(31.13001224,-14.89297982),point(29.93744208,-21.07286026),point(22.01796071,-22.36219782),point(31.57228369,-15.9343209),point(35.98087143,-18.21722291),point(27.03212567,-13.78630417),point(23.55451347,-13.15239602),point(23.75302144,-13.05517835),point(31.53013268,-8.70230512),point(24.60250474,-16.3803821),point(34.04686943,-20.57741823),point(29.95694891,-16.50893731),point(35.033891,-14.31961988),point(29.74665976,-7.99866778),point(35.28691118,-19.29847739),polyline(33.93080311,-14.9803036),point(21.78327976,-12.76808508),point(29.92179026,-9.96640354),point(30.76692446,-6.72708487),point(21.77632166,-9.48186831),point(27.47824858,-8.2670365),polygon(26.95322275,-9.62021746),point(31.63021681,-8.56869028),point(27.27374739,-21.05669476),point(21.2351458,-14.52008546),point(23.70985018,-8.82152247),point(29.1674654,-22.81027281),point(24.42905044,-13.85941941),point(31.10630996,-20.82987345),point(31.22778805,-5.96246322),point(31.10017218,-8.74970239),point(19.96489237,-14.62515843),point(27.52593452,-16.93222167),point(27.64648411,-16.3869938),point(34.58349527,-10.34768064),point(21.1516471,-14.63070848),point(23.74566577,-12.05861448),point(36.78891621,-13.02375831),point(22.37938981,-12.17504848),point(29.57736468,-17.61850206),point(28.48012716,-6.72711062),point(22.50365704,-14.12276637),point(31.48242103,-7.47343721),polyline(33.13862809,-13.53193432),point(32.41578151,-15.27247905) };

	private static Point point(double latitude, double longitude) {
		return new Point(latitude, longitude);
	}

	private static Polygon polygon(double latitude, double longitude) {
		Polygon result = new Polygon();
		int randomValue = nextRandomInt(1);
		result.add(point(latitude, longitude));
		result.add(point(latitude - randomValue, longitude + randomValue));
		result.add(point(latitude + randomValue, longitude + randomValue + randomValue));
		result.add(point(latitude, longitude));
		return result;
	}

	private static Polyline polyline(double latitude, double longitude) {
		Polyline result = new Polyline();
		for (int i = 0; i< nextRandomInt(2); i++) result.add(point(latitude + (0.3 * i), longitude + (0.3 * i)));
		return result;
	}

	private static int nextRandomInt(int min) {
		Random random = new Random();
		int randomValue = random.nextInt(5);
		while (randomValue < min) randomValue = random.nextInt(5);
		return randomValue;
	}
}
