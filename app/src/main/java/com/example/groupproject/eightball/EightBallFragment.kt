package com.example.groupproject.eightball

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class EightBallFragment: Fragment() {
    private val viewModel: EightBallViewModel by lazy {
        ViewModelProvider(this).get(EightBallViewModel::class.java)
    }
}