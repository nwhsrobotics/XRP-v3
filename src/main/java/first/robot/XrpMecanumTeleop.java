// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;
import org.wpilib.opmode.OpMode;
import org.wpilib.opmode.Teleop;

@Teleop
public class XrpMecanumTeleop implements OpMode {
    public XrpMecanumTeleop(Robot robot) {
        System.out.println("Constructing XrpMecanumTeleop");
        robot.always_trigger.whileTrue(robot.drive.driveCommand(robot.exampleController));
    }
}
