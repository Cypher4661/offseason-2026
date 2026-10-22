package frc.robot.Subsystems.Swerve;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.Cancoder;

public class SwerveModule implements Sendable {
    protected TalonFXMotor DriveMotor;
    protected TalonFXMotor SteerMotor;
    private Cancoder CanCoder;
    private double lastSteerAngle = 0;
    private boolean debug = false;
    public ModuleConfig moduleConfig;

    SwerveModule(ModuleConfig config){
        this.moduleConfig = config;
        this.DriveMotor = new TalonFXMotor(config.DriveConfig);
        this.SteerMotor = new TalonFXMotor(config.SteerConfig);
        this.CanCoder = new Cancoder(config.CanCoderConfig);
        SteerMotor.setEncoderPosition(getAbsoluteAngle() - config.CanCoderOffset);
        SmartDashboard.putData(config.Name + " Module", this);
    }

    // set commands
    public void setNeutralMode(boolean isBrake){
        DriveMotor.setNeutralMode(isBrake);
        SteerMotor.setNeutralMode(isBrake);
    }
    public void stopModule(){
        DriveMotor.set(0);
        SteerMotor.set(0);
    }
    public void setStop(){
        DriveMotor.setDuty(0);
        SteerMotor.setDuty(0);
    }
     public void setDrivePower(double power){
        DriveMotor.setDuty(power);
    }
    public void setSteerPower(double power){
        SteerMotor.setDuty(power);
    }
    public void setDriveVelocity(double velocityRadPerSec){
        DriveMotor.setVelocity(velocityRadPerSec);
    }
    public void setSteerVelocity(double velocityRadPerSec){
        SteerMotor.setVelocity(velocityRadPerSec);
    }
    public void setSteerAngle(double angleRadian){
        SteerMotor.setPositionVoltage(angleRadian);
    }

    // get commands
     public double getSteerAngle(){
        return SteerMotor.getCurrentAngle();
    }
    public double getAbsoluteAngle() {
        return CanCoder.getCurrentAbsPosition();
    }
    public Rotation2d getSteerRotation(){
        return new Rotation2d(getSteerAngle());
    }
    public double getDrivePosition(){
        return DriveMotor.getCurrentPosition();
    }
    public double getDriveVelocity(){
        return DriveMotor.getCurrentVelocity();
    }
    public double getSteerVelocity(){
        return SteerMotor.getCurrentVelocity();
    }
    public double getDriveAcceleration(){
        return DriveMotor.getCurrentAcceleration();
    }
    public double getSteerAcceleration(){
        return SteerMotor.getCurrentAcceleration();
    }
     public double getDriveVoltage(){
        return DriveMotor.getCurrentVoltage();
    }
    public double getSteerVoltage(){
        return SteerMotor.getCurrentVoltage();
    }
    public SwerveModulePosition getModulePosition(){
        double currentAngle = getSteerAngle();
        double angle = (currentAngle + lastSteerAngle) / 2;
        lastSteerAngle = currentAngle;
        return new SwerveModulePosition(DriveMotor.getCurrentPosition() + SteerMotor.getCurrentPosition() * SwerveConstants.ModuleConstants.SteerDriveRatio,
             Rotation2d.fromRadians(angle));
    }
    public SwerveModuleState getmModuleState(){
        return new SwerveModuleState(getDriveVelocity() + getSteerVelocity() * SwerveConstants.ModuleConstants.SteerDriveRatio, getSteerRotation());
    }
    
    // state commands
    public SwerveModuleState getState(){
        return new SwerveModuleState(getDriveVelocity(), getSteerRotation());
    }
    public void setState(SwerveModuleState state){
        double targetAngle = state.angle.getRadians();
        if(debug) SmartDashboard.putNumber("DEBUG Target", targetAngle);
        double angle = getAbsoluteAngle() - moduleConfig.CanCoderOffset;
        if(debug) SmartDashboard.putNumber("DEBUG Angle", angle);
        double diff = targetAngle - angle;
        if(debug) SmartDashboard.putNumber("DEBUG Diff1", diff);
        double vel = state.speedMetersPerSecond;
        diff = MathUtil.angleModulus(diff);
        if(debug) SmartDashboard.putNumber("DEBUG Diff2", diff);
        if(diff > Math.PI/2){
            vel = -vel;
            diff = diff - Math.PI;
        }
        else if(diff < -Math.PI/2){
            vel = -vel;
            diff = diff + Math.PI;
        }
        if(debug) SmartDashboard.putNumber("DEBUG Diff3", diff);
        double steerAngle = SteerMotor.getCurrentPosition() + diff;
        setSteerAngle(steerAngle);
        if(debug) SmartDashboard.putNumber("DEBUG tgt", steerAngle);
        setDriveVelocity(vel);
    }

      @Override
    public void initSendable(SendableBuilder builder){
        builder.addDoubleProperty("Abs", this::getAbsoluteAngle, null);
    }

}

