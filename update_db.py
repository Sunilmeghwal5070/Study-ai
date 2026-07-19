import re

# Entities.kt
with open("app/src/main/java/com/example/data/Entities.kt", "r") as f:
    content = f.read()

content = content.replace("val setupCompleted: Boolean = false\n)", "val setupCompleted: Boolean = false,\n    val favoriteSubjects: String = \"\"\n)")

with open("app/src/main/java/com/example/data/Entities.kt", "w") as f:
    f.write(content)

# AppDatabase.kt
with open("app/src/main/java/com/example/data/AppDatabase.kt", "r") as f:
    db_content = f.read()

db_content = db_content.replace('version = 1,', 'version = 2,')
db_content = db_content.replace('import androidx.room.RoomDatabase', 'import androidx.room.RoomDatabase\nimport androidx.room.migration.Migration\nimport androidx.sqlite.db.SupportSQLiteDatabase')

migration_code = """        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE user_settings ADD COLUMN favoriteSubjects TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase"""

db_content = db_content.replace('        fun getDatabase', migration_code)
db_content = db_content.replace('                    "study_ai_database"\n                ).build()', '                    "study_ai_database"\n                )\n                .addMigrations(MIGRATION_1_2)\n                .build()')

with open("app/src/main/java/com/example/data/AppDatabase.kt", "w") as f:
    f.write(db_content)
