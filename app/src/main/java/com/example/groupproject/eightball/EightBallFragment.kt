package com.example.groupproject.eightball

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.groupproject.databinding.FragmentEightBallBinding

class EightBallFragment: Fragment(), SensorEventListener {
    private lateinit var binding: FragmentEightBallBinding
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private val viewModel: EightBallViewModel by lazy {
        ViewModelProvider(this).get(EightBallViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEightBallBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel  // Set up data binding
        binding.lifecycleOwner = viewLifecycleOwner // Set the lifecycle owner for LiveData
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        viewModel.selectedAnswer.observe(viewLifecycleOwner) { newAnswer ->
            // Optionally, update UI or trigger transition here
            applyTransition(newAnswer)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    override fun onSensorChanged(event: SensorEvent?) {
        event?.values?.let { values ->
            Log.d("SensorValues", values.joinToString())
        }
        // Check if the event is a shake
        if (isShake(event)) {
            // Perform the action in the ViewModel
            viewModel.showAnswer()

        } else {
            print("EVIL IS HERE")
        }
    }

    private fun isShake(event: SensorEvent?): Boolean {
        val threshold = 100.0f // You may need to adjust this threshold based on testing
        // Calculate the acceleration magnitude
        val acceleration = event?.values?.let { values ->
            values.map { it * it }.sum()
        } ?: 0.0f
        // Check if the acceleration magnitude exceeds the threshold
        return acceleration > threshold
    }
    private fun applyTransition(newAnswer: Answer) { // THIS IS SO CHALKED
        val fade = Fade()
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, fade)
        binding.executePendingBindings()
    }
//
//    viewModel.selectedAnswer.observe(viewLifecycleOwner, { newValue ->
//        val textView = binding.textView
//        if textView.text.toString() != newValue) {
//            TransitionManager.beginDelayedTransition(textView.parent as ViewGroup)
//            textView.text = newValue
//        }
//    })
}