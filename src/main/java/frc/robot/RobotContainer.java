package frc.robot;

import frc.robot.Commands.Indexer.indexerKickerCommand;
import frc.robot.Commands.Intake.intakeCommand;
import frc.robot.Commands.Shooter.ShooterCommand;
import frc.robot.Commands.Swerve.ChassisDrive;
import frc.robot.Subsystems.Swerve.ChassisSubsystem;
import frc.robot.Subsystems.vision.Vision2DConfidence;
import frc.robot.Subsystems.vision.Vision2DSubsystem;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Subsystems.Shooter.ShooterSubsystem;
import frc.robot.Subsystems.Indexer.IndexerSubsystem;
import frc.robot.Subsystems.Intake.IntakeSubsystem;
import frc.robot.Subsystems.Shooter.KickerSubsystem;
public class RobotContainer implements Sendable{
  public static ChassisSubsystem chassis = new ChassisSubsystem();
  public CommandXboxController DriveController = new CommandXboxController(0);
  public static Vision2DSubsystem Vision2DBag;
  public static Vision2DConfidence visionConfidence;
  private final ShooterSubsystem shooter;
  private final KickerSubsystem kicker;
  private final IntakeSubsystem intake;
  private final IndexerSubsystem indexer;
  public static Robot robot;
  public static boolean iWantToShoot = false;
  public static double CYCLE_TIME = 0.02;
  public static boolean openIntake = false;
  public static boolean spinIntake = false;
  private final SendableChooser<Command> autoChooser;
  




  public RobotContainer() {
    configureBindings();
      autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
    SmartDashboard.putData("Scheduler", CommandScheduler.getInstance());
    SmartDashboard.putData("RobotContainer", this);
    Vision2DBag = new Vision2DSubsystem(Constants.VisionConstants.flywheelCameraBag);
    visionConfidence = new Vision2DConfidence(Vision2DBag, Vision2DBag, chassis);
    shooter = new ShooterSubsystem();
    kicker = new KickerSubsystem();
    intake = new IntakeSubsystem();
    indexer = new IndexerSubsystem();
    chassis.setDefaultCommand(new ChassisDrive(chassis, DriveController));
    shooter.setDefaultCommand(new ShooterCommand(shooter));
    indexer.setDefaultCommand(new indexerKickerCommand(indexer, kicker));
    intake.setDefaultCommand(new intakeCommand(intake, openIntake, spinIntake));

    
  }

  private void configureBindings() {
    DriveController.rightBumper().toggleOnTrue(new InstantCommand(() -> {iWantToShoot = !iWantToShoot;}));
    DriveController.leftBumper().toggleOnTrue(new InstantCommand(() -> {openIntake = !openIntake;}));
    DriveController.a().toggleOnTrue(new InstantCommand(() -> {spinIntake = !spinIntake;}));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("RobotContainer");
    builder.addBooleanProperty("isRed", () -> RobotDataCenter.getIsRed(), null);
    builder.addStringProperty("iWantToShoot", () -> String.valueOf(iWantToShoot), null);
    builder.addStringProperty("openIntake", () -> String.valueOf(openIntake), null);
    builder.addStringProperty("spinIntake", () -> String.valueOf(spinIntake), null);

  }
}


