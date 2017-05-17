package com.cdg.alex.simpleorganizer.bus

import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer

object AlarmBus{
     val instance: Bus by lazy { Bus(ThreadEnforcer.ANY) }
}

