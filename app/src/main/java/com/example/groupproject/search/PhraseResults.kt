data class PhraseResults(
    val id: String,
    val suggestions: List<Suggestion>

)

data class Suggestion(
    val text: String
)