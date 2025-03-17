package indigo

@Suppress("SpellCheckingInspection")
class Carte(val valeur: String, val signe: Char) {
    companion object {
        val VALEUR = listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
        const val COEUR = '♥'
        const val CARREAU = '♦'
        const val PIQUE = '♠'
        const val TREFLE = '♣'
    }

    override fun toString(): String {
        return "$valeur$signe"
    }
}