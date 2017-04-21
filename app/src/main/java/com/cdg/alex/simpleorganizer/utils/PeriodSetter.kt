package com.cdg.alex.simpleorganizer.utils

import android.content.Context
import java.util.*

class PeriodSetter(private var dayList: ArrayList<Boolean>, private var period: String, private var context: Context): SettingsToHolder {

    private val dayListFromSettings: ArrayList<Boolean> = ArrayList()

    private var paramFlag: Boolean = false // флаг для определения использования первого или второго периода
    private var periodFlag: Boolean = false // флаг для проверки симметричности первого и второго параметров периода

    //получение первого значения периода в виде числа
    private fun getFirstOption(): Int = splitString(0).toInt()

    //получение второго элемента периода в виде числа
    private fun getSecondOption(): Int = splitString(1).toInt()

    //метод для разрезания строки на составляющие
    private fun splitString(periodParams: Int): String {
        val argSplit: List<String> = period.split("/")
        var temp: String = ""
        if (periodParams == 0) {
            temp = argSplit[0]
        } else if (periodParams == 1) {
            temp = argSplit[1]
        }
        return temp
    }

    //метод для обработки периода звонков будильника
    fun managePeriod(): ArrayList<Boolean> {
        val iterator = dayList.listIterator()
        val newList: ArrayList<Boolean> = ArrayList()
        var i: Int = 0
        while (iterator.hasNext()) { //определяю с какого элемента начинать отсчет
            if (!dayList[i]) {
                iterator.next()
                i++
            } else break
        }

        //если изначально выбранный элемент не первый в списке, то забиваю предыдущие значения fals'ами
        if (i != 0) {
            for (it in 0..i - 1) {
                newList.add(it, false)
            }
        }

        //забиваю остальные позиции нужными значениями в соответствии с параметром "period"
        while (iterator.hasNext()) {
            for (it in 0..getFirstOption() - 1) { //вычисления с первым параметром периода
                if (iterator.hasNext()) {
                    newList.add(i, true)
                    i++
                    iterator.next()
                }
            }

            for (it in 0..getSecondOption() - 1) { //вычисления со вторым параметром периода
                if (iterator.hasNext()) {
                    newList.add(i, false)
                    i++
                    iterator.next()
                }
            }
        }

        return newList //возвращаю новый список с нужными установками
    }

    private fun getShiftValue(alarmCounter: Int): Int {
        var temp: Int = 0

//        билд нового списка со считанными из ServiceSettingsHolder днями
        dayListFromSettings.add(0, readFromSettingsAndSaveToHolder(context)[alarmCounter].isMonday)
        dayListFromSettings.add(1, readFromSettingsAndSaveToHolder(context)[alarmCounter].isTuesday)
        dayListFromSettings.add(2, readFromSettingsAndSaveToHolder(context)[alarmCounter].isWednesday)
        dayListFromSettings.add(3, readFromSettingsAndSaveToHolder(context)[alarmCounter].isThursday)
        dayListFromSettings.add(4, readFromSettingsAndSaveToHolder(context)[alarmCounter].isFriday)
        dayListFromSettings.add(5, readFromSettingsAndSaveToHolder(context)[alarmCounter].isSaturday)
        dayListFromSettings.add(6, readFromSettingsAndSaveToHolder(context)[alarmCounter].isSunday)

//        вычисление размера списка
        var k: Int = dayListFromSettings.size

        val iterator = dayListFromSettings.listIterator()

//        установка курсора на последний элемент списка
        while (iterator.hasNext()) {
            iterator.next()
        }

//        вычисление кол-ва дней для сдвига
        val z: Boolean = dayListFromSettings[k - 1]
        if (z) {
            for (i in 0..getFirstOption() - 1) {
                if (dayListFromSettings[k - 1]) {
                    temp++
                    if (iterator.hasPrevious()) {
                        iterator.previous()
                        k--
                    }
                } else {
                    paramFlag = true

                    if (temp == getFirstOption())
                        periodFlag = true

                    temp = getFirstOption() - temp
                    break
                }

                if (temp == getFirstOption()) {
                    paramFlag = false
                    periodFlag = true
                } else {
                    paramFlag = dayListFromSettings[k - 1]
                }
            }
        } else {
            for (i in 0..getSecondOption() - 1) {
                if (!dayListFromSettings[k - 1]) {
                    temp++
                    if (iterator.hasPrevious()) {
                        iterator.previous()
                        k--
                    }
                } else {
                    paramFlag = false

                    if (temp == getSecondOption())
                        periodFlag = true

                    temp = getSecondOption() - temp
                    break
                }

                if (temp == getSecondOption()) {
                    paramFlag = true
                    periodFlag = true
                } else {
                    paramFlag = dayListFromSettings[k - 1]
                }
            }
        }

        return temp
    }
//    main function for build new week
    fun buildNewPeriodWeek(alarmCounter: Int): ArrayList<Boolean> {
        var daysCounter: Int = 0
        val shiftValue = getShiftValue(alarmCounter)
        val newPeriodList: ArrayList<Boolean> = ArrayList()

//        для разных значений периода
        if (paramFlag && periodFlag) {

            for (i in 0..3) {                  //нужно оптимизировать алгоритм вычисления кол-ва проходов!!!
                if (paramFlag) {
                    for (it in 0..getFirstOption() - 1) { //вычисления с первым параметром периода
                        newPeriodList.add(daysCounter, true)
                        daysCounter++
                    }
                    paramFlag = false
                } else {
                    for (it in 0..getSecondOption() - 1) { //вычисления со вторым параметром периода
                        newPeriodList.add(daysCounter, false)
                        daysCounter++
                    }
                    paramFlag = true
                }
            }
        } else if (!paramFlag && periodFlag) {

            for (i in 0..3) {
                if (!paramFlag) {
                    for (it in 0..getSecondOption() - 1) { //вычисления со вторым параметром периода
                        newPeriodList.add(daysCounter, false)
                        daysCounter++
                    }
                    paramFlag = true
                } else {
                    for (it in 0..getFirstOption() - 1) { //вычисления с первым параметром периода
                        newPeriodList.add(daysCounter, true)
                        daysCounter++
                    }
                    paramFlag = false
                }
            }
        }
//        добавление кол-ва дней в зависимости от значения сдвига
        for (i in 0..shiftValue - 1) {
            newPeriodList.add(daysCounter, paramFlag)
            daysCounter++
        }

        for (i in 0..3) {
            if (paramFlag) {
                for (it in 0..getSecondOption() - 1) { //вычисления со вторым параметром периода
                    newPeriodList.add(daysCounter, false)
                    daysCounter++
                }
                paramFlag = false
            } else {
                for (it in 0..getFirstOption() - 1) { //вычисления с первым параметром периода
                    newPeriodList.add(daysCounter, true)
                    daysCounter++
                }
                paramFlag = true
            }
        }

        return newPeriodList
    }
}