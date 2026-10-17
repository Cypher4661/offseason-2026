package frc.robot.Shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.SparkMaxMotor;
import frc.demacia.utils.motors.TalonFXMotor;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

public class ShooterSubsystem extends SubsystemBase {

  private final MotorInterface shooterMotorLeft1;
  private final MotorInterface shooterMotorRight1;
  private final MotorInterface shooterMotorLeft2;
  private final MotorInterface shooterMotorRight2;

  public ShooterSubsystem() {
    shooterMotorLeft1 = new TalonFXMotor(ShooterConstants.shooterMotorLeft1Config);
    shooterMotorRight1 = new TalonFXMotor(ShooterConstants.shooterMotorRight1Config);
    ((TalonFX) shooterMotorRight1).setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Opposed));
    shooterMotorLeft2 = new TalonFXMotor(ShooterConstants.shooterMotorLeft2Config);
    ((TalonFX) shooterMotorLeft2).setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Aligned));
    shooterMotorRight2 = new TalonFXMotor(ShooterConstants.shooterMotorRight2Config);
    ((TalonFX) shooterMotorRight2).setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Opposed));
  }

  public void setShooterMotorLeft1Power(double power) {
    shooterMotorLeft1.setDuty(power);
  }

  public void setShooterMotorLeft1Position(double position) {
    shooterMotorLeft1.setPositionVoltage(position);
  }

  public double getShooterMotorLeft1Position() {
    return shooterMotorLeft1.getCurrentPosition();
  }

  public void setShooterMotorLeft1EncoderPosition(double position) {
    shooterMotorLeft1.setEncoderPosition(position);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
