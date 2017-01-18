package com.cdg.alex.simpleorganizer.settings_builder


class JsonSettingsString (var time: CharSequence?, var period: CharSequence?,  var ringtone: CharSequence?, var onOfSwitch: Boolean,var monday: Boolean
                            , var tuesday: Boolean,var wednesday: Boolean, var thursday: Boolean, var friday: Boolean, var saturday: Boolean
                            ,var sunday: Boolean, var checkPeriod: Boolean, var alarmId: CharSequence?, var soundPath: CharSequence?) {

    private constructor(builder: Builder) : this (builder.time, builder.period, builder.ringtone, builder.onOfSwitch, builder.monday, builder.tuesday, builder.wednesday
    , builder.thursday, builder.friday, builder.saturday, builder.sunday, builder.checkPeriod, builder.alarmId, builder.soundPath)


class Builder {
    var time: CharSequence? = null
        private set

    var period: CharSequence? = null
        private set

    var ringtone: CharSequence? = null
        private set

    var onOfSwitch: Boolean = false
        private set

    var monday: Boolean = false
        private set

    var tuesday: Boolean = false
        private set

    var wednesday: Boolean = false
        private set

    var thursday: Boolean = false
        private set

    var friday: Boolean = false
        private set

    var saturday: Boolean = false
        private set

    var sunday: Boolean = false
        private set

    var checkPeriod: Boolean = false
        private set

    var alarmId: CharSequence? = null
        private set

    var soundPath: CharSequence? = null
        private set


    fun setTime (time: CharSequence): Builder {
        this.time = time
        return this
    }

    fun setPeriod (period: CharSequence): Builder {
        this.period = period
        return this
    }

    fun setRingtone (ringtone: CharSequence): Builder {
        this.ringtone = ringtone
        return this
    }

    fun setOnOfSwitch (onOfSwitch: Boolean): Builder {
        this.onOfSwitch = onOfSwitch
        return this
    }

    fun setMonday (monday: Boolean): Builder {
        this.monday = monday
        return this
    }

    fun setTuesday (tuesday: Boolean): Builder {
        this.tuesday = tuesday
        return this
    }

    fun setWednesday (wednesday: Boolean): Builder {
        this.wednesday = wednesday
        return this
    }

    fun setThursday (thursday: Boolean): Builder {
        this.thursday = thursday
        return this
    }

    fun setFriday (friday: Boolean): Builder {
        this.friday = friday
        return this
    }

    fun setSaturday (saturday: Boolean): Builder {
        this.saturday = saturday
        return this
    }

    fun setSunday (sunday: Boolean): Builder {
        this.sunday = sunday
        return this
    }

    fun setCheckPeriod (checkPeriod: Boolean): Builder {
        this.checkPeriod = checkPeriod
        return this
    }

    fun setAlarmId (alarmId: CharSequence): Builder {
        this.alarmId = alarmId
        return this
    }

    fun setSoundPath (soundPath: CharSequence): Builder {
        this.soundPath = soundPath
        return this
    }

    fun build() = JsonSettingsString(this)

    }

    override fun toString(): String {
       val buffer = StringBuffer()
        buffer.append("{\"settings\":[{\"timeTextView\":").append("\"").append(time).append("\"")
                .append(",\"setPeriodView\":").append("\"").append(period).append("\"").append(",\"setRingtoneView\":")
                .append("\"").append(ringtone).append("\"").append(",\"onOfSwitch\":").append(onOfSwitch)
                .append(",\"monday\":").append(monday).append(",\"tuesday\":").append(tuesday)
                .append(",\"wednesday\":").append(wednesday).append(",\"thursday\":").append(thursday)
                .append(",\"friday\":").append(friday).append(",\"saturday\":").append(saturday)
                .append(",\"sunday\":").append(sunday).append(",\"checkPeriod\":").append(checkPeriod)
                .append(",\"id\":").append("\"").append(alarmId).append("\"").append(",\"soundPath\":").append("\"")
                .append(soundPath).append("\"").append("}]}")
        return buffer.toString()
    }

}
