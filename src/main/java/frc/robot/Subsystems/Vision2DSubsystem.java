// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import java.lang.module.ModuleDescriptor.Builder;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision2DSubsystem extends SubsystemBase {
  private Camera camera;
  //private Field2d CamField = new Field2d();
  private NetworkTable table;
  private AHRS gyro;
  private LimelightHelpers.PoseEstimate visionEstimate;
  private double[] botStats2d;
  public Pose2d pose = new Pose2d();
  
  public Vision2DSubsystem(Camera camera) {
    super();
    this.camera = camera;
    table = NetworkTableInstance.getDefault().getTable(camera.getTableName());
    botStats2d = table.getEntry("t2d").getDoubleArray(new double[16]);

    // Initialize and reset the NavX gyro
    gyro = new AHRS(NavXComType.kMXP_SPI);
    gyro.reset();

    // Set the camera's 3D position relative to the robot's physical center
    LimelightHelpers.setCameraPose_RobotSpace(
        camera.getTableName(), 
        camera.getbotPose().getX(),  // X:  meters forward from the robot center
        camera.getbotPose().getY(),   // Y: meters left from the robot center
        camera.getbotPose().getZ(),  // Z:  meters off the ground
        camera.getRotation().getX(),   // Roll (degrees)
        camera.getRotation().getY(),  // Pitch (degrees tilted upward)
        camera.getRotation().getZ()    // Yaw (degrees facing straight forward)
    );

    // Push the field object to the dashboard
    visionEstimate = getVisionPose();
    if (visionEstimate != null && visionEstimate.tagCount >= 1) {
      pose = visionEstimate.pose;
    }
    SmartDashboard.putData("Vision camera - " + camera.getTableName(), this);
  }

  
  public void periodic() {
    // Grab the estimate using your preferred method
    visionEstimate = getVisionPose();
    botStats2d = table.getEntry("t2d").getDoubleArray(new double[16]);

    // Update the dashboard widget if we see at least 1 tag
    if (visionEstimate != null && visionEstimate.tagCount >= 1) {
      pose = visionEstimate.pose;
    }
  }


  public double getBotX() {
    if (visionEstimate != null) {
      return visionEstimate.pose.getX();
    }
    return 0.0;
  }

  public double getBotY() {
    if (visionEstimate != null) {
      return visionEstimate.pose.getY();
    }
    return 0.0;
  }

  public double getCameraToTagYaw() {
    return LimelightHelpers.getTX(camera.getTableName());
  }

  public double getCameraToTagPitch() {
    return LimelightHelpers.getTY(camera.getTableName());
  }

  public boolean getTV() {
    return LimelightHelpers.getTV(camera.getTableName());
  }

  public double getTagCount() {
    return LimelightHelpers.getTargetCount(camera.getTableName());
  }

  public double getAverageTagDistance() {    
    if (visionEstimate != null) {
      return visionEstimate.avgTagDist;
    }
    return 0.0;
  }

  public double getLatency() {
    if (botStats2d == null || botStats2d.length < 4) {
    return 0.0;
  }


  // Index 2 is target/pipeline latency (tl), Index 3 is capture latency (cl)
  return botStats2d[2] + botStats2d[3];
  }
  
  public LimelightHelpers.PoseEstimate getVisionPose() {
    return LimelightHelpers.getBotPoseEstimate_wpiBlue(camera.getTableName());
  }
  @Override
  public void initSendable(SendableBuilder builder) {
      // TODO Auto-generated method stub
      super.initSendable(builder);

      builder.addDoubleProperty("BotX", () -> LimelightHelpers.getBotPoseEstimate_wpiBlue(camera.getTableName()).pose.getX(), null);
      builder.addDoubleProperty("BotY", () -> LimelightHelpers.getBotPoseEstimate_wpiBlue(camera.getTableName()).pose.getY(), null);
      builder.addDoubleProperty("pitch", () -> getCameraToTagPitch(), null);
      builder.addDoubleProperty("yaw", () -> getCameraToTagYaw(), null);
      builder.addDoubleProperty("tagCount", () -> getTagCount(), null);
      builder.addDoubleProperty("avgTagDistance", () -> getAverageTagDistance(), null);
  }
}
