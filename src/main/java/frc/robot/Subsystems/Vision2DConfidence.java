// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Vision2DConfidence extends SubsystemBase {
  /** Creates a new Vision2DConfidence. */
  public Vision2DConfidence() {}

  private Field2d camField;
  private double latency;
  private double confidence1;
  private double confidence2;
  public Pose2d pose;
  public Vision2DSubsystem camera1;
  public Vision2DSubsystem camera2;
  

  public Vision2DConfidence(Vision2DSubsystem camera1, Vision2DSubsystem camera2) {
    super();
    this.camera1 = camera1;
    this.camera2 = camera2;
    camField = new Field2d();
    pose = new Pose2d();
    SmartDashboard.putData("Vision", this);
    SmartDashboard.putData("Camera Field With Confidence", camField);
  }

  @Override
  public void periodic() {
    camera1.periodic();
    camera2.periodic();
    latency = (camera1.getLatency() + camera2.getLatency()) / 2.0;
    confidence1 = getConfidence(camera1);
    confidence2 = getConfidence(camera2);
    

    if ((camera1.getTV()) || (camera2.getTV())) {
      pose = getPose();
      if (pose.getX() == 0.0) {
        return;
      }
      camField.setRobotPose(pose);
      //chassis.addVisionMeasurement(pose, Timer.getFPGATimestamp() - latency / 1000.0 - 0.01);
    }

  }

  public double getConfidence(Vision2DSubsystem camera) {
    // get distance from robot to tag
    double distance = camera.getAverageTagDistance();

    // if dist is too big return 0
    if (distance > Constants.VisionConstants.WORST_RELIABLE_DIST || distance == 0.0) {
      return 0;
    }
    // if dist is close return high confidence
    if (distance <= Constants.VisionConstants.BEST_RELIABLE_DIST) {
      return 1.0;
    }

    // Calculate how far we are into the falloff range (0 to 1)
    double normalizedDist = (distance - Constants.VisionConstants.BEST_RELIABLE_DIST)
        / (Constants.VisionConstants.WORST_RELIABLE_DIST - Constants.VisionConstants.BEST_RELIABLE_DIST);

    // higher confidence for closer distances
    return Math.pow(1 - normalizedDist, 3);
  }

  public Pose2d getPose() {
    double confidence = confidence1 + confidence2;
    if (confidence < 0.1) {
      return new Pose2d();
    }
    double c1 = confidence1 / confidence;
    double c2 = confidence2 / confidence;
    return new Pose2d(camera1.pose.getX() * c1 + camera2.pose.getX() * c2,
        camera1.pose.getY() * c1 + camera2.pose.getY() * c2,
            new Rotation2d(camera1.pose.getRotation().getRadians() * c1 +
      camera2.pose.getRotation().getRadians() * c2));
    //chassis.getPose().getRotation()
  }



  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);

    builder.addDoubleProperty("BotX with confidence", () -> pose.getX(), null);
    builder.addDoubleProperty("BotY with confidence", () -> pose.getY(), null);
    builder.addDoubleProperty("BotRotation with confidence", () -> pose.getRotation().getDegrees(), null);
    builder.addBooleanProperty("Camera1 tv", () -> camera1.getTV(), null);
    builder.addBooleanProperty("Camera2 tv", () -> camera2.getTV(), null);
    builder.addDoubleProperty("Camera1 confidence", () -> confidence1, null);
    builder.addDoubleProperty("Camera2 confidence", () -> confidence2, null);
    builder.addDoubleProperty("Camera1 distance", () -> camera1.getAverageTagDistance(), null);
    builder.addDoubleProperty("Camera2 distance", () -> camera2.getAverageTagDistance(), null);
    builder.addDoubleProperty("Camera1 X", () -> camera1.pose.getX(), null);
    builder.addDoubleProperty("Camera1 Y", () -> camera1.pose.getY(), null);
    builder.addDoubleProperty("Camera2 X", () -> camera2.pose.getX(), null);
    builder.addDoubleProperty("Camera2 Y", () -> camera2.pose.getY(), null);
    builder.addDoubleProperty("Camera1 Rotation", () -> camera1.pose.getRotation().getDegrees(), null);
    builder.addDoubleProperty("Camera2 Rotation", () -> camera2.pose.getRotation().getDegrees(), null);

  }
}
