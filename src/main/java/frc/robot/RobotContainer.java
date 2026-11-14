
package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.Shooter.ShooterSubsystem;
import frc.robot.Shooter.KickerSubsystem;
import frc.robot.Shooter.Commands.ShooterCommand;

public class RobotContainer {
    public static Robot robot;

    // Controllers
    private final CommandXboxController driverController = new CommandXboxController(0);

    // Subsystems
  private final ShooterSubsystem Shooter;
  private final KickerSubsystem Kicker;

    // Add other subsystems here

    // Constructor
    public RobotContainer() {
    Shooter = new ShooterSubsystem();
    Kicker = new KickerSubsystem();
          SmartDashboard.putData(CommandScheduler.getInstance());

        configureBindings();
    }

    // Controller bindings
    private void configureBindings() {
        driverController.y().whileTrue(
                new ShooterCommand(Shooter, Kicker)
        );
    }

    // Autonomous
    public Command getAutonomousCommand() {
        return null;
    }
}
