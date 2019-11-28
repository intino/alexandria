package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Rule;

public enum Language implements Rule<Enum> {
	Afar("aa"), Abkhaz("ab"), Avestan("ae"), Afrikaans("af"), Akan("ak"), Amharic("am"), Aragonese("an"),
	Arabic("ar"), Assamese("as"), Avaric("av"), Aymara("ay"), Azerbaijani("az"), SouthAzerbaijani("az"),
	Bashkir("ba"), Belarusian("be"), Bulgarian("bg"), Bihari("bh"), Bislama("bi"), Bambara("bm"), Bengali("bn"),
	TibetanStandard("bo"), Breton("br"), Bosnian("bs"), CatalanValencian("ca"), Chechen("ce"), Chamorro("ch"),
	Corsican("co"), Cree("cr"), Czech("cs"), OldChurchSlavonic("cu"), Chuvash("cv"), Welsh("cy"), Danish("da"),
	German("de"), Divehi("dv"), Dzongkha("dz"), Ewe("ee"), GreekModern("el"), English("en"), Esperanto("eo"),
	Spanish("es"), Estonian("et"), Basque("eu"), Persian("fa"), FulaFulah("ff"), Finnish("fi"),
	Fijian("fj"), Faroese("fo"), French("fr"), WesternFrisian("fy"), Irish("ga"), ScottishGaelic("gd"),
	Galician("gl"), Guaraní("gn"), Gujarati("gu"), Manx("gv"), Hausa("ha"), Hebrew("he"), Hindi("hi"),
	HiriMotu("ho"), Croatian("hr"), Haitian("ht"), Hungarian("hu"), Armenian("hy"), Herero("hz"), Interlingua("ia"),
	Indonesian("id"), Interlingue("ie"), Igbo("ig"), Nuosu("ii"), Inupiaq("ik"), Ido("io"), Icelandic("is"),
	Italian("it"), Inuktitut("iu"), Japanese("ja"), Javanese("jv"), Georgian("ka"), Kongo("kg"), KikuyuGikuyu("ki"),
	Kwanyama("kj"), Kazakh("kk"), KalaallisutGreenlandic("kl"), Khmer("km"), Kannada("kn"), Korean("ko"), Kanuri("kr"),
	Kashmiri("ks"), Kurdish("ku"), Komi("kv"), Cornish("kw"), Kyrgyz("ky"), Latin("la"), Luxembourgish("lb"),
	Ganda("lg"), Limburgish("li"), Lingala("ln"), Lao("lo"), Lithuanian("lt"), LubaKatanga("lu"), Latvian("lv"),
	Malagasy("mg"), Marshallese("mh"), Māori("mi"), Macedonian("mk"), Malayalam("ml"), Mongolian("mn"), Marathi("mr"),
	Malay("ms"), Maltese("mt"), Mexican("mx"), Burmese("my"), Nauru("na"), NorwegianBokmal("nb"), NorthNdebele("nd"), Nepali("ne"),
	Ndonga("ng"), Dutch("nl"), NorwegianNynorsk("nn"), Norwegian("no"), SouthNdebele("nr"), Navajo("nv"), Chichewa("ny"),
	Occitan("oc"), Ojibwe("oj"), Oromo("om"), Oriya("or"), Ossetian("os"), Panjabi("pa"), Pali("pi"), Polish("pl"),
	Pashto("ps"), Portuguese("pt"), Quechua("qu"), Romansh("rm"), Kirundi("rn"), Romanian("ro"), Russian("ru"),
	Kinyarwanda("rw"), Sanskrit("sa"), Sardinian("sc"), Sindhi("sd"), NorthernSami("se"), Sango("sg"), Sinhala("si"),
	Slovak("sk"), Slovene("sl"), Samoan("sm"), Shona("sn"), Somali("so"), Albanian("sq"), Serbian("sr"), Swati("ss"),
	SouthernSSotho("st"), Sundanese("su"), Swedish("sv"), Swahili("sw"), Tamil("ta"), Telugu("te"), Tajik("tg"),
	Thai("th"), Tigrinya("ti"), Turkmen("tk"), Tagalog("tl"), Tswana("tn"), Tonga("to"), Turkish("tr"), Tsonga("ts"),
	Tatar("tt"), Twi("tw"), Tahitian("ty"), Uyghur("ug"), Ukrainian("uk"), Urdu("ur"), Uzbek("uz"), Venda("ve"),
	Vietnamese("vi"), Volapük("vo"), Walloon("wa"), Wolof("wo"), Xhosa("xh"), Yiddish("yi"), Yoruba("yo"),
	ZhuangChuang("za"), Chinese("zh"), Zulu("zu");

	private String code;

	Language(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

	@Override
	public boolean accept(Enum value) {
		return value instanceof Language;
	}
}
