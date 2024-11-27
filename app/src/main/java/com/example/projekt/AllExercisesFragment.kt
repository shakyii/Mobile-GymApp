package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt.Exercise
import com.example.projekt.ExerciseAdapter
import com.example.projekt.R
import com.google.firebase.firestore.FirebaseFirestore


class AllExercisesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_all_exercises_fragment, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewAllExercises)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("exercises")
            .get()
            .addOnSuccessListener { documents ->
                val exercises = mutableListOf<Exercise>()

                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    val mainMuscle = document.getString("main muscle group") ?: ""
                    val sideMuscle = document.getString("side muscle group") ?: ""
                    val difficultyField = document.get("difficulty")
                    val difficulty = when (difficultyField) {
                        is String -> difficultyField
                        is Number -> difficultyField.toString()
                        else -> "Unknown Difficulty"
                    }

                    exercises.add(Exercise(id, name, mainMuscle, sideMuscle, difficulty, false))
                }

                exerciseAdapter = ExerciseAdapter(exercises) { exercise ->
                    showDeleteConfirmationDialog(exercise)
                }
                recyclerView.adapter = exerciseAdapter
            }
            .addOnFailureListener { exception ->

            }

        return view
    }
    fun showDeleteConfirmationDialog(exercise: Exercise) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this exercise?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val exerciseRef = firestore.collection("exercises")
                .document(exercise.id)

            exerciseRef.delete()
                .addOnSuccessListener {
                    Log.d("ExerciseListActivity", "Exercise deleted successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("ExerciseListActivity", "Error deleting exercise", e)
                }
            exerciseAdapter.removeExercise(exercise)
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

