package dngsoftware.acerfid

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filaments")
data class Filament(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "filament_position")
    var position: Int = 0,

    @ColumnInfo(name = "filament_name")
    var filamentName: String? = null,

    @ColumnInfo(name = "filament_id")
    var filamentID: String? = null,

    @ColumnInfo(name = "filament_vendor")
    var filamentVendor: String? = null,

    @ColumnInfo(name = "filament_param")
    var filamentParam: String? = null,
)