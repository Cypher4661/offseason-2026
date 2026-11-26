package frc.robot;

import frc.robot.commands.Swerve.ChassisDrive;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;import frc.robot.subsystems.Vision2DConfidence;
import frc.robot.subsystems.Vision2DSubsystem;
import frc.robot.subsystems.Swerve.ChassisSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;



public class RobotContainer {


  private final CommandXboxController drivController = new CommandXboxController(Constants.OperatorConstants.kDriverControllerPort);


  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {

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
