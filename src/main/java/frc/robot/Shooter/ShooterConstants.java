package frc.robot.Shooter;

import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.motors.SparkMaxConfig;
import frc.demacia.utils.motors.SparkMaxMotor;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.MotorInterface;

public class ShooterConstants {

      public static final class Shooter {
    public static final int leftMotorId = 20;
    public static final int shooterMotorId1 = 21;

    public static final String shooterName = "leftMotor";

    public static final boolean isShooterInverted = true;
    public static final boolean shooterBrake = false;

    public static final double maxCurrent = 40; 
    public static final double rampTime = 0.2;
    public static final double shooterMaxVoltage = 12.0;
    public static final double gearRatio = 1;
    public static final double diameter = 0.1016;
    public static final double tolaranceVelocity = 1.0;
    public static final double addShooterOffset = Math.sqrt(Math.pow(-0.0877, 2) + Math.pow(0.0101, 2));

      public static final TalonFXConfig shooterMotorLeft1Config = new TalonFXConfig(0, Canbus.Rio, "shooterMotorLeft1")
                  .withBrake(true)
                  .withInvert(false)
                  .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                  .withMaxPositionError(5)
                  .withCurrent(40)
                  .withVolts(12);
      public static final MotorInterface shooterMotorLeft1 = new TalonFXMotor(shooterMotorLeft1Config);

      public static final TalonFXConfig shooterMotorRight1Config = new TalonFXConfig(0, Canbus.Rio,
                  "shooterMotorRight1")
                  .withBrake(true)
                  .withInvert(false)
                  .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                  .withCurrent(40)
                  .withVolts(12);
      public static final MotorInterface shooterMotorRight1 = new TalonFXMotor(shooterMotorRight1Config);

      public static final TalonFXConfig shooterMotorLeft2Config = new TalonFXConfig(0, Canbus.Rio, "shooterMotorLeft2")
                  .withBrake(true)
                  .withInvert(false)
                  .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                  .withCurrent(40)
                  .withVolts(12);
      public static final MotorInterface shooterMotorLeft2 = new TalonFXMotor(shooterMotorLeft2Config);

      public static final TalonFXConfig shooterMotorRight2Config = new TalonFXConfig(0, Canbus.Rio,
                  "shooterMotorRight2")
                  .withBrake(true)
                  .withInvert(false)
                  .withPID(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                  .withCurrent(40)
                  .withVolts(12);
      public static final MotorInterface shooterMotorRight2 = new TalonFXMotor(shooterMotorRight2Config);
      }

       public static final class Angle {
    public static final int motorId = 22;
    public static final String name = "ShooterAngle";

    public static final double gearRatio = 184 / 19;

    public static final boolean isInverted = true;
    public static final boolean brake = true;
    public static final double minimumDegree = 43.0;
    public static final double maximumDegree = 73.1;
    public static final double toleranceAngle = 2.0;
    public static final double currentLimit = 40.0;
    public static final double offsetAngle = -70.5;

    
    public static final SparkMaxConfig amgleMotorConfig = new SparkMaxConfig(motorId, "Angle")
        .withRadiansMotor(gearRatio)
        .withBrake(brake)
        .withInvert(isInverted)
        .withCurrent(currentLimit)
        .withPID(0, 0.9, 0.00, 0.05, 0, 0, 0, 0);
      public static final MotorInterface angleMotor = new SparkMaxMotor(amgleMotorConfig);

  }

  public static final class ShooterLookup {
    public static final InterpolatingDoubleTreeMap distanceToAngleDeg = new InterpolatingDoubleTreeMap();

    public static final double minDistance = 0.5;
    public static final double maxDistance = 4.0;
    

  static {
        
        distanceToAngleDeg.put(1.45 , 73.0);
        distanceToAngleDeg.put(2.0 , 68.0);
        distanceToAngleDeg.put(2.5 , 65.0);
        distanceToAngleDeg.put(3.0 , 66.0);
        distanceToAngleDeg.put(3.5 , 63.0);
        distanceToAngleDeg.put(4.0 , 62.0);
        distanceToAngleDeg.put(4.5 , 59.0);
        distanceToAngleDeg.put(5.0 , 58.0);
        
      }
    }

    public static final class velocityLookup {
      public static final InterpolatingDoubleTreeMap distanceToVelocity = new InterpolatingDoubleTreeMap();


      static {
        distanceToVelocity.put(1.45 , 13.2);
        distanceToVelocity.put(2.0 , 13.8);
        distanceToVelocity.put(2.5 , 14.5);
        distanceToVelocity.put(3.0 , 15.0);
        distanceToVelocity.put(3.5 , 16.0);
        distanceToVelocity.put(4.0 , 17.0);
        distanceToVelocity.put(4.5 , 17.5);
        distanceToVelocity.put(5.0 , 18.3);
      }
    }

}
