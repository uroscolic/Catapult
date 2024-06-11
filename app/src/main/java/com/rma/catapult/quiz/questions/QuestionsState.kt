package com.rma.catapult.quiz.questions

import com.rma.catapult.quiz.questions.model.Question
import com.rma.catapult.quiz.questions.model.QuestionUiModel

data class QuestionsState (
    var questions : List<QuestionUiModel> = emptyList(),
)