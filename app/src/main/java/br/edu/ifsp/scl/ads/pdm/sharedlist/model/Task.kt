package br.edu.ifsp.scl.ads.pdm.sharedlist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class Task (
    @PrimaryKey(autoGenerate = true) val id: Int? = -1,
    var titulo: String = "",
    var descricao: String = "",
    var dataCriacao: String = "",
    var dataPrevistaCumprimento: String = "",
    var usuario: String = "",
    var status: Boolean = false,
    var usuarioConclusao: String = "",
): Parcelable