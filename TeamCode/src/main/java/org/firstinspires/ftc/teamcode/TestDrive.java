package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@TeleOp
public class TestDrive extends LinearOpMode {
    private DcMotor frontRight;
    private DcMotor frontLeft;

    @Override
    public void runOpMode() {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        double power = 0;
        while (opModeIsActive()) {
            power = -this.gamepad1.left_stick_y;
            frontRight.setPower(power);
            frontLeft.setPower(-power);
            telemetry.addData("Target Power", power);
            telemetry.addData("Motor Power", frontRight.getPower());
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}
