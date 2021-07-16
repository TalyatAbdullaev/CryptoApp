package com.example.cryptoapp.utils

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.R
import com.example.cryptoapp.adapters.CoinInfoAdapter
import com.example.cryptoapp.pojo.CoinPriceInfo
import kotlinx.android.synthetic.main.activity_coins_list.*

class CoinsListActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coins_list)

        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClickListener = object: CoinInfoAdapter.OnCoinClickListener{
            override fun OnCoinClick(coinPriceInfo: CoinPriceInfo) {
                Log.d("TAG", "OnClick ${coinPriceInfo.fromSymbol}")
                startActivity(CoinDetailActivity.getIntent(this@CoinsListActivity, coinPriceInfo.fromSymbol))
            }
        }
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this, CoinViewModelFactory(application)).get(CoinViewModel::class.java)
        viewModel.priceList.observe(this, Observer {
            Log.d("TAG", "PriceList $it")
            adapter.listCoin = it
        })
    }
}
