package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.Subsystems.Vision2DConfidence;
import frc.robot.Subsystems.Vision2DSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;



public class RobotContainer {


  private final CommandXboxController drivController = new CommandXboxController(OperatorConstants.kDriverControllerPort);
  public static Vision2DSubsystem Vision2DBag;
  public static Vision2DConfidence visionConfidence;


  public RobotContainer() {
    configureBindings();

  }

  private void configureBindings() {
    Vision2DBag = new Vision2DSubsystem(Constants.VisionConstants.flywheelCameraBag);
    visionConfidence = new Vision2DConfidence(Vision2DBag, Vision2DBag);
  }

  
  public Command getAutonomousCommand() {
    return null;
  }
}
