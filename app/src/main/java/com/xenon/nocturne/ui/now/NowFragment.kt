package com.xenon.nocturne.ui.now

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.xenon.nocturne.R
import com.xenon.nocturne.databinding.FragmentNowBinding

class NowFragment : Fragment() {

    private var _binding: FragmentNowBinding? = null
    private val binding get() = _binding!!
    private var isLiked = false // Track the state of the like button
    private var currentDrawable: Drawable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nowViewModel = ViewModelProvider(this)[NowViewModel::class.java]

        _binding = FragmentNowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val likeButton: ImageButton = binding.likeButton
        //set the default drawable at the beginning
        currentDrawable = AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.like_button)
        likeButton.setImageDrawable(currentDrawable)

        likeButton.setOnClickListener {
            isLiked = !isLiked // Toggle the state

            if (isLiked) {
                currentDrawable = AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.like_button)
                likeButton.setImageDrawable(currentDrawable)
                (currentDrawable as? AnimatedVectorDrawableCompat)?.start()
                // If its android 11 or higher
                if (currentDrawable is AnimatedVectorDrawable) {
                    (currentDrawable as AnimatedVectorDrawable).registerAnimationCallback(object : Animatable2.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            super.onAnimationEnd(drawable)
                            (currentDrawable as AnimatedVectorDrawable).clearAnimationCallbacks()
                            (currentDrawable as AnimatedVectorDrawable).reset()
                        }
                    })
                } else if (currentDrawable is AnimatedVectorDrawableCompat) {
                    (currentDrawable as AnimatedVectorDrawableCompat).registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            super.onAnimationEnd(drawable)
                            (currentDrawable as AnimatedVectorDrawableCompat).clearAnimationCallbacks()
                        }
                    })
                }

            } else {
                currentDrawable = AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.like_button_reverse)
                likeButton.setImageDrawable(currentDrawable)
                (currentDrawable as? AnimatedVectorDrawableCompat)?.start()
                if (currentDrawable is AnimatedVectorDrawable) {
                    (currentDrawable as AnimatedVectorDrawable).registerAnimationCallback(object : Animatable2.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            super.onAnimationEnd(drawable)
                            (currentDrawable as AnimatedVectorDrawable).clearAnimationCallbacks()
                        }
                    })
                } else if (currentDrawable is AnimatedVectorDrawableCompat) {
                    (currentDrawable as AnimatedVectorDrawableCompat).registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            super.onAnimationEnd(drawable)
                            (currentDrawable as AnimatedVectorDrawableCompat).clearAnimationCallbacks()
                        }
                    })
                }
            }

            // TODO: Implement logic for liking/unliking the song/content
            // You can update your ViewModel here to reflect the 'isLiked' state
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}