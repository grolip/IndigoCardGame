package indigo

@Suppress("SpellCheckingInspection")
class TasDeCartes {
    val cartes = mutableListOf<Carte>()
    val taille: Int
        get() = cartes.size
    val vide: Boolean
        get() = cartes.size == 0
    val premiereCarte: Carte
        get() = cartes.last()

    fun reset(){
        cartes.clear()
    }

    fun genererJeuComplet(){
        Carte.VALEUR.forEach {
            cartes.add(Carte(it, Carte.COEUR))
            cartes.add(Carte(it, Carte.CARREAU))
            cartes.add(Carte(it, Carte.PIQUE))
            cartes.add(Carte(it, Carte.TREFLE))
        }
    }

    fun melanger(){
        cartes.shuffle()
    }

    fun distribuer(nCartes: Int): List<Carte> {
        if (cartes.size >= nCartes)
            return List(nCartes) { cartes.removeLast() }
        return List(cartes.size) { cartes.removeLast() }
    }

    fun choisir(index: Int) = cartes.removeAt(index)

    fun choisir(carte: Carte) = cartes.remove(carte)

    fun ajouter(nouvelleCarte: Carte) = cartes.add(nouvelleCarte)

    fun ajouter(nouvellesCartes: List<Carte>) = cartes.addAll(nouvellesCartes)

    fun rechercher(valeur: String) =
        cartes.find { it.valeur == valeur }

    fun rechercher(signe: Char) =
        cartes.find { it.signe == signe }

    fun compter(valeur: String) =
        cartes.fold(0) { acc, carte ->
            acc + if (carte.valeur == valeur) 1 else 0 }

    fun compter(signe: Char) =
        cartes.fold(0) { acc, carte ->
            acc + if (carte.signe == signe) 1 else 0 }

    fun afficher(){
        cartes.forEach { print("$it ") }
        println()
    }

    fun afficherAvecNombre(){
        cartes.forEachIndexed { i, carte -> print("${i + 1})$carte ") }
        println()
    }
}