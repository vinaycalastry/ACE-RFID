package dngsoftware.acerfid

object Constants {
    enum class FilamentName(val value: String) {
        ABS("ABS"),
        ASA("ASA"),
        PETG("PETG"),
        PLA("PLA"),
        PLA_PLUS("PLA+"),
        PLA_GLOW("PLA GLOW"),
        PLA_HS("PLA HIGH SPEED"),
        PLA_MARBLE("PLA MARBLE"),
        PLA_MATTE("PLA MATTE"),
        PLA_SE("PLA SE"),
        PLA_SILK("PLA SILK"),
        TPU("TPU");
    }

    val defaultFilamentList: List<Filament> = listOf(
        Filament(
            filamentID = "SHABBK-102",
            filamentName = FilamentName.ABS.value,
            filamentVendor = "AC",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 220,
                    extruderMaxTemp = 250,
                    bedMinTemp = 90,
                    bedMaxTemp = 100,
                ),
            )
        ),
        Filament(
            filamentID = "",
            filamentName = FilamentName.ASA.value,
            filamentVendor = "",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 240,
                    extruderMaxTemp = 280,
                    bedMinTemp = 90,
                    bedMaxTemp = 100,
                ),
            )
        ),
        Filament(
            filamentID = "",
            filamentName = FilamentName.PETG.value,
            filamentVendor = "",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 230,
                    extruderMaxTemp = 250,
                    bedMinTemp = 70,
                    bedMaxTemp = 90,
                ),
            )
        ),
        Filament(
            filamentID = "AHPLBK-101",
            filamentName = FilamentName.PLA.value,
            filamentVendor = "AC",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 190,
                    extruderMaxTemp = 230,
                    bedMinTemp = 50,
                    bedMaxTemp = 60,
                ),
            ),
            isDefault = true,
        ),
        Filament(
            filamentID = "AHPLPBK-102",
            filamentName = FilamentName.PLA_PLUS.value,
            filamentVendor = "AC",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 210,
                    extruderMaxTemp = 230,
                    bedMinTemp = 45,
                    bedMaxTemp = 60,
                ),
            )
        ),
        Filament(
            filamentID = "",
            filamentName = FilamentName.PLA_GLOW.value,
            filamentVendor = "",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 190,
                    extruderMaxTemp = 230,
                    bedMinTemp = 50,
                    bedMaxTemp = 60,
                ),
            )
        ),
        Filament(
            filamentID = "AHHSBK-103",
            filamentName = FilamentName.PLA_HS.value,
            filamentVendor = "AC",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 190,
                    extruderMaxTemp = 230,
                    bedMinTemp = 50,
                    bedMaxTemp = 60,
                ),
            )
        ),
        Filament(
            filamentID = "",
            filamentName = FilamentName.PLA_MARBLE.value,
            filamentVendor = "",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 200,
                    extruderMaxTemp = 230,
                    bedMinTemp = 50,
                    bedMaxTemp = 60,
                ),
            )
        ),
        Filament(
            filamentID = "HYGBK-102",
            filamentName = FilamentName.PLA_MATTE.value,
            filamentVendor = "AC",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 190,
                    extruderMaxTemp = 230,
                    bedMinTemp = 55,
                    bedMaxTemp = 65,
                ),
            )
        ),
        Filament(
            filamentID = "",
            filamentName = FilamentName.PLA_SE.value,
            filamentVendor = "",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 190,
                    extruderMaxTemp = 230,
                    bedMinTemp = 55,
                    bedMaxTemp = 65,
                ),
            )
        ),
        Filament(
            filamentID = "AHSCWH-102",
            filamentName = FilamentName.PLA_SILK.value,
            filamentVendor = "AC",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 200,
                    extruderMaxTemp = 230,
                    bedMinTemp = 55,
                    bedMaxTemp = 65,
                ),
            )
        ),
        Filament(
            filamentID = "STPBK-101",
            filamentName = FilamentName.TPU.value,
            filamentVendor = "AC",
            filamentParam = FilamentParameters(
                filamentTemperatures = FilamentTemperature(
                    extruderMinTemp = 210,
                    extruderMaxTemp = 230,
                    bedMinTemp = 25,
                    bedMaxTemp = 60,
                ),
            )
        ),
    )

    var materialWeights: List<String> = listOf(
        "1 KG",
        "750 G",
        "600 G",
        "500 G",
        "250 G"
    )

    fun GetMaterialLength(materialWeight: String): Int {
        when (materialWeight) {
            "1 KG" -> return 330
            "750 G" -> return 247
            "600 G" -> return 198
            "500 G" -> return 165
            "250 G" -> return 82
        }
        return 330
    }

    fun GetMaterialWeight(materialLength: Int): String {
        when (materialLength) {
            330 -> return "1 KG"
            247 -> return "750 G"
            198 -> return "600 G"
            165 -> return "500 G"
            82 -> return "250 G"
        }
        return "1 KG"
    }

    fun GetDefaultTemps(materialType: String): FilamentTemperature = when (materialType) {
        "ABS" -> FilamentTemperature(220, 250, 90, 100)
        "ASA" -> FilamentTemperature(240, 280, 90, 100)
        "HIPS" -> FilamentTemperature(230, 245, 80, 100)
        "PA" -> FilamentTemperature(220, 250, 70, 90)
        "PA-CF" ->FilamentTemperature(260, 280, 80, 100)
        "PC" -> FilamentTemperature(260, 300, 100, 110)
        "PETG" -> FilamentTemperature(230, 250, 70, 90)
        "PLA" -> FilamentTemperature(190, 230, 50, 60)
        "PLA-CF" -> FilamentTemperature(210, 240, 45, 65)
        "PVA" -> FilamentTemperature(215, 225, 45, 60)
        "PP" -> FilamentTemperature(225, 245, 80, 105)
        "TPU" -> FilamentTemperature(210, 230, 25, 60)
        else -> FilamentTemperature(185, 300, 45, 110)
    }

    var filamentVendors: List<String> = listOf(
        "3Dgenius",
        "3DJake",
        "3DXTECH",
        "3D BEST-Q",
        "3D Hero",
        "3D-Fuel",
        "Aceaddity",
        "AddNorth",
        "Amazon Basics",
        "AMOLEN",
        "Ankermake",
        "Anycubic",
        "Atomic",
        "AzureFilm",
        "BASF",
        "Bblife",
        "BCN3D",
        "Beyond Plastic",
        "California Filament",
        "Capricorn",
        "CC3D",
        "colorFabb",
        "Comgrow",
        "Cookiecad",
        "Creality",
        "CERPRiSE",
        "Das Filament",
        "DO3D",
        "DOW",
        "DSM",
        "Duramic",
        "ELEGOO",
        "Eryone",
        "Essentium",
        "eSUN",
        "Extrudr",
        "Fiberforce",
        "Fiberlogy",
        "FilaCube",
        "Filamentive",
        "Fillamentum",
        "FLASHFORGE",
        "Formfutura",
        "Francofil",
        "FilamentOne",
        "Fil X",
        "GEEETECH",
        "Giantarm",
        "Gizmo Dorks",
        "GreenGate3D",
        "HATCHBOX",
        "Hello3D",
        "IC3D",
        "IEMAI",
        "IIID Max",
        "INLAND",
        "iProspect",
        "iSANMATE",
        "Justmaker",
        "Keene Village Plastics",
        "Kexcelled",
        "LDO",
        "MakerBot",
        "MatterHackers",
        "MIKA3D",
        "NinjaTek",
        "Nobufil",
        "Novamaker",
        "OVERTURE",
        "OVVNYXE",
        "Polymaker",
        "Priline",
        "Printed Solid",
        "Protopasta",
        "Prusament",
        "Push Plastic",
        "R3D",
        "Re-pet3D",
        "Recreus",
        "Regen",
        "Sain SMART",
        "SliceWorx",
        "Snapmaker",
        "SnoLabs",
        "Spectrum",
        "SUNLU",
        "TTYT3D",
        "Tianse",
        "UltiMaker",
        "Valment",
        "Verbatim",
        "VO3D",
        "Voxelab",
        "VOXELPLA",
        "YOOPAI",
        "Yousu",
        "Ziro",
        "Zyltech"
    )

    var filamentTypes: List<String> = listOf(
        "ABS",
        "ASA",
        "HIPS",
        "PA",
        "PA-CF",
        "PC",
        "PETG",
        "PLA",
        "PLA-CF",
        "PVA",
        "PP",
        "TPU"
    )
}