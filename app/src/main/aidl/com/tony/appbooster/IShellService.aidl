// IShellService.aidl
package com.tony.appbooster;

/**
 * AIDL interface for Shizuku-based shell command execution.
 * This service runs with elevated privileges (shell UID).
 */
interface IShellService {
    /**
     * Executes a shell command and returns the result.
     *
     * @param command The shell command to execute.
     * @return Array containing [exitCode, stdout, stderr]
     */
    String[] executeCommand(String command);

    /**
     * Destroys the service and releases resources.
     */
    void destroy();
}
