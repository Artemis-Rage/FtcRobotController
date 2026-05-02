package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.abs
import kotlin.math.max

/**
 * Mecanum TeleOp for the goBILDA Strafer base.
 *
 * Wiring assumed (must match the Driver Hub configuration named "Strafer"):
 *   Control Hub motor 0 -> leftFront   (REVERSE)
 *   Control Hub motor 1 -> leftBack    (REVERSE)
 *   Control Hub motor 2 -> rightFront  (FORWARD)
 *   Control Hub motor 3 -> rightBack   (FORWARD)
 *
 * Controls (gamepad 1):
 *   left stick Y  -> forward/back
 *   left stick X  -> strafe
 *   right stick X -> rotate
 */
@TeleOp(name = "Strafer TeleOp (Kotlin)", group = "Drive")
class StraferTeleOp : LinearOpMode() {

    override fun runOpMode() {
        val leftFront  = hardwareMap.get(DcMotor::class.java, "leftFront")
        val leftBack   = hardwareMap.get(DcMotor::class.java, "leftBack")
        val rightFront = hardwareMap.get(DcMotor::class.java, "rightFront")
        val rightBack  = hardwareMap.get(DcMotor::class.java, "rightBack")

        leftFront.direction  = DcMotor.Direction.REVERSE
        leftBack.direction   = DcMotor.Direction.REVERSE
        rightFront.direction = DcMotor.Direction.FORWARD
        rightBack.direction  = DcMotor.Direction.FORWARD

        telemetry.addLine("Strafer ready. Press play.")
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            val y  = -gamepad1.left_stick_y.toDouble()
            val x  =  gamepad1.left_stick_x.toDouble() * 1.1  // strafe is weaker than forward; small boost
            val rx =  gamepad1.right_stick_x.toDouble()

            val denom = max(abs(y) + abs(x) + abs(rx), 1.0)
            leftFront.power  = (y + x + rx) / denom
            leftBack.power   = (y - x + rx) / denom
            rightFront.power = (y - x - rx) / denom
            rightBack.power  = (y + x - rx) / denom

            telemetry.addData("LF/LB", "%.2f / %.2f", leftFront.power, leftBack.power)
            telemetry.addData("RF/RB", "%.2f / %.2f", rightFront.power, rightBack.power)
            telemetry.update()
        }
    }
}
