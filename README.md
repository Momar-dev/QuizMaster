# Quiz Master

Application Android native de quiz de culture générale, conçue en **Java** avec des layouts **XML** et Material Components. L'application est 100% autonome (offline), rapide, fluide et compatible avec tous les formats d'écrans Android.

---

## Fonctionnalités

- **Accueil & Statistiques** : Meilleur score et nombre de parties jouées enregistrés localement.
- **Session de Quiz** :
  - 15 questions aléatoires parmi 18 questions vérifiées (Sciences, Histoire, Géographie, Tech, Nature, Maths, Culture).
  - 4 choix par question avec une seule réponse exacte.
  - Verrouillage anti-triche : impossible de changer de réponse ou de cliquer plusieurs fois.
  - Feedback instantané (vert pour correct, rouge pour incorrect).
  - Explication pédagogique détaillée affichée sous chaque question.
  - Progression synchronisée (compteur dynamique et jauge).
- **Écran de Résultats** :
  - Score final et pourcentage de réussite.
  - Palier de performance contextuel (*Grandmaster*, *Excellent*, *Well Played*, *Keep Learning*).
  - Détail des réponses correctes et incorrectes.
  - Mise à jour du record personnel.
- **Révision des Réponses** :
  - Liste complète des questions de la session avec votre choix, la bonne réponse et l'explication.
- **Gestion de la Rotation** :
  - L'état de la partie, les choix sélectionnés et le score sont préservés lors de la rotation de l'appareil.

---

## Stack Technique

- **Langage** : Java (SDK 24+)
- **Interface** : XML natif (ConstraintLayout, LinearLayout, ScrollView, CardView, RecyclerView)
- **Design System** : Google Material Components (Thème sombre bleu marine et indigo)
- **Persistance locale** : Android SharedPreferences
- **Architecture** : Séparation stricte entre les vues (Activities) et la logique métier (`QuizEngine`, `QuizRepository`, `Question`)

---

## Structure du Code

```
app/src/main/
├── java/com/example/
│   ├── MainActivity.java         # Écran d'accueil
│   ├── QuizActivity.java         # Déroulement du quiz
│   ├── ResultsActivity.java      # Écran des résultats
│   ├── ReviewActivity.java       # Écran de révision
│   ├── data/QuizRepository.java  # Banque de questions
│   ├── engine/QuizEngine.java    # Logique métier et scoring
│   ├── model/Question.java       # Modèle de données immuable
│   ├── ui/ReviewAdapter.java     # Adaptateur RecyclerView
│   └── util/ScoreStorage.java    # Sauvegarde locale SharedPreferences
├── res/
│   ├── drawable/                 # Drawables vectoriels et formes
│   ├── layout/                   # Layouts XML des écrans
│   └── values/                   # colors.xml, strings.xml, themes.xml
└── AndroidManifest.xml           # Configuration des activités
```

---

## Démarrage Rapide

### 1. Ouvrir le projet
Ouvrez le dossier racine du projet dans **Android Studio** et laissez Gradle synchroniser les dépendances.

### 2. Lancer les tests unitaires
```bash
gradle :app:testDebugUnitTest
```
13 tests unitaires couvrent la logique du quiz (validation, scoring, verrouillage, sérialisation).

### 3. Générer l'APK
```bash
gradle :app:assembleDebug
```
L'APK se trouve ensuite dans `app/build/outputs/apk/debug/app-debug.apk`.
