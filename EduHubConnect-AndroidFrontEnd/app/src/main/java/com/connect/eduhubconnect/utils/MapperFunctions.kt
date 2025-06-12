package com.connect.eduhubconnect.utils

import com.connect.eduhubconnect.dto.AddressDTO
import com.connect.eduhubconnect.dto.CommentDTO
import com.connect.eduhubconnect.dto.FriendRequestDTO
import com.connect.eduhubconnect.dto.PostDTO
import com.connect.eduhubconnect.dto.RequestStatus
import com.connect.eduhubconnect.dto.UserDTO
import com.connect.eduhubconnect.model.Comment
import com.connect.eduhubconnect.model.FriendRequest
import com.connect.eduhubconnect.model.Post
import com.connect.eduhubconnect.model.User
import com.connect.eduhubconnect.dto.EducationDTO
import com.connect.eduhubconnect.dto.ExperienceDTO
import com.connect.eduhubconnect.dto.SelfPostDTO
import com.connect.eduhubconnect.dto.SkillDTO
import com.connect.eduhubconnect.dto.UserProfileDTO
import com.connect.eduhubconnect.model.Address
import com.connect.eduhubconnect.model.Education
import com.connect.eduhubconnect.model.Experience
import com.connect.eduhubconnect.model.SelfPost
import com.connect.eduhubconnect.model.Skill
import com.connect.eduhubconnect.model.UserProfile

// ---------- User Mappers ----------
fun UserDTO.toEntity() = User(
    id = id,
    name = name,
    email = email,
    bio = bio,
    profileImage = profileImage
)

fun User.toDTO() = UserDTO(
    id = id,
    name = name,
    email = email,
    bio = bio,
    profileImage = profileImage
)

// ---------- Post Mappers ----------
fun PostDTO.toEntity() = Post(
    id = id,
    content = content,
    imageUrl = imageUrl,
    createdAt = createdAt.toString(),
    userId = userId,
    userName = userName,
    likeCount = likeCount,
    commentCount = commentCount,
    likedByUser = likedByUser
)

fun Post.toDTO(
    likedBy: List<UserDTO> = emptyList(),
    comments: List<CommentDTO> = emptyList()
) = PostDTO(
    id = id,
    content = content,
    imageUrl = imageUrl,
    createdAt = createdAt,
    userId = userId,
    userName = userName,
    likeCount = likeCount,
    commentCount = commentCount,
    likedByUser = likedByUser,
    likedBy = likedBy,
    comments = comments
)

// ---------- Comment Mappers ----------
fun CommentDTO.toEntity() = Comment(
    id = id,
    content = content,
    userId = userId,
    postId = postId,
    userName = userName,
    createdAt = createdAt.toString(),
    userJobTitle = userJobTitle,
    userProfileImage = userProfileImage,
    updatedAt = updatedAt
)

fun Comment.toDTO() = CommentDTO(
    id = id,
    content = content,
    userId = userId,
    postId = postId,
    userName = userName,
    createdAt = createdAt,
    userJobTitle = userJobTitle,
    userProfileImage = userProfileImage,
    updatedAt = updatedAt
)

// ---------- Friend Request Mappers ----------
fun FriendRequestDTO.toEntity() = FriendRequest(
    id = id,
    senderId = senderId,
    receiverId = receiverId,
    status = status.name,
    sentAt = sentAt.toString(),
    senderName = senderName,
    userBio = userBio,
    senderEmail = senderEmail,
    senderProfileImage = senderProfileImage
)

fun FriendRequest.toDTO() = FriendRequestDTO(
    id = id,
    senderId = senderId,
    receiverId = receiverId,
    status = RequestStatus.valueOf(status),
    sentAt = sentAt,
    senderName = senderName,
    userBio = userBio,
    senderEmail = senderEmail,
    senderProfileImage = senderProfileImage
)


// ---------- Experience Mappers ----------
fun ExperienceDTO.toEntity() = Experience(
    id = id,
    jobTitle = jobTitle,
    companyName = companyName,
    startDate = startDate,
    endDate = endDate,
    description = description,
    userId = userId
)

fun Experience.toDTO() = ExperienceDTO(
    id = id,
    jobTitle = jobTitle,
    companyName = companyName,
    startDate = startDate,
    endDate = endDate,
    description = description,
    userId = userId
)

// ---------- Education Mappers ----------
fun EducationDTO.toEntity() = Education(
    id = id,
    degree = degree,
    institution = institution,
    graduationYear = graduationYear,
    userId = userId
)

fun Education.toDTO() = EducationDTO(
    id = id,
    degree = degree,
    institution = institution,
    graduationYear = graduationYear,
    userId = userId
)

// ---------- Skill Mappers ----------
fun SkillDTO.toEntity() = Skill(
    id = id,
    name = name,
    userId = userId
)

fun Skill.toDTO() = SkillDTO(
    id = id,
    name = name,
    userId = userId
)


// Address Mappers
fun AddressDTO.toEntity() = Address(
    id = id,
    street = street ?: "Unknown Street", // Default if street is null
    city = city ?: "Unknown City", // Default if city is null
    state = state ?: "Unknown State", // Default if state is null
    country = country ?: "Unknown Country", // Default if country is null
    pincode = pincode ?: "Unknown Pincode", // Default if pincode is null
    userId = userId
)

fun Address.toDTO() = AddressDTO(
    id = id,
    street = street ?: "Unknown Street", // Default if street is null
    city = city ?: "Unknown City", // Default if city is null
    state = state ?: "Unknown State", // Default if state is null
    country = country ?: "Unknown Country", // Default if country is null
    pincode = pincode ?: "Unknown Pincode", // Default if pincode is null
    userId = userId
)


// SelfPost Mappers

// To convert SelfPostDTO to SelfPost (Entity)
fun SelfPostDTO.toEntity() = SelfPost(
    id = id,
    content = content,
    imageUrl = imageUrl,
    createdAt = createdAt,
    userId = userId,
    userName = userName,
    likeCount = likeCount,
    commentCount = commentCount,
    likedByUser = likedByUser,
    likedBy = likedBy.map { it }, // Convert likedBy UserDTOs to User entities
    comments = comments.map { it } // Convert CommentDTOs to Comment entities
)

// To convert SelfPost (Entity) to SelfPostDTO
fun SelfPost.toDTO() = SelfPostDTO(
    id = id,
    content = content,
    imageUrl = imageUrl,
    createdAt = createdAt,
    userId = userId,
    userName = userName,
    likeCount = likeCount,
    commentCount = commentCount,
    likedByUser = likedByUser,
    likedBy = likedBy.map { it }, // Convert likedBy Users to UserDTOs
    comments = comments.map { it } // Convert Comments to CommentDTOs
)


// UserProfile Mappers
// UserProfile Mappers
fun UserProfileDTO.toEntity() = UserProfile(
    userId = userId,
    userName = userName,
    bio = bio ?: "No bio available", // Default value for null bio
    collageName = collageName ?: "Unknown College", // Default if collageName is null
    addressDTO = addressDTO?.toEntity() ?: AddressDTO( // Check for null and map accordingly
        id = 0,
        street = "Unknown",
        city = "Unknown",
        state = "Unknown",
        country = "Unknown",
        pincode = "Unknown",
        userId = userId
    ).toEntity(), // Default Address if null
    profileImageUrl = profileImageUrl ?: "default_profile_image_url", // Default if null
    bannerImageUrl = bannerImageUrl ?: "default_banner_image_url", // Default if null
    experiences = experiences.map { it.toEntity() },
    educationList = educationList.map { it.toEntity() },
    skills = skills.map { it.toEntity() },
    selfPosts = selfPosts.map { it.toEntity() },
    about = about ?: "No about information available" // Default if null
)

fun UserProfile.toDTO() = UserProfileDTO(
    userId = userId,
    userName = userName,
    bio = bio,
    collageName = collageName,
    addressDTO = addressDTO.toDTO(),
    profileImageUrl = profileImageUrl,
    bannerImageUrl = bannerImageUrl,
    experiences = experiences.map { it.toDTO() },
    educationList = educationList.map { it.toDTO() },
    skills = skills.map { it.toDTO() },
    selfPosts = selfPosts.map { it.toDTO() },
    about = about
)




