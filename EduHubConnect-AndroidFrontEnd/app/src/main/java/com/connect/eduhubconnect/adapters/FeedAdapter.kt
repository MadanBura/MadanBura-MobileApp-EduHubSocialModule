package com.connect.eduhubconnect.adapters

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.dto.CommentDTO
import com.connect.eduhubconnect.dto.PostDTO
import com.connect.eduhubconnect.utils.CONSTANTS
import com.connect.eduhubconnect.utils.getTimeAgo

class FeedAdapter(
    private val postList: List<PostDTO>,
    private val likeClick: (PostDTO) -> Unit,
    private val addCommentClick: (PostDTO, String) -> Unit,
//    private var comments: MutableList<CommentDTO>
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    val likedPosts = mutableSetOf<Long>()

    private var comments: List<CommentDTO> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post = postList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = postList.size

    inner class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        private val tvPostText: TextView = view.findViewById(R.id.tvPostText)
        private val ivPostImage: ImageView = view.findViewById(R.id.ivPostImage)
        private val tvLikeCount: TextView = view.findViewById(R.id.tvLikeCount)
        private val tvCommentCount: TextView = view.findViewById(R.id.tvCommentCount)
        private val btnLike: ImageView = view.findViewById(R.id.btnLike)
        private val tvTime: TextView = view.findViewById(R.id.tvTime)
        private val likeTitle: TextView = view.findViewById(R.id.likeTitle)

        private val btnCommentPost: Button = view.findViewById(R.id.postCommentBtn)
        private val editComment: EditText = view.findViewById(R.id.tvCommentPlaceholder)

        // Comment UI
        private val commentProPic: ImageView = view.findViewById(R.id.commentUserPost)
        private val commentUserName: TextView = view.findViewById(R.id.commentUserNamePost)
        private val commentUserJt: TextView = view.findViewById(R.id.commentUserJTPost)
        private val commentUserContent: TextView = view.findViewById(R.id.commentUserContentPost)
        private val commentUserCard: CardView = view.findViewById(R.id.cardCommentPost)

        fun bind(post: PostDTO) {
            tvUserName.text = post.userName
            tvPostText.text = post.content
            tvLikeCount.text = "${post.likeCount} likes"
            tvCommentCount.text = "${post.commentCount} comments"

            tvTime.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getTimeAgo(post.createdAt)
            } else {
                post.createdAt
            }

            // Load post image
            val postImageUrl = post.imageUrl?.replace("localhost", CONSTANTS.IP_ADD)
            Glide.with(itemView).load(postImageUrl).into(ivPostImage)

            // Like logic
            val likeIcon = if (likedPosts.contains(post.id) || post.likedByUser) {
                R.drawable.like_.also { likeTitle.text = "Liked" }
            } else {
                R.drawable.like
            }
            btnLike.setImageResource(likeIcon)

            btnLike.setOnClickListener { likeClick(post) }

            // Load latest comment
//            val userComments = comments.filter { it.postId == post.id }
//            Log.e("USERCOMMENT", userComments.toString())
//            Log.e("USERCOMMENT", comments.toString())
            Log.e("USERCOMMENT", post.comments.toString())
            val userComments = post.comments
            val newInsertedComment = comments
            Log.e("NEWUSERCOMMENT", post.comments.toString())

            if (userComments.isNotEmpty()) {
                commentUserCard.visibility = View.VISIBLE
                val latestComment = userComments.last()
                commentUserName.text = latestComment.userName
                commentUserJt.text = latestComment.userJobTitle
                commentUserContent.text = latestComment.content

                val profileImageUrl = latestComment.userProfileImage.replace("localhost", CONSTANTS.IP_ADD)
//                val profileImageUrl = latestComment.userProfileImage.replace("localhost", "172.20.10.3")
                Glide.with(itemView.context).load(profileImageUrl).into(commentProPic)
            } else if(!newInsertedComment.isNullOrEmpty()){
                commentUserCard.visibility = View.VISIBLE
                val latestComment = newInsertedComment.firstOrNull()
                commentUserName.text = latestComment?.userName
                commentUserJt.text = latestComment?.userJobTitle
                commentUserContent.text = latestComment?.content

                val profileImageUrl = latestComment?.userProfileImage?.replace("localhost", CONSTANTS.IP_ADD)
//                val profileImageUrl = latestComment.userProfileImage.replace("localhost", "172.20.10.3")
                if (!profileImageUrl.isNullOrEmpty()) {
                    Glide.with(itemView.context).load(profileImageUrl).into(commentProPic)
                }
            }else {
                commentUserCard.visibility = View.GONE
            }

            editComment.setText("") // reset field
            btnCommentPost.visibility = View.GONE // reset button

            // Handle comment input visibility
            editComment.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnCommentPost.visibility = if (!s.isNullOrBlank()) View.VISIBLE else View.GONE
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            btnCommentPost.setOnClickListener {
                val commentText = editComment?.text?.toString()?.trim()

                if (!commentText.isNullOrEmpty()) {
                    addCommentClick(post, commentText)
                    editComment?.text?.clear()
                    btnCommentPost.visibility = View.GONE
                    commentUserCard.visibility = View.VISIBLE
                } else {
                    Log.e("FEED ADAPTER", "bind: \"Comment cannot be empty\"")
                }
            }

        }
    }

    fun setComments(newComments: List<CommentDTO>) {
        comments = newComments
        notifyDataSetChanged()
    }
}
