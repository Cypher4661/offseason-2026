// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands.Indexer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotDataCenter;
import frc.robot.Subsystems.Indexer.IndexerSubsystem;
import frc.robot.Subsystems.Shooter.KickerSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class indexerKickerCommand extends Command {
  IndexerSubsystem indexer;
  KickerSubsystem kicker;
  boolean iWantToShoot;
  public indexerKickerCommand(IndexerSubsystem indexer, KickerSubsystem kicker) {
    addRequirements(indexer, kicker);
    this.indexer = indexer;
    this.kicker = kicker;
    this.iWantToShoot = iWantToShoot;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    iWantToShoot = RobotContainer.iWantToShoot;
    if (iWantToShoot && RobotDataCenter.isReadyToShoot && RobotDataCenter.isTunind){
      indexer.setLeftMotorPower(0.5);
      kicker.setKickerPower(0.5);
    }
    else{
      indexer.setLeftMotorPower(0);
      kicker.stop();
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
