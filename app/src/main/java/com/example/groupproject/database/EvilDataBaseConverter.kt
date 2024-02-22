import androidx.room.TypeConverter
import com.example.groupproject.database.PlantEnum

class EvilPlantEnumConverter {
    @TypeConverter
    fun fromPlantEnum(plantEnum: PlantEnum): String {
        return plantEnum.stringValue
    }

    @TypeConverter
    fun toPlantEnum(value: String): PlantEnum {
        return enumValueOf(value)
    }
}