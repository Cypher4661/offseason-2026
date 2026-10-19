package frc.robot.Subsystems;

import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class SwerveConstants {
    public static class ModuleConstants {
        public static final double Diameter = 0.1016; // 4 inches in meters
        public static final double GearRatioSteer = 150 / 7.0;
        public static final double GearRatioDrive = 6.75;
        public static final double FR_CancoderOffset = 0.0; // in radians
        public static final double FL_CancoderOffset = 0.0; // in radians
        public static final double BL_CancoderOffset = 0.0; // in radians
        public static final double BR_CancoderOffset = 0.0; // in radians
        
    }

    public static class ChassisConstants {
        public static final ModuleConfig[] ModulesConfig = {
            new ModuleConfig("Front_Right", ChassisConstants.FR_ModulesID, ChassisConstants.FR_DriveID, ChassisConstants.FR_SteerID, ChassisConstants.FR_CancoderID, ModuleConstants.Diameter, ModuleConstants.FR_CancoderOffset, ChassisConstants.cypherCANivore),
            new ModuleConfig("Front_Left", ChassisConstants.FL_ModulesID, ChassisConstants.FL_DriveID, ChassisConstants.FL_SteerID, ChassisConstants.FL_CancoderID, ModuleConstants.Diameter, ModuleConstants.FL_CancoderOffset, ChassisConstants.cypherCANivore),
            new ModuleConfig("Back_Left", ChassisConstants.BL_ModulesID, ChassisConstants.BL_DriveID, ChassisConstants.BL_SteerID, ChassisConstants.BL_CancoderID, ModuleConstants.Diameter, ModuleConstants.BL_CancoderOffset, ChassisConstants.cypherCANivore),
            new ModuleConfig("Back_Right", ChassisConstants.BR_ModulesID, ChassisConstants.BR_DriveID, ChassisConstants.BR_SteerID, ChassisConstants.BR_CancoderID, ModuleConstants.Diameter, ModuleConstants.BR_CancoderOffset, ChassisConstants.cypherCANivore)
        };
        public static final int FR_ModulesID = 1;
        public static final int FL_ModulesID = 2;
        public static final int BL_ModulesID = 3;
        public static final int BR_ModulesID = 4;
        public static final int FR_DriveID = 1;
        public static final int FL_DriveID = 4;
        public static final int BL_DriveID = 7;
        public static final int BR_DriveID = 10;
        public static final int FR_SteerID = 2;
        public static final int FL_SteerID = 5;
        public static final int BL_SteerID = 8;
        public static final int BR_SteerID = 11;
        public static final int FR_CancoderID = 3;
        public static final int FL_CancoderID = 6;
        public static final int BL_CancoderID = 9;
        public static final int BR_CancoderID = 12;
        public static final int GyroID = 13;
        public static final double X_Y = 0.273525; // in meters
        public static final double Max_Linear_Speed = 4.5; // in meters per second
        public static final double Max_Linear_Acceleration = 7.0; // in meters per second squared
        public static final double Max_Rotation_Speed = 3.0; // in radians per second
        public static final double Deadband = 0.08; // joystick deadband
        public static final Canbus cypherCANivore = Canbus.CANIvore;

    }
}
