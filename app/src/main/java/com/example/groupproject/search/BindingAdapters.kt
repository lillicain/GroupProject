package com.example.groupproject.search

import PhraseResults
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("definitions")
fun bindDefinitionData(textView: TextView, phraseResults: PhraseResults) {
    textView.text = "Definition: ${phraseResults.suggestions[1]}"
}