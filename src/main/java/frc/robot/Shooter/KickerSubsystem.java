package frc.robot.Shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.SparkMaxConfig;
import frc.demacia.utils.motors.SparkMaxMotor;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.Constants;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class KickerSubsystem extends SubsystemBase {

  private final TalonFXMotor KickerMotor;

  private static final double KickerDuty = 0.7;

  TalonFXConfig motorcConfig = new TalonFXConfig(ShooterConstants.kicker.motorId, Canbus.Rio, "kicker")
      .withRadiansMotor(ShooterConstants.kicker.gearRatio)
      .withBrake(ShooterConstants.kicker.isBrake)
      // .withInvert(ShooterConstants.kicker.isInverted)
      .withCurrent(ShooterConstants.kicker.currentLimit)
      .withPID(0, ShooterConstants.kicker.kP, ShooterConstants.kicker.kI, ShooterConstants.kicker.kD,
          ShooterConstants.kicker.kS, ShooterConstants.kicker.kV, ShooterConstants.kicker.kA,
          ShooterConstants.kicker.kG);

  public KickerSubsystem() {
    KickerMotor = new TalonFXMotor(motorcConfig);
    SmartDashboard.putNumber("kicker-Power", 0.0);

    SmartDashboard.putData("start-kicker", new InstantCommand(
        () -> {
          setKickerPower(SmartDashboard.getNumber("kicker-Power", 0.0));
        }, this));

  }

  public void feed() {
    KickerMotor.setDuty(KickerDuty);
  }

  public void stop() {
    KickerMotor.stop();
  }

  public void setKickerPower(double power) {
    KickerMotor.setDuty(power);
  }

}
