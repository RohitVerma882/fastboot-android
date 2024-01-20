/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.rohitverma882.fastboot

class FastbootCommand private constructor(private val command: String) {
    override fun toString(): String {
        return command
    }

    companion object {
        fun getVar(variable: String): FastbootCommand = FastbootCommand("getvar:$variable")

        fun oem(vararg args: String): FastbootCommand =
            FastbootCommand("oem ${args.joinToString(" ")}")

        fun flashing(vararg args: String): FastbootCommand =
            FastbootCommand("flashing ${args.joinToString(" ")}")

        fun download(size: Int): FastbootCommand =
            FastbootCommand(String.format("download:%08x", size))

        fun flash(partition: String): FastbootCommand = FastbootCommand("flash:$partition")

        fun erase(partition: String): FastbootCommand = FastbootCommand("erase:$partition")

        fun setActiveSlot(slot: String): FastbootCommand = FastbootCommand("set_active:$slot")

        fun boot(): FastbootCommand = FastbootCommand("boot")

        fun continueBooting(): FastbootCommand = FastbootCommand("continue")

        fun shutdown(): FastbootCommand = FastbootCommand("shutdown")

        fun reboot(): FastbootCommand = FastbootCommand("reboot")

        private fun rebootTo(target: String): FastbootCommand = FastbootCommand("reboot-$target")

        fun rebootBootloader(): FastbootCommand = rebootTo("bootloader")

        fun rebootRecovery(): FastbootCommand = rebootTo("recovery")

        fun rebootFastboot(): FastbootCommand = rebootTo("fastboot")
    }
}