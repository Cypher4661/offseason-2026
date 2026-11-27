package frc.robot.subsystems.Shooter;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.demacia.utils.motors.MotorInterface;


public class KickerSubsystem extends SubsystemBase {

  private final MotorInterface kickerMotorRight;
  private final MotorInterface kickerMotorleft;

  public KickerSubsystem() {
    super();
    this.kickerMotorRight = ShooterConstants.kicker.KickerMotorRight;
    this.kickerMotorleft = ShooterConstants.kicker.KickerMotorLeft;
    ((TalonFX) this.kickerMotorRight).setControl(new Follower(ShooterConstants.kicker.kickerMotorLeftConfig.id, MotorAlignmentValue.Opposed));
    
    SmartDashboard.putNumber("kicker-Power", 0.0);
    new InstantCommand( () -> { setKickerPower(SmartDashboard.getNumber("kicker-Power", 0.0));}).schedule();
    SmartDashboard.putData("kicker", this);
  }

  public void feed() {
    kickerMotorleft.setDuty(ShooterConstants.kicker.KickerDuty);
  }

  public void stop() {
    kickerMotorleft.setDuty(0.0);
  }

  public void setKickerPower(double power) {
    kickerMotorleft.setDuty(power);
  }
  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("kicker-Power", () -> SmartDashboard.getNumber("kicker-Power", 0.0), this::setKickerPower);
  }
}

// @Override
// public void periodic() {

// }
