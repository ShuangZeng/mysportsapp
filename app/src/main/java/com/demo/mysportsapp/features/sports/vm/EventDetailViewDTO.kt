package com.demo.mysportsapp.features.sports.vm

data class EventDetailViewDTO(
    val eventName:String,
    val eventDate:String,
    val eventVenue:String,
    val eventLeague:String,
    val homeTeam:String,
    val awayTeam:String,
    val homeAwayScore:String,
) {
    fun getFormattedHomeAwayScore(): String? {
        return homeAwayScore
    }
}