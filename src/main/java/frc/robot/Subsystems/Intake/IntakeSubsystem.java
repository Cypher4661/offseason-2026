package frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.SparkMaxMotor;
import frc.demacia.utils.motors.TalonFXMotor;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class IntakeSubsystem extends SubsystemBase {

  private final MotorInterface openingMotor;
  private final MotorInterface leftMotor;
  private final MotorInterface rightMotor;

  private final DutyCycleEncoder encoder;

  private boolean isOpeningMotorCalibrated = false;

  public IntakeSubsystem() {
    openingMotor = new TalonFXMotor(IntakeConstants.openingMotorConfig);
    leftMotor = new TalonFXMotor(IntakeConstants.leftMotorConfig);
    rightMotor = new TalonFXMotor(IntakeConstants.rightMotorConfig);
    ((TalonFX) rightMotor).setControl(new Follower(IntakeConstants.leftMotorConfig.id, MotorAlignmentValue.Opposed));

    this.encoder = new DutyCycleEncoder(IntakeConstants.encoderChannel);
  }

  public void setOpeningMotorPower(double power) {
    openingMotor.setDuty(power);
  }

  public void setOpeningMotorPosition(double position) {
    openingMotor.setPositionVoltage(position);
  }

  public double getOpeningMotorPosition() {
    return openingMotor.getCurrentPosition();
  }

  public void setOpeningMotorEncoderPosition(double position) {
    openingMotor.setEncoderPosition(position);
  }

  public void calibrateOpeningMotorFromEncoder() {
    if (!isOpeningMotorCalibrated) {
      double angle = getEncoderAbsoluteAngle();
      openingMotor.setEncoderPosition(Math.toRadians(angle - IntakeConstants.openingMotorOffsetAngle));
      isOpeningMotorCalibrated = true;
    }
  }

  public void setLeftMotorPower(double power) {
    leftMotor.setDuty(power);
  }

  public double getEncoderAbsoluteAngle() {
    return (encoder.get() * 360.0) / 2;
  }

  @Override
  public void periodic() {
    calibrateOpeningMotorFromEncoder();
  }
}
