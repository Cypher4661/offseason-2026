package frc.robot.Subsystems.Swerve;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;
import java.io.IOException;
import org.json.simple.parser.ParseException;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.demacia.utils.sensors.Pigeon;
import frc.demacia.utils.sensors.PigeonConfig;
import frc.robot.RobotContainer;
import frc.robot.RobotDataCenter;

public class ChassisSubsystem extends SubsystemBase {
    private SwerveDrivePoseEstimator PoseEstimator;
    public SwerveDriveKinematics Kinematics;
    public CommandXboxController Controller;
    public final SwerveModule[] modules;
    public Field2d Field = new Field2d();
    public Pose2d pose = new Pose2d();
    public boolean PreparingToShoot = false;
    public boolean PrecisionMode = false;
    public Translation2d HUB;
    public SwerveModule FR;
    public SwerveModule FL;
    public SwerveModule BL;
    public SwerveModule BR;
    public Pigeon Gyro;

    public ChassisSubsystem() {
        super();
        FR = new SwerveModule(SwerveConstants.ChassisConstants.ModulesConfig[0]);
        FL = new SwerveModule(SwerveConstants.ChassisConstants.ModulesConfig[1]);
        BL = new SwerveModule(SwerveConstants.ChassisConstants.ModulesConfig[2]);
        BR = new SwerveModule(SwerveConstants.ChassisConstants.ModulesConfig[3]);
        modules = new SwerveModule[]{FR, FL, BL, BR};

        Kinematics = new SwerveDriveKinematics(new Translation2d[] {
            new Translation2d(SwerveConstants.ChassisConstants.X_Y, -SwerveConstants.ChassisConstants.X_Y),
            new Translation2d(SwerveConstants.ChassisConstants.X_Y, SwerveConstants.ChassisConstants.X_Y),
            new Translation2d(-SwerveConstants.ChassisConstants.X_Y, SwerveConstants.ChassisConstants.X_Y),
            new Translation2d(-SwerveConstants.ChassisConstants.X_Y, -SwerveConstants.ChassisConstants.X_Y)
        });

        Gyro = new Pigeon(new PigeonConfig(SwerveConstants.ChassisConstants.GyroID, 
        SwerveConstants.ChassisConstants.cypherCANivore, "Chassis_Gyro"));

        PoseEstimator = new SwerveDrivePoseEstimator(Kinematics, getGyroAngle(), getModulePositions(), pose);

        SmartDashboard.putData("Chassis", this);
        SmartDashboard.putData("Field", Field);
        SmartDashboard.putData("Stpo Chassis", new InstantCommand(()-> stopChassis(), this));
        SmartDashboard.putData("set Brake", new InstantCommand(() -> setBrake(true)).ignoringDisable(true));
        SmartDashboard.putData("Chassis set Coast Command", new InstantCommand(() -> setCoast(true)).ignoringDisable(true));
        //SmartDashboard.putData("Set 90 Degrees", new RunCommand(() -> setAllModulesTo(0.5 * Math.PI), this));
        //SmartDashboard.putData("Set 0 Degrees", new RunCommand(() -> setAllModulesTo(0), this));
        //SmartDashboard.putData("Set 95 Degrees", new RunCommand(() -> setAllModulesTo(Math.toRadians(95)), this));
        //SmartDashboard.putData("Set position to 1 on 4", new InstantCommand(() -> resetPose(new Pose2d(1, 4, getHeading()))));

        RobotConfig config = null;
        try {
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            // Handle exception as needed
            e.printStackTrace();
        }

        // Configure AutoBuilder last
        AutoBuilder.configure(
                this::getPose, // Robot pose supplier
                this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
                this::getChassisSpeedsRobotRel, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                (speeds, feedforwards) -> setvelocities(speeds), // Method that will drive the robot given ROBOT
                                                                      // RELATIVE ChassisSpeeds. Also optionally outputs
                                                                      // individual module feedforwards
                new PPHolonomicDriveController( 
                        new PIDConstants(3, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(3, 0.0, 0.0) // Rotation PID constants
                ),
                config, // The robot configuration
                () -> { 
                    // Boolean supplier that controls when the path will be mirrored for the red
                    // alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                this // Reference to this subsystem to set requirements
        );
    }

    public Command followPath(String pathName) {
        try {
            return AutoBuilder.followPath(PathPlannerPath.fromPathFile(pathName));
        } catch (FileVersionException e) {
            e.printStackTrace();
            return new InstantCommand();
        } catch (IOException e) {
            e.printStackTrace();
            return new InstantCommand();
        } catch (ParseException e) {
            e.printStackTrace();
            return new InstantCommand();
         }
    }
    public Command followAuto(String autoName) {
        try {
            return AutoBuilder.buildAuto(autoName);
        } catch (FileVersionException e) {
            e.printStackTrace();
            return new InstantCommand();
        } 
    }

    // set commands
    public void setBrake(boolean brake) {
        for (SwerveModule module : modules) {
            module.setNeutralMode(brake);
        }
    }
    public void setCoast(boolean coast) {
        for (SwerveModule module : modules) {
            module.setNeutralMode(!coast);
        }
    }
    public void stopChassis() {
        for (SwerveModule module : modules) {
            module.stopModule();
        }
    }
    public void setZeroHeading() {
        resetPose(new Pose2d(pose.getTranslation(), Rotation2d.kZero));
    }
    public void setModuleVelocity( double vel) {
        for (SwerveModule module : modules) {
            module.setDriveVelocity(vel);
        }
    }
    public void setAllModulesTo(double angleInRadian) {
        for (SwerveModule module : modules) {
            module.setSteerAngle(angleInRadian);
        }
    }
    public void setDrivePower(double power) {
        for (SwerveModule module : modules) {
            module.setDrivePower(power);
        }
    }
    public void setSteerPosition(double angleInRadian) {
        for (SwerveModule module : modules) {
            module.setSteerAngle(angleInRadian);
        }
    }
    public void setModuleStates(SwerveModuleState[] states) {
        SwerveDriveKinematics.desaturateWheelSpeeds(states, SwerveConstants.ChassisConstants.Max_Linear_Speed);
        FR.setState(states[0]);
        FL.setState(states[1]);
        BL.setState(states[2]);
        BR.setState(states[3]);
    }
    public void setvelocities(ChassisSpeeds speeds) {
        if (PreparingToShoot) {
            Translation2d pos = new Translation2d(pose.getX(), pose.getY());
            if(isRed()){
                HUB = RobotDataCenter.RED_HUB;
            } else {
                HUB = RobotDataCenter.BLUE_HUB;
            }
            Translation2d toHub = HUB.minus(pos);

            double wanted_angle = toHub.getAngle().minus(getHeading()).getRadians();

            speeds.omegaRadiansPerSecond = wanted_angle * 2;
        }
        if (Math.abs(speeds.vxMetersPerSecond) < 0.01 && Math.abs(speeds.vyMetersPerSecond) < 0.01
                && Math.abs(speeds.omegaRadiansPerSecond) < 0.01) {
            for (SwerveModule m : modules) {
                m.setStop();
            }
        } else {
            ChassisSpeeds rspeeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, getHeading());
            SwerveModuleState[] states = Kinematics.toSwerveModuleStates(rspeeds);
            // SwerveModuleState[] states =
            // demaciaKinematics.toSwerveModuleStatesWithLimit(speeds,
            // getChassisSpeedsFieldRel(), getHeading());
            setModuleStates(states);
        }
    }
     public void setvelocitiesRobotRel(ChassisSpeeds speeds) {
        if (Math.abs(speeds.vxMetersPerSecond) < 0.01 && Math.abs(speeds.vyMetersPerSecond) < 0.01
                && Math.abs(speeds.omegaRadiansPerSecond) < 0.01) {
            for (SwerveModule m : modules) {
                m.setStop();
            }
            return;
        }
        SwerveModuleState[] states = Kinematics.toSwerveModuleStates(speeds);
        setModuleStates(states);

    }
    public void setVelocityWithAccel(ChassisSpeeds wanted_Speeds) {
        Translation2d limitedVelocityVector = calculateVelocityWithAccel(wanted_Speeds.vxMetersPerSecond,
                wanted_Speeds.vyMetersPerSecond);
        ChassisSpeeds limitedVelocities = new ChassisSpeeds(limitedVelocityVector.getX(), limitedVelocityVector.getY(),
                wanted_Speeds.omegaRadiansPerSecond);
        setvelocities(limitedVelocities);
    }

    // get commands
    public SwerveModuleState[] getModuleStates() {
        return new SwerveModuleState[] {FR.getState(), FL.getState(), BL.getState(), BR.getState()};
    }
    public SwerveModulePosition[] getModulePositions() {
        return new SwerveModulePosition[] {FR.getModulePosition(), FL.getModulePosition(), BL.getModulePosition(), 
            BR.getModulePosition()};
    }
    public ChassisSpeeds getChassisSpeedsFieldRel() {
        return Kinematics.toChassisSpeeds(getModuleStates());
    }
    public ChassisSpeeds getChassisSpeedsRobotRel() {
        ChassisSpeeds fieldRelSpeeds = getChassisSpeedsFieldRel();
        return ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelSpeeds, getHeading());
    }
    public Rotation2d getGyroAngle() {
        return Rotation2d.fromRadians(Gyro.getCurrentYaw());
    }
    public Rotation2d getHeading() {
        return pose.getRotation();
    }
    public Pose2d getPose() {
        return pose;
    }

    // more commands
    public void resetPose(Pose2d pose) {
        PoseEstimator.resetPosition(getGyroAngle(), getModulePositions(), pose);
    }
    public void addVisionMeasurement(Pose2d visionPose, double time) {
        PoseEstimator.addVisionMeasurement(visionPose, time);
        pose = PoseEstimator.getEstimatedPosition();
        Field.setRobotPose(pose);
        SmartDashboard.putString("VisionUpdated",
                visionPose.getX() + "/" + visionPose.getY() + "/" + visionPose.getRotation().getDegrees() +
                        " pose=" + pose.getX() + "/" + pose.getY() + "/" + pose.getRotation().getDegrees());
    }
    public Translation2d calculateVelocityWithAccel(double wanted_Vx, double wanted_Vy) {
        ChassisSpeeds currentSpeeds = getChassisSpeedsFieldRel();
        double current_Vx = currentSpeeds.vxMetersPerSecond;
        double current_Vy = currentSpeeds.vyMetersPerSecond;

        double X_Accel = (wanted_Vx - current_Vx) / 0.02;
        double Y_Accel = (wanted_Vy - current_Vy) / 0.02;

        if (Math.abs(X_Accel) > SwerveConstants.ChassisConstants.Max_Linear_Acceleration) {
            X_Accel = Math.signum(X_Accel) * SwerveConstants.ChassisConstants.Max_Linear_Acceleration;
        }
        if (Math.abs(Y_Accel) > SwerveConstants.ChassisConstants.Max_Linear_Acceleration) {
            Y_Accel = Math.signum(Y_Accel) * SwerveConstants.ChassisConstants.Max_Linear_Acceleration;
        }

        return new Translation2d(current_Vx + X_Accel * 0.02, current_Vy + Y_Accel * 0.02);
    }
    public boolean isRed() {
        return RobotContainer.isRed;
    }

    @Override
    public void periodic() {
        Rotation2d gyro = getGyroAngle();
        PoseEstimator.update(gyro, getModulePositions());
        pose = PoseEstimator.getEstimatedPosition();
        Field.setRobotPose(pose);
        PreparingToShoot = RobotContainer.TuneToHub;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Heading", () -> getHeading().getDegrees(), null);
        builder.addDoubleProperty("Gyro", () -> getGyroAngle().getDegrees(), null);
    }

}
