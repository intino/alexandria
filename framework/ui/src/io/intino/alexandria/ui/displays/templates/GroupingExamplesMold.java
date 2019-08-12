package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.items.Grouping1Mold;
import io.intino.alexandria.ui.displays.items.Grouping2Mold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GroupingExamplesMold extends AbstractGroupingExamplesMold<AlexandriaUiBox> {

    public GroupingExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        list1.source(Datasources.personDatasource());
        list1.onAddItem(this::onAddItem);
        list2.source(Datasources.personDatasource());
        list2.onAddItem(this::onAddItem);
        country1.groups(countries());
        country2.groups(countries());
    }

    private void onAddItem(AddItemEvent event) {
        if (event.component() instanceof Grouping1Mold) {
            Grouping1Mold component = event.component();
            Person person = event.item();
            component.firstName1.value(person.firstName());
            component.gender1.value(person.gender().name());
            component.age1.value(String.valueOf(person.age()));
        }
        else if (event.component() instanceof Grouping2Mold) {
            Grouping2Mold component = event.component();
            Person person = event.item();
            component.firstName2.value(person.firstName());
            component.gender2.value(person.gender().name());
            component.age2.value(String.valueOf(person.age()));
        }
    }

    private List<Group> countries() {
        return Arrays.asList(group("Afghanistan"),group("Albania"),group("Algeria"),group("Andorra"),group("Angola"),group("Antigua and Barbuda"),group("Argentina"),group("Armenia"),group("Australia"),group("Austria"),group("Azerbaijan"),group("Bahamas"),group("Bahrain"),group("Bangladesh"),group("Barbados"),group("Belarus"),group("Belgium"),group("Belize"),group("Benin"),group("Bhutan"),group("Bolivia"),group("Bosnia and Herzegovina"),group("Botswana"),group("Brazil"),group("Brunei"),group("Bulgaria"),group("Burkina Faso"),group("Burundi"),group("Côte d'Ivoire"),group("Cabo Verde"),group("Cambodia"),group("Cameroon"),group("Canada"),group("Central African Republic"),group("Chad"),group("Chile"),group("China"),group("Colombia"),group("Comoros"),group("Congo (Congo-Brazzaville)"),group("Costa Rica"),group("Croatia"),group("Cuba"),group("Cyprus"),group("Czechia"),group("Democratic Republic of the Congo"),group("Denmark"),group("Djibouti"),group("Dominica"),group("Dominican Republic"),group("Ecuador"),group("Egypt"),group("El Salvador"),group("Equatorial Guinea"),group("Eritrea"),group("Estonia"),group("Ethiopia"),group("Fiji"),group("Finland"),group("France"),group("Gabon"),group("Gambia"),group("Georgia"),group("Germany"),group("Ghana"),group("Greece"),group("Grenada"),group("Guatemala"),group("Guinea"),group("Guinea-Bissau"),group("Guyana"),group("Haiti"),group("Holy See"),group("Honduras"),group("Hungary"),group("Iceland"),group("India"),group("Indonesia"),group("Iran"),group("Iraq"),group("Ireland"),group("Israel"),group("Italy"),group("Jamaica"),group("Japan"),group("Jordan"),group("Kazakhstan"),group("Kenya"),group("Kiribati"),group("Kuwait"),group("Kyrgyzstan"),group("Laos"),group("Latvia"),group("Lebanon"),group("Lesotho"),group("Liberia"),group("Libya"),group("Liechtenstein"),group("Lithuania"),group("Luxembourg"),group("Madagascar"),group("Malawi"),group("Malaysia"),group("Maldives"),group("Mali"),group("Malta"),group("Marshall Islands"),group("Mauritania"),group("Mauritius"),group("Mexico"),group("Micronesia"),group("Moldova"),group("Monaco"),group("Mongolia"),group("Montenegro"),group("Morocco"),group("Mozambique"),group("Myanmar (formerly Burma)"),group("Namibia"),group("Nauru"),group("Nepal"),group("Netherlands"),group("New Zealand"),group("Nicaragua"),group("Niger"),group("Nigeria"),group("North Korea"),group("North Macedonia"),group("Norway"),group("Oman"),group("Pakistan"),group("Palau"),group("Palestine State"),group("Panama"),group("Papua New Guinea"),group("Paraguay"),group("Peru"),group("Philippines"),group("Poland"),group("Portugal"),group("Qatar"),group("Romania"),group("Russia"),group("Rwanda"),group("Saint Kitts and Nevis"),group("Saint Lucia"),group("Saint Vincent and the Grenadines"),group("Samoa"),group("San Marino"),group("Sao Tome and Principe"),group("Saudi Arabia"),group("Senegal"),group("Serbia"),group("Seychelles"),group("Sierra Leone"),group("Singapore"),group("Slovakia"),group("Slovenia"),group("Solomon Islands"),group("Somalia"),group("South Africa"),group("South Korea"),group("South Sudan"),group("Spain"),group("Sri Lanka"),group("Sudan"),group("Suriname"),group("Swaziland"),group("Sweden"),group("Switzerland"),group("Syria"),group("Tajikistan"),group("Tanzania"),group("Thailand"),group("Timor-Leste"),group("Togo"),group("Tonga"),group("Trinidad and Tobago"),group("Tunisia"),group("Turkey"),group("Turkmenistan"),group("Tuvalu"),group("Uganda"),group("Ukraine"),group("United Arab Emirates"),group("United Kingdom"),group("United States of America"),group("Uruguay"),group("Uzbekistan"),group("Vanuatu"),group("Venezuela"),group("Vietnam"),group("Yemen"),group("Zambia"),group("Zimbabwe"));
    }

    private Group group(String label) {
        Random random = new Random();
        return new Group().label(label).count(random.nextInt(1000));
    }
}