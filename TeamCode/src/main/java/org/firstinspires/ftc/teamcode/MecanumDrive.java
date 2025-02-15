package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;

public class MecanumDrive {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backRight;
    public DcMotor backLeft;
    //public DcMotor rightLift;
    //public DcMotor leftLift;
    //private Servo armServo;
    private boolean switcher = true;

    public static double GEAR_RATIO = 0.5; // for simulator - ours should be 0.5f;
    public static double WHEEL_RADIUS = 5.0;  // 5 cm
    public static double TICKS_PER_ROTATION = 1120.0;  // From NeveRest (for simulator)  GoBilda should be 383.6f

    public static double CM_PER_TICK = (2 * Math.PI * GEAR_RATIO * WHEEL_RADIUS) / TICKS_PER_ROTATION;

    private double maxSpeed = 0.7;

    private MatrixF conversion;
    private GeneralMatrixF encoderMatrix = new GeneralMatrixF(3, 1);

    private int frontLeftOffset;
    private int frontRightOffset;
    private int backRightOffset;
    private int backLeftOffset;


    MecanumDrive() {
        float[] data = {1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, 1.0f};
        conversion = new GeneralMatrixF(3, 3, data);
        conversion = conversion.inverted();
    }

    void init(HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class, "frontLeft");
        //frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight = hwMap.get(DcMotor.class, "frontRight");
        //frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft = hwMap.get(DcMotor.class, "backLeft");
        //backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight = hwMap.get(DcMotor.class, "backRight");
        //backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //backRight = hwMap.get(DcMotor.class, "backRight");
        /*
        rightLift = hwMap.get(DcMotor.class, "rightLift");
        leftLift = hwMap.get(DcMotor.class, "leftLift");
         */


        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    void setEndPowerBehaviorBrake() {
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void setSpeeds(double flSpeed, double frSpeed, double blSpeed, double brSpeed) {
        double largest = maxSpeed;
        /*
        largest = Math.max(largest, Math.abs(flSpeed));
        largest = Math.max(largest, Math.abs(frSpeed));
        largest = Math.max(largest, Math.abs(blSpeed));
        largest = Math.max(largest, Math.abs(brSpeed));
        */

         frontLeft.setPower(flSpeed * largest);
         frontRight.setPower(frSpeed * largest);
         backLeft.setPower(blSpeed * largest);
         backRight.setPower(brSpeed * largest);
    }

    void driveMecanum(double forward, double strafe, double rotate) {
        double frontLeftSpeed = -1 * forward - strafe + rotate;
        double frontRightSpeed = -1 * forward - strafe - rotate;
        double backLeftSpeed = -1 * forward + strafe + rotate;
        double backRightSpeed = -1 * forward + strafe - rotate;

        setSpeeds(frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
    }


    // Returns forward, strafe
    double[] getDistanceCm() {
        double[] distances = {0.0, 0.0};

        encoderMatrix.put(0, 0, (float) ((frontLeft.getCurrentPosition() - frontLeftOffset) * CM_PER_TICK));
        encoderMatrix.put(1, 0, (float) ((frontRight.getCurrentPosition() - frontRightOffset) * CM_PER_TICK));
        encoderMatrix.put(2, 0, (float) ((backLeft.getCurrentPosition() - backLeftOffset) * CM_PER_TICK));
        encoderMatrix.put(3, 0, (float) ((backRight.getCurrentPosition() - backRightOffset) * CM_PER_TICK));

        MatrixF distanceMatrix = conversion.multiplied(encoderMatrix);
        distances[0] = distanceMatrix.get(0, 0);
        distances[1] = distanceMatrix.get(1, 0);

        return distances;
    }

    void setMaxSpeed(double speed) {
        maxSpeed = Math.min(speed, 1.0);
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setEncoderOffsets() {
        frontRightOffset = frontRight.getCurrentPosition();
        frontLeftOffset = frontLeft.getCurrentPosition();
        backLeftOffset = backLeft.getCurrentPosition();
        backRightOffset = backRight.getCurrentPosition();
    }
}