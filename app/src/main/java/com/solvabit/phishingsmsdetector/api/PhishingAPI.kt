package com.solvabit.phishingsmsdetector.api

import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import com.solvabit.phishingsmsdetector.models.Tweets
import com.solvabit.phishingsmsdetector.models.YoutubeData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


const val BASE_URL = "https://c0f4-2405-201-a407-28-16d6-2b5b-efb8-c040.ngrok.io/"
const val yt_developer_key = "AIzaSyD3-rEEEWa3hIbHL7LcBb2tzq-JXIlliWM"
const val twitter_api_key = "AAAAAAAAAAAAAAAAAAAAAERTYwEAAAAAXrrRMw2RePv7QzP18Lhlm6n4Fo8%3DmOjhRZHuyUuowBrDSGgesWcZLPS3Uls2r7QQxr43XLeBvCwZfJ"

interface PhishingAPI {

    @Headers("Content-Type: application/json")
    @POST("sms_check/")
    fun checkPhishing(@Body message: Phishing_Message): Call<Phishing>

    @GET("search")
    fun getYoutubeVideos(
        @Query("q") q: String,
        @Query("key") developerKey: String = yt_developer_key,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video"
    ): Call<YoutubeData>

    @GET("recent")
    fun getTweets(
        @Query("query") query: String,
        @Header("Authorization") token: String = "Bearer $twitter_api_key",
        @Query("tweet.fields") fields: String = "created_at",
        @Query("expansions") expansions: String = "created_at"
    ): Call<Tweets>
}

object PhishingService {
    val phishingAPInstance: PhishingAPI
    val youtubeAPInstance: PhishingAPI
    val twitterAPInstance: PhishingAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitYoutube = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitTwitter = Retrofit.Builder()
            .baseUrl("https://api.twitter.com/2/tweets/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        phishingAPInstance = retrofit.create(PhishingAPI::class.java)
        youtubeAPInstance = retrofitYoutube.create(PhishingAPI::class.java)
        twitterAPInstance = retrofitTwitter.create(PhishingAPI::class.java)
    }
}