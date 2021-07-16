package com.example.cryptoapp.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_detail.*

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)

        if(!intent.hasExtra(FSYM_KEY)) {
            finish()
            return
        }
        val fromSymbol = intent.getStringExtra(FSYM_KEY)
        viewModel = ViewModelProvider(this, CoinViewModelFactory(application)).get(CoinViewModel::class.java)
        if (fromSymbol != null) {
            viewModel.getDetailInfo(fromSymbol).observe(this, Observer {
                Log.d("TAG", "DetailInfo ${it.toString()}")
                tvFromSym.text = it.fromSymbol
                tvToSym.text = it.toSymbol
                tvPriceVal.text = it.price.toString()
                tvMinVal.text = it.lowDay.toString()
                tvMaxVal.text = it.highDay.toString()
                tvLastMarketVal.text = it.lastMarket
                tvUpdateVal.text = it.getFormattedDate()
                Picasso.get().load(it.getFullImgUrl()).into(imageViewCoinLogo)
            })
        }
    }

    companion object {
        private const val FSYM_KEY = "fSym"

        fun getIntent(context: Context, fSym: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(FSYM_KEY, fSym)
            return intent
        }
    }


}