package com.example.cryptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.pojo.CoinInfo
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_item.view.*
import java.util.ArrayList

class CoinInfoAdapter(private val context: Context) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    var listCoin: List<CoinPriceInfo> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coin_item, parent, false)
        return CoinInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = listCoin[position]
        val symbolsTemplate = context.resources.getString(R.string.symbols_format_template)
        val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
        holder.textViewCoinSymbols.text =
            String.format(symbolsTemplate, coin.fromSymbol, coin.toSymbol)
        holder.textViewValue.text = coin.price.toString()
        holder.textViewLastUpdate.text = String.format(lastUpdateTemplate, coin.getFormattedDate())
        Picasso.get().load(coin.getFullImgUrl()).into(holder.imageViwCoinIcon)
        holder.itemView.setOnClickListener {
            onCoinClickListener?.OnCoinClick(coin)
        }
    }

    override fun getItemCount(): Int = listCoin.size

    inner class CoinInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCoinSymbols = itemView.textViewCoinSymbols
        val textViewValue = itemView.textViewValue
        val textViewLastUpdate = itemView.textViewLastUpdate
        val imageViwCoinIcon = itemView.imageViewCoinIcon
    }

    interface OnCoinClickListener {
        fun OnCoinClick(coinPriceInfo: CoinPriceInfo)
    }
}