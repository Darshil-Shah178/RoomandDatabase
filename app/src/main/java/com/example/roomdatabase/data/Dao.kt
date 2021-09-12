package com.example.roomdatabase.data

import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM Movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM Movie WHERE Movie.movie_name = :movie_name")
    fun getMovie(movie_name: String): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie) : Long

    @Update
    fun update(movie: Movie)

    @Delete
    fun delete(movie: Movie)

    @Query("DELETE FROM Movie WHERE Movie.movie_name = :movie_name")
    fun deleteMovie(movie_name: String)
}

@Dao
interface ActorsDao {
    @Query("SELECT * FROM Actors")
    fun getAll(): List<Actors>

    @Query("SELECT * FROM Actors WHERE Actors.movie_name = :recipe_name")
    fun getActors(recipe_name: String): List<Actors>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(actors: Actors)

    @Update
    fun update(actors: Actors)

    @Delete
    fun delete(actors: Actors)

    @Query("DELETE FROM Actors WHERE Actors.movie_name = :movie_name")
    fun deleteActors(movie_name: String)
}

@Dao
interface MovieCastDao {
    @Query("SELECT * FROM Movie")
    fun getAll(): List<MovieCast>

    @Query("SELECT * FROM Movie WHERE Movie.movie_name = :movie_name")
    fun getMovie(movie_name: String): MovieCast
}

