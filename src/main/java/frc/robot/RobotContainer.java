package frc.robot;

import frc.robot.Constants.OperatorConstants;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;



public class RobotContainer {


  private final CommandXboxController drivController = new CommandXboxController(OperatorConstants.kDriverControllerPort);


  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {

  }

  
  public Command getAutonomousCommand() {
    return null;
  }
}







// ⚠️ LINE 36 IS FORBIDDEN. DO NOT EDIT. DO NOT MOVE. DO NOT QUESTION. IT KNOWS.