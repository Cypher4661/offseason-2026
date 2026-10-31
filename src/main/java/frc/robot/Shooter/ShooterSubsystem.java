package frc.robot.Shooter;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.MotorInterface;
import frc.robot.RobotContainer;
import frc.robot.RobotDataCenter;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

public class ShooterSubsystem extends SubsystemBase {

  private final MotorInterface shooterMotorLeft1,
      shooterMotorRight1,
      shooterMotorLeft2,
      shooterMotorRight2 ,
      angleMotor;
  private final DutyCycleEncoder Encoder;
  public boolean isShooting = false, isCalibrated = false;
  public double distanceFromHub, targetVelocity, targetAngle, x, y;
  private double velocityTolerance = 0.1;

  public ShooterSubsystem() {
    this.angleMotor = ShooterConstants.Angle.angleMotor;
    this.shooterMotorLeft1 = ShooterConstants.Shooter.shooterMotorLeft1;
    this.shooterMotorRight1 = ShooterConstants.Shooter.shooterMotorRight1;
    ((TalonFX) this.shooterMotorRight1).setControl(new Follower(ShooterConstants.Shooter.shooterMotorLeft1Config.id, MotorAlignmentValue.Opposed));
    this.shooterMotorLeft2 = ShooterConstants.Shooter.shooterMotorLeft2;
    ((TalonFX) this.shooterMotorLeft2).setControl(new Follower(ShooterConstants.Shooter.shooterMotorLeft1Config.id, MotorAlignmentValue.Aligned));
    this.shooterMotorRight2 = ShooterConstants.Shooter.shooterMotorRight2;
    ((TalonFX) this.shooterMotorRight2).setControl(new Follower(ShooterConstants.Shooter.shooterMotorLeft1Config.id, MotorAlignmentValue.Opposed));
    Encoder = new DutyCycleEncoder(0);

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

  public void stop() {
     if (stopShooter() == true) {
    shooterMotorLeft1.stop();
    angleMotor.stop();
    isShooting = false;}
  }
    public boolean stopShooter() {
    return SmartDashboard.getBoolean("stop", false);
  }
  public boolean isAtTargetVelocityLookUp(double targetVelocity) {
    double currentVelocity = shooterMotorLeft1.getCurrentVelocity();
    this.targetVelocity = targetVelocity;
    return Math.abs(currentVelocity - targetVelocity) <= ShooterConstants.Shooter.tolaranceVelocity;
  }

  public boolean isAtTargetVelocity() {
    double currentVelocity = shooterMotorLeft1.getCurrentVelocity();
    return Math.abs(currentVelocity - this.targetVelocity) <= ShooterConstants.Shooter.tolaranceVelocity;
  }
  public double getCurrentVelocity() {
    return shooterMotorLeft1.getCurrentVelocity();
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
public boolean attargetAngleLookUp(double targetAngle) {
    return Math.abs(getAngleDeg() - targetAngle) <= ShooterConstants.Angle.toleranceAngle;
  }

  public boolean attargetAngle() {
    return Math.abs(getAngleDeg() - getTargetAngle()) <= ShooterConstants.Angle.toleranceAngle;
  }

  public void makeAngleToZero(double knownAngle) {
    angleMotor.setEncoderPosition(Math.toRadians(knownAngle));
  }

  public double getAbsoluteAngle() {
    return Math.toDegrees(Math.toRadians(Encoder.get() * 360.0) * (19.0 / 184.0));
  }

  public void calibrateFromEncoder() {
    if (isCalibrated == false) {
      makeAngleToZero((getAbsoluteAngle() - ShooterConstants.Angle.offsetAngle));
      isCalibrated = true;
    }

  }

  public boolean isAnsEncoderConnected() {
    boolean isConnected = Encoder.isConnected();

    return isConnected;
  }

  public double getTargetAngle() {
    return SmartDashboard.getNumber("target angle", 65.0);
  }

  public double getDistanceFromHub() {
return RobotDataCenter.hubDistance ;
  }

  public double getRobotX() {
    return x;
  }

  public double getRobotY() {
    return y;
  }
  
  @Override
  public void periodic() {
  calibrateFromEncoder();
    getDistanceFromHub();
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

     builder.addDoubleProperty("CurrentVelocity", this::getCurrentVelocity, null);
    builder.addBooleanProperty("isAtTargetVelocity?", this::isAtTargetVelocity, null);
    builder.addBooleanProperty("isAtTargetAngle?", this::attargetAngle, null);
    builder.addDoubleProperty("CurrentAngle", this::getAngleDeg, null);
    builder.addDoubleProperty("AbsoluteAngle", this::getAbsoluteAngle, null);
    builder.addBooleanProperty("EncoderConnected", this::isAnsEncoderConnected, null);
    builder.addDoubleProperty("Distance from Hub", this::getDistanceFromHub, null);
    builder.addBooleanProperty("stopShooter?", this::stopShooter, null);
  }
}

