package info.nightscout.androidaps.utils.buildHelper

import info.nightscout.androidaps.BuildConfig
import info.nightscout.androidaps.interfaces.Config
import info.nightscout.androidaps.R
import info.nightscout.androidaps.plugins.general.maintenance.PrefFileListProvider
import info.nightscout.androidaps.utils.sharedPreferences.SP
import java.io.File

class BuildHelperImpl constructor(
    private val config: Config,
    fileListProvider: PrefFileListProvider,
    private val sp: SP
) : BuildHelper {

    private var devBranch = false
    private var engineeringMode = false

    init {
        val engineeringModeSemaphore = File(fileListProvider.ensureExtraDirExists(), "engineering__mode")

        engineeringMode = engineeringModeSemaphore.exists() && engineeringModeSemaphore.isFile || sp.getBoolean(R.string.key_engineering_mode, false)
        devBranch = BuildConfig.VERSION.contains("-") || BuildConfig.VERSION.matches(Regex(".*[a-zA-Z]+.*"))
    }

    override fun isEngineeringModeOrRelease(): Boolean =
        if (!config.APS) true else engineeringMode || !devBranch

    override fun isEngineeringMode(): Boolean = engineeringMode

    override fun isDev(): Boolean = devBranch
}