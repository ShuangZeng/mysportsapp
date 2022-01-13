package com.demo.mysportsapp.features.sports.vm

data class TeamDetailViewDTO(val clubDetail: ClubDetailViewDTO, val teamDetail:List<TeamMemberDetailViewDTO>, val eventDetail:List<EventDetailViewDTO>)