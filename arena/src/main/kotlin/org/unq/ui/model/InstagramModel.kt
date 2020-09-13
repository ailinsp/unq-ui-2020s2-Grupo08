package org.unq.ui.model


import org.unq.ui.bootstrap.getInstagramSystem
import org.uqbar.commons.model.annotations.Observable


@Observable
class Postmodel(var id: String,  var description :String, var landscape: String, var portrait: String){ }

@Observable
class DatosUsuario(var id: String,var email: String, var name:String ){}

@Observable
class UserModel(){
    var id: String =""
    var email: String=""
    var name:String=""

    constructor(DatosUsuario: DatosUsuario):this() {

        id = DatosUsuario.id
        email= DatosUsuario.email
        name= DatosUsuario.name

    }
}
@Observable
class DraftPostModel(){

    var description = ""
    var landscape =""
    var portrait = ""


    constructor(postModel: Postmodel):this() {

        description = postModel.description
        landscape = postModel.landscape
        portrait = postModel.portrait

    }
  }


@Observable
class InstagramModel(val instagramSystem: InstagramSystem = getInstagramSystem()) {

    lateinit var posts: List<Postmodel>


    var search: String = ""
        set(tag) {
            field = tag
            search()
        }

    var post = mutableListOf<Postmodel>()
    var filteredpost = mutableListOf<UserModel>()

    fun search() {
        post = posts.filter { it.description.contains(search) }.toMutableList()
    }


    init {
        updatePosts()
    }

    init {
        posts = instagramSystem.posts.map {
            val p = Postmodel(it.id,it.description,it.landscape,it.portrait)
            p

        }.toMutableList()

    }

    private fun updatePosts() {
        posts = instagramSystem.posts.map { Postmodel(it.id, it.description, it.landscape, it.portrait) }
    }

    lateinit var user: User
    var password = ""
    var name = ""
    var email = ""
    var id = ""
    var selected: Postmodel? = null


    /*
    set(value) {
         check = true
         field = value
    }
    var check = false

 */
    fun setearDatos(user: User) {
        name = user.name
        email = user.email
        id = user.id

    }

    fun login(email: String, password: String) {
        user = instagramSystem.login(email, password)
        setearDatos(user)
    }

    fun addPost(id: String, post: DraftPostModel) {
        instagramSystem.addPost(id, DraftPost(post.portrait, post.landscape, post.description))
        updatePosts()
    }

    fun editPost(id: String, post: DraftPostModel) {
        instagramSystem.editPost(id, DraftPost(post.portrait, post.landscape, post.description))
        updatePosts()
    }

    fun deletePost(noteid: String) {
        instagramSystem.deletePost(noteid)
        updatePosts()
    }

    fun filterPost(search: String) {
        instagramSystem.searchByTag(search)
        updatePosts()
    }





}
