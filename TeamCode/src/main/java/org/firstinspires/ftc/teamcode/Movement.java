package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Movement extends LinearOpMode {
    private MecanumDrive mecanumDrive = new MecanumDrive();
    private double maxSpeed = 1;
     private DcMotor arm;
    // private Servo ClawServo;
     private Servo WheelServo;

    public void runOpMode() {
        mecanumDrive.init(hardwareMap);
         arm = hardwareMap.get(DcMotor.class, "arm");
        // ClawServo = hardwareMap.get(Servo.class, "ClawServo");
         WheelServo = hardwareMap.get(Servo.class, "WheelServo");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double forward = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            //Gamepad 1 Code
            /*
            if(gamepad1.right_trigger >= 0.5){
                maxSpeed = 0.2;
                mecanumDrive.setMaxSpeed(0.2);
            }
            else{
                maxSpeed = 0.5;
                mecanumDrive.setMaxSpeed(1);
            }
            */

             if(gamepad2.x)
            {
                arm.setPower(0.8);
            }
            else if(gamepad2.y)
             {
                arm.setPower(-0.8);
            }
            else
            {
                 arm.setPower(0);
             }


            /*
            if(gamepad2.right_bumper)
            {
                ClawServo.setPosition(1);
            }
            else if (gamepad2.left_bumper)
            {
                ClawServo.setPosition(-1);
            }
            else {
                ClawServo.setPosition(0);
            }
            */
            if(gamepad2.a)
            {
                WheelServo.setPosition(1);
            }
            else if(gamepad2.b) {
                WheelServo.setPosition(-1);
            }
            else
            {
                WheelServo.setPosition(0);
            }


            mecanumDrive.driveMecanum(forward, strafe, rotate);

            telemetry.addData("Max Speed = ", maxSpeed);
            telemetry.addData("forward = ", forward);
            telemetry.addData("strafe = ", strafe);
            telemetry.addData("rotate = ", rotate);

            telemetry.update();

        }
    }
}
