/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-27
 * This file is part of Rise of Benum Tower Defense and starts the desktop (LWJGL3) application. 
 * Core based on default startup code from libGDX. https://libgdx.com/wiki/app/starter-classes-and-configuration 
 */

package io.github.towerDefense.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.github.towerDefense.Main;

public class Lwjgl3Launcher {
    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; 
        createApplication();
    }

    /** 
     * @return Lwjgl3Application
     */
    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    /** 
     * @return Lwjgl3ApplicationConfiguration
     */
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Rise of Benum Tower Defense"); // Sets the title of the window.

        config.setMaximized(true); // Automatically maximizes the window on startup.
        config.setResizable(true); // Allows the window to be resizable.

        config.useVsync(true); // Enables VSync to synchronize the frame rate with the monitor's refresh rate.
        config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);   // Sets the foreground FPS to the monitor's refresh rate plus one, ensuring smooth performance.

        config.setWindowIcon("benumTowerLogo-128.png", "benumTowerLogo-64.png", "benumTowerLogo-32.png", "benumTowerLogo-16.png");

        return config;
    }
}