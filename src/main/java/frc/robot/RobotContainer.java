package frc.robot;

import frc.robot.commands.Swerve.ChassisDrive;
import frc.robot.subsystems.Swerve.ChassisSubsystem;
import frc.robot.subsystems.vision.Vision2DConfidence;
import frc.robot.subsystems.vision.Vision2DSubsystem;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Shooter.ShooterSubsystem;
import frc.robot.Shooter.KickerSubsystem;
import frc.robot.Shooter.Commands.ShooterCommand;
public class RobotContainer implements Sendable{
  public static ChassisSubsystem Chassis = new ChassisSubsystem();
  public CommandXboxController DriveController = new CommandXboxController(0);
  public static Vision2DSubsystem Vision2DBag;
  public static Vision2DConfidence visionConfidence;
  private final ShooterSubsystem Shooter;
  private final KickerSubsystem Kicker;
  public static Robot robot;
  public static boolean isRed = false;
  public static boolean TuneToHub = false;
  public static double CYCLE_TIME = 0.02;


  public boolean TuneToHub() {
    return TuneToHub;
  }

  public RobotContainer() {
    configureBindings();
    SmartDashboard.putData("Scheduler", CommandScheduler.getInstance());
    Vision2DBag = new Vision2DSubsystem(Constants.VisionConstants.flywheelCameraBag);
    visionConfidence = new Vision2DConfidence(Vision2DBag, Vision2DBag);
    Shooter = new ShooterSubsystem();
    Kicker = new KickerSubsystem();
    Chassis.setDefaultCommand(new ChassisDrive(Chassis, DriveController));
  }

  private void configureBindings() {

    DriveController.povDown().toggleOnTrue(new InstantCommand(() -> {TuneToHub = !TuneToHub;}));
    DriveController.y().toggleOnTrue(new ShooterCommand(Shooter, Kicker));
  }

  public Command getAutonomousCommand() {
    return Chassis.followAuto(null);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("RobotContainer");
    builder.addBooleanProperty("isRed", () -> RobotDataCenter.getIsRed(), null);
  }
}


