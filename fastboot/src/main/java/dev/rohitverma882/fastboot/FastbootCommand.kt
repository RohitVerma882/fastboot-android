package dev.rohitverma882.fastboot

class FastbootCommand private constructor(private val command: String) {
    override fun toString(): String {
        return command
    }

    companion object {
        fun getVar(variable: String): FastbootCommand = FastbootCommand("getvar:$variable")

        fun oem(value: String): FastbootCommand = FastbootCommand("oem $value")

        fun download(size: Int): FastbootCommand = FastbootCommand("download:$size")

        fun flash(partition: String): FastbootCommand = FastbootCommand("flash:$partition")

        fun erase(partition: String): FastbootCommand = FastbootCommand("erase:$partition")

        fun setActiveSlot(slot: String): FastbootCommand = FastbootCommand("set_active:$slot")

        fun reboot(): FastbootCommand = FastbootCommand("reboot")

        fun rebootBootloader(): FastbootCommand = FastbootCommand("reboot-bootloader")

        fun boot(): FastbootCommand = FastbootCommand("boot")

        fun continueBooting(): FastbootCommand = FastbootCommand("continue")
    }
}