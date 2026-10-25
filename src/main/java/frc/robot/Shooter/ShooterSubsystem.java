package frc.robot.Shooter;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.SparkMaxMotor;
import frc.demacia.utils.motors.TalonFXMotor;

import java.lang.module.ModuleDescriptor.Builder;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

public class ShooterSubsystem extends SubsystemBase {

  private final MotorInterface shooterMotorLeft1,
      shooterMotorRight1,
      shooterMotorLeft2,
      shooterMotorRight2;
  public boolean isShooting = false, isCalibrated = false;
  public double distanceFromHub, targetVelocity, targetAngle, x, y;
  private double velocityTolerance = 0.1;

  public ShooterSubsystem() {
    this.shooterMotorLeft1 = ShooterConstants.shooterMotorLeft1;
    this.shooterMotorRight1 = ShooterConstants.shooterMotorRight1;
    ((TalonFX) this.shooterMotorRight1)
        .setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Opposed));
    this.shooterMotorLeft2 = ShooterConstants.shooterMotorLeft2;
    ((TalonFX) this.shooterMotorLeft2)
        .setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Aligned));
    this.shooterMotorRight2 = ShooterConstants.shooterMotorRight2;
    ((TalonFX) this.shooterMotorRight2)
        .setControl(new Follower(ShooterConstants.shooterMotorLeft1Config.id, MotorAlignmentValue.Opposed));
    SmartDashboard.putData("Shooter", this);
  }

  public boolean IsOperating(boolean isOperating) {
    SmartDashboard.putBoolean("ShooterIsOperating", isOperating);
    if (isShooting) {
      setShooterPower(0.0);
      angleMotor.stop();
    }
    this.isShooting = isOperating;
    return isShooting;
  }

  public void stopShooter() {
    shooterMotorLeft1.stop();
    angleMotor.stop();
    isShooting = false;
  }

  public boolean isAtTargetVelocityLookUp(double targetVelocity) {
    double currentVelocity = shooterMotorLeft1.getCurrentVelocity();
    this.targetVelocity = targetVelocity;
    return Math.abs(currentVelocity - targetVelocity) <= ShooterConstants.tolaranceVelocity;
  }

  public boolean isAtTargetVelocity() {
    double currentVelocity = shooterMotorLeft1.getCurrentVelocity();
    return Math.abs(currentVelocity - this.targetVelocity) <= ShooterConstants.tolaranceVelocity;
  }

  public void setShooterPower(double power) {
    shooterMotorLeft1.setDuty(power);
  }

  public void setShooterVelocity(double speed) {
    this.targetVelocity = speed;
    shooterMotorLeft1.setVelocity(speed);
  }

  public double getAngleDeg() {
    return Math.toDegrees(angleMotor.getCurrentAngle());
  }

  public void setAngleDeg(double targetAngle) {
    targetAngle = safeAngleRange(targetAngle, ShooterConstants.Angle.minimumDegree,
        ShooterConstants.Angle.maximumDegree);
    angleMotor.setPositionVoltage(Math.toRadians(targetAngle));
  }

  private static double safeAngleRange(double targetAngle, double min, double max) {
    return Math.max(min, Math.min(max, targetAngle));
  }

  // public void setShooterMotorLeft1Power(double power) {
  // this.shooterMotorLeft1.setDuty(power);
  // }

  // public void setShooterMotorLeft1Position(double position) {
  // this.shooterMotorLeft1.setPositionVoltage(position);
  // }

  // public double getShooterMotorLeft1Position() {
  // return this.shooterMotorLeft1.getCurrentPosition();
  // }

  // public void setShooterMotorLeft1EncoderPosition(double position) {
  // this.shooterMotorLeft1.setEncoderPosition(position);
  // }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    // builder.addDoubleProperty("Left Position",
    // this::getShooterMotorLeft1Position, null);
    // builder.addDoubleProperty("Absolute Encoder Angle", this::getAbsoluteEnAngle,
    // null);
    builder.addDoubleProperty("TargetVelocity", () -> this.targetVelocity, this::setShooterVelocity);

    builder.addBooleanProperty("ShooterIsOperating", () -> this.isShooting, this::IsOperating);
    // builder.addBooleanProperty("ShooterIsOperating", () -> this.IsOperating(this.isShooting), (value) -> this.isShooting = this.IsOperating(value));

  }
}
