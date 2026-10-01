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

    public static final AprilTagFieldLayout FIELD_LAYOUT =
            AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);

    public static final double FIELD_LENGTH = FIELD_LAYOUT.getFieldLength();
    public static final double FIELD_WIDTH = FIELD_LAYOUT.getFieldWidth();
    public static final double ALLIANCE_WIDTH = FIELD_LAYOUT.getTagPose(25).get().getX() - 0.4;
    public static final double HUB_X = FIELD_LAYOUT.getTagPose(21).get().getX();
    public static final double OBSTACLE_WIDTH = 2 * (HUB_X - ALLIANCE_WIDTH);

    public static final Translation2d BLUE_HUB = new Translation2d(HUB_X, FIELD_WIDTH / 2);
    public static final Translation2d RED_HUB = new Translation2d(FIELD_LENGTH - HUB_X, FIELD_WIDTH / 2);

    private static final double AUTO_END = 140;
    private static final double TRANSITION_END = 130;
    private static final double SHIFT_LENGTH = 25;
    private static final double ENDGAME_START = 30;

    public enum Shift {
        AUTO, TRANSITION, ALLIANCE, OTHER_ALLIANCE, END, NONE
    }

    public enum Area {
        ALLIANCE, OBSTACLE1, NETURAL, OBSTACLE2, OTHER_ALLIANCE
    }

    public enum State {
        NONE, INTAKE, SHOOT_HUB, SHOOT_DELIVERY, 
    }

    public enum CastomState {
        NONE, SHOOT_TEST, SHOOT_TOWER, EMERGENCY_STOP
    }

    public static Pose2d currentPose = null;
    public static ChassisSpeeds robotSpeeds = null;
    public static ChassisSpeeds fieldSpeeds = null;
    public static Translation2d HUB = BLUE_HUB;
    public static boolean isRed = false;
    public static boolean isAuto = false;
    public static double matchTime = 0;
    public static double singeltimer = 0;
    public static Shift shift = Shift.NONE;
    public static CastomState castomState = CastomState.NONE;
    public static State state = State.SHOOT_HUB;
    public static Area area = Area.ALLIANCE;
    public static double hubDistance = 0;
    public static double hubHeading = 0;
    public static Rotation2d hubRotation = null;

    public static final RobotDataCenter instance = new RobotDataCenter();

    private RobotDataCenter() {
        SmartDashboard.putData("Robot Data Center", this);
    }

    public static void setRobotPoseAndSpeeds(Pose2d pose, ChassisSpeeds speeds) {
        robotSpeeds = speeds;
        fieldSpeeds = ChassisSpeeds.fromRobotRelativeSpeeds(speeds, pose.getRotation());
        matchTime = Timer.getMatchTime();
        isAuto = DriverStation.isAutonomous();
        setPose(pose);

        if (isAuto) {
            shift = Shift.AUTO;
            singeltimer = matchTime;
            return;
        }

        if (matchTime > TRANSITION_END) {
            shift = Shift.TRANSITION;
            singeltimer = matchTime - TRANSITION_END;
            return;
        }

        if (matchTime <= ENDGAME_START && !isAuto) {
            shift = Shift.END;
            singeltimer = matchTime;
            return;
        }

       
        boolean firstAllianceRed = isRed;
        try {
            firstAllianceRed = DriverStation.getGameSpecificMessage().charAt(0) == 'R';
        } catch (Exception e) {
            SmartDashboard.putString("Game Specific Message", "Error: " + e.getMessage());
        }

        if (matchTime > 30 && matchTime < 130) {
           
            int intervalIndex = (int)((130 - matchTime) / 25); 
            boolean isFirstAllianceTurn = (intervalIndex % 2 == 0); 
            boolean isMyTurn = (isRed == firstAllianceRed) == isFirstAllianceTurn;

            shift = isMyTurn ? Shift.ALLIANCE : Shift.OTHER_ALLIANCE;
            singeltimer = (matchTime - 30) % 25;
        }


    }

    public static boolean canShootHub() {
        return castomState != CastomState.EMERGENCY_STOP;
    }

    public static boolean isHubActive() {
        return shift != Shift.OTHER_ALLIANCE;
    }

    public static void setPose(Pose2d pose) {
        currentPose = pose;
        double x = isRed ? FIELD_LENGTH - pose.getX() : pose.getX();

        if (x < ALLIANCE_WIDTH) area = Area.ALLIANCE;
        else if (x < ALLIANCE_WIDTH + OBSTACLE_WIDTH) area = Area.OBSTACLE1;
        else if (x < FIELD_LENGTH - ALLIANCE_WIDTH - OBSTACLE_WIDTH) area = Area.NETURAL;
        else if (x < FIELD_LENGTH - ALLIANCE_WIDTH) area = Area.OBSTACLE2;
        else area = Area.OTHER_ALLIANCE;

        hubDistance = pose.getTranslation().getDistance(HUB);
        hubRotation = HUB.minus(pose.getTranslation()).getAngle();
        hubHeading = hubRotation.getDegrees();
    }

    private void setAllianceColor(boolean isRed) {
        RobotDataCenter.isRed = isRed;
        HUB = isRed ? RED_HUB : BLUE_HUB;
    }

    public static boolean getIsRed() {
        return isRed;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Match Time", () -> matchTime, null);
        builder.addStringProperty("Shift", () -> shift.toString(), null);
        builder.addDoubleProperty("Shift Time", () -> singeltimer, null);
        builder.addStringProperty("Area", () -> area.toString(), null);
        builder.addDoubleProperty("Hub Distance", () -> hubDistance, null);
        builder.addDoubleProperty("Hub Heading", () -> hubHeading, null);
        builder.addBooleanProperty("Is Red", () -> isRed, this::setAllianceColor);
        builder.addStringProperty("State", () -> state.toString(), null);

        SendableChooser<CastomState> stateChooser = new SendableChooser<>();

        for (CastomState s : CastomState.values()) {
            if (s == CastomState.NONE) stateChooser.setDefaultOption(s.toString(), s);
            else stateChooser.addOption(s.toString(), s);
        }

        stateChooser.onChange(s -> castomState = s);
        SmartDashboard.putData("CustomState", stateChooser);
    }
}