package frc.robot.Shooter;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.SparkMaxMotor;
import frc.demacia.utils.motors.TalonFXMotor;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

public class ShooterSubsystem extends SubsystemBase {

  private final MotorInterface shooterMotorLeft1,
                               shooterMotorRight1, 
                               shooterMotorLeft2,
                               shooterMotorRight2;

  public ShooterSubsystem() {
    this.shooterMotorLeft1 = new TalonFXMotor(ShooterConstants.shooterMotorLeft1Config);
    this.shooterMotorRight1 = new TalonFXMotor(ShooterConstants.shooterMotorRight1Config);
    ((TalonFX) this.shooterMotorRight1).setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Opposed));
    this.shooterMotorLeft2 = new TalonFXMotor(ShooterConstants.shooterMotorLeft2Config);
    ((TalonFX) this.shooterMotorLeft2).setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Aligned));
    this.shooterMotorRight2 = new TalonFXMotor(ShooterConstants.shooterMotorRight2Config);
    ((TalonFX) this.shooterMotorRight2).setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Opposed));
  }

  public void setShooterMotorLeft1Power(double power) {
    this.shooterMotorLeft1.setDuty(power);
  }

  public void setShooterMotorLeft1Position(double position) {
    this.shooterMotorLeft1.setPositionVoltage(position);
  }

  public double getShooterMotorLeft1Position() {
    return this.shooterMotorLeft1.getCurrentPosition();
  }

  public void setShooterMotorLeft1EncoderPosition(double position) {
    this.shooterMotorLeft1.setEncoderPosition(position);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    //builder.addDoubleProperty("Left Position", this::getShooterMotorLeft1Position, null);
    //builder.addDoubleProperty("Absolute Encoder Angle", this::getAbsoluteEnAngle, null);
  }
}
