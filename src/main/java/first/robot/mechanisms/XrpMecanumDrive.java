// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.mechanisms;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.command3.button.CommandGamepad;
import org.wpilib.drive.MecanumDrive;
import org.wpilib.hardware.rotation.Encoder;
import org.wpilib.xrp.XRPMotor;

/** Add your docs here. */
public class XrpMecanumDrive implements Mechanism {
    private static final double kGearRatio =
        (30.0 / 14.0) * (28.0 / 16.0) * (36.0 / 9.0) * (26.0 / 8.0); // 48.75:1
    private static final double kCountsPerMotorShaftRev = 12.0;
    private static final double kCountsPerRevolution = kCountsPerMotorShaftRev * kGearRatio; // 585.0
    private static final double kWheelDiameterInch = 2.3622; // 60 mm

    // The XRP has the left and right motors set to
    // channels 0 and 1 respectively
    private final XRPMotor m_leftFrontMotor = new XRPMotor(0);
    private final XRPMotor m_rightFrontMotor = new XRPMotor(1);
    private final XRPMotor m_leftBackMotor = new XRPMotor(2);
    private final XRPMotor m_rightBackMotor = new XRPMotor(3);

    // The XRP has onboard encoders that are hardcoded
    // to use DIO pins 4/5 and 6/7 for the left and right
    private final Encoder m_leftFrontEncoder = new Encoder(4, 5);
    private final Encoder m_rightFrontEncoder = new Encoder(6, 7);
    private final Encoder m_leftBackEncoder = new Encoder(8, 9);
    private final Encoder m_rightBackEncoder = new Encoder(10, 11);


    // Set up the differential drive controller
    private final MecanumDrive m_mecanumDrive =
        new MecanumDrive(m_leftFrontMotor, m_leftBackMotor, m_rightFrontMotor, m_rightBackMotor);

    // Constructor
    public XrpMecanumDrive() {
        // Use inches as unit for encoder distances
        m_leftFrontEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
        m_rightFrontEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
        m_leftBackEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
        m_rightBackEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
        resetEncoders();

        // Invert right side since motor is flipped
        m_rightFrontMotor.setInverted(true);
        m_rightBackMotor.setInverted(true);        
    }

    private void resetEncoders() {
        m_leftFrontEncoder.reset();
        m_rightFrontEncoder.reset();
        m_leftBackEncoder.reset();
        m_rightBackEncoder.reset();
    }

    public void arcadeDrive(double xaxisSpeed, double yaxisSpeed, double zaxisRotate) {
        m_mecanumDrive.driveCartesian(xaxisSpeed, yaxisSpeed, zaxisRotate);
    }  

    // TODO-DW : Figure out what these are used for.  Do we need back encoder get methods?
    public double getLeftFrontDistanceInch() {
        return m_leftFrontEncoder.getDistance();
    }

    public double getRightFrontDistanceInch() {
        return m_rightFrontEncoder.getDistance();
    }

    // Run the teleop drive command
    public Command driveCommand(CommandGamepad gamepad) {
        return run(coroutine -> {
            System.out.println("Started driveCommand.");
            while (true) {
                // Read joysticks
                double x_speed = gamepad.getLeftY();
                double y_speed = gamepad.getLeftX();
                double rotate = gamepad.getRightX();

                System.out.println("Running driveCommand.");

                // Call arcadeDrive
                arcadeDrive(x_speed, y_speed, rotate);

                // Yield
                coroutine.yield();
            }
        })
        .named("Mecanum Drive Command");
    }
}
