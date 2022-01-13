package com.demo.mysportsapp.features.sports.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.demo.mysportsapp.R
import com.demo.mysportsapp.common.ui.TitleDetailView
import androidx.recyclerview.widget.DividerItemDecoration
import com.demo.mysportsapp.features.sports.vm.EventDetailViewDTO
import com.demo.mysportsapp.features.sports.vm.TeamMemberDetailViewDTO
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.demo.mysportsapp.features.sports.vm.TeamSearchViewModel


class TeamDetailFragment : Fragment() {

    private lateinit var viewModel: TeamSearchViewModel
    private lateinit var clubNameTextView:TextView
    private lateinit var stadiumNameTextView:TitleDetailView
    private lateinit var leagueNameTextView: TitleDetailView
    private lateinit var stadiumDescTextView: TitleDetailView
    private lateinit var stadiumLocationTextView: TitleDetailView
    private lateinit var expandedImage: ImageView

    private lateinit var eventRvAdapter: EventDetailRvAdapter
    private lateinit var teamRvAdapter: TeamMemDetailRvAdapter
    private lateinit var teamDetailRv: RecyclerView
    private lateinit var eventDetailRv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.team_detail_framgnet, container, false)
        clubNameTextView = v.findViewById(R.id.clubNameTextView)
        stadiumNameTextView = v.findViewById(R.id.stadiumNameTxtView)
        stadiumDescTextView = v.findViewById(R.id.stadiumDescriptionTxtView)
        leagueNameTextView = v.findViewById(R.id.leagueNameTxtView)
        stadiumLocationTextView = v.findViewById(R.id.stadiumLocTxtView)
        expandedImage = v.findViewById(R.id.expandedImage)

        teamDetailRv = v.findViewById(R.id.teamDetailRv)
        eventDetailRv = v.findViewById(R.id.eventDetailRv)
        if(savedInstanceState==null)
        performDefaultAction()
        return v
    }

    private fun performDefaultAction() {
        eventRvAdapter=EventDetailRvAdapter()
        teamRvAdapter= TeamMemDetailRvAdapter()
        teamDetailRv.adapter = teamRvAdapter
        eventDetailRv.adapter = eventRvAdapter


        val dividerItemDecoration = DividerItemDecoration(
            teamDetailRv.context,
            LinearLayout.VERTICAL
        )
        teamDetailRv.addItemDecoration(dividerItemDecoration)
        eventDetailRv.addItemDecoration(dividerItemDecoration)

        viewModel = ViewModelProvider(requireActivity()).get(TeamSearchViewModel::class.java)

        viewModel.bind().observe(viewLifecycleOwner){
            clubNameTextView.text=it.clubDetail.clubName
            stadiumDescTextView.setDetail(it.clubDetail.stadiumDesc)
            stadiumNameTextView.setDetail(it.clubDetail.stadiumName)
            leagueNameTextView.setDetail(it.clubDetail.leagueName)
            stadiumLocationTextView.setDetail(it.clubDetail.stadiumLocation)

                    eventRvAdapter.setData(it.eventDetail)
        teamRvAdapter.setData(it.teamDetail)

            Glide.with(this)
                .load(it.clubDetail.stadiumImg) // image url
                .placeholder(R.drawable.all_team_logos) // any placeholder to load at start
                .error(R.drawable.all_team_logos)  // any image in case of error
                .centerCrop()
                .into(expandedImage);

        }


    }




}

class TeamMemDetailRvAdapter : RecyclerView.Adapter<TeamDetailRvVH>() {
    var itemList: List<TeamMemberDetailViewDTO> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<TeamMemberDetailViewDTO>) {
        itemList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamDetailRvVH {
        return TeamDetailRvVH(
            LayoutInflater.from(parent.context).inflate(R.layout.team_deail_rv_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TeamDetailRvVH, position: Int) {
       val playerInfo: TeamMemberDetailViewDTO =itemList.get(position)
        holder.playerNameTextView.setDetail(playerInfo.playerName)
        holder.playerNationality.setDetail(playerInfo.playerNationality)
        holder.playerDob.setDetail(playerInfo.playerDob)
        holder.playerDetail.setDetail(playerInfo.playerDetail)
        holder.playerPos.setDetail(playerInfo.playerPosition)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}

class TeamDetailRvVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val playerNameTextView: TitleDetailView = itemView.findViewById(R.id.playerNameTextView)
    val playerNationality: TitleDetailView = itemView.findViewById(R.id.playerNationalityTextView)
    val playerDob: TitleDetailView = itemView.findViewById(R.id.playerDobTextView)
    val playerDetail: TitleDetailView = itemView.findViewById(R.id.playerDescription)
    val playerPos: TitleDetailView = itemView.findViewById(R.id.playerPosition)


}

class EventDetailRvAdapter : RecyclerView.Adapter<EventDetailRvVH>() {
    var itemList: List<EventDetailViewDTO> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<EventDetailViewDTO>) {
        itemList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailRvVH {
        return EventDetailRvVH(
            LayoutInflater.from(parent.context).inflate(R.layout.event_deail_rv_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventDetailRvVH, position: Int) {
        val eventInfo: EventDetailViewDTO =itemList.get(position)
        holder.eventName.text=eventInfo.eventName
        holder.eventDate.setDetail(eventInfo.eventDate)
        holder.eventLeague.setDetail(eventInfo.eventLeague)
        holder.eventVenue.setDetail(eventInfo.eventVenue)
        holder.homeTeam.setDetail(eventInfo.homeTeam)
        holder.awayTeam.setDetail(eventInfo.awayTeam)
        holder.homeAwayScore.setDetail(eventInfo.getFormattedHomeAwayScore())
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}

class EventDetailRvVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val eventName: TextView = itemView.findViewById(R.id.eventName)
    val eventDate: TitleDetailView = itemView.findViewById(R.id.eventDate)
    val eventVenue: TitleDetailView = itemView.findViewById(R.id.eventVenue)
    val eventLeague: TitleDetailView = itemView.findViewById(R.id.eventLeague)
    val homeTeam: TitleDetailView = itemView.findViewById(R.id.homeTeam)
    val awayTeam: TitleDetailView = itemView.findViewById(R.id.awayTeam)
    val homeAwayScore: TitleDetailView = itemView.findViewById(R.id.homeAwayScore)



}


