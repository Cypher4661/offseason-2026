package frc.robot.Subsystems.Indexer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.SparkMaxMotor;
import frc.demacia.utils.motors.TalonFXMotor;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

public class IndexerSubsystem extends SubsystemBase {

  private final MotorInterface leftMotor;
  private final MotorInterface rightMotor;

  public IndexerSubsystem() {
    leftMotor = new TalonFXMotor(IndexerConstants.leftMotorConfig);
    rightMotor = new TalonFXMotor(IndexerConstants.rightMotorConfig);
    ((TalonFX) rightMotor).setControl(new Follower(IndexerConstants.leftMotorConfig.id, MotorAlignmentValue.Opposed));
  }

  public void setLeftMotorPower(double power) {
    leftMotor.setDuty(power);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
