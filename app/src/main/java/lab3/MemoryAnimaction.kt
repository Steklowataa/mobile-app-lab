package lab3

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import java.util.Random

class MemoryAnimation {
    fun animateMatch(button: ImageButton, onEnd: () -> Unit) {
        val random = Random()
        button.pivotX = random.nextFloat() * button.width
        button.pivotY = random.nextFloat() * button.height


        val rotation = ObjectAnimator.ofFloat(button, "rotation", 0f, 360f)
        val scalingX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.1f)
        val scalingY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.1f)
        val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)

        val set = AnimatorSet()
        set.duration = 1000
        set.interpolator = DecelerateInterpolator()
        set.playTogether(rotation, scalingX, scalingY, fade)

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                button.alpha = 0f
                onEnd()
            }
        })
        set.start()
    }

    fun animateNoMatch(button: ImageButton, onEnd: () -> Unit) {
        val shake = ObjectAnimator.ofFloat(button, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 0f)
        val rotate = ObjectAnimator.ofFloat(button, "rotation", 0f, 10f, -10f, 10f, -10f, 0f)

        val set = AnimatorSet()
        set.duration = 600
        set.interpolator = AccelerateDecelerateInterpolator()
        set.playTogether(shake, rotate)

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onEnd()
            }
        })
        set.start()
    }
}