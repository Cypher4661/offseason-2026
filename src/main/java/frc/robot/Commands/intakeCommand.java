// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotDataCenter;
import frc.robot.subsystems.Intake.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class intakeCommand extends Command {
  /** Creates a new intake. */
  private final IntakeSubsystem intake;
  private final boolean openIntake;
  private final boolean spinIntake;
  public intakeCommand(IntakeSubsystem intake, boolean openIntake, boolean spinIntake) {
    this.intake = intake;
    this.openIntake = openIntake;
    this.spinIntake = spinIntake;

    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (openIntake) {
      intake.setOpeningMotorPosition(Math.toRadians(90));
      if (spinIntake) {
        intake.setLeftMotorPower(0.5);
        RobotDataCenter.state = RobotDataCenter.State.INTAKE;
      } else {
        intake.setLeftMotorPower(0);
      }
    } else {
      intake.setOpeningMotorPosition(Math.toRadians(0));
      intake.setLeftMotorPower(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
