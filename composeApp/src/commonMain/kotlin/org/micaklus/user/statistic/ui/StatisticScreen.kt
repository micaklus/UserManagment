package org.micaklus.user.statistic.ui


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.Pie
import org.micaklus.user.common.LoadingScreen
import org.micaklus.user.user.domain.model.Gender
import org.micaklus.user.user.domain.model.User
import si.mitja.domain.common.MyResult

class StatisticScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<StatisticViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.onEvent(StatisticEvent.RefreshUsers)
        }

        Scaffold(
            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
            topBar = {
                TopAppBar(title = {
                    Text("Users Statistics")
                },
                    navigationIcon = {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.clickable {
                                navigator.pop()
                            })
                    })
            },

            ) { innerPadding ->
            Column(Modifier.padding(innerPadding).fillMaxSize()) {
                when (val currentstate = state) {
                    is MyResult.Success -> {
                        GenderPieChart(currentstate.data)
                        AgeColumnChart(currentstate.data)
                    }

                    is MyResult.Loading -> {
                        LoadingScreen()
                    }

                    is MyResult.Error,
                    MyResult.Empty,
                    MyResult.Pending -> {
                        // Do Nothing
                    }
                }
            }
        }
    }

    private @Composable
    fun AgeColumnChart(data: List<User>) {
        ColumnChart(
            modifier = Modifier.size(400.dp).padding(horizontal = 50.dp).fillMaxWidth(),
            data = remember {
                listOf(
                    Bars(
                        label = "0-12",
                        values = listOf(
                            Bars.Data(
                                label = "Children",
                                value = data.filter { it.age in 0..12 }.size.toDouble(),
                                color = SolidColor(Color.Cyan)
                            )

                        ),
                    ),
                    Bars(
                        label = "13-17",
                        values = listOf(
                            Bars.Data(
                                label = "Teenagers",
                                value = data.filter { it.age in 13..17 }.size.toDouble(),
                                color = SolidColor(Color.Blue)
                            )
                        ),
                    ),
                    Bars(
                        label = "18-24",
                        values = listOf(
                            Bars.Data(
                                label = "Young Adults",
                                value = data.filter { it.age in 18..24 }.size.toDouble(),
                                color = SolidColor(Color.Red)
                            )
                        ),
                    ),
                    Bars(
                        label = "25-34",
                        values = listOf(
                            Bars.Data(
                                label = "Early Adults",
                                value = data.filter { it.age in 25..34 }.size.toDouble(),
                                color = SolidColor(Color.Gray)
                            )
                        ),
                    ),
                    Bars(
                        label = "35-49",
                        values = listOf(
                            Bars.Data(
                                label = "Midlife Adults",
                                value = data.filter { it.age in 35..49 }.size.toDouble(),
                                color = SolidColor(Color.Green)
                            )
                        ),
                    ),
                    Bars(
                        label = "50-64",
                        values = listOf(
                            Bars.Data(
                                label = "Mature Adults",
                                value = data.filter { it.age in 50..65 }.size.toDouble(),
                                color = SolidColor(Color.Magenta)
                            )
                        ),
                    ),
                    Bars(
                        label = "65+",
                        values = listOf(
                            Bars.Data(
                                label = "Seniors",
                                value = data.filter { it.age!! > 64 }.size.toDouble(),
                                color = SolidColor(Color.DarkGray)
                            )
                        ),
                    )
                )
            },
            barProperties = BarProperties(
                cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                spacing = 3.dp,
                thickness = 20.dp
            ),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
        )
    }

    @Composable
    fun GenderLegend() {

        Column(modifier = Modifier.padding(16.dp)) {
            Gender.entries.forEach { gender ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(gender.color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = gender.name)
                }
            }
        }

    }

    @Composable
    private fun GenderPieChart(data: List<User>) {
        var data by remember {
            mutableStateOf(
                listOf(
                    Pie(
                        label = "Male",
                        data = data.filter { it.getGenderEnum() == Gender.MALE }.size.toDouble(),
                        color = Gender.MALE.color,
                        selectedColor = Color.Black
                    ),
                    Pie(
                        label = "Female",
                        data = data.filter { it.getGenderEnum() == Gender.FEMALE }.size.toDouble(),
                        color = Gender.FEMALE.color,
                        selectedColor = Color.Black
                    ),
                    Pie(
                        label = "Other",
                        data = data.filter { it.getGenderEnum() == Gender.OTHER }.size.toDouble(),
                        color = Gender.OTHER.color,
                        selectedColor = Color.Black
                    ),
                )
            )
        }
        Row {
            PieChart(
                modifier = Modifier.size(300.dp).padding(50.dp).weight(1f),
                data = data,
                onPieClick = {
                    println("${it.label} Clicked")
                    val pieIndex = data.indexOf(it)
                    data =
                        data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
                },
                selectedScale = 1.2f,
                scaleAnimEnterSpec = spring<Float>(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                colorAnimEnterSpec = tween(300),
                colorAnimExitSpec = tween(300),
                scaleAnimExitSpec = tween(300),
                spaceDegreeAnimExitSpec = tween(300),
                style = Pie.Style.Fill
            )
            GenderLegend()
        }
    }

    val Gender.color: Color
        get() = when (this) {
            Gender.MALE -> Color.Cyan
            Gender.FEMALE -> Color.Yellow
            Gender.OTHER -> Color.Green
        }
}