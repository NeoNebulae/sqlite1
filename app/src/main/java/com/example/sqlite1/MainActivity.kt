package com.example.sqlite1

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var studentListView: ListView
    private lateinit var studentList: ArrayList<StudentModel>
    private lateinit var adapter: ArrayAdapter<StudentModel>
    private lateinit var emptyListTextView: TextView
    private lateinit var addStudentButton: Button
    private lateinit var readStudentButton: Button
    private lateinit var updateStudentButton: Button
    private lateinit var deleteStudentButton: Button
    private lateinit var editTextRollNo: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        studentListView = findViewById(R.id.studentListView)
        studentList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        studentListView.adapter = adapter
        emptyListTextView = findViewById(R.id.emptyListTextView)
        addStudentButton = findViewById(R.id.addStudentButton)
        readStudentButton = findViewById(R.id.readStudentButton)
        updateStudentButton = findViewById(R.id.updateStudentButton)
        deleteStudentButton = findViewById(R.id.deleteStudentButton)
        editTextRollNo = findViewById(R.id.edittext_rollNo)
        editTextName = findViewById(R.id.edittext_name)
        editTextAge = findViewById(R.id.edittext_gender)

        addStudentButton.setOnClickListener {
            val rollNo = editTextRollNo.text.toString().toInt()
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toInt()
            val student = StudentModel(rollNo, name, age)
            if (dbHelper.insertStudent(student)) {
                studentList.add(student)
                adapter.notifyDataSetChanged()
                editTextRollNo.setText("")
                editTextName.setText("")
                editTextAge.setText("")
                emptyListTextView.visibility = View.GONE
            } else {
                Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show()
            }
        }

        readStudentButton.setOnClickListener {
            studentList.clear()
            studentList.addAll(dbHelper.getAllStudents())
            adapter.notifyDataSetChanged()
            if (studentList.isEmpty()) {
                emptyListTextView.visibility = View.VISIBLE
            } else {
                emptyListTextView.visibility = View.GONE
            }
        }


        studentListView.setOnItemClickListener { _, _, position, _ ->
            val student = studentList[position]
            editTextRollNo.setText(student.rollNo.toString())
            editTextName.setText(student.name)
            editTextAge.setText(student.age.toString())
        }

        updateStudentButton.setOnClickListener {
            val rollNo = editTextRollNo.text.toString().toInt()
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toInt()

            // find the original student object from the list
            val originalStudent = studentList.find { it.rollNo == rollNo }

            originalStudent?.name = name
            originalStudent?.age = age

            if (originalStudent != null && dbHelper.updateStudent(originalStudent)) {
                adapter.notifyDataSetChanged()
                editTextRollNo.setText("")
                editTextName.setText("")
                editTextAge.setText("")
                Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update student", Toast.LENGTH_SHORT).show()
            }
        }

        deleteStudentButton.setOnClickListener {
            val rollNo = editTextRollNo.text.toString().toInt()
            val student = StudentModel(rollNo, "", 0)
            if (dbHelper.deleteStudent(student)) {
                studentList.remove(student)
                adapter.notifyDataSetChanged()
                editTextRollNo.setText("")
                editTextName.setText("")
                editTextAge.setText("")
            } else {
                Toast.makeText(this, "Failed to delete student", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
