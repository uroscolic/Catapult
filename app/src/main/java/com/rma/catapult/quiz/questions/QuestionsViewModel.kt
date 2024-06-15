package com.rma.catapult.quiz.questions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.cat.db.Cat
import com.rma.catapult.cat.mapper.asCatDbModel
import com.rma.catapult.cat.repository.Repository
import com.rma.catapult.catImages.db.CatPhoto
import com.rma.catapult.catImages.model.CatImageUiModel

import com.rma.catapult.quiz.questions.model.Question
import com.rma.catapult.quiz.questions.model.QuestionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){


    private val _state = MutableStateFlow(QuestionsState())
    val state = _state.asStateFlow()

    private val _allCats = MutableStateFlow<List<Cat>>(emptyList())
    val allCats = _allCats.asStateFlow()
    private fun setState(reducer: QuestionsState.() -> QuestionsState) = _state.update(reducer)
    init {
        observeCats()
    }

    private fun observeCats() {
        viewModelScope.launch {
            repository.observeAllCats()
                .distinctUntilChanged()
                .collect { cats ->
                    _allCats.value = cats
                    generateQuestions()
                }
        }

    }

    private fun generateQuestions()
    {
        viewModelScope.launch {
            val questions = mutableListOf<Question>()
            for (i in 0 until 20) {
                questions.add(generateQuestion())
            }
            setState {
                copy(questions = questions.map { it.asQuestionUiModel() })
            }
        }
    }

    private val type1 = "Which cat lives longer?"
    private val type2 = "Which cat weighs more on average?"
    suspend fun generateQuestion(): Question {

        var random = Random.nextBoolean()
        val text = if (random) type1 else type2

        lateinit var catPhoto1: CatPhoto
        lateinit var catPhoto2: CatPhoto

        val cats: List<Cat> = if (random) getTwoRandomCatsWithDifferentLifeSpans()
        else getTwoRandomCatsWithDifferentWeights()

        catPhoto1 = getRandomCatPhoto(cats[0].id)
        while (catPhoto1.url.isBlank()) {
            Log.d("PHOTO_ERROR", "Cat photo 1 is not valid, retrying...")
            catPhoto1 = getRandomCatPhoto(cats[0].id)
        }

        catPhoto2 = getRandomCatPhoto(cats[1].id)
        while (catPhoto2.url.isBlank()) {
            Log.d("PHOTO_ERROR", "Cat photo 2 is not valid, retrying...")
            catPhoto2 = getRandomCatPhoto(cats[1].id)
        }

        Log.d("CATS1", cats[0].name)
        Log.d("CATS2", cats[1].name)

        val correctAnswer = if (random) {
            if (cats[0].avg_life_span > cats[1].avg_life_span) catPhoto1.url else catPhoto2.url
        } else {
            if (cats[0].avg_weight > cats[1].avg_weight) catPhoto1.url else catPhoto2.url
        }

        val incorrectAnswer = if (random) {
            if (cats[0].avg_life_span > cats[1].avg_life_span) catPhoto2.url else catPhoto1.url
        } else {
            if (cats[0].avg_weight > cats[1].avg_weight) catPhoto2.url else catPhoto1.url
        }


        return Question(
            text = text,
            correctAnswer = correctAnswer,
            incorrectAnswer = incorrectAnswer,
        )
    }

    fun getTwoRandomCatsWithDifferentLifeSpans() : List<Cat> {
        var cat1: Cat
        do {
            cat1 = allCats.value.random()
        } while (cat1.image.url.isEmpty())

        var cat2: Cat
        do {
            cat2 = allCats.value.random()
        } while (cat2.avg_life_span == cat1.avg_life_span || cat2.image.url.isEmpty())

        return listOf(cat1, cat2)
    }
    fun getTwoRandomCatsWithDifferentWeights() : List<Cat> {
        var cat1: Cat
        do {
            cat1 = allCats.value.random()
        } while (cat1.image.url.isEmpty())

        var cat2: Cat
        do {
            cat2 = allCats.value.random()
        } while (cat2.avg_weight == cat1.avg_weight || cat2.image.url.isEmpty())

        return listOf(cat1, cat2)
    }
    suspend fun getRandomCatPhoto(catId: String) : CatPhoto {
        return repository.getRandomCatPhoto(catId)
    }
    private fun Question.asQuestionUiModel() = QuestionUiModel(
        text = text,
        correctAnswer = correctAnswer,
        incorrectAnswer = incorrectAnswer
    )
}