package com.demo.mysportsapp.features.sports.vm

import Event
import Player
import Team
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.mysportsapp.features.sports.data.SportSearchRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

interface TeamSearchViewModelViewContract
{
        fun showPopUpMessage(message:String)
        fun showProgressBar()
        fun hideProgressBar()
    fun showDetailScreen()
}
class TeamSearchViewModel(
    private val view: TeamSearchViewModelViewContract,
    private val sportSearchRepository: SportSearchRepository
) : ViewModel() {

    val useCaseSubs: CompositeDisposable = CompositeDisposable()
    val teamDetailStateLiveData: MutableLiveData<TeamDetailViewDTO> = MutableLiveData()

    /**
     * UI observes the state exposed by View Model
     */
    fun bind(): LiveData<TeamDetailViewDTO> {
        return teamDetailStateLiveData;
    }

    fun onSearchQueryReceived(searchQuery: String) {
        useCaseSubs.add(
            sportSearchRepository.getTeams(searchQuery)
                .doOnSubscribe { view.showProgressBar()}
                .subscribeOn(Schedulers.io())
                .flatMap {
                    val nearestMatchedTeam: Team = it.get(0)
                    Observable.zip(
                        sportSearchRepository.getPlayerList(nearestMatchedTeam.idTeam),
                        sportSearchRepository.getEventList(nearestMatchedTeam.idTeam),
                        Observable.just(nearestMatchedTeam),
                        { c, d, t -> createTeamDetail(c, d, t) })
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view.hideProgressBar() }
                .subscribeWith(object : DisposableObserver<TeamDetailViewDTO>() {
                    override fun onNext(t: TeamDetailViewDTO) {
                        view.showDetailScreen()
                        teamDetailStateLiveData.value=t
                    }

                    override fun onError(e: Throwable) {
                       view.showPopUpMessage(e.message.toString())
                    }

                    override fun onComplete() {
                    }
                })


        )
    }

    private fun createTeamDetail(
        playerList: List<Player>,
        eventList: List<Event>,
        team: Team
    ): TeamDetailViewDTO {

        val clubDetail: ClubDetailViewDTO= ClubDetailViewDTO(
            team.strTeam,
            team.strStadium,
            team.strStadiumDescription,
            team.strStadiumThumb,
            team.strStadiumLocation,
            team.strLeague
        )

        var playerListDTO: MutableList<TeamMemberDetailViewDTO> = mutableListOf()
        playerList?.forEach {
            playerListDTO.add(
                TeamMemberDetailViewDTO(
                    it.strPlayer,
                    it.strNationality,
                    it.dateBorn,
                    it.strDescriptionEN,
                    it.strPosition
                )
            )
        }

        var eventListDTO:MutableList<EventDetailViewDTO> = mutableListOf()
        eventList?.forEach {
            eventListDTO.add(
                EventDetailViewDTO(
                it.strEvent,
                it.dateEvent,
                it.strVenue,
                    it.strLeague,
                    it.strHomeTeam,
                    it.strAwayTeam,
                    it.intHomeScore+" "+it.intAwayScore
                    )
            )
        }

        return TeamDetailViewDTO(clubDetail,playerListDTO,eventListDTO)
    }


}

