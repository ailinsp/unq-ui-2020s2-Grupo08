package org.unq.ui.ViewModel


import org.unq.ui.Exceptions.FieldsBlank
import org.unq.ui.Exceptions.InvalidUserOPassword
import org.unq.ui.bootstrap.getInstagramSystem
import org.unq.ui.model.DraftPost
import org.unq.ui.model.InstagramSystem
import org.unq.ui.model.Post
import org.unq.ui.model.User
import org.uqbar.commons.model.annotations.Observable
import org.uqbar.commons.model.exceptions.UserException

/**
 * Represents the user data values.
 */
@Observable
class UserDataModel(var name: String, var password: String, var image:String ){ }


/**
 * Represents the post data values.
 */
@Observable
class PostModel(var id: String, var description :String, var landscape: String, var portrait: String){ }

@Observable
class DraftPostModel(){
    var description = ""
    var landscape =""
    var portrait = ""

    constructor(postModel: PostModel):this() {
        description = postModel.description
        landscape = postModel.landscape
        portrait = postModel.portrait
    }
}

/**
 * Implements the domain model
 */
@Observable
class InstagramModel(val instagramSystem: InstagramSystem = getInstagramSystem()) {
    lateinit var allPosts: MutableList<PostModel>
    lateinit var postsUserLogged : List<Post>
    lateinit var user: User

    var selected: PostModel? = null
    var search = ""

    /**
     * Updates the displayed posts with the search input.
     * @param search It's the hashtag to search.
     * @throws UserException When there are no results that match the search.
     */
    fun searchTag(search : String) {
        val filteredPost = allPosts.filter { it.description.contains(search) }.toMutableList()
        updatePosts()
        if(filteredPost.isEmpty() ){
            updatePosts()
            throw UserException("There are no results for your search")
        }
        if(search.isEmpty()) {
                updatePosts()
            } else{
                allPosts = filteredPost
        }
    }

    /**
     * Updates the logged user posts.
     */
    private fun updatePosts() {
        postsUserLogged = instagramSystem.searchByUserId(user.id)
        allPosts = postsUserLogged.map{ PostModel(it.id, it.description, it.landscape, it.portrait) }.toMutableList()
    }

    /**
     * Sets up the data of the logged in user.
     * @param user It's the logged in user.
     */
    fun setData(user: User) {
        name = user.name
        password = user.password
        email = user.email
        id = user.id
        image = user.image
    }






    /**
     * Adds a post to the user logged in feed.
     * @param post It's the new post to add.
     */
    fun addPost(post: DraftPostModel) {
        instagramSystem.addPost(id, DraftPost(post.portrait, post.landscape, post.description))
        updatePosts()
    }

    /**
     * Edits a post from the user logged in feed.
     * @param postId It's the post's id to modify.
     * @param post It's the model that contains the data of the new post.
     */
    fun editPost(postId: String, post: DraftPostModel) {
        instagramSystem.editPost(postId, DraftPost(post.portrait, post.landscape, post.description))
        updatePosts()
    }

    /**
     * Deletes a post from the user logged in feed.
     * @param postId It's the post's id to delete.
     */
    fun deletePost(postId: String) {
        instagramSystem.deletePost(postId)
        updatePosts()
    }

    /**
     * Edits the data of the logged in user.
     * @param user It's the model that contains new data of the user.
     */
    fun editProfile(user: UserDataModel) {
        val editedUser = instagramSystem.editProfile(id, user.name, user.password, user.image)
        setData(editedUser)
    }

    fun cleanUserAttributes() {
        id = ""
        name = ""
        image = ""
        allPosts = emptyList<PostModel>().toMutableList()
        password = ""
        email = "";
    }
}
