package frc.robot;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import frc.robot.Subsystems.Camera;

public final class Constants {


  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }


    public static class VisionConstants {

    public static final Camera flywheelCameraLeft = new Camera("tag", new Translation3d(), new Rotation3d(), "tag");
    public static final Camera flywheelCameraRight = new Camera("bag", new Translation3d(), new Rotation3d(), "bag");

    public static final double WORST_RELIABLE_DIST = 4.0; // max distance for reliable pose estimation in meters
    public static final double BEST_RELIABLE_DIST = 1.0; // best distance for reliable pose estimation in meters

    public static final Translation2d middleOfTheBlueHubLocation = new Translation2d(4.611624, 4.021328);
  }
}