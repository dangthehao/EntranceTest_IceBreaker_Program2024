package com.example.entrancetest_icebreaker_program2024

import android.provider.BaseColumns

object TaskContract {
    object TaskEntry : BaseColumns {
        const val TABLE_NAME = "tasks"
        const val COLUMN_TASK_NAME = "name"
        const val COLUMN_TASK_DESCRIPTION = "description"
    }
}
