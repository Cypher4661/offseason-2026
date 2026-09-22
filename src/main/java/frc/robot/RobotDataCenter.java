package frc.robot;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotDataCenter implements Sendable {

    public static final RobotDataCenter instance = new RobotDataCenter();

    // Field
    public static final AprilTagFieldLayout FIELD_LAYOUT =
            AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);

    public static final double FIELD_LENGTH = FIELD_LAYOUT.getFieldLength();
    public static final double FIELD_WIDTH = FIELD_LAYOUT.getFieldWidth();

    public static final double ALLIANCE_WIDTH =
            FIELD_LAYOUT.getTagPose(25).get().getX() - 0.4;

    public static final double HUB_X =
            FIELD_LAYOUT.getTagPose(21).get().getX();

    public static final double OBSTACLE_WIDTH =
            2 * (HUB_X - ALLIANCE_WIDTH);

    public static final Translation2d BLUE_HUB =
            new Translation2d(HUB_X, FIELD_WIDTH / 2);

    public static final Translation2d RED_HUB =
            new Translation2d(FIELD_LENGTH - HUB_X, FIELD_WIDTH / 2);

    // Match
    private static final double TELEOP_START = 10;
    private static final double ENDGAME_START = 110;
    private static final double SHIFT_LENGTH = 25;

    public enum Shift {
        AUTO, TRANSITION, ALLIANCE, OTHER_ALLIANCE, END, NONE
    }

    public enum Area {
        ALLIANCE, OBSTACLE1, NETURAL, OBSTACLE2, OTHER_ALLIANCE
    }

    public enum State {
        NONE, INTAKE, SHOOT_HUB, SHOOT_TEST, SHOOT_TOWER, SHOOT_DELIVERY, CLIMB
    }

    // Robot data
    public static Pose2d currentPose = null;
    public static ChassisSpeeds robotSpeeds = null;
    public static ChassisSpeeds fieldSpeeds = null;

    public static Translation2d HUB = BLUE_HUB;

    public static boolean isRed = false;
    public static boolean isAuto = false;

    public static double matchTime = 0;
    public static Shift shift = Shift.NONE;
    public static State state = State.NONE;
    public static Area area = Area.ALLIANCE;

    public static double hubDistance = 0;
    public static double hubHeading = 0;
    public static Rotation2d hubRotation = null;

    private RobotDataCenter() {
        SmartDashboard.putData("Robot Data Center", this);
    }

    public static void setRobotPoseAndSpeeds(Pose2d pose, ChassisSpeeds speeds) {
        robotSpeeds = speeds;
        fieldSpeeds = ChassisSpeeds.fromRobotRelativeSpeeds(speeds, pose.getRotation());

        matchTime = Timer.getMatchTime();
        isAuto = DriverStation.isAutonomous();

        setPose(pose);

        // Match shift
        if (isAuto) {
            shift = Shift.AUTO;
        } else if (!DriverStation.isTeleop()) {
            shift = Shift.NONE;
        } else if (matchTime < TELEOP_START) {
            shift = Shift.TRANSITION;
        } else if (matchTime > ENDGAME_START) {
            shift = Shift.END;
        } else {
            String gameData = DriverStation.getGameSpecificMessage();

            if (gameData.isEmpty()) {
                shift = Shift.NONE;
                return;
            }

            boolean firstAllianceRed = false;
            try {
                firstAllianceRed = gameData.charAt(0) == 'R';
            } catch (Exception e) {
                firstAllianceRed = isRed;
                SmartDashboard.putString("Game Data Error", "no fms data, using alliance color: " + (isRed ? "Red" : "Blue"));
            }
            
            boolean allianceShift = ((int) ((matchTime - TELEOP_START) / SHIFT_LENGTH)) % 2 == 0;

            shift = allianceShift == (isRed == firstAllianceRed) ? Shift.ALLIANCE : Shift.OTHER_ALLIANCE;
        }
    }

    public static boolean canShootHub() {
        return state == State.SHOOT_TEST
                || ((state == State.SHOOT_HUB || state == State.SHOOT_TOWER)
                && shift != Shift.OTHER_ALLIANCE
                && area == Area.ALLIANCE);
    }

    public static boolean isHubActive() {
        return shift != Shift.OTHER_ALLIANCE;
    }

    public static void setPose(Pose2d pose) {
        currentPose = pose;

        double x = isRed ? FIELD_LENGTH - pose.getX() : pose.getX();

        if (x < ALLIANCE_WIDTH)
            area = Area.ALLIANCE;
        else if (x < ALLIANCE_WIDTH + OBSTACLE_WIDTH)
            area = Area.OBSTACLE1;
        else if (x < FIELD_LENGTH - ALLIANCE_WIDTH - OBSTACLE_WIDTH)
            area = Area.NETURAL;
        else if (x < FIELD_LENGTH - ALLIANCE_WIDTH)
            area = Area.OBSTACLE2;
        else
            area = Area.OTHER_ALLIANCE;

        hubDistance = pose.getTranslation().getDistance(HUB);
        hubRotation = HUB.minus(pose.getTranslation()).getAngle();
        hubHeading = hubRotation.getDegrees();
    }

    private void setAllianceColor(boolean isRed) {
        RobotDataCenter.isRed = isRed;
        HUB = isRed ? RED_HUB : BLUE_HUB;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Match Time", () -> matchTime, null);
        builder.addStringProperty("Shift", () -> shift.toString(), null);
        builder.addStringProperty("Area", () -> area.toString(), null);
        builder.addDoubleProperty("Hub Distance", () -> hubDistance, null);
        builder.addDoubleProperty("Hub Heading", () -> hubHeading, null);
        builder.addBooleanProperty("Is Red", () -> isRed, this::setAllianceColor);

        SendableChooser<State> stateChooser = new SendableChooser<>();

        for (State s : State.values()) {
            if (s == State.NONE)
                stateChooser.setDefaultOption(s.toString(), s);
            else
                stateChooser.addOption(s.toString(), s);
        }

        stateChooser.onChange(s -> state = s);
        SmartDashboard.putData("State", stateChooser);
    }
}