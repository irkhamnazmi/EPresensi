package com.rsudbrebes.epresensi.model.dummy

import android.provider.Settings.Global.getString
import com.rsudbrebes.epresensi.EPresensi

class LocDummy {
    var location = ""
        get() = field
        set(value)  {
            field = value
        }
}