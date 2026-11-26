// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.lang.module.ModuleDescriptor.Builder;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.vision.utils.LimelightHelpers;
import frc.demacia.vision.utils.VisionConstants;
import frc.robot.Constants;

public class Vision2DSubsystem extends SubsystemBase {
  private Camera camera;
  //private Field2d CamField = new Field2d();
  private NetworkTable table;
  private AHRS gyro;
  private Translation2d visionEstimate;
  private double[] botStats2d;
  public Pose2d pose = new Pose2d();

  private double camToTagYaw;
  private double camToTagPitch;
  private int id;

  private Translation2d cameraToTag;

  // vector for robot
  private Translation2d robotToTag;
  private Translation2d originToRobot;

  // vector for tag
  private Translation2d origintoTag;

  private double dist;
  private double alpha;
  private double height;
  
  public Vision2DSubsystem(Camera camera) {
    super();
    this.camera = camera;
    table = NetworkTableInstance.getDefault().getTable(camera.getTableName());
    botStats2d = table.getEntry("t2d").getDoubleArray(new double[16]);

    // Initialize and reset the NavX gyro
    gyro = new AHRS(NavXComType.kMXP_SPI);
    gyro.reset();

    // Push the field object to the dashboard
    visionEstimate = getVisionPose();
    if (visionEstimate != null) {
      pose = new Pose2d(visionEstimate.getX(), visionEstimate.getY(), new Rotation2d(0.0));
    }
    SmartDashboard.putData("Vision camera - " + camera.getTableName(), this);
  }

  
  public void cameraUpdate() {
    // Grab the estimate using your preferred method
    visionEstimate = getVisionPose();
    botStats2d = table.getEntry("t2d").getDoubleArray(new double[16]);
    if (botStats2d != null && botStats2d.length >= 4) {
      camToTagYaw = table.getEntry("tx").getDouble(0.0); // Index 0 is the horizontal angle to the target (tx)
      camToTagPitch = table.getEntry("ty").getDouble(0.0); // Index 1 is the vertical angle to the target (ty)
    }
    id = (int) table.getEntry("tid").getDouble(-1.0); // Get the ID of the detected tag
 
    

    // Update the dashboard widget if we see at least 1 tag
    if (visionEstimate != null && getTV()) {
      pose = new Pose2d(visionEstimate.getX(), visionEstimate.getY(), new Rotation2d(0.0));
    }
  }


  public double getBotX() {
    if (visionEstimate != null) {
      return visionEstimate.getX();
    }
    return 0.0;
  }

  public double getBotY() {
    if (visionEstimate != null) {
      return visionEstimate.getY();
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

  public double getLatency() {
    if (botStats2d == null || botStats2d.length < 4) {
    return 0.0;
  }


  // Index 2 is target/pipeline latency (tl), Index 3 is capture latency (cl)
  return botStats2d[2] + botStats2d[3];
  }

public Translation2d getVisionPose() {
    if (id < 0 || id >= VisionConstants.TAG_HEIGHT.length) {
      return null;
    }
    origintoTag = VisionConstants.O_TO_TAG[this.id == -1 ? 0 :  this.id];

    height = VisionConstants.TAG_HEIGHT[this.id];
    if (origintoTag != null) {

      originToRobot = origintoTag.minus(getRobotToTagFieldRel());

      return originToRobot;
    }
    return new Translation2d();

  }

  /**
   * Calculates vector from robot center to detected AprilTag
   * Accounts for camera offset from robot center
   * * @return Translation2d representing vector to tag
   */
  public Translation2d getRobotToTagFieldRel() {
    // Convert camera measurements to vector
    cameraToTag = new Translation2d(getDistFromCamera(),
        Rotation2d.fromDegrees(camToTagYaw + camera.getYaw()));
    // Add camera offset to get robot center to tag vector
    robotToTag = (camera.getbotPose().toTranslation2d()
        .plus(cameraToTag));
        //.rotateBy(Chassis.getInstance().getGyroAngle());
    return robotToTag;
  }

  public double getDistFromCamera() {
    if(id < 0){
      return 0.0;
    }

    double deltaHeight = Constants.VisionConstants.TAG_HEIGHT[id] - camera.getHeight();
    double alpha = Math.toRadians(camera.getPitch() + camToTagPitch);
    double distance = Math.abs(deltaHeight / Math.tan(alpha)) / Math.cos((Math.toRadians(camToTagYaw)));


    return distance;    
  }

  @Override
  public void initSendable(SendableBuilder builder) {
      // TODO Auto-generated method stub
      super.initSendable(builder);

      builder.addDoubleProperty("BotX", () -> pose.getX(), null);
      builder.addDoubleProperty("BotY", () -> pose.getY(), null);
      builder.addDoubleProperty("pitch", () -> getCameraToTagPitch(), null);
      builder.addDoubleProperty("yaw", () -> getCameraToTagYaw(), null);
      builder.addDoubleProperty("tagCount", () -> getTagCount(), null);
      builder.addDoubleProperty("distance from " + camera.getTableName(), () -> getDistFromCamera(), null);
      builder.addBooleanProperty(camera.getTableName() + " - Tv", () -> getTV(), null);
      builder.addIntegerProperty("Tag in view of " + camera.getTableName(), () ->  id, null);
  }
}
