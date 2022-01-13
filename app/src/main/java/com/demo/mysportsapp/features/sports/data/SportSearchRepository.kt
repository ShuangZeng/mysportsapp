package com.demo.mysportsapp.features.sports.data

import Event
import com.demo.mysportsapp.common.ui.HttpClient
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import retrofit2.http.GET


import okhttp3.ResponseBody
import retrofit2.http.Query


import Player

import Team
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SportSearchRepository {
    companion object {
        const val TEAM_SEARCH = "searchteams.php"
        const val PLAYER_DETAILS = "lookup_all_players.php"
        const val EVENT_DETAILS = "lookupevent.php"

    }


    fun getTeams(searchQuery: String): Observable<List<Team>> {

        return HttpClient.instance!!.create(SportServiceEndpoint::class.java)
            .getTeamListFromServer(searchQuery).flatMap {
                val teamApiResponse: TeamAPIResponse = GsonBuilder().create()
                    .fromJson(
                        it.string(),
                        TypeToken.getParameterized(TeamAPIResponse::class.java).type
                    )
                if (teamApiResponse.teamsList == null)
                    teamApiResponse.teamsList= mutableListOf()
                    Observable.just(teamApiResponse.teamsList)
            }
    }

    fun getPlayerList(teamId: String): Observable<List<Player>> {
        return HttpClient.instance!!.create(SportServiceEndpoint::class.java)
            .getPlayerDetailsFromServer(teamId).flatMap {
                val playerApiResponse: PlayerAPIResponse = GsonBuilder().create()
                    .fromJson(
                        it.string(),
                        TypeToken.getParameterized(PlayerAPIResponse::class.java).type
                    )
                if (playerApiResponse.playerList == null)
                    playerApiResponse.playerList = mutableListOf()
                Observable.just(playerApiResponse.playerList)
            }
    }

    fun getEventList(teamId: String): Observable<List<Event>> {
        return HttpClient.instance!!.create(SportServiceEndpoint::class.java)
            .getEventDetailsFromServer(teamId).flatMap {
                val eventAPIResponse: EventAPIResponse = GsonBuilder().create()
                    .fromJson(
                        it.string(),
                        TypeToken.getParameterized(EventAPIResponse::class.java).type
                    )
                if (eventAPIResponse.eventList == null)
                    eventAPIResponse.eventList = mutableListOf()

                Observable.just(eventAPIResponse.eventList)
            }
    }

    internal interface SportServiceEndpoint {

        @GET(SportSearchRepository.TEAM_SEARCH)
        fun getTeamListFromServer(@Query("t") searchQueryParam: String): Observable<ResponseBody>

        @GET(SportSearchRepository.PLAYER_DETAILS)
        fun getPlayerDetailsFromServer(@Query("id") searchQueryParam: String): Observable<ResponseBody>

        @GET(SportSearchRepository.EVENT_DETAILS)
        fun getEventDetailsFromServer(@Query("id") searchQueryParam: String): Observable<ResponseBody>

    }
}

class TeamAPIResponse {
    @SerializedName("teams")
    @Expose
    var teamsList: MutableList<Team>? = null
}

class PlayerAPIResponse {
    @SerializedName("player")
    @Expose
    var playerList: MutableList<Player>? = null
}

class EventAPIResponse {
    @SerializedName("events")
    @Expose
    var eventList: MutableList<Event>? = null
}