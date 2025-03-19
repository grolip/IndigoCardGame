package indigo

@Suppress("SpellCheckingInspection")
class Partie {
    private val pioche = TasDeCartes()
    private val defausse = TasDeCartes()
    private val joueurHumain = Joueur("Player")
    private val joueurMachine = Joueur("Computer", true)
    private lateinit var joueurActuel: Joueur
    private lateinit var premierJoueur: Joueur
    private var dernierGagnant: Joueur? = null

    companion object {
        const val NCARTES_A_PIOCHER = 6
        const val NCARTES_INIT_DEFAUSSE = 4
        const val NPOINTS_BONUS_NCARTES = 3
        val VALEURS_GAGNANTES = Carte.VALEUR.subList(8, 13)
    }

    private fun quiCommence(){
        while(true){
            println("Play first? ")
            when(readln()){
                "yes" -> {
                    joueurActuel = joueurHumain
                    premierJoueur = joueurHumain
                    break
                }
                "no" -> {
                    joueurActuel = joueurMachine
                    premierJoueur = joueurMachine
                    break
                }
            }
        }
    }

    fun configurer(){
        pioche.reset()
        pioche.genererJeuComplet()
        pioche.melanger()

        joueurHumain.reset()
        joueurHumain.piocher(pioche, NCARTES_A_PIOCHER)
        joueurMachine.reset()
        joueurMachine.piocher(pioche, NCARTES_A_PIOCHER)
        defausse.ajouter(pioche.distribuer(NCARTES_INIT_DEFAUSSE))

        quiCommence()
    }

    private fun afficherScore(){
        println("Score: ${joueurHumain.nom} ${joueurHumain.score} - " +
                    "${joueurMachine.nom} ${joueurMachine.score}")
        println("Cards: ${joueurHumain.nom} ${joueurHumain.nCartesGagnees} - " +
                "${joueurMachine.nom} ${joueurMachine.nCartesGagnees}")
    }

    private fun afficherDefausse(){
        print("Initial cards on the table: ")
        defausse.afficher()
    }

    private fun afficherResumeDefausse(){
        if (defausse.taille > 0) {
            println(
                "${defausse.taille} cards on the table, " +
                        "and the top card is ${defausse.premiereCarte}"
            )
        } else {
            println("No cards on the table")
        }
    }

    private fun calculerPoints(cartesEnJeu: TasDeCartes) =
        cartesEnJeu.cartes.count { it.valeur in VALEURS_GAGNANTES }

    private fun resolutionManche(carte: Carte){
        var carteActuelle: Carte? = null

        // On récupère la dernière carte posée
        if (defausse.taille > 0)
            carteActuelle = defausse.premiereCarte

        defausse.ajouter(carte)

        if(carteActuelle != null){
            if (carte.valeur == carteActuelle.valeur ||
                carte.signe == carteActuelle.signe
            ) {
                dernierGagnant = joueurActuel
                joueurActuel.score += calculerPoints(defausse)
                joueurActuel.nCartesGagnees += defausse.taille
                defausse.reset()

                println("${joueurActuel.nom} wins cards")
                afficherScore()
            }
        }
    }

    private fun resolutionFin(){
        if (defausse.taille > 0) {
            dernierGagnant!!.score += calculerPoints(defausse)
            dernierGagnant!!.nCartesGagnees += defausse.taille
        }

        if (joueurHumain.nCartesGagnees > joueurMachine.nCartesGagnees)
            joueurHumain.score += NPOINTS_BONUS_NCARTES
        else if (joueurHumain.nCartesGagnees < joueurMachine.nCartesGagnees)
            joueurMachine.score += NPOINTS_BONUS_NCARTES

        afficherScore()
    }

    private fun changerDeJoueur(){
        joueurActuel = if (joueurActuel === joueurHumain)
            joueurMachine
        else
            joueurHumain
    }

    fun jouer(){
        var carteActuelle: Carte?

        afficherDefausse()

        while(true){
            println()
            afficherResumeDefausse()

            if (pioche.vide && joueurActuel.plusDeCartes){
                resolutionFin()
                break
            }

            carteActuelle = joueurActuel.jouer(defausse)
            if (carteActuelle == null)
                break

            resolutionManche(carteActuelle)

            if (joueurActuel.plusDeCartes)
                joueurActuel.piocher(pioche, NCARTES_A_PIOCHER)
            changerDeJoueur()
        }
        println("Game Over")
    }
}