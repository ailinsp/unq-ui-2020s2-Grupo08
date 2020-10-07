package org.unq.ui

import io.javalin.http.Context
import org.unq.ui.mappers.*
import org.unq.ui.token.TokenJWT
import org.unq.ui.model.InstagramSystem
import org.unq.ui.model.NotFound
import org.unq.ui.model.Post
import org.unq.ui.model.UsedEmail
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

class UserController(val system: InstagramSystem) {

    val tokenController = TokenJWT()
    val instagramAccessManager = InstagramAccessManager(system)
    var userLoggedtoken :String? = null


    /**
     * Registra a un usuario
     */
    fun register(ctx: Context) {
        val newUser = ctx.bodyValidator<UserRegisterMapper>()
            .check(
                {
                    it.name != null && it.email != null && it.password != null && it.image != null
                },
                "Invalid body: name, email, password and image should not be null"
            )
            .get()

        try {
            val user = system.register(newUser.name!!, newUser.email!!, newUser.password!!, newUser.image!!)
            ctx.header("Authorization", tokenController.generateToken(user))
            ctx.status(201).json(ResultResponse("Ok"))
        } catch (e: UsedEmail) {
            ctx.status(400).json(ResultResponse(e.message!!))
        }
    }

    /**
     * Logea a un usuario
     */
    fun login(ctx: Context) {
        val user = ctx.bodyValidator<UserLoginMapper>()
            .check(
                {
                    it.email != null && it.password != null
                },
                "Invalid body: email and password should not be null"
            )
            .get()

        try {
            val user = system.login(user.email!!, user.password!!)
            userLoggedtoken = tokenController.generateToken(user)   //TODO Refactor para no generar 2 tokens a pesar de que sean iguales.
            ctx.header("Authorization", tokenController.generateToken(user))
            ctx.status(200).json(ResultResponse("Ok"))

        } catch (e: NotFound) {
            ctx.status(404).json(MessageResponse("error", "User not found"))
        }


    }

    /**
     * Retorna al usuario con el mismo id que es pasado como parametro y sus posts
     */
    fun getUserById(ctx: Context) {
        val id = ctx.pathParam("userId")
        try {
            val token = ctx.header("Authorization")
            val user = system.getUser(id)
            val followers = user.followers.map{UserMapper(it.name, it.image)}.toMutableList()
            val posts = system.searchByUserId(id).map{PostTimelineMapper(it.id,it.description,it.portrait, it.landscape,
                                                                         it.likes.map {UserMapper(it.name,it.image)}.toMutableList(),
                                                                         it.date.format(ISO_LOCAL_DATE), UserMapper(it.user.name,it.user.image))}.toMutableList()
            ctx.header("Authorization", token!!)
            ctx.status(200).json(UserPostbyIDMapper(user.name, user.image, followers, emptyList<PostTimelineMapper>().toMutableList()))
        } catch (e: NotFound){
            ctx.status(404).json(ResultResponse("Not found user with id $id"))
        }
    }


    /**
     * Retorna al usuario logueado con su timeline
     */


    fun getLoggedUser(ctx: Context) {

            val token = ctx.header("Authorization")
            val userLogged = instagramAccessManager.getUser(userLoggedtoken!!)
            val timelineMapper = system.timeline(userLogged.id).map{PostTimelineMapper(it.id,it.description,it.portrait, it.landscape,
                                                                                       it.likes.map {UserMapper(it.name,it.image)}.toMutableList(),
                                                                                       it.date.format(ISO_LOCAL_DATE), UserMapper(it.user.name,it.user.image))}.toMutableList()
            val followers = userLogged.followers.map{ UserMapper(it.name, it.image) }.toMutableList()

            ctx.header("Authorization", userLoggedtoken!!)
            ctx.status(200).json(UserLoggedMapper(userLogged.name, userLogged.image, followers, timelineMapper ))
    }



    /**
     * Agrega/elimina al usuario como follower del userId
     */
    fun updateFollowerById(ctx: Context) {
        val idUserToFollow = ctx.pathParam("userId")
        val token = ctx.header("Authorization")
        val idUserLogged = instagramAccessManager.getUser(token!!).id

                try{
                   system.updateFollower(idUserToFollow,idUserLogged)
                    ctx.status(200).json(ResultResponse("Ok"))
                }catch (e: NotFound){
                    ctx.status(404).json(ResultResponse("Not found user with id $idUserToFollow"))
                }

    }








}
