package dngsoftware.acerfid

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Filament::class], version = 1)
abstract class FilamentDB : RoomDatabase() {
    abstract fun filamentDao(): FilamentDao
}