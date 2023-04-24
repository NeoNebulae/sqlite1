package com.example.sqlite1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "students.db"
        private const val TABLE_NAME = "students"
        private const val COLUMN_ROLL_NO = "roll_no"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ROLL_NO INTEGER PRIMARY KEY,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_AGE INTEGER" + ")")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertStudent(student: StudentModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ROLL_NO, student.rollNo)
        values.put(COLUMN_NAME, student.name)
        values.put(COLUMN_AGE, student.age)
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id != -1L
    }


    fun getAllStudents(): List<StudentModel> {
        val studentList = mutableListOf<StudentModel>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val rollNoIndex = cursor.getColumnIndex(COLUMN_ROLL_NO)
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val ageIndex = cursor.getColumnIndex(COLUMN_AGE)

            do {
                val rollNo = if (rollNoIndex >= 0) cursor.getInt(rollNoIndex) else 0
                val name = if (nameIndex >= 0) cursor.getString(nameIndex) else ""
                val age = if (ageIndex >= 0) cursor.getInt(ageIndex) else 0
                val student = StudentModel(rollNo, name, age)
                studentList.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }

    fun updateStudent(student: StudentModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, student.name)
        values.put(COLUMN_AGE, student.age)
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ROLL_NO = ?", arrayOf(student.rollNo.toString()))
        db.close()
        return rowsAffected > 0
    }


    fun deleteStudent(student: StudentModel): Boolean {
        val db = this.writableDatabase
        val rowsAffected = db.delete(TABLE_NAME, "$COLUMN_ROLL_NO = ?", arrayOf(student.rollNo.toString()))
        db.close()
        return rowsAffected > 0
    }

}
