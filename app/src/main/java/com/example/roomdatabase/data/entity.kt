package com.example.roomdatabase.data

import androidx.room.*

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = false)
    val iid: Long? = null,
    val movie_name: String,
    val release_date: String,
    val director: String,

    ) {
    override fun toString() = "($movie_name) $release_date $director"
}

// Has a Many to One relation with Recipe
@Entity
data class Actors(
    @PrimaryKey(autoGenerate = true)
    val mid : Int? = null,
    val movie_name: String,
    val actor_name: String, // Ingredient name & quantity (1tbsp/1l/5g)
    val role : String
)


data class MovieCast(
    @Embedded
    val movie: Movie,

    @Relation(parentColumn = "movie_name", entityColumn = "movie_name")
    val actor_name: List<Actors>
)