package dngsoftware.acerfid

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Filament::class], version = 3)
@TypeConverters(FilamentTemperatureConverter::class)
abstract class FilamentDB : RoomDatabase() {
    abstract fun filamentDao(): FilamentDao
}