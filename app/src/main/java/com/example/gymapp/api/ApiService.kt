package com.example.gymapp.api
import com.example.gymapp.data.models.*
import retrofit2.http.*
import java.sql.Date

// Definir como la app va a comunicarse con Spring Boot backend
interface ApiService {
    @GET("gym_user/{id_user}")
    suspend fun getUserProfile(@Path("id_user") id: Int): User
/*
    @PUT("api/gym_user/{id_user}")
    suspend fun updateUserProfile(@Path("id_user") id: Int, @Body user: User): User


    // Endpoints para mostrar todas las sesiones disponibles
    @GET("api/sessions")
    suspend fun getAllAvailableSessions(): List<SessionDetails>

    @GET("api/sessions")
    suspend fun getSessionsByDate(@Query("date") date: Date): List<SessionDetails>

    // Endpoints para ver las reservas
    @POST("api/registrations")
    suspend fun registerForSession(@Body registration: UserSessionRegistration): UserSessionRegistration

    @DELETE("api/registrations/{registrationId}")
    suspend fun cancelRegistration(@Path("registrationId") registrationId: Int): Boolean

    @GET("api/users/{userId}/registrations")
    suspend fun getUserRegistrations(@Path("userId") userId: Int): List<RegistrationDetails>

    @GET("api/users/{userId}/registrations")
    suspend fun getUserRegistrationsByType(
        @Path("userId") userId: Int,
        @Query("type") type: String
    ): List<RegistrationDetails>

     */
}