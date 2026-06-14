package br.com.vigiadeposto.domain.model

enum class Status(
    val color: String,
    val icon: String
) {
    GREEN("#22C55E", "👍"),
    YELLOW("#F59E0B", "✊"),
    RED("#EF4444", "👎"),
    GREY("#6B7280", "⚪");

    companion object {
        fun computeStatus(pos: Int, neg: Int): Status {
            return when {
                pos == 0 && neg == 0 -> GREY
                pos >= 1 && neg == 0 -> GREEN
                neg >= 1 && pos == 0 -> RED
                (pos - neg) >= 5 -> GREEN
                (pos - neg) <= -5 -> RED
                else -> YELLOW
            }
        }
    }
}
