package dngsoftware.acerfid

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

@Entity(tableName = "filaments")
data class Filament(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "filament_name")
    var filamentName: String? = null,

    @ColumnInfo(name = "filament_id")
    var filamentID: String? = null,

    @ColumnInfo(name = "filament_vendor")
    var filamentVendor: String? = null,

    @Embedded(prefix = "fp_")
    var filamentParam: FilamentParameters? = null,

    @ColumnInfo(name = "filament_default")
    var isDefault: Boolean = false,
)

data class FilamentParameters(
    @TypeConverters(FilamentTemperatureConverter::class)
    var filamentTemperatures: FilamentTemperature? = null,
)

data class FilamentTemperature(
    var extruderMinTemp: Int? = null,
    var extruderMaxTemp: Int? = null,
    var bedMinTemp: Int? = null,
    var bedMaxTemp: Int? = null,
)

class FilamentTemperatureConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromFilamentTemperature(filamentTemperature: FilamentTemperature?): String? {
        return gson.toJson(filamentTemperature)
    }

    @TypeConverter
    fun toFilamentTemperature(filamentTemperatureString: String?): FilamentTemperature? {
        return gson.fromJson(filamentTemperatureString, FilamentTemperature::class.java)
    }
}
