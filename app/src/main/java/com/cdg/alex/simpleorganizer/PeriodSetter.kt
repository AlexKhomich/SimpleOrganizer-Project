package com.cdg.alex.simpleorganizer

import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class PeriodSetter(private var dayList: ArrayList<Boolean>, private var period: String, private var context: Context) {

    private val dayListFromSettings: ArrayList<Boolean> = ArrayList()

    private var paramFlag: Boolean = false // флаг для определения использования первого или второго периода
    private var periodFlag: Boolean = false // флаг для проверки симметричности первого и второго параметров периода

    private val alarmParser: AlarmParser = AlarmParser()

    private val MONDAY: String = "monday"
    private val TUESDAY: String = "tuesday"
    private val WEDNESDAY: String = "wednesday"
    private val THURSDAY: String = "thursday"
    private val FRIDAY: String = "friday"
    private val SATURDAY: String = "saturday"
    private val SUNDAY: String = "sunday"

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

//        билд нового списка со считанными из shared prefs днями
        dayListFromSettings.add(0, parseDay(MONDAY, alarmCounter))
        dayListFromSettings.add(1, parseDay(TUESDAY, alarmCounter))
        dayListFromSettings.add(2, parseDay(WEDNESDAY, alarmCounter))
        dayListFromSettings.add(3, parseDay(THURSDAY, alarmCounter))
        dayListFromSettings.add(4, parseDay(FRIDAY, alarmCounter))
        dayListFromSettings.add(5, parseDay(SATURDAY, alarmCounter))
        dayListFromSettings.add(6, parseDay(SUNDAY, alarmCounter))

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

    fun buildNewPeriodWeek(alarmCounter: Int): ArrayList<Boolean> {
        var daysCounter: Int = 0
        val shiftValue = getShiftValue(alarmCounter)
        val newPeriodList: ArrayList<Boolean> = ArrayList()

//        для разных значений периода
        if (paramFlag && periodFlag) {

            for (i in 0..3) {                  //нужно придумать алгоритм вычисления кол-ва проходов!!!
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

            for (i in 0..3) {                  //нужно придумать алгоритм вычисления кол-ва проходов!!!
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

        for (i in 0..3) {                  //нужно придумать алгоритм вычисления кол-ва проходов!!!
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

    //    функция для считывания параметров из файла настроек (этот метод необходимо будет немного переделать, чтобы доставать дни из класса ServiceSettingsHolder)
    private fun parseDay(witchDay: String, counter: Int): Boolean {
        val allAlarmsList = alarmParser.getAllAlarms(context)
        val result = allAlarmsList[counter]
        val jsonObject = JSONObject(result)
        val jsonArray = jsonObject.getJSONArray("settings")
        var day = false
        var isMonday = false
        var isTuesday = false
        var isWednesday = false
        var isThursday = false
        var isFriday = false
        var isSaturday = false
        var isSunday = false

        when (witchDay) {
            MONDAY -> try {
                (0..jsonArray.length() - 1)
                        .map { jsonArray.getJSONObject(it) }
                        .forEach { isMonday = it.getBoolean(MONDAY) }
                day = isMonday
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            TUESDAY -> try {
                (0..jsonArray.length() - 1)
                        .map { jsonArray.getJSONObject(it) }
                        .forEach { isTuesday = it.getBoolean(TUESDAY) }
                day = isTuesday
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            WEDNESDAY -> try {
                (0..jsonArray.length() - 1)
                        .map { jsonArray.getJSONObject(it) }
                        .forEach { isWednesday = it.getBoolean(WEDNESDAY) }
                day = isWednesday
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            THURSDAY -> try {
                (0..jsonArray.length() - 1)
                        .map { jsonArray.getJSONObject(it) }
                        .forEach { isThursday = it.getBoolean(THURSDAY) }
                day = isThursday
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            FRIDAY -> try {
                (0..jsonArray.length() - 1)
                        .map { jsonArray.getJSONObject(it) }
                        .forEach { isFriday = it.getBoolean(FRIDAY) }
                day = isFriday
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            SATURDAY -> try {
                (0..jsonArray.length() - 1)
                        .map { jsonArray.getJSONObject(it) }
                        .forEach { isSaturday = it.getBoolean(SATURDAY) }
                day = isSaturday
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            SUNDAY -> try {
                (0..jsonArray.length() - 1)
                        .map { jsonArray.getJSONObject(it) }
                        .forEach { isSunday = it.getBoolean(SUNDAY) }
                day = isSunday
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        return day
    }

//сделать функцию записи в shared preferences
}