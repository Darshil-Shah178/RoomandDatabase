package com.example.roomdatabase

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.data.Movie
import com.example.roomdatabase.data.MovieDB
import kotlinx.coroutines.*


class MoviesFragment : Fragment() {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter

    @DelicateCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
       ): View {
        val view = inflater.inflate(R.layout.fragment_movies, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = MovieAdapter()
        adapter.onClick = { showDetail(it) }
        recyclerView.adapter = adapter
        refreshAsync()
        view.findViewById<Button>(R.id.addMovie).setOnClickListener {
            add()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
    }

    @DelicateCoroutinesApi
    private fun showDetail(movie : String)
    {
        Log.e("DBG","Showing $movie")
        GlobalScope.launch {
            val db = MovieDB.get(requireContext().applicationContext)
            val moVie = db.moviecastDao().getMovie(movie)
            withContext(Dispatchers.Main) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ActorFragment.newInstance(moviecast = moVie))
                    .addToBackStack("my_fragment")
                    .commit()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @DelicateCoroutinesApi
    private fun refreshAsync(movie: List<Movie>? = null)
    {
        if(movie == null) {
            GlobalScope.launch {
                val getMovie = MovieDB.get(requireContext().applicationContext).movieDao().getAll()
                withContext(Dispatchers.Main) {
                    adapter.items = getMovie.toMutableList()
                    adapter.notifyDataSetChanged()
                }
            }
        }
        else
        {
            adapter.items = movie.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    @DelicateCoroutinesApi
    private fun add() {
        GlobalScope.launch {
            val db = MovieDB.get(requireContext().applicationContext)
            if(db.movieDao().getMovie("new movie") == null) {
                db.movieDao().insert(
                    Movie(
                        movie_name = "new movie",
                        release_date = "",
                        director = "",
                        )
                )
                refreshAsync()
            }
        }
    }
}