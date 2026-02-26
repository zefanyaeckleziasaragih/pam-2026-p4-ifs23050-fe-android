package org.delcom.pam_p4_ifs23051.helper

class ConstHelper {
    enum class RouteNames(val path: String) {
        // ── Existing ────────────────────────────────────────
        Home(path    = "home"),
        Profile(path = "profile"),

        Plants(path      = "plants"),
        PlantsAdd(path   = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path  = "plants/{plantId}/edit"),

        // ── Bahasa Bunga (Flower Language) ──────────────────
        FlowerLanguage(path       = "flower-language"),
        FlowerLanguageAdd(path    = "flower-language/add"),
        FlowerLanguageDetail(path = "flower-language/{flowerId}"),
        FlowerLanguageEdit(path   = "flower-language/{flowerId}/edit"),
    }
}