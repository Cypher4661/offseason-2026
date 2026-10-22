package frc.robot.Subsystems.Swerve;

import edu.wpi.first.math.geometry.Translation2d;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.sensors.CancoderConfig;

public class ModuleConfig {
    public TalonFXConfig DriveConfig;
    public TalonFXConfig SteerConfig;
    public CancoderConfig CanCoderConfig;
    public String Name;
    public int ModuleID;
    public int DriveID;
    public int SteerID;
    public int CanCoderID;
    public double GearRatioSteer = 150 / 7.0;
    public double GearRatioDrive = 6.75;
    public double Diameter;
    public double CanCoderOffset;
    public boolean IsDriveInverted;
    public boolean IsSteerInverted;
    public Canbus cypherCANivore;
    public Translation2d posion = Translation2d.kZero;

    public ModuleConfig(String name, int moduleID, int driveID, int steerID, int canCoderID, double diameter, double canCoderOffset, Canbus cypherCANivore) {
        this.Name = name;
        this.ModuleID = moduleID;
        this.DriveID = driveID;
        this.SteerID = steerID;
        this.CanCoderID = canCoderID;
        this.Diameter = diameter;
        this.CanCoderOffset = canCoderOffset;
        this.cypherCANivore = cypherCANivore;

        DriveConfig = new TalonFXConfig(driveID, Canbus.CANIvore, name + " Drive")
            .withBrake(true)
            .withInvert(IsDriveInverted)
            .withMeterMotor(GearRatioDrive, diameter)
            .withCurrent(20)
            .withVolts(10)
            .withPID(5, 0, 0, 0.1, 2.45, 0, 0)
            .withRampTime(0.2)
            .withMotionParam(720, 1200, 2000);
        
        SteerConfig = new TalonFXConfig(steerID, Canbus.CANIvore, name + " Steer")
            .withBrake(true)
            .withInvert(IsSteerInverted)
            .withMeterMotor(GearRatioSteer, diameter)
            .withCurrent(20)
            .withVolts(10)
            .withPID(0.65, 0, 0, 0, 0, 0, 0)
            .withRampTime(0.2)
            .withMotionParam(720, 1200, 2000)
            .withMaxPositionError(2);

        CanCoderConfig = new CancoderConfig(canCoderID, Canbus.CANIvore, name + " Cancoder");
            
    }

}
