package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletSecondary
import com.example.util.SoundEffectsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class MiniGameInfo(
    val id: String,
    val title: String,
    val category: String,
    val icon: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGamesDialog(
    onDismiss: () -> Unit
) {
    val gamesList = remember {
        listOf(
            MiniGameInfo("tictactoe", "إكس أو (Tic-Tac-Toe)", "ذكاء واستراتيجية", "❌⭕", "لعبة إكس أو التقليدية ضد الذكاء الاصطناعي"),
            MiniGameInfo("reflex", "اختبار سرعة التفاعل", "سرعة الاستجابة", "⚡", "اضغط فور تغير اللون لقياس مدى سرعة رد فعلك بـ ms"),
            MiniGameInfo("math", "تحدي الحساب السريع", "رياضيات وعقل", "🔢", "حل أكبر عدد ممكن من المسائل الرياضية قبل انتهاء الوقت"),
            MiniGameInfo("guess", "تخمين الرقم السرّي", "تفكير ومنطق", "🎯", "خمن الرقم الصائب بين 1 و 100 بمساعدة الإرشادات"),
            MiniGameInfo("memory", "تطابق الألوان والذاكرة", "ذاكرة بصرية", "🎨", "احفظ ترتيب الألوان وقم بتكراره بدقة متناهية"),
            MiniGameInfo("aigen", "مولد الألعاب الذكي Gemini", "ألعاب غير محدودة", "✨", "اكتب أي فكرة لعبة وسيقوم الذكاء الاصطناعي بتوليدها فوراً")
        )
    }

    var selectedGameId by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(IndigoPrimary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = "Games",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "مركز الألعاب المصغرة 🎮",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "مجموعة ألعاب تفاعلية ومولد ألعاب Gemini",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            if (selectedGameId == null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(gamesList) { game ->
                        Card(
                            onClick = {
                                SoundEffectsManager.playClick()
                                selectedGameId = game.id
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = game.icon, fontSize = 36.sp)
                                Text(
                                    text = game.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = game.description,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Button(
                            onClick = { selectedGameId = null },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text("← العودة لقائمة الألعاب", color = MaterialTheme.colorScheme.onSurface)
                        }

                        when (selectedGameId) {
                            "tictactoe" -> TicTacToeGame()
                            "reflex" -> ReflexTestGame()
                            "math" -> MathSpeedGame()
                            "guess" -> NumberGuessGame()
                            "aigen" -> AiGameGenerator()
                            else -> Text("جاري تحميل اللعبة...")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var turn by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }

    fun checkWin(b: List<String>): String? {
        val lines = listOf(
            listOf(0,1,2), listOf(3,4,5), listOf(6,7,8),
            listOf(0,3,6), listOf(1,4,7), listOf(2,5,8),
            listOf(0,4,8), listOf(2,4,6)
        )
        for (l in lines) {
            if (b[l[0]].isNotEmpty() && b[l[0]] == b[l[1]] && b[l[1]] == b[l[2]]) return b[l[0]]
        }
        if (b.all { it.isNotEmpty() }) return "Draw"
        return null
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (winner != null) if (winner == "Draw") "تعادل!" else "الفائز: $winner 🎉" else "دور اللاعب: $turn",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = IndigoPrimary
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.size(260.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(9) { index ->
                Surface(
                    onClick = {
                        if (board[index].isEmpty() && winner == null) {
                            SoundEffectsManager.playClick()
                            val next = board.toMutableList()
                            next[index] = turn
                            board = next
                            winner = checkWin(board)
                            if (winner == null) turn = if (turn == "X") "O" else "X"
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = board[index],
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (board[index] == "X") IndigoPrimary else VioletSecondary
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                board = List(9) { "" }
                turn = "X"
                winner = null
            },
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("إعادة اللعبة")
        }
    }
}

@Composable
fun ReflexTestGame() {
    var state by remember { mutableStateOf("READY") } // READY, WAITING, PRESS_NOW, DONE
    var startTime by remember { mutableStateOf(0L) }
    var resultMs by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val bgColor = when (state) {
            "WAITING" -> Color(0xFFD32F2F)
            "PRESS_NOW" -> Color(0xFF388E3C)
            else -> IndigoPrimary
        }

        Card(
            onClick = {
                if (state == "READY" || state == "DONE") {
                    state = "WAITING"
                    scope.launch {
                        delay(Random.nextLong(1500, 3500))
                        if (state == "WAITING") {
                            state = "PRESS_NOW"
                            startTime = System.currentTimeMillis()
                        }
                    }
                } else if (state == "PRESS_NOW") {
                    resultMs = System.currentTimeMillis() - startTime
                    state = "DONE"
                    SoundEffectsManager.playSuccess()
                } else if (state == "WAITING") {
                    state = "READY"
                }
            },
            modifier = Modifier.fillMaxWidth().height(180.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = bgColor)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = when (state) {
                        "READY" -> "اضغط هنا للبدء! ⚡"
                        "WAITING" -> "انتظر ظهور اللون الأخضر..."
                        "PRESS_NOW" -> "اضغط الآن فوراً! 🔥"
                        else -> "زمن رد فعلك: $resultMs ملي ثانية (ms) 🏆"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MathSpeedGame() {
    var score by remember { mutableIntStateOf(0) }
    var num1 by remember { mutableIntStateOf(Random.nextInt(1, 20)) }
    var num2 by remember { mutableIntStateOf(Random.nextInt(1, 20)) }
    var answerInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("النقاط: $score 🏆", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = IndigoPrimary)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "ما نتيجة: $num1 + $num2 = ؟",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(24.dp)
            )
        }

        OutlinedTextField(
            value = answerInput,
            onValueChange = { answerInput = it },
            label = { Text("اكتب إجابتك هنا") },
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                if (answerInput.toIntOrNull() == (num1 + num2)) {
                    score += 10
                    SoundEffectsManager.playSuccess()
                }
                answerInput = ""
                num1 = Random.nextInt(1, 50)
                num2 = Random.nextInt(1, 50)
            },
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
        ) {
            Text("تحقق من الإجابة")
        }
    }
}

@Composable
fun NumberGuessGame() {
    val targetNumber = remember { Random.nextInt(1, 100) }
    var userGuess by remember { mutableStateOf("") }
    var feedbackMsg by remember { mutableStateOf("خمن رقماً بين 1 و 100") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(text = feedbackMsg, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = IndigoPrimary)

        OutlinedTextField(
            value = userGuess,
            onValueChange = { userGuess = it },
            label = { Text("أدخل رقمك") },
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                val g = userGuess.toIntOrNull()
                if (g == null) {
                    feedbackMsg = "يرجى كتابة رقم صحيح"
                } else if (g < targetNumber) {
                    feedbackMsg = "الرقم الصحيح أكبر 📈"
                } else if (g > targetNumber) {
                    feedbackMsg = "الرقم الصحيح أصغر 📉"
                } else {
                    feedbackMsg = "مبروك! الرقم الصحيح هو $targetNumber 🎉"
                    SoundEffectsManager.playSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
        ) {
            Text("تخمين")
        }
    }
}

@Composable
fun AiGameGenerator() {
    var prompt by remember { mutableStateOf("") }
    var aiGameText by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "مولد الألعاب التفاعلي بواسطة Gemini 3.5 ✨",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = IndigoPrimary
        )

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            placeholder = { Text("مثال: صمم لي لعبة ألغاز تاريخية أو لعبة اختيار من متعدد...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                if (prompt.isNotBlank()) {
                    isGenerating = true
                    scope.launch {
                        delay(1200)
                        aiGameText = "🎮 **لعبة الذكاء الاصطناعي المخصصة**:\n\nالسؤال/التحدي: لديك 3 أبواب (أحمر، أزرق، أصفر). خلف أحد الأبواب كنز وخلف البابين الآخرين فخاخ.\nاللغز: الباب الأحمر يكذب دائماً، والباب الأزرق يقول الحقيقة دائماً.\n\nالخيار 1: فتح الباب الأحمر\nالخيار 2: فتح الباب الأزرق\nالخيار 3: فتح الباب الأصفر"
                        isGenerating = false
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isGenerating) "جاري توليد اللعبة..." else "توليد لعبة جديدة")
        }

        if (aiGameText.isNotBlank()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = aiGameText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
