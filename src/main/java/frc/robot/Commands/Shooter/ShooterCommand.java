package frc.robot.Commands.Shooter;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotDataCenter;
import frc.robot.Subsystems.Shooter.ShooterSubsystem;
import frc.robot.Subsystems.Shooter.KickerSubsystem;
import frc.robot.Subsystems.Shooter.ShooterConstants;

public class ShooterCommand extends Command {
  private final ShooterSubsystem shooter;

  private double speed = 10;
  private double angle = 60;
  double targetSpeed = 0;
  double targetAngle = 0;
  Pose2d pose;
  ChassisSpeeds speeds;
  private boolean iWantToShoot;

  public ShooterCommand(ShooterSubsystem shooter) {
    super();
    this.shooter = shooter;
    addRequirements(shooter);
    SmartDashboard.putData("Shooter Command", this);
  }

  @Override
  public void execute() {
    iWantToShoot = RobotDataCenter.iWantToShoot;
    if (RobotDataCenter.isAuto) {
      targetSpeed = ShooterConstants.velocityLookup.distanceToVelocity.get(RobotDataCenter.hubDistance);
      targetAngle = ShooterConstants.ShooterLookup.distanceToAngleDeg.get(RobotDataCenter.hubDistance);
      shooter.setShooterVelocity(targetSpeed);
      shooter.setAngleDeg(targetAngle);

    } else if (iWantToShoot) {
      switch (RobotDataCenter.castomState) {
        case NONE:
          if (RobotDataCenter.area == RobotDataCenter.Area.ALLIANCE) {
            if (RobotDataCenter.canShootHub()) {
              targetSpeed = ShooterConstants.velocityLookup.distanceToVelocity.get(RobotDataCenter.hubDistance);
              targetAngle = ShooterConstants.ShooterLookup.distanceToAngleDeg.get(RobotDataCenter.hubDistance);
              RobotDataCenter.state = RobotDataCenter.State.SHOOT_HUB;
            } else {
              targetSpeed = 0.0;
              targetAngle = ShooterConstants.Angle.minimumDegree;

            }
          } else {
            double dis = RobotDataCenter.hubDistance + 1.5;
            targetSpeed = ShooterConstants.velocityLookup.distanceToVelocity.get(dis);
            targetAngle = ShooterConstants.ShooterLookup.distanceToAngleDeg.get(dis);
            RobotDataCenter.state = RobotDataCenter.State.SHOOT_DELIVERY;
          }

          break;

        case SHOOT_TOWER:
          if (RobotDataCenter.canShootHub()) {
            targetSpeed = 16;
            targetAngle = 66;
            RobotDataCenter.state = RobotDataCenter.State.SHOOT_TOWER;
          }
          break;
        case SHOOT_TEST:

          targetSpeed = speed;
          targetAngle = angle;
          RobotDataCenter.state = RobotDataCenter.State.SHOOT_TEST;
          break;
        case EMERGENCY_STOP:
          shooter.stop();
          targetAngle = ShooterConstants.Angle.minimumDegree;
          targetSpeed = 0.0;
          RobotDataCenter.state = RobotDataCenter.State.NONE;
          break;
        default:
          targetAngle = ShooterConstants.Angle.minimumDegree;
          targetSpeed = 0.0;
          break;

      }
      shooter.setShooterVelocity(targetSpeed);
      shooter.setAngleDeg(targetAngle);
      RobotDataCenter.isReadyToShoot = shooter.isAtTargetVelocityLookUp(targetSpeed)
          && shooter.attargetAngleLookUp(targetAngle) && RobotDataCenter.iWantToShoot;
    } else {
      targetAngle = ShooterConstants.Angle.minimumDegree;
      targetSpeed = 0.0;
      shooter.setShooterVelocity(targetSpeed);
      shooter.setAngleDeg(targetAngle);
      RobotDataCenter.isReadyToShoot = false;
    }

  }

  @Override
  public void end(boolean interrupted) {

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
