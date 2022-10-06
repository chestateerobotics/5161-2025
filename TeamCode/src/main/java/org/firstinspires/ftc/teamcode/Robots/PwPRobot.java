package org.firstinspires.ftc.teamcode.Robots;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Components.Aligner;
import org.firstinspires.ftc.teamcode.Components.CV.CVMaster;
import org.firstinspires.ftc.teamcode.Components.Claw;
import org.firstinspires.ftc.teamcode.Components.ClawExtension;
import org.firstinspires.ftc.teamcode.Components.Field;
import org.firstinspires.ftc.teamcode.Components.Lift;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public class PwPRobot extends BasicRobot{
    private Aligner aligner =null;
    private Claw claw = null;
    private ClawExtension clawExtension = null;
    private Lift lift = null;
    public Field field = null;
    public CVMaster cv= null;
    public SampleMecanumDrive roadrun = null;



    public PwPRobot(LinearOpMode opMode, boolean p_isTeleop) {
        super(opMode, p_isTeleop);
        roadrun = new SampleMecanumDrive(op.hardwareMap);
        field = new Field(roadrun);
//        aligner = new Aligner();
//        claw = new Claw();
//        clawExtension = new ClawExtension();
//        lift = new Lift();
        cv = new CVMaster(roadrun);

    }
    public void stop(){
        cv.stopCamera();
        logger.closeLog();
        op.stop();
    }
    public void autoAim(){
        if(queuer.queue(false,!roadrun.isBusy())) {
            if (!roadrun.isBusy()&&field.lookingAtPole()) {
                double[] coords = field.lookedAtPole();

                Trajectory trajectory = roadrun.trajectoryBuilder(roadrun.getPoseEstimate()).lineToLinearHeading(
                        new Pose2d(roadrun.getPoseEstimate().getX() + (coords[0] - 9) * cos(roadrun.getPoseEstimate().getHeading() + coords[1]),
                                roadrun.getPoseEstimate().getY() + (coords[0] - 9) * sin(roadrun.getPoseEstimate().getHeading() + coords[1]),
                                roadrun.getPoseEstimate().getHeading() + coords[1])).build();
                roadrun.followTrajectoryAsync(trajectory);
            }
        }
    }
    public void followTrajectorySequenceAsync(TrajectorySequence trajectorySequence) {
        if (queuer.queue(false, !roadrun.isBusy())) {
            if (!roadrun.isBusy()) {
                roadrun.followTrajectorySequenceAsync(trajectorySequence);
            }
        }
    }
    public void setFirstLoop(boolean value){
        queuer.setFirstLoop(value);
    }
    public void openClaw() {
        if (queuer.queue(true, op.getRuntime() > claw.clawServoLastSwitchTime +
                claw.CLAW_SERVO_SWITCH_TIME)) {

            claw.clawServoLastSwitchTime = op.getRuntime();
            claw.openClaw();
        }
    }

    public void closeClaw() {
        if (queuer.queue(true, op.getRuntime() > claw.clawServoLastSwitchTime +
                claw.CLAW_SERVO_SWITCH_TIME)) {

            claw.clawServoLastSwitchTime = op.getRuntime();
            claw.closeClaw();
        }
    }

    public void spinAlignerIntake() {
        if (queuer.queue(true, aligner.isConeInAligner())) {

            aligner.spinAlignerIntake();
        }
    }

    public void stopAlignerIntake() {
        if (queuer.queue(true, op.getRuntime() > aligner.alignerMotorLastStopTime +
                aligner.ALIGNER_MOTOR_STOP_TIME)) {

            aligner.stopAlignerIntake();
        }
    }

    public void reverseAlignerIntake() {
        if (queuer.queue(true, !aligner.isConeInAligner())){

            aligner.reverseAlignerIntake();
        }
    }

    public void teleOp(){
        //omnidirectional movement + turning
        if(op.gamepad1.left_stick_x!=0||op.gamepad1.left_stick_y!=0||op.gamepad1.right_stick_x!=0) {
            roadrun.setWeightedDrivePower(
                    new Pose2d(
                            op.gamepad1.left_stick_y,
                            op.gamepad1.left_stick_x,
                            op.gamepad1.right_stick_x
                    )
            );
        }
        //manual lift up/down
        if(op.gamepad2.right_trigger!=0||op.gamepad2.left_trigger!=0){
            lift.setPower(op.gamepad2.right_trigger-op.gamepad2.left_trigger);
        }
        //when not manual lifting, automate lifting
        else{
            lift.liftToTarget();
        }
        //toggle automate lift target to higher junc
        if(op.gamepad2.dpad_up){
            lift.toggleLiftPosition(1);
        }
        //toggle automate lift target to lower junc
        if(op.gamepad2.dpad_down){
            lift.toggleLiftPosition(-1);
        }
        //toggle liftArm position
        if(op.gamepad2.a){
            //liftArm.toggleArmPosition
        }
        //manual open/close claw (will jsut be open claw in the future)
        if(op.gamepad1.a){
            //claw.toggleClawPosition
        }
        //will only close when detect cone
        //claw.closeClaw
    }
}
