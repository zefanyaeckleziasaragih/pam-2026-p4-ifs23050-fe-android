package org.delcom.pam_p4_ifs23050.helper

class ConstHelper {
    enum class RouteNames(val path: String) {
        // ── Existing ────────────────────────────────────────
        Home(path    = "home"),
        Profile(path = "profile"),

        Plants(path      = "plants"),
        PlantsAdd(path   = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path  = "plants/{plantId}/edit"),

        // ── Zodiac (Rasi Bintang) ────────────────────────────
        Zodiac(path       = "zodiac"),
        ZodiacAdd(path    = "zodiac/add"),
        ZodiacDetail(path = "zodiac/{zodiacId}"),
        ZodiacEdit(path   = "zodiac/{zodiacId}/edit"),
    }
}