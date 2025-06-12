package com.connect.eduhubconnect.model

data class UserProfile(
    val addressDTO: Address,
    val bannerImageUrl: String,
    val bio: String,
    val collageName: String,
    val educationList: List<Education>,
    val experiences: List<Experience>,
    val profileImageUrl: String,
    val about:String,
    val selfPosts: List<SelfPost>,
    val skills: List<Skill>,
    val userId: Int,
    val userName: String
)