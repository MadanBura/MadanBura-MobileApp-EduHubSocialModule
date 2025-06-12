package com.connect.eduhubconnect.dto

data class UserProfileDTO(
    val addressDTO: AddressDTO,
    val bannerImageUrl: String,
    val bio: String,
    val collageName: String,
    val educationList: List<EducationDTO>,
    val experiences: List<ExperienceDTO>,
    val profileImageUrl: String,
    val about:String,
    val selfPosts: List<SelfPostDTO>,
    val skills: List<SkillDTO>,
    val userId: Int,
    val userName: String
)