// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

/** Add your docs here. */
public class Camera {
    private String name;
    private Translation3d botPose;
    private String tableName;
    private Rotation3d rotation;

    public Camera(String name, Translation3d botPose, Rotation3d rotation, String tableName) {
        this.name = name;
        this.botPose = botPose;
        this.rotation = rotation;
        this.tableName = "limelight-" + tableName;
    }

    public Translation3d getbotPose() {
        return botPose;
    }

    public double getHeight() {
        return botPose.getZ();
    }

    public double getPitch() {
        return this.rotation.getX();
    }

    public double getYaw() {
        return this.rotation.getZ();
    }

    public Rotation3d getRotation() {
        return rotation;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }
}
