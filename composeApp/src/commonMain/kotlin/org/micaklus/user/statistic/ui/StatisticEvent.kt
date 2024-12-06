package org.micaklus.user.statistic.ui



sealed interface StatisticEvent {
    data object RefreshUsers : StatisticEvent
}