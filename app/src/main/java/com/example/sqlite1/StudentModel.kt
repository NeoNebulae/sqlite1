package com.example.sqlite1

class StudentModel(var rollNo: Int, var name: String, var age: Int) {

    companion object {
        const val TABLE_NAME = "students"
        const val COLUMN_ROLL_NO = "rollNo"
        const val COLUMN_NAME = "name"
        const val COLUMN_AGE = "age"

        const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ROLL_NO INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_AGE INTEGER)"
    }
    override fun toString(): String {
        return "Roll No: $rollNo\nName: $name\nAge: $age"
    }

}
