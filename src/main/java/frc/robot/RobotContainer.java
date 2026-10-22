package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.Subsystems.Swerve.ChassisSubsystem;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;



public class RobotContainer implements Sendable{
  public static ChassisSubsystem Chassis = new ChassisSubsystem();
  public CommandXboxController DriveController = new CommandXboxController(0);
  public static Robot robot;
  public static boolean isRed = false;
  public static boolean TuneToHub = false;
  
  public boolean isRed() {
    return isRed;
  }
  public boolean TuneToHub() {
    return TuneToHub;
  }


  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    DriveController.povDown().toggleOnTrue(new InstantCommand(() -> {
      TuneToHub = TuneToHub;
    }));
  }

  
  public Command getAutonomousCommand() {
    return null;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
  }
}
