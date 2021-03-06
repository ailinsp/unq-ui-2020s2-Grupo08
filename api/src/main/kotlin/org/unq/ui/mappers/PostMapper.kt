package org.unq.ui.mappers

data class PostMapper (
        val id: String? = null,
        val description: String? = null,
        val portrait:String? = null,
        val landscape:String? = null,
        val likes: MutableList<UserMapper>,
        val date: String? = null,
        val user: UserMapper? = null,
        val comments: MutableList<CommentMapper>
)