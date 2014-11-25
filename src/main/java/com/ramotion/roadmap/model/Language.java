package com.ramotion.roadmap.model;

import java.util.HashMap;

/**
 * Created by Oleg Vasiliev on 20.11.2014.
 * Enum with all ISO639-1 language codes;
 */
public enum Language {

    ab("Abkhazian"),
    aa("Afar"),
    af("Afrikaans"),
    sq("Albanian"),
    am("Amharic"),
    ar("Arabic"),
    hy("Armenian"),
    as("Assamese"),
    ay("Aymara"),
    az("Azerbaijani"),
    ba("Bashkir"),
    eu("Basque"),
    bn("Bengali (Bangla)"),
    dz("Bhutani"),
    bh("Bihari"),
    bi("Bislama"),
    br("Breton"),
    bg("Bulgarian"),
    my("Burmese"),
    be("Belarusian"),
    km("Cambodian"),
    ca("Catalan"),
    zh("Chinese"),
    co("Corsican"),
    hr("Croatian"),
    cs("Czech"),
    da("Danish"),
    nl("Dutch"),
    en("English"),
    eo("Esperanto"),
    et("Estonian"),
    fo("Faeroese"),
    fa("Farsi"),
    fj("Fiji"),
    fi("Finnish"),
    fr("French"),
    fy("Frisian"),
    gl("Galician"),
    gd("Gaelic (Scottish)"),
    gv("Gaelic (Manx)"),
    ka("Georgian"),
    de("German"),
    el("Greek"),
    kl("Greenlandic"),
    gn("Guarani"),
    gu("Gujarati"),
    ha("Hausa"),
    he("Hebrew"),
    hi("Hindi"),
    hu("Hungarian"),
    is("Icelandic"),
    id("Indonesian"),
    ia("Interlingua"),
    ie("Interlingue"),
    iu("Inuktitut"),
    ik("Inupiak"),
    ga("Irish"),
    it("Italian"),
    ja("Javanese"),
    kn("Kannada"),
    ks("Kashmiri"),
    kk("Kazakh"),
    rw("Kinyarwanda (Ruanda)"),
    ky("Kirghiz"),
    rn("Kirundi (Rundi)"),
    ko("Korean"),
    ku("Kurdish"),
    lo("Laothian"),
    la("Latin"),
    lv("Latvian (Lettish)"),
    li("Limburgish (Limburger)"),
    ln("Lingala"),
    lt("Lithuanian"),
    mk("Macedonian"),
    mg("Malagasy"),
    ms("Malay"),
    ml("Malayalam"),
    mt("Maltese"),
    mi("Maori"),
    mr("Marathi"),
    mo("Moldavian"),
    mn("Mongolian"),
    na("Nauru"),
    ne("Nepali"),
    no("Norwegian"),
    oc("Occitan"),
    or("Oriya"),
    om("Oromo (Afan,Galla)"),
    ps("Pashto (Pushto)"),
    pl("Polish"),
    pt("Portuguese"),
    pa("Punjabi"),
    qu("Quechua"),
    rm("Rhaeto-Romance"),
    ro("Romanian"),
    ru("Russian"),
    sm("Samoan"),
    sg("Sangro"),
    sa("Sanskrit"),
    sr("Serbian"),
    sh("Serbo-Croatian"),
    st("Sesotho"),
    tn("Setswana"),
    sn("Shona"),
    sd("Sindhi"),
    si("Sinhalese"),
    ss("Siswati"),
    sk("Slovak"),
    sl("Slovenian"),
    so("Somali"),
    es("Spanish"),
    su("Sundanese"),
    sw("Swahili (Kiswahili)"),
    sv("Swedish"),
    tl("Tagalog"),
    tg("Tajik"),
    ta("Tamil"),
    tt("Tatar"),
    te("Telugu"),
    th("Thai"),
    bo("Tibetan"),
    ti("Tigrinya"),
    to("Tonga"),
    ts("Tsonga"),
    tr("Turkish"),
    tk("Turkmen"),
    tw("Twi"),
    ug("Uighur"),
    uk("Ukrainian"),
    ur("Urdu"),
    uz("Uzbek"),
    vi("Vietnamese"),
    vo("Volapuk"),
    cy("Welsh"),
    wo("Wolof"),
    xh("Xhosa"),
    yi("Yiddish"),
    yo("Yoruba"),
    zu("Zulu");

    private final String name;

    Language(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static HashMap<String, String> getCodesMap() {
        HashMap<String, String> resMap = new HashMap<>(Language.values().length);
        for (Language lang : Language.values()) {
            resMap.put(lang.toString(), lang.getName());
        }
        return resMap;
    }

    public static Language valueOfOrNullIgnoreCase(String code) {
        for (Language lang : Language.values()) {
            if (lang.toString().equals(code.toLowerCase()))
                return lang;
        }
        return null;
    }
}
