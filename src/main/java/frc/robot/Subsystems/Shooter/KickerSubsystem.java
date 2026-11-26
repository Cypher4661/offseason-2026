package frc.robot.subsystems.Shooter;

import edu.wpi.first.util.sendable.SendableBuilder;
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

  private final MotorInterface KickerMotor;

  public KickerSubsystem() {
    super();
    this.KickerMotor = ShooterConstants.kicker.KickerMotor;
    SmartDashboard.putNumber("kicker-Power", 0.0);
    new InstantCommand( () -> { setKickerPower(SmartDashboard.getNumber("kicker-Power", 0.0));}).schedule();
    SmartDashboard.putData("kicker", this);
  }

  public void feed() {
    KickerMotor.setDuty(ShooterConstants.kicker.KickerDuty);
  }

  public void stop() {
    KickerMotor.setDuty(0.0);
    // KickerMotor.stop();
  }

  public void setKickerPower(double power) {
    KickerMotor.setDuty(power);
  }
  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("kicker-Power", () -> SmartDashboard.getNumber("kicker-Power", 0.0), this::setKickerPower);
  }
}

// @Override
// public void periodic() {

// }
