package com.example.projekt


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.view.View


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var btnLogout: Button
    private lateinit var textView: TextView
    private lateinit var ename: TextInputEditText
    private lateinit var mainMuscle: TextInputEditText
    private lateinit var sideMuscle: TextInputEditText
    private lateinit var diff: TextInputEditText
    private lateinit var btnAdd: Button
    private lateinit var btnExercise: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        btnLogout = findViewById(R.id.btnLogout)
        textView = findViewById(R.id.user_details)
        ename = findViewById(R.id.eName)
        mainMuscle = findViewById(R.id.mainMuscle)
        sideMuscle = findViewById(R.id.sideMuscle)
        diff = findViewById(R.id.diff)
        btnAdd = findViewById(R.id.btnAdd)
        btnExercise = findViewById(R.id.btnGoToExercises)
        firestore = FirebaseFirestore.getInstance()


        val user = auth.currentUser
        val uid = user?.uid

        if (user == null || uid.isNullOrBlank()) {
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            textView.text = "Add your exercise"

            btnAdd.setOnClickListener {
                val exerciseName = ename.text.toString()
                val exerciseMainMuscle = mainMuscle.text.toString()
                val exerciseSideMuscle = sideMuscle.text.toString()
                val exerciseDifficulty = diff.text.toString()

                if (exerciseName.isNotBlank() && exerciseMainMuscle.isNotBlank() &&
                    exerciseSideMuscle.isNotBlank() && exerciseDifficulty.isNotBlank() && exerciseDifficulty<"6" && exerciseDifficulty>"0"
                ) {
                    val exerciseData = hashMapOf(
                        "name" to exerciseName,
                        "main muscle group" to exerciseMainMuscle,
                        "side muscle group" to exerciseSideMuscle,
                        "difficulty" to exerciseDifficulty,
                        "userId" to uid
                    )

                    firestore.collection("user_exercises").add(exerciseData)
                        .addOnSuccessListener { documentReference ->
                            val exerciseId = documentReference.id
                            println("Dodano ćwiczenie o ID: $exerciseId")
                            ename.setText("")
                            mainMuscle.setText("")
                            sideMuscle.setText("")
                            diff.setText("")
                        }
                        .addOnFailureListener { e ->
                            println("Błąd podczas dodawania ćwiczenia: $e")
                        }
                } else {
                    println("Wszystkie pola muszą być uzupełnione.")
                }
            }

            btnExercise.setOnClickListener {
                val intent = Intent(this@MainActivity, ExerciseListActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}