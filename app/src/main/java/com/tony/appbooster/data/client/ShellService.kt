package com.tony.appbooster.data.client

import android.util.Log
import com.tony.appbooster.IShellService
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Shizuku UserService implementation that runs with elevated (shell UID) privileges.
 *
 * This service is started by Shizuku and runs in a separate process with the same
 * privileges as ADB shell. It can execute any shell command.
 *
 * Note: This class runs in Shizuku's process, not the app's process.
 */
class ShellService : IShellService.Stub() {

    companion object {
        private const val TAG = "ShellService"
    }

    override fun executeCommand(command: String): Array<String> {
        Log.d(TAG, "Executing: $command")

        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))

            val outputReader = BufferedReader(InputStreamReader(process.inputStream))
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))

            val output = outputReader.readText()
            val error = errorReader.readText()
            val exitCode = process.waitFor()

            outputReader.close()
            errorReader.close()
            process.destroy()

            Log.d(TAG, "Command completed: exitCode=$exitCode")

            arrayOf(exitCode.toString(), output.trim(), error.trim())
        } catch (e: Exception) {
            Log.e(TAG, "Command failed", e)
            arrayOf("-1", "", e.message ?: "Unknown error")
        }
    }

    override fun destroy() {
        Log.d(TAG, "ShellService destroyed")
    }
}
