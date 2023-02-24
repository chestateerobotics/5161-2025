package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.teamUtil.ConfigNames;
import org.firstinspires.ftc.teamcode.teamUtil.RobotConfig;

public class MecanumDriveBase {
    RobotConfig r;

    private final DcMotor frontRight;
    private final DcMotorEx backRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx frontLeft;

    public MecanumDriveBase(RobotConfig r){
        this.r = r;

        frontRight = r.hardwareMap.get(DcMotorEx.class, ConfigNames.frontRight);
        backRight = r.hardwareMap.get(DcMotorEx.class, ConfigNames.backRight);
        backLeft = r.hardwareMap.get(DcMotorEx.class, ConfigNames.backLeft);
        frontLeft = r.hardwareMap.get(DcMotorEx.class, ConfigNames.frontLeft);

        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void rightStrafe(double Power){
        frontRight.setPower(-Power);
        backRight.setPower(Power);
        backLeft.setPower(-Power);
        frontLeft.setPower(Power);
    }
    public void leftStrafe(double Power){
        frontRight.setPower(Power);
        backRight.setPower(-Power);
        backLeft.setPower(Power);
        frontLeft.setPower(-Power);
    }
    public void drive(double leftPower, double rightPower){
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
        backLeft.setPower(leftPower);
        frontLeft.setPower(leftPower);
    }

    public void trueHolonomicDrive(double planarX, double planarY, double headingControl){
        frontRight.setPower(planarX - planarY + headingControl);
        backRight.setPower(planarX + planarY - headingControl);
        backLeft.setPower(planarX - planarY - headingControl);
        frontLeft.setPower(planarX + planarY + headingControl);
    }

}