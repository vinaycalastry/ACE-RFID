package dngsoftware.acerfid

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FilamentDao {
    @Insert
    fun addItem(item: Filament)

    @Insert
    fun addItems(items: List<Filament>)

    @Update
    fun updateItem(item: Filament)

    @Delete
    fun deleteItem(item: Filament)

    @get:Query("SELECT COUNT(id) FROM filaments")
    val itemCount: Int

    @get:Query("SELECT * FROM filaments ORDER BY filament_name ASC")
    val allItems: List<Filament>

    @Query("DELETE FROM filaments")
    fun deleteAll()

    @Query("SELECT * FROM filaments  WHERE filament_vendor = :filamentVendor ORDER BY filament_name ASC")
    fun getFilamentsByVendor(filamentVendor: String?): List<Filament?>?

    @Query("SELECT * FROM filaments WHERE filament_id = :filamentID")
    fun getFilamentById(filamentID: String?): Filament?

    @Query("SELECT * FROM filaments WHERE filament_name = :filamentName")
    fun getFilamentByName(filamentName: String?): Filament?
}