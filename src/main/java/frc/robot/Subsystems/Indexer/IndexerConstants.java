package frc.robot.subsystems.Indexer;

import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.SparkMaxConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class IndexerConstants {

  public static final TalonFXConfig leftMotorConfig = new TalonFXConfig(41, Canbus.Rio, "leftMotor")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withCurrent(40)
        .withVolts(12);

  public static final TalonFXConfig rightMotorConfig = new TalonFXConfig(42, Canbus.Rio, "rightMotor")
        .withBrake(true)
        .withInvert(false)
        .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        .withCurrent(40)
        .withVolts(12);

}
