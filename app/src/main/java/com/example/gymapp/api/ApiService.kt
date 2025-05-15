package com.example.gymapp.api
import com.example.gymapp.data.model.*
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate


// Definir endpoints: cómo la app se va a comunicar con Spring Boot Application (backend)
interface ApiService {
    @GET("gym_user/{id_user}")
    suspend fun getUserProfile(@Path("id_user") id: Int): User

    @PUT("gym_user/{id_user}")
    suspend fun updateUserProfile(@Path("id_user") id: Int, @Body user: User): User

    @POST("gym_user/login")
    suspend fun login(@Body request: LoginDetails): LoginResponse

    @GET("session/{session_day}")
    suspend fun getSessionsForDay(@Path("session_day") day: String,
                                  @Query("startDate") startDate: String,
                                  @Query("endDate") endDate: String): List<SessionDetails>

    @GET("session_instance/{session_instance_date}")
    suspend fun getSessionDate(@Path("session_instance_date") date: LocalDate): SessionInstance

    @GET("session_instance/{id_session_instance}")
    suspend fun getIdSessionInstance(@Path("id_session_instance") id: Int): SessionInstance

    @GET("user_session_registration/future/{id_user}")
    suspend fun getFutureUserSessionsByUser(@Path("id_user") userId: Int): List<UserRegisteredSession>

    @GET("user_session_registration/past/{id_user}")
    suspend fun getPastUserSessionsByUser(@Path("id_user") userId: Int): List<UserRegisteredSession>

    @POST("user_session_registration")
    suspend fun registerForSession(@Body registration: UserSessionRegistration): UserSessionRegistration

    @DELETE("user_session_registration/{id_registration}")
    suspend fun cancelRegistration(@Path("id_registration") idRegistration: Int): Boolean

}