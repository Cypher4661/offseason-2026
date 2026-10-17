package frc.robot.Shooter;

import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.SparkMaxConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class ShooterConstants {

  public static final TalonFXConfig shooterMotorLeft1Config = new TalonFXConfig(0, Canbus.Rio, "shooterMotorLeft1")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withMaxPositionError(5)
        .withCurrent(40)
        .withVolts(12);

  public static final TalonFXConfig shooterMotorRight1Config = new TalonFXConfig(0, Canbus.Rio, "shooterMotorRight1")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withCurrent(40)
        .withVolts(12);

  public static final TalonFXConfig shooterMotorLeft2Config = new TalonFXConfig(0, Canbus.Rio, "shooterMotorLeft2")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withCurrent(40)
        .withVolts(12);

  public static final TalonFXConfig shooterMotorRight2Config = new TalonFXConfig(0, Canbus.Rio, "shooterMotorRight2")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withCurrent(40)
        .withVolts(12);

}
