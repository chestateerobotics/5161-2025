package org.firstinspires.ftc.teamcode;  //place where the code is located


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Erection {
    Telemetry telemetry;
    HardwareMap hardwareMap;
    private Servo leftServo;
    private Servo rightServo;
    private DcMotorEx frontElevatorEx;
    private DcMotorEx backElevatorEx;
    private boolean isError = false;

    public void initErection(HardwareMap hardwareMapPorted, Telemetry telemetryPorted) {
        try {
            hardwareMap = hardwareMapPorted;
            telemetry = telemetryPorted;


            //map Dc motors with encoders, it is in a try, catch because if the expansion hub is not
            //properly connected the robot will throw an error and prevent the code from running

            frontElevatorEx = hardwareMap.get(DcMotorEx.class, "Motor_Port_1_EH");
            backElevatorEx = hardwareMap.get(DcMotorEx.class, "Motor_Port_0_EH");

            frontElevatorEx.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backElevatorEx.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontElevatorEx.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backElevatorEx.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontElevatorEx.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backElevatorEx.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);


        } catch (Exception e) {
            isError = true;
        }
    }

    public void raise(double leftStick, double rightStick, boolean bottom, boolean height80, boolean height100, boolean height120) {

        if (!isError) {
            if (bottom) {
                runToHeight(0);
            }
            if (height80) {
                runToHeight(1184);
            }
            if (height100) {
                runToHeight(1480);
            }
            if (height120) {
                runToHeight(1776);
            }
            if (!(height80 || height100 || height120 || bottom)) {
                frontElevatorEx.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //runs using power
                backElevatorEx.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                frontElevatorEx.setPower(rightStick);
                backElevatorEx.setPower(-rightStick);
            }


            telemetry.addData("front erector position", frontElevatorEx.getCurrentPosition());
            telemetry.addData("back erector position", backElevatorEx.getCurrentPosition());

        } else {

            telemetry.addData("erectile disfunction", isError);
        }
    }

    public void runToHeight(int height) {
        frontElevatorEx.setMode(DcMotor.RunMode.RUN_TO_POSITION); //runs to position
        backElevatorEx.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontElevatorEx.setTargetPosition(height);//1000(height mm)/(6mm(hex shaft diameter)*3,14)*28(ticks per rotation)
        backElevatorEx.setTargetPosition(height);
        backElevatorEx.setPower(1);
        frontElevatorEx.setPower(1);
    }

}  