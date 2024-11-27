package com.example.projekt

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseAdapter(
    private val exercises: MutableList<Exercise>,
    var onDeleteClickListener: ((Exercise) -> Unit)?
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textExerciseName: TextView = itemView.findViewById(R.id.textExerciseName)
        val textMainMuscle: TextView = itemView.findViewById(R.id.textMainMuscle)
        val textSideMuscle: TextView = itemView.findViewById(R.id.textSideMuscle)
        val textDifficulty: TextView = itemView.findViewById(R.id.textDifficulty)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_item, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.textExerciseName.text = exercise.name
        holder.textMainMuscle.text = "Główna grupa mięśniowa: ${exercise.mainMuscle}"
        holder.textSideMuscle.text = "Dodatkowa grupa mięśniowa: ${exercise.sideMuscle}"
        holder.textDifficulty.text = "Trudność: ${exercise.difficulty}"

        if (exercise.isDeleteButtonVisible) {
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener {
                onDeleteClickListener?.invoke(exercise)
            }
        } else {
            holder.btnDelete.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeExercise(exercise: Exercise) {
        Log.d("ExerciseAdapter", "Removing exercise: ${exercise.name}")
        exercises.remove(exercise)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}
