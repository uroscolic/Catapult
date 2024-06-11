package com.rma.catapult.quiz.questions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.rma.catapult.cat.details.orange
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.quiz.questions.model.QuestionUiModel
import kotlin.random.Random

fun NavGraphBuilder.questions(
    route: String,
    toCatList: () -> Unit
) = composable(
    route = route,
) {
    val questionsViewModel = hiltViewModel<QuestionsViewModel>()
    val state = questionsViewModel.state.collectAsState()
    QuestionScreen(state.value, toCatList = toCatList)
}


@Composable
fun QuestionScreen(state: QuestionsState, toCatList: () -> Unit){
    var score by remember { mutableIntStateOf(0) }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    BackHandler {

    }
    if (state.questions.isNotEmpty()) {
        if(currentQuestionIndex >= state.questions.size){
            EndOfQuizScreen(score = score,
                onHomeClick = toCatList,
            )
        }
        else {
            QuestionContent(
                question = state.questions[currentQuestionIndex],
                onAnswerSelected = {
                    if (it) {
                        score += 1
                    }
                    currentQuestionIndex += 1
                },
                number = currentQuestionIndex + 1,
                onGiveUp = toCatList,
                score = score
            )
        }
    } else {
        Text("No questions available")
    }
}

@Composable
fun EndOfQuizScreen(score: Int, onHomeClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Quiz Completed", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Your Score: $score/100", fontSize = 20.sp, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onHomeClick()
        /* Share result logic */
        }) {
            Text("Share Result", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onHomeClick) {
            Text("Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionContent(
    question: QuestionUiModel,
    onAnswerSelected: (Boolean) -> Unit,
    number: Int,
    onGiveUp: () -> Unit,
    score: Int
) {
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Question #${number}", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = Samsung) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {

                /*Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {*/
                val correctOption = Random.nextBoolean()
                Text(text = "Correct Answers: ${score}/20", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = question.text, fontSize = 18.sp, fontWeight = FontWeight.Bold)


                AsyncImage(
                    model = if(correctOption) question.correctAnswer else question.incorrectAnswer,
                    contentDescription = "option1",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onAnswerSelected(correctOption) }
                )

                AsyncImage(
                    model = if(correctOption) question.incorrectAnswer else question.correctAnswer,
                    contentDescription = "option2",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onAnswerSelected(correctOption) }
                )
                Button(
                    onClick = { showDialog = true },
                    content = {
                        Text("Give up", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Samsung
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = orange
                    )
                )
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = {
                            Text(text = "Confirm Give Up",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = Samsung
                            ) },
                        text = {
                            Text(text = "Are you sure you want to give up?",
                                fontFamily = Samsung

                            ) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                    onGiveUp()
                                }
                            ) {
                                Text(text = "Yes",
                                    fontFamily = Samsung
                                )
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialog = false }
                            ) {
                                Text(text = "No",
                                    fontFamily = Samsung
                                )
                            }
                        }
                    )
                }

                //}
            }
        }
    }
}
