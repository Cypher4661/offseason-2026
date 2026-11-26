package frc.robot.subsystems.Shooter.Commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotDataCenter;
import frc.robot.subsystems.Shooter.ShooterSubsystem;
import frc.robot.subsystems.Shooter.KickerSubsystem;
// import frc.robot.Subsystems.led.ledSubsystem;
import frc.robot.subsystems.Shooter.ShooterConstants;

public class ShooterCommand extends Command {
  private final ShooterSubsystem shooter;
  KickerSubsystem kicker;
  // ledSubsystem led;
  private double speed = 10;
  private double angle = 60;
  double targetSpeed = 0;
  double targetAngle = 0;
  Pose2d pose;
  private boolean kickerON = false;
  ChassisSpeeds speeds;

  public ShooterCommand(ShooterSubsystem shooter, KickerSubsystem kicker) {
    super();
    // led.setColor(255, 165, 0);
    this.shooter = shooter;
    this.kicker = kicker;
    // this.led = led ;
    addRequirements(shooter, kicker);
    SmartDashboard.putData("Shooter Command Udi", this);
  }

  @Override
  public void execute() {
    // default values
    targetAngle = ShooterConstants.Angle.maximumDegree;
    if (RobotDataCenter.isAuto){
        targetSpeed = ShooterConstants.velocityLookup.distanceToVelocity.get(RobotDataCenter.hubDistance);
        targetAngle = ShooterConstants.ShooterLookup.distanceToAngleDeg.get(RobotDataCenter.hubDistance);
        kickerON = true;

    }
    else{
      switch (RobotDataCenter.state) {
        case SHOOT_HUB:
          if (RobotDataCenter.canShootHub()) {
            targetSpeed = ShooterConstants.velocityLookup.distanceToVelocity.get(RobotDataCenter.hubDistance);
            targetAngle = ShooterConstants.ShooterLookup.distanceToAngleDeg.get(RobotDataCenter.hubDistance);
            kickerON = true;
          
          }
          else if (RobotDataCenter.area != RobotDataCenter.Area.ALLIANCE){
            targetSpeed = 40;
            targetAngle = 45;
            kickerON = true;

          }
          else {
            targetSpeed = 0.0;
            targetAngle = 73.1;
            kickerON = false;

          }
          
          
          break;
        case SHOOT_DELIVERY:
          if (RobotDataCenter.area != RobotDataCenter.Area.ALLIANCE) {
            targetSpeed = 40;
            targetAngle = 45;
            kickerON = true;
          }
          break;
          
        case SHOOT_TOWER:
          if (RobotDataCenter.canShootHub()) {
            targetSpeed = 16;
            targetAngle = 66 ;
            kickerON = true;
          }
          break;
        case SHOOT_TEST:
          // always shoot with dashboard values, for testing purposes
          targetSpeed = speed;
          targetAngle = angle;
          kickerON = true;
          break;
        default:
          // set default values for other states, such as intake or climb
          break;
    }}

    shooter.setShooterVelocity(targetSpeed);
    shooter.setAngleDeg(targetAngle);
    if (kickerON) {
      kicker.feed();
    } else {
      kicker.stop();
    }
    ;

     if (RobotDataCenter.castomState == RobotDataCenter.CastomState.EMERGENCY_STOP) {
        shooter.stop();
        kicker.stop();
        return;
    }
  }

  @Override
  public void end(boolean interrupted) {
    shooter.setShooterVelocity(0);
    shooter.setAngleDeg(73);
    kicker.stop();
    // led.setColor(255, 255, 0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Target Velocity", () -> speed, (value) -> speed = value);
    builder.addDoubleProperty("Target Angle", () -> angle, (value) -> angle = value);
  }
}
