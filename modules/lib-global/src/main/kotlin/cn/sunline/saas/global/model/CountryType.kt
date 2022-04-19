package cn.sunline.saas.global.model

/**
 * @title: Country
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/21 11:43
 */
enum class CountryType(
    val countryCode: String,
    val countryName: String,
    val numberCode: String,
    val currencyCode: String,
    val mobileArea: String,
    val datetimeZone: String
) {
    CHN("CHN", "China", "156", "CNY", "86", "Asia/Shanghai"),
    HKG("HKG", "Hong Kong", "344", "HKD", "852", "Asia/Hong_Kong"),
    MAC("MAC", "Macao", "446", "MOP", "853", "Asia/Macau"),
    TWN("TWN", "Taiwan", "158", "TWD", "886", "Asia/Taipei"),
    SGP("SGP", "Singapore", "702", "SGD", "65", "Asia/Singapore"),
    THA("THA", "Thailand", "764", "THB", "66", "Asia/Bangkok"),
    IND("IND", "Indian", "356", "INR", "91", "Asia/Calcutta"),
    JPN("JPN", "Japan", "392", "JPY", "81", "Asia/Tokyo"),
    KOR("KOR", "South Korea", "410", "KRW", "82", "Asia/Seoul"),
    PAK("PAK", "Pakistan", "586", "PKR", "92", "Asia/Karachi"),
    USA("USA", "United States of America (USA)", "840", "USD", "1", "America/Los_Angeles"),
    GBR("GBR", "Great Britain (United Kingdom; England)", "826", "GBP", "44", "Europe/London"),
    FRA("FRA", "France", "250", "GBP", "33", "Europe/Paris"),
    DEU("DEU", "Germany", "276", "EUR", "49", "Europe/Berlin"),
    IDN("IDN", "Indonesia", "360", "IDR", "62", "Asia/Jakarta"),
    MYS("MYS", "Malaysia", "458", "MYR", "60", "Asia/Kuala_Lumpur"),

}