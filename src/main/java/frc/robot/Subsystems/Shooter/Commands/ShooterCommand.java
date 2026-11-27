package frc.robot.subsystems.Shooter.Commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotDataCenter;
import frc.robot.subsystems.Shooter.ShooterSubsystem;
import frc.robot.subsystems.Shooter.KickerSubsystem;
import frc.robot.subsystems.Shooter.ShooterConstants;

public class ShooterCommand extends Command {
  private final ShooterSubsystem shooter;

  private double speed = 10;
  private double angle = 60;
  double targetSpeed = 0;
  double targetAngle = 0;
  Pose2d pose;
  ChassisSpeeds speeds;
  private boolean iWantToShoot;

  public ShooterCommand(ShooterSubsystem shooter, boolean iWantToShoot) {
    super();
    this.shooter = shooter;
    this.iWantToShoot = iWantToShoot;
    addRequirements(shooter);
    SmartDashboard.putData("Shooter Command", this);
  }

  @Override
  public void execute() {
    if (RobotDataCenter.isAuto) {
      targetSpeed = ShooterConstants.velocityLookup.distanceToVelocity.get(RobotDataCenter.hubDistance);
      targetAngle = ShooterConstants.ShooterLookup.distanceToAngleDeg.get(RobotDataCenter.hubDistance);
      shooter.setShooterVelocity(targetSpeed);
      shooter.setAngleDeg(targetAngle);

    }
    else if (iWantToShoot) {
      switch (RobotDataCenter.state) {
        case SHOOT_HUB:
          if (RobotDataCenter.canShootHub()) {
            targetSpeed = ShooterConstants.velocityLookup.distanceToVelocity.get(RobotDataCenter.hubDistance);
            targetAngle = ShooterConstants.ShooterLookup.distanceToAngleDeg.get(RobotDataCenter.hubDistance);

          } 
          else {
            targetSpeed = 0.0;
            targetAngle = ShooterConstants.Angle.minimumDegree;

          }

          break;
        case SHOOT_DELIVERY:
          if (RobotDataCenter.area != RobotDataCenter.Area.ALLIANCE) {
            RobotDataCenter.hubDistance = RobotDataCenter.hubDistance + 1.5; 
            targetSpeed = ShooterConstants.velocityLookup.distanceToVelocity.get(RobotDataCenter.hubDistance);
            targetAngle = ShooterConstants.ShooterLookup.distanceToAngleDeg.get(RobotDataCenter.hubDistance);
          }
          break;

        case SHOOT_TOWER:
          if (RobotDataCenter.canShootHub()) {
            targetSpeed = 16;
            targetAngle = 66;

          }
          break;
        case SHOOT_TEST:

          targetSpeed = speed;
          targetAngle = angle;

          break;
        default:
          targetAngle = ShooterConstants.Angle.minimumDegree;
          targetSpeed = 0.0;
          break;
     
       
      }
      shooter.setShooterVelocity(targetSpeed);
      shooter.setAngleDeg(targetAngle);
      RobotDataCenter.isReadyToShoot = shooter.isAtTargetVelocityLookUp(targetSpeed) && shooter.attargetAngleLookUp(targetAngle);
    }
    else{
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
