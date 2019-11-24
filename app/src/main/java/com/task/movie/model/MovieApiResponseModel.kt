package com.task.movie.model
import com.google.gson.annotations.SerializedName



/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :17-11-2019 19:48
 * Updated on : 
 * File Name : MovieResponseModelodel.kt
 * ClassName : MovieApiResponseModel
 * Module Name : app
 * Desc : 
 */
data class MovieApiResponseModel(
    @SerializedName("dates")
    val dates: Dates = Dates(),
    @SerializedName("page")
    val page: Int = 0, // 1
    @SerializedName("results")
    val results: List<Result> = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int = 0, // 70
    @SerializedName("total_results")
    val totalResults: Int = 0 // 1381
)

data class Dates(
    @SerializedName("maximum")
    val maximum: String = "", // 2019-11-22
    @SerializedName("minimum")
    val minimum: String = "" // 2019-10-05
)

data class Result(
    @SerializedName("adult")
    val adult: Boolean = false, // false
    @SerializedName("backdrop_path")
    val backdropPath: String = "", // /cXyfAViYly0Lk2CVpEKgYbt9wKQ.jpg
    @SerializedName("genre_ids")
    val genreIds: List<Int> = listOf(),
    @SerializedName("id")
    val id: Int = 0, // 535581
    @SerializedName("original_language")
    val originalLanguage: String = "", // en
    @SerializedName("original_title")
    val originalTitle: String = "", // The Dead Don't Die
    @SerializedName("overview")
    var overview: String = "", // In a small peaceful town, zombies suddenly rise to terrorize the town. Now three bespectacled police officers and a strange Scottish morgue expert must band together to defeat the undead.
    @SerializedName("popularity")
    val popularity: Double = 0.0, // 26.398
    @SerializedName("poster_path")
    var posterPath: String = "", // /fgGzTEoNxptCRtEOpOPvIEdlxAq.jpg
    @SerializedName("release_date")
    val releaseDate: String = "", // 2019-05-15
    @SerializedName("title")
    var title: String = "", // The Dead Don't Die
    @SerializedName("video")
    val video: Boolean = false, // false
    @SerializedName("vote_average")
    var voteAverage: Double = 0.3, // 5.4
    @SerializedName("vote_count")
    val voteCount: Int = 0 // 723
)
