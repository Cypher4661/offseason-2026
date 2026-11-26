package frc.robot;

import static frc.demacia.vision.utils.VisionConstants.inchToMeter;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import frc.robot.subsystems.Camera;

public final class Constants {


  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }


    public static class VisionConstants {

    public static final Camera flywheelCameraLeft = new Camera("tag", new Translation3d(), new Rotation3d(), "tag");
    public static final Camera flywheelCameraBag = new Camera("bag", new Translation3d(0,0,0), new Rotation3d(0,32.16,0), "bag");

    public static final double WORST_RELIABLE_DIST = 4.0; // max distance for reliable pose estimation in meters
    public static final double BEST_RELIABLE_DIST = 1.0; // best distance for reliable pose estimation in meters

    public static final Translation2d middleOfTheBlueHubLocation = new Translation2d(4.611624, 4.021328);

     private static final double OUTPOST_TAG_HEIGHT = inchToMeter(35.0); // Outpost tags (human player stations)
        private static final double HUB_TAG_HEIGHT = inchToMeter(44.25); // Hub scoring location tags
        private static final double DEPOT_TAG_HEIGHT = inchToMeter(21.75); // Depot perimeter tags

    public static final Translation2d[] O_TO_TAG = {
                        null, // 0 - unused
                        new Translation2d(inchToMeter(467.637), inchToMeter(292.314)), // 1
                        new Translation2d(inchToMeter(469.111), inchToMeter(182.6)), // 2
                        new Translation2d(inchToMeter(445.349), inchToMeter(172.844)), // 3
                        new Translation2d(inchToMeter(445.349), inchToMeter(158.844)), // 4
                        new Translation2d(inchToMeter(469.111), inchToMeter(135.088)), // 5
                        new Translation2d(inchToMeter(467.637), inchToMeter(25.374)), // 6
                        new Translation2d(inchToMeter(470.586), inchToMeter(25.374)), // 7
                        new Translation2d(inchToMeter(483.111), inchToMeter(135.088)), // 8
                        new Translation2d(inchToMeter(492.881), inchToMeter(144.844)), // 9
                        new Translation2d(inchToMeter(492.881), inchToMeter(158.844)), // 10
                        new Translation2d(inchToMeter(483.111), inchToMeter(182.6)), // 11
                        new Translation2d(inchToMeter(470.586), inchToMeter(292.314)), // 12
                        new Translation2d(inchToMeter(650.918), inchToMeter(291.469)), // 13
                        new Translation2d(inchToMeter(650.918), inchToMeter(274.469)), // 14
                        new Translation2d(inchToMeter(650.904), inchToMeter(170.219)), // 15
                        new Translation2d(inchToMeter(650.904), inchToMeter(153.219)), // 16
                        new Translation2d(inchToMeter(183.586), inchToMeter(25.374)), // 17
                        new Translation2d(inchToMeter(182.111), inchToMeter(135.088)), // 18
                        new Translation2d(inchToMeter(205.873), inchToMeter(144.844)), // 19
                        new Translation2d(inchToMeter(205.873), inchToMeter(158.844)), // 20
                        new Translation2d(inchToMeter(182.111), inchToMeter(182.6)), // 21
                        new Translation2d(inchToMeter(183.586), inchToMeter(292.314)), // 22
                        new Translation2d(inchToMeter(180.637), inchToMeter(292.314)), // 23
                        new Translation2d(inchToMeter(168.111), inchToMeter(182.6)), // 24
                        new Translation2d(inchToMeter(158.341), inchToMeter(172.844)), // 25
                        new Translation2d(inchToMeter(158.341), inchToMeter(158.844)), // 26
                        new Translation2d(inchToMeter(168.111), inchToMeter(135.088)), // 27
                        new Translation2d(inchToMeter(180.637), inchToMeter(25.374)), // 28
                        new Translation2d(inchToMeter(0.305), inchToMeter(26.219)), // 29
                        new Translation2d(inchToMeter(0.305), inchToMeter(43.219)), // 30
                        new Translation2d(inchToMeter(0.318), inchToMeter(147.469)), // 31
                        new Translation2d(inchToMeter(0.318), inchToMeter(164.469)) // 32
        };

        /**
         * Array of AprilTag rotation angles (yaw).
         * Each Rotation2d represents the angle the tag faces.
         * Index corresponds to AprilTag ID (0 is unused).
         * 
         * Updated for 2026 REEFSCAPE season.
         */
        public static final Rotation2d[] TAG_ANGLE = {
                        null, // 0 - unused
                        Rotation2d.fromDegrees(180), // 1
                        Rotation2d.fromDegrees(90), // 2
                        Rotation2d.fromDegrees(180), // 3
                        Rotation2d.fromDegrees(180), // 4
                        Rotation2d.fromDegrees(270), // 5
                        Rotation2d.fromDegrees(180), // 6
                        Rotation2d.fromDegrees(0), // 7
                        Rotation2d.fromDegrees(270), // 8
                        Rotation2d.fromDegrees(0), // 9
                        Rotation2d.fromDegrees(0), // 10
                        Rotation2d.fromDegrees(90), // 11
                        Rotation2d.fromDegrees(0), // 12
                        Rotation2d.fromDegrees(180), // 13
                        Rotation2d.fromDegrees(180), // 14
                        Rotation2d.fromDegrees(180), // 15
                        Rotation2d.fromDegrees(180), // 16
                        Rotation2d.fromDegrees(0), // 17
                        Rotation2d.fromDegrees(270), // 18
                        Rotation2d.fromDegrees(0), // 19
                        Rotation2d.fromDegrees(0), // 20
                        Rotation2d.fromDegrees(90), // 21
                        Rotation2d.fromDegrees(0), // 22
                        Rotation2d.fromDegrees(180), // 23
                        Rotation2d.fromDegrees(90), // 24
                        Rotation2d.fromDegrees(180), // 25
                        Rotation2d.fromDegrees(180), // 26
                        Rotation2d.fromDegrees(270), // 27
                        Rotation2d.fromDegrees(180), // 28
                        Rotation2d.fromDegrees(0), // 29
                        Rotation2d.fromDegrees(0), // 30
                        Rotation2d.fromDegrees(0), // 31
                        Rotation2d.fromDegrees(0) // 32
        };

        /**
         * Array of AprilTag heights from the ground.
         * Each value corresponds to the Z-coordinate from the CSV.
         * Index corresponds to AprilTag ID (0 is unused).
         * 
         * Updated for 2026 REEFSCAPE season.
         */
        public static final double[] TAG_HEIGHT = {
                        0, // 0 - unused
                        OUTPOST_TAG_HEIGHT, // 1 - 35" (Outpost)
                        HUB_TAG_HEIGHT, // 2 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 3 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 4 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 5 - 44.25" (Hub)
                        OUTPOST_TAG_HEIGHT, // 6 - 35" (Outpost)
                        OUTPOST_TAG_HEIGHT, // 7 - 35" (Outpost)
                        HUB_TAG_HEIGHT, // 8 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 9 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 10 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 11 - 44.25" (Hub)
                        OUTPOST_TAG_HEIGHT, // 12 - 35" (Outpost)
                        DEPOT_TAG_HEIGHT, // 13 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 14 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 15 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 16 - 21.75" (Depot)
                        OUTPOST_TAG_HEIGHT, // 17 - 35" (Outpost)
                        HUB_TAG_HEIGHT, // 18 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 19 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 20 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 21 - 44.25" (Hub)
                        OUTPOST_TAG_HEIGHT, // 22 - 35" (Outpost)
                        OUTPOST_TAG_HEIGHT, // 23 - 35" (Outpost)
                        HUB_TAG_HEIGHT, // 24 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 25 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 26 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 27 - 44.25" (Hub)
                        OUTPOST_TAG_HEIGHT, // 28 - 35" (Outpost)
                        DEPOT_TAG_HEIGHT, // 29 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 30 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 31 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT // 32 - 21.75" (Depot)
        };
  }
}