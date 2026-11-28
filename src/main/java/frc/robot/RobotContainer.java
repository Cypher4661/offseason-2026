package frc.robot;

import frc.robot.Commands.Swerve.ChassisDrive;
import frc.robot.Subsystems.Swerve.ChassisSubsystem;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer implements Sendable{
  public static ChassisSubsystem Chassis = new ChassisSubsystem();
  public CommandXboxController DriveController = new CommandXboxController(0);
  public static Robot robot;
  public static boolean isRed = false;
  public static boolean TuneToHub = false;
  public static double CYCLE_TIME = 0.02;

  public boolean isRed() {
    return isRed;
  }
  public boolean TuneToHub() {
    return TuneToHub;
  }

  public RobotContainer() {
    configureBindings();
    SmartDashboard.putData("Scheduler", CommandScheduler.getInstance());
    Chassis.setDefaultCommand(new ChassisDrive(Chassis, DriveController));
  }

  private void configureBindings() {
    DriveController.povDown().toggleOnTrue(new InstantCommand(() -> {
      TuneToHub = TuneToHub;
    }));
  }

  public Command getAutonomousCommand() {
    return Chassis.followAuto(null);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("RobotContainer");
    builder.addBooleanProperty("isRed", this::isRed, null);
  }
}
