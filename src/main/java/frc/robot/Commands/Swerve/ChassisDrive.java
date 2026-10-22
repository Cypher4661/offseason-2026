package frc.robot.Commands.Swerve;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.demacia.utils.DemaciaUtils;
import frc.robot.Subsystems.Swerve.ChassisSubsystem;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class ChassisDrive extends Command {
    private ChassisSubsystem chassis;
    private CommandXboxController controller;
    private ChassisSpeeds speeds;
    private double direction;
    private boolean isRed;
    private boolean UseStick;
    private boolean isInverse;

    public ChassisDrive(ChassisSubsystem chassis, CommandXboxController controller) {
        this.chassis = chassis;
        this.controller = controller;
        addRequirements(chassis);
        SmartDashboard.putBoolean("Use Stick", false);
        SmartDashboard.putBoolean("is Inverse", false);
    }

    @Override
    public void execute() {
        // Decide which direction the robot will drive
        UseStick = SmartDashboard.getBoolean("Use Stick", false);
        direction = DemaciaUtils.getIsRed() ? 1 : -1;
        double joyX = controller.getLeftY() * direction;
        double joyY = controller.getLeftX() * direction;

        // Calculate rotation 
        double rot = 0;
        if(UseStick){
             // Calcilate rotation from stick axes
            rot = isInverse ? controller.getRightX() : -controller.getRightX();
        } else {
             // Calculate rotation from trigger axes
            rot = controller.getLeftTriggerAxis() - controller.getRightTriggerAxis();
            //rot = controller.getRightTriggerAxis() - controller.getLeftTriggerAxis();
        }

        // calculate velocities based on joystick inputs and max speeds
        double velX = Math.pow(joyX, 2) * SwerveConstants.ChassisConstants.Max_Linear_Speed * Math.signum(joyX);
        double velY = Math.pow(joyY, 2) * SwerveConstants.ChassisConstants.Max_Linear_Speed * Math.signum(joyY);
        double velRot = Math.pow(rot, 2) * SwerveConstants.ChassisConstants.Max_Rotation_Speed * Math.signum(rot);
       
        // Create a new ChassisSpeeds object with the calculated velocities
        speeds = new ChassisSpeeds(velX, velY, velRot);
        chassis.setvelocities(speeds);
    }
}
