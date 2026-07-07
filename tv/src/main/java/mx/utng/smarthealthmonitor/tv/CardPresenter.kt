// tv/.../CardPresenter.kt
package mx.utng.smarthealthmonitor.tv

import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter
import android.view.LayoutInflater

/**
 * Presenter para las tarjetas del BrowseSupportFragment.
 * Cada tarjeta muestra título y descripción de un ítem de salud.
 */
class CardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val card = item as TvCard
        val root = viewHolder.view
        root.findViewById<TextView>(R.id.card_title).text       = card.titulo
        root.findViewById<TextView>(R.id.card_description).text = card.descripcion
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
}
