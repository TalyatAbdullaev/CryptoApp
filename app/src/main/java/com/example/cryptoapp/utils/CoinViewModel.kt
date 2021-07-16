package com.example.cryptoapp.utils

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptoapp.api.ApiFactory
import com.example.cryptoapp.database.AppDatabase
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.example.cryptoapp.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    val priceList = db.coinPriceInfoDao().getPriceList()

    init {
        loadData()
    }

    fun getDetailInfo(fSym: String): LiveData<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    private fun loadData() {
        ApiFactory.apiService.getTopCoinsInfo(limit = 50)
            .map { it.data?.map { it.coinInfo?.name }?.joinToString(",") }
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            .map { getPriceListFromRawData(it) }
            .subscribeOn(Schedulers.io())
            .delaySubscription(rx.Observable.timer(10, TimeUnit.SECONDS))
            .retry()
            .subscribe({
                Log.d("TAG", "Add toDB $it.toString()")
                db.coinPriceInfoDao().insertPriceList(it)
            }, {
                Log.d("TAG", "BAD $it")
            })
    }

    private fun getPriceListFromRawData(coinPriceInfoRawData: CoinPriceInfoRawData): List<CoinPriceInfo> {
        val resultList = ArrayList<CoinPriceInfo>()

        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return resultList
        val coinsKeys = jsonObject.keySet()
        for (coinKey in coinsKeys) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeys = currencyJson.keySet()
            for (currencyKey in currencyKeys) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                resultList.add(priceInfo)
            }
        }
        return resultList
    }
}