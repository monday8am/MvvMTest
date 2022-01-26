package com.monday8am.baseapp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.monday8am.baseapp.R
import com.monday8am.baseapp.domain.model.User
import com.monday8am.baseapp.ui.ScreenNavigator
import com.monday8am.baseapp.ui.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    @Inject
    lateinit var screenNavigator: ScreenNavigator

    private val viewModel: MainViewModel by viewModels()

    private lateinit var userContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userContainer = view.findViewById(R.id.user_container)
        setupToolbar()

        launchAndRepeatWithViewLifecycle {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainUiState.collect {
                    when (it.state) {
                        DataState.Idle -> addUsers(it.users)
                        DataState.Loading -> Timber.d("Loading")
                        is DataState.Error -> Timber.d("Error! ${it.state.message}")
                    }
                }
            }
        }
    }

    private fun addUsers(users: List<User>) {
        Timber.d("Users: $users")
        val userItemHeight = dp2px(requireContext(), 82)
        userContainer.removeAllViews()

        users.forEachIndexed { index, user ->
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_user, null)
            view.layoutParams = FrameLayout.LayoutParams(
                MATCH_PARENT,
                userItemHeight,
                Gravity.START
            )
            //view.translationY = userItemHeight.toFloat() * index.toFloat()
            val deleteBtn = view.findViewById<MaterialButton>(R.id.remove_button)
            val titleText = view.findViewById<TextView>(R.id.title_view)
            val subtitleTxt = view.findViewById<TextView>(R.id.subtitle_view)
            val imageView = view.findViewById<ImageView>(R.id.thumbnail_image_view)

            titleText.text = user.name
            subtitleTxt.text = "${user.platform} :: ${user.position}"
            deleteBtn.setOnClickListener {
                viewModel.removeUserFronList(user.name)
            }

            Glide.with(view.context)
                .load(user.pic)
                .fitCenter()
                .into(imageView)

            userContainer.addView(view)
        }
        userContainer.invalidate()

    }

    private fun setupToolbar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }
    }

    fun dp2px(context: Context, dp: Int): Int {
        val resources = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
    }

    fun dp2px(context: Context, dp: Float): Int {
        val resources = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    }
}
