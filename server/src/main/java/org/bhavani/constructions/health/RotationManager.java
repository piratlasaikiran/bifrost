package org.bhavani.constructions.health;

import com.google.inject.Singleton;

@Singleton
public class RotationManager {
    private volatile boolean status = false;

    public RotationManager() {
    }

    public boolean status() {
        return this.status;
    }

    public void bir() {
        this.status = true;
    }

    public void oor() {
        this.status = false;
    }
}
