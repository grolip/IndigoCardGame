package indigo

@Suppress("SpellCheckingInspection")
class Joueur(val nom: String, private val robot: Boolean = false) {
    private val main = TasDeCartes()
    var score = 0
    var nCartesGagnees = 0
    val plusDeCartes: Boolean
        get() = main.taille == 0


    fun reset(){
        main.reset()
        score = 0
    }

    fun piocher(pioche: TasDeCartes, nCartes: Int){
        main.ajouter(pioche.distribuer(nCartes))
    }

    private fun jouerAuto(defausse: TasDeCartes): Carte? {
        var carte: Carte? = null
        val grilleValeurs = mutableMapOf<String, Int>()
        val grilleSignes = mutableMapOf<Char, Int>()

        Carte.VALEUR.forEach { grilleValeurs[it] = main.compter(it) }
        grilleSignes[Carte.COEUR] = main.compter(Carte.COEUR)
        grilleSignes[Carte.CARREAU] = main.compter(Carte.CARREAU)
        grilleSignes[Carte.PIQUE] = main.compter(Carte.PIQUE)
        grilleSignes[Carte.TREFLE] = main.compter(Carte.TREFLE)

        if (defausse.taille > 0){
            val premiereCarte = defausse.premiereCarte
            val nValeur = grilleValeurs[premiereCarte.valeur]
            val nSigne = grilleSignes[premiereCarte.signe]

            if (nValeur!! + nSigne!! > 0) {
                carte = if (nValeur >= nSigne)
                    main.rechercher(premiereCarte.valeur)
                else
                    main.rechercher(premiereCarte.signe)
            }
        }

        if (carte == null){
            val maxValeur = grilleValeurs.maxBy { it.value }
            val maxSigne = grilleSignes.maxBy { it.value }

            carte = if (maxSigne.value > 1)
                main.rechercher(maxSigne.key)
            else
                main.rechercher(maxValeur.key)
        }

        main.afficher()

        if (carte != null) {
            main.choisir(carte) // On supprime la carte de la main de l'ordinateur
            println("$nom plays $carte")
        }
        return carte
    }

    fun jouer(defausse: TasDeCartes): Carte? {
        if (robot)
            return jouerAuto(defausse)

        val carte: Carte?

        print("Cards in hand: ")
        main.afficherAvecNombre()

        while (true) {
            println("Choose a card to play (1-${main.taille}):")
            val choix = readln()

            if (choix == "exit"){
                carte = null
                break
            }
            else if (choix.toIntOrNull() in 1..main.taille) {
                carte = main.choisir(choix.toInt() - 1)
                break
            }
        }
        return carte
    }
}