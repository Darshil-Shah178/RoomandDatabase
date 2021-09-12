package com.example.roomdatabase

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.example.roomdatabase.data.Actors
import com.example.roomdatabase.data.Movie
import com.example.roomdatabase.data.MovieCast
import com.example.roomdatabase.data.MovieDB
import kotlinx.coroutines.*

class ActorFragment(private val moviecast: MovieCast) : Fragment() {

    companion object {
        fun newInstance(moviecast: MovieCast) = ActorFragment(moviecast)
    }

    private lateinit var layout: ConstraintLayout
    private lateinit var movielist: LinearLayout

    @DelicateCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showBackButton()
        layout = inflater.inflate(R.layout.fragment_actor, container, false) as ConstraintLayout
        movielist = layout.findViewById<LinearLayout>(R.id.linearLayoutMovieList)!!

        refreshManifest()

        // Title
        layout.findViewById<EditText>(R.id.editName).setText(moviecast.movie.movie_name)

        // Description
        layout.findViewById<EditText>(R.id.editYear)
            .setText(moviecast.movie.release_date)

        layout.findViewById<EditText>(R.id.editDirector)
            .setText(moviecast.movie.director)

        layout.findViewById<Button>(R.id.addActorBtn).setOnClickListener {
            addIngredient()
        }

        // Delete
        layout.findViewById<ImageView>(R.id.btnDelete).setOnClickListener {
            remove()
        }

        return layout
    }

    private fun showBackButton(show: Boolean = true) {
        if (activity is AppCompatActivity) {
            setHasOptionsMenu(show)
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(show)
        }
    }

    @SuppressLint("InflateParams")
    private fun refreshManifest() {

        moviecast.actor_name.forEach {
            val view = layoutInflater.inflate(R.layout.view_actors, null)
            val actor = view.findViewById<EditText>(R.id.editActor)
            actor.setText(it.actor_name)
            val role = view.findViewById<TextView>(R.id.editRole)
            role.text = it.role
            val remove = view.findViewById<ImageView>(R.id.removeBtn)
            remove.setOnClickListener {
                movielist.removeView(view)
            }
            movielist.addView(view)
        }
    }

    @DelicateCoroutinesApi
    private fun remove()
    {
        GlobalScope.launch {
            val db = MovieDB.get(requireContext().applicationContext)
            db.actorsDao().deleteActors(moviecast.movie.movie_name)
            db.movieDao().deleteMovie(movie_name = moviecast.movie.movie_name)

            withContext(Dispatchers.Main) {
                showBackButton(false)
                requireActivity().onBackPressed()
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun addIngredient() {
        val view = layoutInflater.inflate(R.layout.view_actors, null)
        val remove = view.findViewById<ImageView>(R.id.removeBtn)
        remove.setOnClickListener {
            movielist.removeView(view)
        }
        movielist.addView(view)
    }

    @DelicateCoroutinesApi
    private fun commitChangesAndPop() {
        GlobalScope.launch {
            val db = MovieDB.get(requireContext().applicationContext)
            db.actorsDao().deleteActors(moviecast.movie.movie_name)

            db.movieDao().update(
                Movie(
                    moviecast.movie.iid,
                    layout.findViewById<EditText>(R.id.editName).text.toString(),
                    layout.findViewById<EditText>(R.id.editYear).text.toString(),
                    layout.findViewById<EditText>(R.id.editDirector).text.toString()

                )
            )


            movielist.children.forEach {
                db.actorsDao().insert(
                    Actors(
                        movie_name = layout.findViewById<EditText>(R.id.editName).text.toString(),
                        actor_name = it.findViewById<EditText>(R.id.editActor).text.toString(),
                        role = it.findViewById<TextView>(R.id.editRole).text.toString()
                    )
                )
            }

            withContext(Dispatchers.Main) {
                showBackButton(false)
                requireActivity().onBackPressed()
            }
        }
    }

    // Actionbar back press
    @DelicateCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        commitChangesAndPop()
        return true
    }

    // Device back press
    @DelicateCoroutinesApi
    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                commitChangesAndPop()
                true
            } else false
        }
    }

}