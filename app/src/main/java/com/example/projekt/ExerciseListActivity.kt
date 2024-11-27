package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class ExerciseListActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var btnGoToMain: Button
    private lateinit var btnLogout: Button
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        btnGoToMain = findViewById(R.id.btnGoToMain)
        btnLogout = findViewById(R.id.btnLogout)

        val currentUser = FirebaseAuth.getInstance().currentUser
        userId = currentUser?.uid ?: ""

        val exercisePagerAdapter = ExercisePagerAdapter(supportFragmentManager)
        viewPager.adapter = exercisePagerAdapter
        tabLayout.setupWithViewPager(viewPager)


        btnGoToMain.setOnClickListener {
            val intent = Intent(this@ExerciseListActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@ExerciseListActivity, Login::class.java)
            startActivity(intent)
            finish()
        }
    }


    private inner class ExercisePagerAdapter(fm: androidx.fragment.app.FragmentManager) :
        androidx.fragment.app.FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AllExercisesFragment()
                1 -> UserExercisesFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "All Exercises"
                1 -> "User Exercises"
                else -> null
            }
        }
    }
}
