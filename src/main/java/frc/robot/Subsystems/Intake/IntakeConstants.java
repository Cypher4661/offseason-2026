package frc.robot.subsystems.Intake;

import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.SparkMaxConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class IntakeConstants {

  public static final TalonFXConfig openingMotorConfig = new TalonFXConfig(0, Canbus.Rio, "openingMotor")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withMaxPositionError(5)
        .withCurrent(40)
        .withVolts(12);
  public static final double openingMotorOffsetAngle = 0.0;

  public static final TalonFXConfig leftMotorConfig = new TalonFXConfig(0, Canbus.Rio, "leftMotor")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withCurrent(40)
        .withVolts(12);

  public static final TalonFXConfig rightMotorConfig = new TalonFXConfig(0, Canbus.Rio, "rightMotor")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withCurrent(40)
        .withVolts(12);

  public static final int encoderChannel = 9;

}
